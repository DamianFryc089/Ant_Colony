import java.awt.*;
import java.util.Random;

/**
 * The Food class represents a food object in the simulation.
 * It extends the Object class and manages food-related attributes and behaviors.
 */
public class Food extends Object{
	/** The counter for the total amount of food in the simulation. */
	public static int foodCounter = 0;

	/** The value of the food, representing the amount of food available. */
	private int foodValue;

	/**
	 * Constructs a Food object with default food value of 1.
	 *
	 * @param x the x-coordinate of the food
	 * @param y the y-coordinate of the food
	 * @param size the size of the food
	 * @param random the Random instance used for randomness
	 * @param gameMap the GameMap instance the food is placed on
	 */
	Food(int x, int y, int size, Random random, GameMap gameMap) {
		super(x, y, size, random, gameMap);
		foodValue = 1;
		foodCounter+=foodValue;
	}

	/**
	 * Defines the action performed by the food object in each simulation tick.
	 * The food object does not perform any action.
	 */
	@Override
	void action(){}

	/**
	 * Decreases the food value by 1 and updates the food counter.
	 * If the food value reaches 0, the food object is removed from the game map.
	 */
	public void decreaseFood() {
		foodValue--;
		foodCounter--;
		if (foodValue <= 0)
			death();
	}

	/**
	 * Returns the color representation of the food object.
	 *
	 * @return the color of the food, which is red
	 */
	public Color getColor(){ return new Color(250, 98, 98);}



	/**
	 * Returns a string representation of the food object, including its food value.
	 *
	 * @return the string representation of the food object
	 */
	@Override
	public String toString() {
		return super.toString() +
			"|" + foodValue;
	}
}
