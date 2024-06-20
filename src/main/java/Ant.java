import java.awt.*;
import java.util.Random;

public class Ant extends Object{
    static int antCounter = 0;
    public boolean carryFood = false;
    private int k, f=0, t=0;//kierunek w krórym idzie mrówka(NIE DOTYKAĆ!!!)
    private int a=50; // 2/a szansa na to że mrówka losowo zmieni kierunek(tylko w tedy gdy mrówka nie wyczuwa zapachu)
    int dx = random.nextInt(-100, 100);
    int dy = random.nextInt(-100, 100);
    int lastX;
    int lastY;
    float homePher = 100;
    float foodPher = 100;
    private float USE_RATE = .995F;
    private int WANDER_CHANCE = 92;
    int bored = 0;
    Ant(int x, int y, int size, Random random, GameMap gameMap) {
        super(x, y, size, random, gameMap);
        antCounter++;
        k = random.nextInt(0,4);
    }
    Ant(int x, int y, int size, Random random, GameMap gameMap, boolean carryFood, int k, int a)
    {
        super(x, y, size, random, gameMap);
        this.carryFood = carryFood;
        this.k = k;
        this.a = a;
        antCounter++;
        int xn=x-gameMap.getNestx();
        int yn=y-gameMap.getNesty();
    }

    @Override
    void action(){
        gameMap.takeObject(this);
        // Wander chance .1
        if (random.nextInt(0,100) > WANDER_CHANCE) dx += random.nextInt(-100, 100);
        if (random.nextInt(0,100) > WANDER_CHANCE) dy += random.nextInt(-100, 100);
        if (random.nextInt(0,100) > 99) bored += random.nextInt(0, 15);

        if (bored>0) {
            // Ignore pheromones
            bored--;
        } else {
            // Sniff trails
            if (carryFood) {
                // Look for home
                int[] direction = getStrongest(x, y);
                dx += direction[0] * random.nextInt(0, 150);
                dy += direction[1] * random.nextInt(0, 150);
            }
            else
            {
                // Look for food
                int[] direction = getStrongest(x, y);
                dx += direction[0] * random.nextInt(0, 150);
                dy += direction[1] * random.nextInt(0, 150);
            }
        }
        // Bounding limits, bounce off of edge
        if (x<2) dx = 100;
        if (x>gameMap.getWidth()-3) dx = -100;
        if (y<2) dy = 100;
        if (y>gameMap.getHeight()-3) dy = -100;

        while(gameMap.tiles[x+dx/100][y+dy/100].cellOccupant!=null && gameMap.tiles[x+dx/100][y+dy/100].cellOccupant.getClass()!=Ant.class){
            dx=-random.nextInt(0,2);
            dy=-random.nextInt(0,2);
        }

        // Speed limit
        dx = Math.min(dx, 100);
        dx = Math.max(dx, -100);
        dy = Math.min(dy, 100);
        dy = Math.max(dy, -100);
        // Move
        x = (int) Math.floor(x+dx/100);
        y = (int) Math.floor(y+dy/100);

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
    int[] getStrongest(int x, int y) {
        float compare = 0;
        float strongestVal = 0;
        int[] strongest = {
                0, 0
        };

        compare = gameMap.tiles[x-1][y-1].getAntScentValue(); // up left
        if (compare > strongestVal) {
            strongest[0] = -1;
            strongest[1] = -1;
            strongestVal = compare;
        }
        compare = gameMap.tiles[x][y-1].getAntScentValue(); // up
        if (compare > strongestVal) {
            strongest[0] = 0;
            strongest[1] = -1;
            strongestVal = compare;
        }
        compare = gameMap.tiles[x+1][y-1].getAntScentValue(); // up right
        if (compare > strongestVal) {
            strongest[0] = 1;
            strongest[1] = -1;
            strongestVal = compare;
        }
        compare = gameMap.tiles[x-1][y].getAntScentValue(); // left
        if (compare > strongestVal) {
            strongest[0] = -1;
            strongest[1] = 0;
            strongestVal = compare;
        }
        compare = gameMap.tiles[x+1][y].getAntScentValue(); // right
        if (compare > strongestVal) {
            strongest[0] = 1;
            strongest[1] = 0;
            strongestVal = compare;
        }
        compare = gameMap.tiles[x-1][y+1].getAntScentValue(); // down left
        if (compare > strongestVal) {
            strongest[0] = -1;
            strongest[1] = 1;
            strongestVal = compare;
        }
        compare = gameMap.tiles[x][y+1].getAntScentValue(); // down
        if (compare > strongestVal) {
            strongest[0] = 0;
            strongest[1] = 1;
            strongestVal = compare;
        }
        compare = gameMap.tiles[x+1][y+1].getAntScentValue(); // down right
        if (compare > strongestVal) {
            strongest[0] = 1;
            strongest[1] = 1;
            strongestVal = compare;
        }

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
