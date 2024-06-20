import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class GameMap {
	private int width, height;
	public Tile[][] tiles;
	private BufferedImage backgroundImage, scentImage, objectsImage;
	Random random;
	ArrayList<Object> objects;
	public final int scale;
	int xn, yn, foodCooldown = 100000, foodTimer = 0;
	GameMap(Random random, ArrayList<Object> objects, int scale) {
		this.random = random;
		this.objects = objects;
		this.scale = scale;
	}

	public class Tile{
		private final int x, y;
		private int scentValue;
		public Object cellOccupant;
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
			if(scentValue>255) scentValue=255;
			if(scentValue<-255) scentValue=-255;
			if(scentValue<0) scentImage.setRGB(x, y,	new Color(255, 0, 0, Math.min(-scentValue,255)).getRGB());
			else  scentImage.setRGB(x, y,	new Color(255, 255, 255, Math.min(scentValue,255)).getRGB());
			//scentImage.setRGB(x, y, new Color(0, 0, 0, Math.min(scentValue,255)).getRGB());
		}
		void setScentValue(int value)
		{
			scentValue=value;
			if(scentValue>255) scentValue=255;
			if(scentValue<-255) scentValue=-255;
			if(scentValue<0) scentImage.setRGB(x, y,	new Color(255, 0, 0, Math.min(-scentValue,255)).getRGB());
			else  scentImage.setRGB(x, y,	new Color(255, 255, 255, Math.min(scentValue,255)).getRGB());
			//scentImage.setRGB(x, y, new Color(0, 0, 0, Math.min(scentValue,255)).getRGB());
		}
	}

	public void generateMap(int frameWidth, int frameHeight) {
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

				int minRand = -20;
				int maxRand = 20;
				switch (random.nextInt(0,1)) {
					case 0:
							// grass
//						new Color(31, 135, 23);
						tiles[x][y].setTileColor(new Color(
								51 + random.nextInt(minRand,maxRand-minRand),
								205 + random.nextInt(minRand,maxRand-minRand),
								43 + random.nextInt(minRand,maxRand-minRand)));
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
				}
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
	public BufferedImage getScentImage() {return scentImage;}
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
	public void spreadScentValues(int value, int maxvalue) {
		int Tab[][];
		Tab = new int[width][height];

		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){

				if(tiles[x][y].scentValue<0) {
					if(Tab[x][y]>tiles[x][y].scentValue)Tab[x][y]=tiles[x][y].scentValue+1;
					if(x-1>0 && tiles[x-1][y].scentValue<=0 && tiles[x-1][y].scentValue>tiles[x][y].scentValue+1 && Tab[x-1][y]>tiles[x][y].scentValue+1 && tiles[x-1][y].cellOccupant==null) Tab[x-1][y]=tiles[x][y].scentValue+1;
					if(y-1>0 && tiles[x][y-1].scentValue<=0 && tiles[x][y-1].scentValue>tiles[x][y].scentValue+1 && Tab[x][y-1]>tiles[x][y].scentValue+1 && tiles[x][y-1].cellOccupant==null) Tab[x][y-1]=tiles[x][y].scentValue+1;
					if(x+1<width && tiles[x+1][y].scentValue<=0 && tiles[x+1][y].scentValue>tiles[x][y].scentValue+1 && Tab[x+1][y]>tiles[x][y].scentValue+1 && tiles[x+1][y].cellOccupant==null) Tab[x+1][y]=tiles[x][y].scentValue+1;
					if(y+1<height && tiles[x][y+1].scentValue<=0 && tiles[x][y+1].scentValue>tiles[x][y].scentValue+1 && Tab[x][y+1]>tiles[x][y].scentValue+1 && tiles[x][y+1].cellOccupant==null) Tab[x][y+1]=tiles[x][y].scentValue+1;
				}
				if(tiles[x][y].scentValue>0) {
					if(Tab[x][y]<tiles[x][y].scentValue)Tab[x][y]=tiles[x][y].scentValue-1;
					if(tiles[x][y].scentValue>1) {
						if (x - 1 > -1 && tiles[x - 1][y].scentValue < 1 && tiles[x - 1][y].scentValue < tiles[x][y].scentValue - 1 && Tab[x - 1][y] < tiles[x][y].scentValue - 1 && tiles[x - 1][y].cellOccupant == null)
							Tab[x - 1][y] = 1;//(tiles[x][y].scentValue-(tiles[x][y].scentValue%100))/100;
						if (y - 1 > -1 && tiles[x][y - 1].scentValue < 1 && tiles[x][y - 1].scentValue < tiles[x][y].scentValue - 1 && Tab[x][y - 1] < tiles[x][y].scentValue - 1 && tiles[x][y - 1].cellOccupant == null)
							Tab[x][y - 1] = 1;//(tiles[x][y].scentValue-(tiles[x][y].scentValue%100))/100;
						if (x + 1 < width && tiles[x + 1][y].scentValue < 1 && tiles[x + 1][y].scentValue < tiles[x][y].scentValue - 1 && Tab[x + 1][y] < tiles[x][y].scentValue - 1 && tiles[x + 1][y].cellOccupant == null)
							Tab[x + 1][y] = 1;//(tiles[x][y].scentValue-(tiles[x][y].scentValue%100))/100;
						if (y + 1 < height && tiles[x][y + 1].scentValue < 1 && tiles[x][y + 1].scentValue < tiles[x][y].scentValue - 1 && Tab[x][y + 1] < tiles[x][y].scentValue - 1 && tiles[x][y + 1].cellOccupant == null)
							Tab[x][y + 1] = 1;//(tiles[x][y].scentValue-(tiles[x][y].scentValue%100))/100;
					}
				}
			}
		}
		for(int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				tiles[x][y].setScentValue(Tab[x][y]);
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
