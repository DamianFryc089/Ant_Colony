import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class GameMap {
	private int width, height;
	public Tile[][] tiles;
	private BufferedImage backgroundImage, foodScentImage, antScentImage, objectsImage;
	Random random;
	ArrayList<Object> objects;
	public final int scale;
	int xn, yn, foodCooldown = 100000, foodTimer = 0;
	private final float MAX_VAL = 100;
	private float EVAPORATION_RATE = .999F;

	GameMap(Random random, ArrayList<Object> objects, int scale) {
		this.random = random;
		this.objects = objects;
		this.scale = scale;
	}

	public class Tile{
		private final int x, y;
		private float antScentValue;
		private float foodScentValue;
		public Object cellOccupant;
		Color tileColor;

		Tile(int x, int y) {
			this.x = x;
			this.y = y;
			antScentValue = 0.0F;
			cellOccupant = null;
		}
		void setTileColor(Color newTileColor) {tileColor = newTileColor;}
		float getAntScentValue() {return antScentValue;}
		void setAntScentValue(float value)
		{
			if(value>255)value=255;
			if(value<0)value=0;
			antScentValue = value;
			antScentImage.setRGB(x, y,	new Color(255, 0, 0, Math.min((int)(antScentValue*2.55),255)).getRGB());
		}
		void setFoodScentValue(float value)
		{
			foodScentValue = value;
			foodScentImage.setRGB(x, y,	new Color(0, 0, 255, Math.min((int)(foodScentValue*2.55),255)).getRGB());
		}
	}

	public void generateMap(int frameWidth, int frameHeight) {
		this.width = frameWidth/scale;
		this.height = frameHeight/scale;
		tiles = new Tile[width][height];
		backgroundImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		foodScentImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		antScentImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		objectsImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				tiles[x][y] = new Tile(x, y);
				tiles[x][y].setTileColor(new Color(255, 255, 255));
//				tiles[x][y].setTileColor(new Color(57, 152, 49));

				int minRand = -20;
				int maxRand = 20;
				tiles[x][y].setTileColor(new Color(
				51 + random.nextInt(minRand,maxRand-minRand),
				205 + random.nextInt(minRand,maxRand-minRand),
				43 + random.nextInt(minRand,maxRand-minRand)));
			}
		}
		xn = random.nextInt(width/10,width - width/5 - 25);
		yn = random.nextInt(height/10,height - height/5 - 25);
		generateImage();
		new AntNest(xn, yn,25, random, this);
		generateWalls(300);
	}

	private void generateImage(){
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				backgroundImage.setRGB(x, y, tiles[x][y].tileColor.getRGB());
			}
		}
	}

	public BufferedImage getBackgroundImage() {return backgroundImage;}
	public BufferedImage getFoodScentImage() {return foodScentImage;}
	public BufferedImage getAntScentImage() {return antScentImage;}
	public BufferedImage getObjectsImage() {return objectsImage;}

	public void takeObject(Object objectToTake){
		for(int x = 0; x < objectToTake.getSize(); x++){
			for(int y = 0; y < objectToTake.getSize(); y++){
				tiles[objectToTake.getX()+x][objectToTake.getY()+y].cellOccupant = null;
				objectsImage.setRGB(objectToTake.getX()+x, objectToTake.getY()+y, new Color(255, 255, 255,0).getRGB());
			}
		}
	}

	public void placeObject(Object objectToPlace){
		for(int x = 0; x < objectToPlace.getSize(); x++){
			for(int y = 0; y < objectToPlace.getSize(); y++){
				tiles[objectToPlace.getX()+x][objectToPlace.getY()+y].cellOccupant = objectToPlace;
				objectsImage.setRGB(objectToPlace.getX()+x, objectToPlace.getY()+y, objectToPlace.getColor().getRGB());
			}
		}
	}

	void decreaseScentValues() {
		for (int i=0; i<tiles.length; i++) {
			for (int j=0; j<tiles[0].length; j++) {
				tiles[i][j].antScentValue *= EVAPORATION_RATE;
				tiles[i][j].foodScentValue *= EVAPORATION_RATE;
			}
		}
	}

	private void generateWalls(int count){
		while (count > 0) {
			int randX = random.nextInt(width);
			int randY = random.nextInt(height);

			int verticalDirection = random.nextBoolean() ? -1 : 1;

			while (true) {
				if (randX < width && randY >= 0 && randY < height && tiles[randX][randY].cellOccupant == null) {
					tiles[randX][randY].cellOccupant = new Wall(randX, randY, 1, random, this);
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
	public void generateFoodField(int size){
		int randX;
		int randY;

		int proba = 0;
		do {
			randX = random.nextInt(width/10, width - width/5);
			randY = random.nextInt(height/10, height - height/5);
			proba++;
		}while (Math.abs(randX - xn) < width / (4+proba*0.1) && Math.abs(randY - yn) < height / (4+proba*0.1));

		for (int i = 0; i < size; i++) {
			for (int j = size - i; j < size + i; j++) {
				generateFood(randX+j, randY + i);
			}
		}
		for (int i = 0; i < size; i++) {
			for (int j = i; j < 2*size - i ; j++) {
				generateFood(randX+j, randY + i + size);
			}
		}
	}
	private void generateFood(int x, int y)
	{
		if (x >= 0 && x < width && y >= 0 && y < height && tiles[x][y].cellOccupant == null) {
			tiles[x][y].cellOccupant = new Food(x, y, 1, random, this);
		}
	}

	public int getWidth(){return width;}
	public int getHeight(){return height;}
	public int getNestx(){return xn+12;}
	public int getNesty(){return yn+12;}
}
