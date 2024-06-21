import java.awt.*;
import java.util.Random;

public class Food extends Object{
	public static int foodCounter = 0;
	private int foodValue;
	Food(int x, int y, int size, Random random, GameMap gameMap) {
		super(x, y, size, random, gameMap);
		foodValue = 1;
		foodCounter+=foodValue;
	}
	@Override
	void action(){}
	Food(int x, int y, int size, Random random, GameMap gameMap, int foodValue) {
		super(x, y, size, random, gameMap);
		this.foodValue = foodValue;
		foodCounter+=foodValue;
	}
	public void decreaseFood() {
		foodValue--;
		foodCounter--;
		if (foodValue <= 0) {
			death();
			gameMap.takeObject(this);
		}
	}
	public Color getColor(){ return new Color(250, 98, 98);}

	@Override
	public String toString() {
		return super.toString() +
			"|" + foodValue;
	}
}
