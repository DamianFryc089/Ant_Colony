import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

class AntNest extends Object {
	ArrayList<Object> objects;
	int foodInNest = 0;

	AntNest(int x, int y, int size, Random random, GameMap gameMap, ArrayList<Object> objects)
	{
		super(x, y, size, random, gameMap);
		this.objects = objects;
	}

	@Override
	void action() {
		foodInNest++; // Automatyczne generowanie mrówek
		if (foodInNest >= 10)
			if(spawnAnt())
				foodInNest-=10;
	}

	boolean spawnAnt()
	{
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
				objects.add(gameMap.tiles[newX][newY].cellOccupant);
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

	@Override
	Color getColor() {return new Color(119, 52, 29);}
}
