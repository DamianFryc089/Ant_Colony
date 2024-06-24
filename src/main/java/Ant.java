import java.awt.*;
import java.util.Random;

/**
 * The Ant class represents an ant in the simulation.
 * It inherits from the Object class and has various attributes and behaviors
 * specific to an ant, such as movement, carrying food, and leaving scents.
 */
public class Ant extends Object{
    static int antCounter = 0;
    public boolean carryFood = false;
    private float dx = random.nextFloat(-1, 1);
    private float dy = random.nextFloat(-1, 1);
    private float xf, yf;
    private float antScent = 100;
    private float foodScent = 100;
    private int[] lastAntPosition = new int[2];
    private int randomMovement = 0;

    /**
     * Constructs an Ant object with the specified initial position, size,
     * random number generator, and game map.
     *
     * @param x       the initial x-coordinate of the ant
     * @param y       the initial y-coordinate of the ant
     * @param size    the size of the ant
     * @param random  the random number generator
     * @param gameMap the game map
     */
    Ant(int x, int y, int size, Random random, GameMap gameMap) {
        super(x, y, size, random, gameMap);
        antCounter++;
        xf=x;
        yf=y;
    }

    /**
     * Defines the action to be performed by the ant, which is finding and gathering food.
     */
    @Override
    void action(){
        gameMap.takeObject(this);
        if(BiteAndSpit()){
            if(carryFood) {
                carryFood = false;
                AntNest.foodInNest++;
                antScent = 100;
            }
            else {
                carryFood = true;
                foodScent = 100;
            }
        }

        if (random.nextFloat(0, 1) > 0.80)
            dx += random.nextFloat(-1, 1);
        if (random.nextFloat(0, 1) > 0.80)
            dy += random.nextFloat(-1, 1);
        if (random.nextFloat(0,1) > 0.99)
            randomMovement += random.nextInt(0, 15);

        if (randomMovement<=0) {
            if (carryFood) {
                int[] direction = searchForAntScent();
                dx += direction[0] * random.nextFloat((float) 0, 1.5F);
                dy += direction[1] * random.nextFloat((float) 0, 1.5F);
            }
            else
            {
                int[] direction = searchForFoodScent();
                dx += direction[0] * random.nextFloat((float) 0, 1.5F);
                dy += direction[1] * random.nextFloat((float) 0, 1.5F);
            }
        } else {
            randomMovement--;
        }

        if (x<2) dx = 1;
        if (x>gameMap.getWidth()-3) dx = -1;
        dx = Math.min(dx, 1);
        dx = Math.max(dx, -1);

        if (y<2) dy = 1;
        if (y>gameMap.getHeight()-3) dy = -1;
        dy = Math.min(dy, 1);
        dy = Math.max(dy, -1);

        if(gameMap.tiles[(int) Math.floor(xf+dx)][(int) Math.floor(yf+dy)].cellOccupant!=null && gameMap.tiles[(int) Math.floor(xf+dx)][(int) Math.floor(yf+dy)].cellOccupant.getClass()!=Ant.class){
            dx = 0;
            dy = 0;
        }

        xf+=dx;
        yf+=dy;
        x = (int) Math.floor(xf);
        y = (int) Math.floor(yf);

        if (lastAntPosition[0] != x || lastAntPosition[1] != y) {
            if (carryFood) {
                foodScent = foodScent * 0.995F;
                gameMap.tiles[x][y].setFoodScentValue(foodScent);
            }
            else
            {
                antScent = antScent * 0.995F;
                gameMap.tiles[x][y].setAntScentValue(antScent);
            }
        }
        lastAntPosition = new int[]{x, y};
        if(x+1>gameMap.getWidth()||x<1||gameMap.getHeight()<y+1||y<1){x= gameMap.getWidth()/2;y= gameMap.getHeight()/2;}
        if(gameMap.tiles[x][y].cellOccupant==null || gameMap.tiles[x][y].cellOccupant.getClass()==Ant.class)gameMap.placeObject(this);
    }
    /**
     * Determines whether the ant is close to the ant nest or food.
     *
     * @return true if the ant is close to the ant nest or food, false otherwise
     */
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
    int[] searchForFoodScent() {
        float scent = 0;
        float maxScent = 0;
        int[] theMaxScent = {0, 0};

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (x+i*2 <= 0 || y+j*2 <= 0 || x+i*2 >= gameMap.getWidth() || y+j*2 >= gameMap.getHeight())
                    continue;
                scent = gameMap.tiles[x+i][y+j].getFoodScentValue();
                if (scent > maxScent) {
                    theMaxScent = new int[]{i, j};
                    maxScent = scent;
                }
            }
        }
        return theMaxScent;
    }
    /**
     * Searches for the strongest ant scent near the ant's current position.
     *
     * @return an array with two elements indicating the direction of the strongest ant scent
     */
    int[] searchForAntScent() {
        float scent = 0;
        float maxScent = 0;
        int[] theMaxScent = {0, 0};

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (x+i*2 <= 0 || y+j*2 <= 0 || x+i*2 >= gameMap.getWidth() || y+j*2 >= gameMap.getHeight())
                    continue;
                scent = gameMap.tiles[x+i][y+j].getAntScentValue();
                if (scent > maxScent) {
                    theMaxScent = new int[]{i, j};
                    maxScent = scent;
                }
            }
        }
        return theMaxScent;
    }

    /**
     * Gets the color of the ant.
     *
     * @return the color of the ant, which is black
     */
    @Override
    public Color getColor() {return new Color(0,0,0);}

    /**
     * Handles the death of the ant.
     */
    @Override
    void death() {
        super.death();
        antCounter--;
    }

    /**
     * Returns a string representation of the ant.
     *
     * @return a string containing the ant's attributes
     */
    @Override
    public String toString() {
        return super.toString() +
                "|" + carryFood +
                "|" + dx +
                "|" + dy +
                "|" + xf +
                "|" + yf +
                "|" + antScent +
                "|" + foodScent +
                "|" + lastAntPosition[0] +
                "|" + lastAntPosition[1] +
                "|" + randomMovement;
    }
}
