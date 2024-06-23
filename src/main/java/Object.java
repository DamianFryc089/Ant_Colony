import java.awt.*;
import java.util.Random;

/**
 * The Object class represents a general object in the game map.
 * It is an abstract class that provides common properties and methods for game objects.
 */
public abstract class Object {
    protected int x, y, size;
    protected Random random;
    protected GameMap gameMap;

    /**
     * Constructs an Object with specified position, size, random instance, and game map.
     *
     * @param x the x-coordinate of the object
     * @param y the y-coordinate of the object
     * @param size the size of the object
     * @param random the Random instance for randomness
     * @param gameMap the GameMap instance the object belongs to
     */
    Object(int x, int y, int size, Random random, GameMap gameMap)
    {
        this.x = x;
        this.y = y;
        this.size = size;
        this.random = random;
        this.gameMap = gameMap;
        gameMap.placeObject(this);
        gameMap.objects.add(this);
    }

    /**
     * Removes the object from the game map and object list.
     */
    void death() {
        gameMap.takeObject(this);
        gameMap.objects.remove(this);
    }

    /**
     * Defines the action to be performed by the object.
     * Must be implemented by subclasses.
     */
    abstract void action();

    /**
     * Gets the color of the object.
     *
     * @return the color of the object, which default is red.
     */
    public Color getColor(){ return new Color(255, 0, 0);}

    /**
     * Gets the x-coordinate of the object.
     *
     * @return the x-coordinate of the object
     */
    public int getX(){ return x;}

    /**
     * Gets the y-coordinate of the object.
     *
     * @return the y-coordinate of the object
     */
    public int getY(){ return y;}

    /**
     * Gets the size of the object.
     *
     * @return the size of the object
     */
    public int getSize(){ return size;}

    @Override
    public String toString() {
        return this.getClass() + "|" + x + "|" + y + "|" + size;
    }
}
