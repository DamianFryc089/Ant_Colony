import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class GameMap {
	private int width, height;
	Tile[][] tiles;
	private BufferedImage backgroundImage, scentImage, objectsImage;
	Random random;
	ArrayList<Object> objects;
	int scale;

	GameMap(Random random, ArrayList<Object> objects, int scale) {
		this.random = random;
		this.objects = objects;
		this.scale = scale;
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
		void decreaseScentValue(int value, int maxvalue) {
			scentValue-=value;
			if(scentValue<0) scentValue = 0;
			if(scentValue>maxvalue) scentValue=maxvalue;
			scentImage.setRGB(x, y,
					new Color(166, 31, 174, Math.min(scentValue,255)).getRGB()
			);
		}
	}

	void generateMap(int frameWidth, int frameHeight) {
		this.width = frameWidth/scale;
		this.height = frameHeight/scale;
		tiles = new Tile[width][height];
		backgroundImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		scentImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		objectsImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

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
				random.nextInt(width/10,width - width/5 - 25),
				random.nextInt(height/10,height - height/5 - 25),
				25, random, this, objects));
//		generateWalls(100);
//		generateFoodField(25);
	}

	void generateImage(){
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				backgroundImage.setRGB(x, y, tiles[x][y].tileColor.getRGB());
			}
		}
	}

	BufferedImage getBackgroundImage() {return scaleImage(backgroundImage);}
	BufferedImage getScentImage() {
		return scaleImage(scentImage);
	}
	BufferedImage getObjectsImage() {
		return scaleImage(objectsImage);
	}

	BufferedImage scaleImage(BufferedImage imageToScale) {
		if (scale == 1) return imageToScale;
		BufferedImage scaledImage = new BufferedImage(imageToScale.getWidth()*scale, imageToScale.getHeight()*scale, BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < imageToScale.getWidth(); x++) {
			for (int y = 0; y < imageToScale.getHeight(); y++) {
				for (int i = 0; i < scale; i++) {
					for (int j = 0; j < scale; j++) {
						scaledImage.setRGB(x*scale+i, y*scale+j, imageToScale.getRGB(x,y));
					}
				}
			}
		}
		return scaledImage;
	}

	void takeObject(Object objectToTake){
		for(int x = 0; x < objectToTake.getSize(); x++){
			for(int y = 0; y < objectToTake.getSize(); y++){
				tiles[objectToTake.getX()+x][objectToTake.getY()+y].cellOccupant = null;
				objectsImage.setRGB(objectToTake.getX()+x, objectToTake.getY()+y, new Color(255, 255, 255,0).getRGB());
			}
		}
	}

	void placeObject(Object objectToPlace){
		for(int x = 0; x < objectToPlace.getSize(); x++){
			for(int y = 0; y < objectToPlace.getSize(); y++){
				tiles[objectToPlace.getX()+x][objectToPlace.getY()+y].cellOccupant = objectToPlace;
				objectsImage.setRGB(objectToPlace.getX()+x, objectToPlace.getY()+y, objectToPlace.getColor().getRGB());
			}
		}
	}

	void decreaseScentValues(int value, int maxvalue) {
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				tiles[x][y].decreaseScentValue(value, maxvalue);
			}
		}
	}
	void spreadScentValues(int value, int maxvalue) {
		int Tab[][];
		Tab = new int[width][height];
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				//Tab[x][y]=0;
				if(tiles[x][y].scentValue>0) {
					Tab[x][y]=1;
					if (x - 1 > 0) Tab[x - 1][y] = 1;
					if (y - 1 > 0) Tab[x][y - 1] = 1;
					if (x + 1 < width ) Tab[x + 1][y] = 1;
					if (y + 1 < height) Tab[x][y + 1] = 1;
				}
			}
		}
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				if(Tab[x][y]==1)tiles[x][y].decreaseScentValue(-1*value, maxvalue);
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
		int randX = random.nextInt(width/10, width - width/5);
		int randY = random.nextInt(height/10, height - height/5);

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
