import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

/**
 * The GameMap class represents the game map where ants and other objects interact.
 * It manages the tiles, scents, objects, and various simulation mechanics.
 */
public class GameMap {
	private int width, height;
	public Tile[][] tiles;
	private BufferedImage backgroundImage, foodScentImage, antScentImage, objectsImage;
	Random random;
	ArrayList<Object> objects;
	public final int scale;
	int foodCooldown, foodTimer = 0;
    private int scentFading;

	/**
	 * Constructs a GameMap object.
	 *
	 * @param random the Random instance used for randomness
	 * @param objects the list of objects on the game map
	 * @param scale the scale of the game map
	 */
	GameMap(Random random, ArrayList<Object> objects, int scale) {
		this.random = random;
		this.objects = objects;
		this.scale = scale;
	}

	/**
	 * The Tile class represents a single tile on the game map.
	 * It stores the values of ant and food scents and a reference to the object that occupies it.
	 */
	public class Tile{
		private final int x, y;
		private float antScentValue;
		private float foodScentValue;
		public Object cellOccupant;
		Color tileColor;

		/**
		 * Constructs a Tile object.
		 *
		 * @param x the x-coordinate of the tile
		 * @param y the y-coordinate of the tile
		 */
		Tile(int x, int y) {
			this.x = x;
			this.y = y;
			antScentValue = 0.0F;
			cellOccupant = null;
		}
		/**
		 * Sets the color of the tile.
		 *
		 * @param newTileColor the new color of the tile
		 */
		void setTileColor(Color newTileColor) {tileColor = newTileColor;}

		/**
		 * Gets the ant scent value of the tile.
		 *
		 * @return the ant scent value
		 */
		float getAntScentValue() {return antScentValue;}

		/**
		 * Gets the food scent value of the tile.
		 *
		 * @return the food scent value
		 */
		float getFoodScentValue() {return foodScentValue;}

		/**
		 * Sets the ant scent value of the tile and updates the scent image.
		 *
		 * @param value the new ant scent value
		 */
		void setAntScentValue(float value)
		{
			antScentValue = Math.max(antScentValue, value);
			antScentImage.setRGB(x, y,	new Color(255, 0, 0, Math.min((int)(antScentValue*2.55),255)).getRGB());
		}

		/**
		 * Sets the food scent value of the tile and updates the scent image.
		 *
		 * @param value the new food scent value
		 */
		void setFoodScentValue(float value)
		{
			foodScentValue = Math.max(foodScentValue, value);
			foodScentImage.setRGB(x, y,	new Color(0, 0, 255, Math.min((int)(foodScentValue*2.55),255)).getRGB());
		}
	}

	/**
	 * Generates the game map with specified dimensions and simulation arguments.
	 *
	 * @param frameWidth the width of the frame
	 * @param frameHeight the height of the frame
	 * @param simulationArgs the simulation arguments
	 */
	public void generateMap(int frameWidth, int frameHeight, int[] simulationArgs) {
		foodCooldown = simulationArgs[3];
		scentFading = simulationArgs[4];
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
		int size = Math.min(25, Math.min(width,height) /12);
		int xn = random.nextInt(width/10,width - width/5 - size);
		int yn = random.nextInt(height/10,height - height/5 - size);
		generateImage();
		new AntNest(xn, yn, size, random, this, 0);
		generateWalls(simulationArgs[5]);
	}

	/**
	 * Generates the background image for the game map.
	 */
	private void generateImage(){
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				backgroundImage.setRGB(x, y, tiles[x][y].tileColor.getRGB());
			}
		}
	}

	/**
	 * Gets the background image of the game map.
	 *
	 * @return the background image
	 */
	public BufferedImage getBackgroundImage() {return backgroundImage;}

	/**
	 * Gets the food scent image of the game map.
	 *
	 * @return the food scent image
	 */
	public BufferedImage getFoodScentImage() {return foodScentImage;}

	/**
	 * Gets the ant scent image of the game map.
	 *
	 * @return the ant scent image
	 */
	public BufferedImage getAntScentImage() {return antScentImage;}

	/**
	 * Gets the objects image of the game map.
	 *
	 * @return the objects image
	 */
	public BufferedImage getObjectsImage() {return objectsImage;}

	/**
	 * Removes an object from the game map.
	 *
	 * @param objectToTake the object to remove
	 */
	public void takeObject(Object objectToTake){
		for(int x = 0; x < objectToTake.getSize(); x++){
			for(int y = 0; y < objectToTake.getSize(); y++){
				tiles[objectToTake.getX()+x][objectToTake.getY()+y].cellOccupant = null;
				objectsImage.setRGB(objectToTake.getX()+x, objectToTake.getY()+y, new Color(255, 255, 255,0).getRGB());
			}
		}
	}

	/**
	 * Places an object on the game map.
	 *
	 * @param objectToPlace the object to place
	 */
	public void placeObject(Object objectToPlace){
		for(int x = 0; x < objectToPlace.getSize(); x++){
			for(int y = 0; y < objectToPlace.getSize(); y++){
				tiles[objectToPlace.getX()+x][objectToPlace.getY()+y].cellOccupant = objectToPlace;
				objectsImage.setRGB(objectToPlace.getX()+x, objectToPlace.getY()+y, objectToPlace.getColor().getRGB());
			}
		}
	}

	/**
	 * Decreases the scent values on the game map over time.
	 */
	void decreaseScentValues() {
		for (int i=0; i<tiles.length; i++) {
			for (int j=0; j<tiles[0].length; j++) {

				tiles[i][j].antScentValue *= (float) scentFading / 10000;
				if (tiles[i][j].antScentValue > 5)
					antScentImage.setRGB(i, j,	new Color(255, 0, 0, Math.min((int)(tiles[i][j].antScentValue*2.55),255)).getRGB());

				if(tiles[i][j].antScentValue < 5)
					tiles[i][j].antScentValue = 0;

				tiles[i][j].foodScentValue *= (float) scentFading / 10000;
				if (tiles[i][j].foodScentValue > 5)
					foodScentImage.setRGB(i, j,	new Color(0, 0, 255, Math.min((int)(tiles[i][j].foodScentValue*2.55),255)).getRGB());

				if(tiles[i][j].foodScentValue < 5)
						tiles[i][j].foodScentValue = 0;
			}
		}
	}

	/**
	 * Generates walls on the game map.
	 *
	 * @param count the number of walls to generate
	 */
	private void generateWalls(int count){
		while (count > 0) {
			int randX = random.nextInt(width);
			int randY = random.nextInt(height);

			int verticalDirection = random.nextBoolean() ? -1 : 1;

			while (true) {
				count--;
				if (randX < width && randY >= 0 && randY < height && tiles[randX][randY].cellOccupant == null) {
					new Wall(randX, randY, 1, random, this);
				}
				else
					break;

					// losowanie kolejnego muru lub przerwy
				int rand = random.nextInt(100);
				if (rand < 48) randX++;
				else if (rand < 96) randY+=verticalDirection;
				else break;
			}
		}
	}


	/**
	 * Generates food field on the game map.
	 *
	 * @param size the size of diamond-shaped food field
	 */
	public void generateFoodField(int size){
		int randX;
		int randY;

		int[] antNestPostion = {0,0};

        for (Object object : objects) {
            if (object.getClass() == AntNest.class) {
				antNestPostion[0] = object.x;
				antNestPostion[1] = object.y;
				break;
            }
        }

		int proba = 0;
		do {
			randX = random.nextInt(width/20, width - width/10);
			randY = random.nextInt(height/20, height - height/10);
			proba++;
		}while (Math.abs(randX - antNestPostion[0]) < width / (4+proba*0.1) && Math.abs(randY - antNestPostion[1]) < height / (4+proba*0.1));

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


	/**
	 * Generates food if a tile isn't occupied.
	 *
	 * @param x the x-coordinate of the food
	 * @param y the y-coordinate of the food
	 */
	private void generateFood(int x, int y)
	{
		if (x >= 0 && x < width && y >= 0 && y < height && tiles[x][y].cellOccupant == null) {
			tiles[x][y].cellOccupant = new Food(x, y, 1, random, this);
		}
	}

	/**
	 * Returns the width of the map.
	 *
	 * @return the width of the map
	 */
	public int getWidth(){return width;}

	/**
	 * Returns the height of the map.
	 *
	 * @return the height of the map
	 */
	public int getHeight(){return height;}

}
