import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class GameMap {
	private int width, height;
	Tile[][] tiles;
	private BufferedImage backgroundImage, scentImage;
	Random random;
	ArrayList<Object> objects;

	GameMap(Random random, ArrayList<Object> objects) {
		this.random = random;
		this.objects = objects;
	}

	public class Tile{
		int x, y;
		private int scentValue;
		Object cellOccupant;
		Color tileColor;

		Tile(int x, int y) {
			this.x = x;
			this.y = y;
			scentValue = 0;
			cellOccupant = null;
		}
		void setTileColor(Color newTileColor) {tileColor = newTileColor;}
		int getScentValue() {return scentValue;}
		void increaseScentValue(int value) {
			scentValue+=value;
			scentImage.setRGB(x, y,
					new Color(166, 31, 174, Math.min(scentValue,255)).getRGB()
			);
		}
		void decreaseScentValue(int value) {
			scentValue-=value;
			if(scentValue<0) scentValue = 0;
			scentImage.setRGB(x, y,
					new Color(166, 31, 174, Math.min(scentValue,255)).getRGB()
			);
		}
	}

	void generateMap(int width, int height) {
		this.width = width;
		this.height = height;
		tiles = new Tile[width][height];
		backgroundImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		scentImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				tiles[x][y] = new Tile(x, y);
				tiles[x][y].setTileColor(new Color(255, 255, 255));
//				tiles[x][y].setTileColor(new Color(57, 152, 49));

					// W teorii żeby mapa nie była zbyt jałowa powinno być tło trochę losowe,
					// ale nie wiem jak to zrobić żeby okay wyglądało
				/*int minRand = -20;
				int maxRand = 20;
				switch (random.nextInt(0,1)) {
					case 0:
							// grass
//						new Color(31, 135, 23);
						tiles[x][y].setTileColor(new Color(
								31 + random.nextInt(minRand,maxRand-minRand),
								135 + random.nextInt(minRand,maxRand-minRand),
								23 + random.nextInt(minRand,maxRand-minRand)));
						break;
					case 1:
							// rocks
//						new Color(100, 100, 100);
						tiles[x][y].setTileColor(new Color(
								100 + random.nextInt(minRand,maxRand-minRand),
								100 + random.nextInt(minRand,maxRand-minRand),
								100 + random.nextInt(minRand,maxRand-minRand)));
						break;
					case 2:
							// sand
//						new Color(163, 179, 37);
						tiles[x][y].setTileColor(new Color(
								163 + random.nextInt(minRand,maxRand-minRand),
								179 + random.nextInt(minRand,maxRand-minRand),
								37 + random.nextInt(minRand,maxRand-minRand)));
						break;
				}*/
			}
		}
		generateImage();
		objects.add(new AntNest(
				random.nextInt(50,width - 100),
				random.nextInt(50,height - 100),
				25, random, this, objects));
		generateWalls(100);
		generateFoodField(25);
	}

	void generateImage(){
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				backgroundImage.setRGB(x, y, tiles[x][y].tileColor.getRGB());
			}
		}
	}

	BufferedImage getBackgroundImage() {
		return backgroundImage;
	}

	BufferedImage getScentImage() {
		return scentImage;
	}

	void takeObject(Object objectToTake){
		for(int x = 0; x < objectToTake.getSize(); x++){
			for(int y = 0; y < objectToTake.getSize(); y++){
				tiles[objectToTake.getX()+x][objectToTake.getY()+y].cellOccupant = null;
			}
		}
	}

	void placeObject(Object objectToPlace){
		for(int x = 0; x < objectToPlace.getSize(); x++){
			for(int y = 0; y < objectToPlace.getSize(); y++){
				tiles[objectToPlace.getX()+x][objectToPlace.getY()+y].cellOccupant = objectToPlace;
			}
		}
	}

	void decreaseScentValues(int value) {
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				tiles[x][y].decreaseScentValue(value);
			}
		}
	}

	void generateWalls(int count){
		while (count > 0) {
			int randX = random.nextInt(width);
			int randY = random.nextInt(height);

			int verticalDirection = random.nextBoolean() ? -1 : 1;

			while (true) {
				if (randX < width && randY >= 0 && randY < height && tiles[randX][randY].cellOccupant == null) {
					tiles[randX][randY].cellOccupant = new Wall(randX, randY, 1, random, this);
					objects.add(tiles[randX][randY].cellOccupant);
					count--;
				}
				else break;

					// losowanie kolejnego muru lub przerwy
				int rand = random.nextInt(100);
				if (rand < 48) randX++;
				else if (rand < 96) randY+=verticalDirection;
				else break;
			}
		}
	}
	void generateFoodField(int size){
		int randX = random.nextInt(50, width - 100);
		int randY = random.nextInt(50, height - 100);

		for (int i = 0; i < size; i++) {
			for (int j = size - i; j < size + i; j++) {
				tryToGenerateFood(randX+j, randY + i);
			}
		}
		for (int i = 0; i < size; i++) {
			for (int j = i; j < 2*size - i ; j++) {
				tryToGenerateFood(randX+j, randY + i + size);
			}
		}
	}
	boolean tryToGenerateFood(int x, int y)
	{
		if (x >= 0 && x < width && y >= 0 && y < height && tiles[x][y].cellOccupant == null) {
			tiles[x][y].cellOccupant = new Food(x, y, 1, random, this);
			objects.add(tiles[x][y].cellOccupant);
			return true;
		}
		return false;
	}

	int getWidth(){return width;}
	int getHeight(){return height;}
}
