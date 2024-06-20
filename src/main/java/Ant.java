import java.awt.*;
import java.util.Random;

public class Ant extends Object{
    static int antCounter = 0;
    public boolean carryFood = false;
    private int k, f=0, t=0;//kierunek w krórym idzie mrówka(NIE DOTYKAĆ!!!)
    private int a=50; // 2/a szansa na to że mrówka losowo zmieni kierunek(tylko w tedy gdy mrówka nie wyczuwa zapachu)
    float dx = random.nextFloat(-1, 1);
    float dy = random.nextFloat(-1, 1);
    int lastX;
    int lastY;
    float xf, yf;
    float homePher = 100;
    float foodPher = 100;
    private float USE_RATE = .995F;
    private float WANDER_CHANCE = .92F;
    int bored = 0;
    Ant(int x, int y, int size, Random random, GameMap gameMap) {
        super(x, y, size, random, gameMap);
        antCounter++;
        k = random.nextInt(0,4);
        xf=x;
        yf=y;
    }
    Ant(int x, int y, int size, Random random, GameMap gameMap, boolean carryFood, int k, int a)
    {
        super(x, y, size, random, gameMap);
        this.carryFood = carryFood;
        this.k = k;
        this.a = a;
        antCounter++;
        xf=x;
        yf=y;
    }

    @Override
    void action(){
        gameMap.takeObject(this);
        // Wander chance .1
        if(BiteAndSpit()){
            if(carryFood) {
                carryFood = false;
                AntNest.foodInNest++;
            }
            else carryFood=true;
        }
        if (random.nextFloat(0, 1) > WANDER_CHANCE) dx += random.nextFloat(-1, 1);
        if (random.nextFloat(0, 1) > WANDER_CHANCE) dy += random.nextFloat(-1, 1);
        if (random.nextFloat(0,1) > 0.92) bored += random.nextInt(0, 15);

        if (bored>0) {
            // Ignore pheromones
            bored--;
        } else {
            // Sniff trails
            if (carryFood) {
                // Look for home
                int[] direction = getStrongest(x, y);
                dx += direction[0] * random.nextFloat((float) 0, 1.5F);
                dy += direction[1] * random.nextFloat((float) 0, 1.5F);
            }
            else
            {
                // Look for food
                int[] direction = getStrongest(x, y);
                dx += direction[0] * random.nextFloat((float) 0, 1.5F);
                dy += direction[1] * random.nextFloat((float) 0, 1.5F);
            }
        }
        // Bounding limits, bounce off of edge
        if (x<2) dx = 1;
        if (x>gameMap.getWidth()-3) dx = -1;
        if (y<2) dy = 1;
        if (y>gameMap.getHeight()-3) dy = -1;
        // Speed limit
        dx = Math.min(dx, 1);
        dx = Math.max(dx, -1);
        dy = Math.min(dy, 1);
        dy = Math.max(dy, -1);
        if(gameMap.tiles[(int) Math.floor(x+dx)][(int) Math.floor(y+dy)].cellOccupant!=null && gameMap.tiles[(int) Math.floor(x+dx)][(int) Math.floor(y+dy)].cellOccupant.getClass()!=Ant.class){
            do {
                dx *= random.nextInt(-1, 2);
                dy *= random.nextInt(-1, 2);
                //System.out.println(random.nextInt(-1,2));
            }
            while(gameMap.tiles[(int) Math.floor(x+dx)][(int) Math.floor(y+dy)].cellOccupant!=null && gameMap.tiles[(int) Math.floor(x+dx)][(int) Math.floor(y+dy)].cellOccupant.getClass()!=Ant.class);
            }
        // Move
        xf+=dx;
        yf+=dy;
        x = (int) Math.floor(xf);
        y = (int) Math.floor(yf);

        // Only if ant has moved enough (to another pixel)
        if (lastX!=x || lastY!=y) {
            // Leave trails
            if (carryFood) {
                // Leave food pheromone trail
                foodPher = foodPher * USE_RATE;
                gameMap.tiles[x][y].setFoodScentValue(foodPher);
            }
            else
            {
                // Leave home pheromone trail
                homePher = homePher * USE_RATE;
                gameMap.tiles[x][y].setAntScentValue(homePher);
            }
        }

        lastX = x;
        lastY = y;
        if(x+1>gameMap.getWidth()||x<1||gameMap.getHeight()<y+1||y<1){x= gameMap.getWidth()/2;y= gameMap.getHeight()/2;}
        if(gameMap.tiles[x][y].cellOccupant==null || gameMap.tiles[x][y].cellOccupant.getClass()==Ant.class)gameMap.placeObject(this);
    }
    private boolean BiteAndSpit()
    {
        if(!carryFood) {
            if (x + 1 < gameMap.getWidth() && gameMap.tiles[x + 1][y].cellOccupant != null && gameMap.tiles[x + 1][y].cellOccupant.getClass() == Food.class){
                ((Food) gameMap.tiles[x + 1][y].cellOccupant).decreaseFood();
                return true;}
            if (y + 1 < gameMap.getHeight() && gameMap.tiles[x][y + 1].cellOccupant != null && gameMap.tiles[x][y + 1].cellOccupant.getClass() == Food.class){
                ((Food) gameMap.tiles[x][y + 1].cellOccupant).decreaseFood();
                return true;}
            if (x - 1 > 0 && gameMap.tiles[x - 1][y].cellOccupant != null && gameMap.tiles[x - 1][y].cellOccupant.getClass() == Food.class){
                ((Food) gameMap.tiles[x - 1][y].cellOccupant).decreaseFood();
                return true;}
            if (y - 1 > 0 && gameMap.tiles[x][y - 1].cellOccupant != null && gameMap.tiles[x][y - 1].cellOccupant.getClass() == Food.class){
                ((Food) gameMap.tiles[x][y - 1].cellOccupant).decreaseFood();
                return true;}
        }
        else {
            if (x + 1 < gameMap.getWidth() && gameMap.tiles[x + 1][y].cellOccupant != null && gameMap.tiles[x + 1][y].cellOccupant.getClass() == AntNest.class)
                return true;
            if (y + 1 < gameMap.getHeight() && gameMap.tiles[x][y + 1].cellOccupant != null && gameMap.tiles[x][y + 1].cellOccupant.getClass() == AntNest.class)
                return true;
            if (x - 1 > 0 && gameMap.tiles[x - 1][y].cellOccupant != null && gameMap.tiles[x - 1][y].cellOccupant.getClass() == AntNest.class)
                return true;
            if (y - 1 > 0 && gameMap.tiles[x][y - 1].cellOccupant != null && gameMap.tiles[x][y - 1].cellOccupant.getClass() == AntNest.class)
                return true;
        }
        return false;
    }
    int[] getStrongest(int x, int y) {
        float compare = 0;
        float strongestVal = 0;
        int[] strongest = {0, 0};
        if(x-2>0&&y-2>0){
        compare = gameMap.tiles[x-1][y-1].getAntScentValue(); // up left
        if (compare > strongestVal) {
            strongest[0] = -1;
            strongest[1] = -1;
            strongestVal = compare;
        }}
        if(y-2>0){
        compare = gameMap.tiles[x][y-1].getAntScentValue(); // up
        if (compare > strongestVal) {
            strongest[0] = 0;
            strongest[1] = -1;
            strongestVal = compare;
        }}
        if(x+2< gameMap.getWidth()&&y-2>0){
        compare = gameMap.tiles[x+1][y-1].getAntScentValue(); // up right
        if (compare > strongestVal) {
            strongest[0] = 1;
            strongest[1] = -1;
            strongestVal = compare;
        }}
        if(x-2>0){
        compare = gameMap.tiles[x-1][y].getAntScentValue(); // left
        if (compare > strongestVal) {
            strongest[0] = -1;
            strongest[1] = 0;
            strongestVal = compare;
        }}
        if(x-2< gameMap.getWidth()){
        compare = gameMap.tiles[x+1][y].getAntScentValue(); // right
        if (compare > strongestVal) {
            strongest[0] = 1;
            strongest[1] = 0;
            strongestVal = compare;
        }}
        if(x-2>0&&y+2< gameMap.getHeight()){
            compare = gameMap.tiles[x-1][y+1].getAntScentValue(); // down left
            if (compare > strongestVal) {
                strongest[0] = -1;
                strongest[1] = 1;
                strongestVal = compare;
        }}
        if(y+2< gameMap.getHeight()){
        compare = gameMap.tiles[x][y+1].getAntScentValue(); // down
        if (compare > strongestVal) {
            strongest[0] = 0;
            strongest[1] = 1;
            strongestVal = compare;
        }}
        if(x+2< gameMap.getWidth()&&y+2< gameMap.getHeight()){
        compare = gameMap.tiles[x+1][y+1].getAntScentValue(); // down right
        if (compare > strongestVal) {
            strongest[0] = 1;
            strongest[1] = 1;
            strongestVal = compare;
        }}

        return strongest;
    }
    @Override
    public Color getColor() {return new Color(0,0,0);}

    @Override
    void death() {
        super.death();
        antCounter--;
    }

    @Override
    public String toString() {
        return super.toString() +
                "|" + carryFood +
                "|" + k +
                "|" + a;
    }
}
