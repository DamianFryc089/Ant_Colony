import java.awt.*;
import java.util.Random;

class AntNest extends Object {
	static int foodInNest = 10;
	static void increaseFood()
	{
		foodInNest++;
	}
	AntNest(int x, int y, int size, Random random, GameMap gameMap)
	{
		super(x, y, size, random, gameMap);
		for (int i = 0; i < 10; i++) {
			spawnAnt();
		}
	}

	@Override
	void action() {
		//foodInNest++; // Automatyczne generowanie mrówek
		if (foodInNest >= 10)
			if(spawnAnt())
				foodInNest-=10;
		generateScent();
	}

	boolean spawnAnt() {
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
				gameMap.tiles[newX][newY].cellOccupant = new Ant(newX,newY,1, random,gameMap);
				return true;
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
		return false;
	}

	void generateScent() {
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
	Color getColor() {return new Color(119, 52, 29);}

	@Override
	public String toString() {
		return super.toString() +
				"|" + foodInNest;
	}
}
