import java.awt.*;
import java.util.Random;

/**
 * Represents a wall on the game map that ants cannot pass through.
 */
public class Wall extends Object{

	/**
	 * Constructs a new wall object with specified coordinates, size, random generator and game map.
	 *
	 * @param x       The x-coordinate of the wall's position.
	 * @param y       The y-coordinate of the wall's position.
	 * @param size    The size of the wall.
	 * @param random  The random generator used for random operations.
	 * @param gameMap The game map where the wall is placed.
	 */
	Wall(int x, int y, int size, Random random, GameMap gameMap) {
		super(x, y, size, random, gameMap);
	}

	/**
	 * Defines the action performed by the wall object in each simulation tick.
	 * The wall object does not perform any action.
	 */
	@Override
	void action() {}

	/**
	 * Returns the color of the wall.
	 *
	 * @return The color of the wall, which is grey.
	 */
	public Color getColor(){ return new Color(120, 120, 120);}
}
