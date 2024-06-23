import java.awt.*;
import java.util.Random;

/**
 * The AntNest class represents the nest of the ants in the simulation.
 * It manages the spawning of ants and the food supply in the nest.
 */
public class AntNest extends Object {
	public static int foodInNest = 10;
	private int ticksSinceFoodCheck;

	/**
	 * Constructs an AntNest with the specified parameters.
	 *
	 * @param x the x-coordinate of the nest
	 * @param y the y-coordinate of the nest
	 * @param size the size of the nest
	 * @param random the random number generator
	 * @param gameMap the game map
	 * @param ticksSinceFoodCheck the number of ticks since the last food check
	 */
	AntNest(int x, int y, int size, Random random, GameMap gameMap, int ticksSinceFoodCheck)
	{
		super(x, y, size, random, gameMap);
		for (int i = 0; i < 75; i++) {
			spawnAnt();
		}
		this.ticksSinceFoodCheck = ticksSinceFoodCheck;
	}

	/**
	 * The action method is called on each tick of the simulation.
	 * It manages the food supply in the nest and spawns or kills ants accordingly.
	 */
	@Override
	void action() {
		ticksSinceFoodCheck++;
		if (ticksSinceFoodCheck >= 2000)
		{
			ticksSinceFoodCheck = 0;
			foodInNest -= Ant.antCounter;
			if (foodInNest < 0)
			{
				foodInNest = 0;
				int antsToKill = Math.max(Ant.antCounter/10,1);
				for (int i = 0; i < gameMap.objects.size(); i++) {
					if (gameMap.objects.get(i).getClass() == Ant.class) {
						gameMap.objects.get(i).death();
						antsToKill--;
						if (antsToKill == 0)
							break;
					}
				}
			}
			if(foodInNest > 0) {
				int antsToSpawn = (int) Math.floor((double) foodInNest /10);
				foodInNest -= antsToSpawn * 5;
				for (int i = 0; i < antsToSpawn; i++) {
					spawnAnt();
				}
			}
		}
	}

	/**
	 * Spawns a new ant at a random position around the nest.
	 */
	private void spawnAnt() {
		int startX = x;
		int startY = y-1;
		switch (random.nextInt(4))
		{
				// up
			case 0:
				startX = random.nextInt(x, x+size);
				startY = y-1;
				break;

				// down
			case 1:
				startX = random.nextInt(x, x+size);
				startY = y+size;
				break;

				// left
			case 2:
				startX = x-1;
				startY = random.nextInt(y, y+size);
				break;

				// right
			case 3:
				startX = x+size;
				startY = random.nextInt(y, y+size);
				break;
		}

		int newX = startX;
		int newY = startY;

		if(gameMap.tiles[newX][newY].cellOccupant == null || gameMap.tiles[newX][newY].cellOccupant.getClass() == Ant.class)
			gameMap.tiles[newX][newY].cellOccupant = new Ant(newX,newY,1, random, gameMap);

		// Jeżeli mrówki miałby się nie pojawiać na innych obiektach
//		do {
//			if(gameMap.tiles[newX][newY].cellOccupant == null){
//				gameMap.tiles[newX][newY].cellOccupant = new Ant(newX,newY,1, random, gameMap);
//				return;
//			}
//			if (newY == y-1){
//				newX++;
//				if (newX == x + size) newY++;
//			}
//			else if(newX == x+size){
//				newY++;
//				if (newY == y + size) newX--;
//			}
//			else if(newY == y + size){
//				newX--;
//				if (newX == x - 1) newY--;
//			}
//			else if(newX == x - 1){
//				newY--;
//				if (newY == y-1) newX++;
//			}
//		}while (startY != newY || startX != newX);

	}

	/**
	 * Gets the color of the nest.
	 *
	 * @return the color of the nest, which is brown
	 */
	@Override
	public Color getColor() {return new Color(119, 52, 29);}

	/**
	 * Returns a string representation of the AntNest.
	 *
	 * @return a string representation of the AntNest
	 */
	@Override
	public String toString() {
		return super.toString() +
				"|" + foodInNest+
				"|" + ticksSinceFoodCheck;
	}
}
