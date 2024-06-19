import java.awt.*;
import java.util.Random;

public class Food extends Object{
	static int foodCounter = 0;
	int foodValue;
	Food(int x, int y, int size, Random random, GameMap gameMap) {
		super(x, y, size, random, gameMap);
		foodValue = 1;
		foodCounter+=foodValue;
	}
	@Override
	void action(){
		//gameMap.tiles[x][y].setScentValue(100);
	}
	Food(int x, int y, int size, Random random, GameMap gameMap, int foodValue) {
		super(x, y, size, random, gameMap);
		this.foodValue = foodValue;
		foodCounter+=foodValue;
	}
	void decreaseFood() {
		foodValue--;
		foodCounter--;
		if (foodValue <= 0) {
			death();
			gameMap.takeObject(this);
		}
	}
	Color getColor(){ return new Color(250, 98, 98);}

	@Override
	public String toString() {
		return super.toString() +
			"|" + foodValue;
	}
}
