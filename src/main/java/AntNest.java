import java.awt.*;
import java.util.Random;

public class AntNest extends Object {
	public static int foodInNest = 10;
	private int ticksSinceFoodCheck = 0;
	AntNest(int x, int y, int size, Random random, GameMap gameMap)
	{
		super(x, y, size, random, gameMap);
		for (int i = 0; i < 100; i++) {
			spawnAnt();
		}
	}

	@Override
	void action() {
		ticksSinceFoodCheck++;
		if (ticksSinceFoodCheck >= 2000)
		{
			ticksSinceFoodCheck = 0;
			foodInNest -= Ant.antCounter * 1;
			if (foodInNest < 0)
			{
				foodInNest = 0;
				for (int i = 0; i < gameMap.objects.size(); i++) {
					if (gameMap.objects.get(i).getClass() == Ant.class) {
							// mrówka która nosi jedzenie nie umiera
//						if (((Ant) gameMap.objects.get(i)).carryFood)
//							continue;
						//gameMap.objects.get(i).death();
						break;
					}
				}
			}
			if(foodInNest>0)spawnAnt();
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

	private void generateScent() {
		int maxGeneratedScent = -100;
			// Generowanie z gory
		for (int i = 0; i < size+2; i++) {
			gameMap.tiles[x+i][y-1].setScentValue(Math.min(gameMap.tiles[x+i][y-1].getScentValue(), maxGeneratedScent));
		}
			// Generowanie z prawej
		for (int i = 0; i < size+2; i++) {
			gameMap.tiles[x+size+1][i+y].setScentValue(Math.min(gameMap.tiles[x+size+1][i+y].getScentValue(), maxGeneratedScent));
		}

			// Generowanie z dołu
		for (int i = 0; i < size+2; i++) {
			gameMap.tiles[x+i][y+size+1].setScentValue(Math.min(gameMap.tiles[x+i][y+size+1].getScentValue(), maxGeneratedScent));
		}

			// Generowanie z lewej
		for (int i = 0; i < size+2; i++) {
			gameMap.tiles[x-1][i+y].setScentValue(Math.min(gameMap.tiles[x-1][i+y].getScentValue(), maxGeneratedScent));
		}
	}
	@Override
	public Color getColor() {return new Color(119, 52, 29);}

	@Override
	public String toString() {
		return super.toString() +
				"|" + foodInNest;
	}
}
