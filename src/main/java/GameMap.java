import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class GameMap {
	private int width, height;
	Tile[][] tiles;
	private BufferedImage backgroundImage, scentImage;

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

	void generateMap(Random random, int width, int height) {
		this.width = width;
		this.height = height;
		tiles = new Tile[width][height];
		backgroundImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		scentImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				tiles[x][y] = new Tile(x, y);
				tiles[x][y].setTileColor(new Color(255, 255, 255));
//				tiles[x][y].setTileColor(new Color(31, 135, 23));

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


	int getWidth(){return width;}
	int getHeight(){return height;}
}
