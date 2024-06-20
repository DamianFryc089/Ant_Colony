import java.awt.*;
import java.util.Random;

public class AntNest extends Object {
	public static int foodInNest = 10;
	private int ticksSinceFoodCheck;
	AntNest(int x, int y, int size, Random random, GameMap gameMap, int ticksSinceFoodCheck)
	{
		super(x, y, size, random, gameMap);
		for (int i = 0; i < 75; i++) {
			spawnAnt();
		}
		this.ticksSinceFoodCheck = ticksSinceFoodCheck;
	}

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
				int antsToKill = Math.min(Ant.antCounter/10,1);
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
		do {
			if(gameMap.tiles[newX][newY].cellOccupant == null){
				gameMap.tiles[newX][newY].cellOccupant = new Ant(newX,newY,1, random, gameMap);
				return;
			}
			if (newY == y-1){
				newX++;
				if (newX == x + size) newY++;
			}
			else if(newX == x+size){
				newY++;
				if (newY == y + size) newX--;
			}
			else if(newY == y + size){
				newX--;
				if (newX == x - 1) newY--;
			}
			else if(newX == x - 1){
				newY--;
				if (newY == y-1) newX++;
			}
		}while (startY != newY || startX != newX);
	}

	@Override
	public Color getColor() {return new Color(119, 52, 29);}

	@Override
	public String toString() {
		return super.toString() +
				"|" + foodInNest+
				"|" + ticksSinceFoodCheck;
	}
}
