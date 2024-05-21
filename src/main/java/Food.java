import java.awt.*;
import java.util.Random;

public class Food extends Object{
	static int foodCounter = 0;
	int foodValue;
	Food(int x, int y, int size, Random random, GameMap gameMap) {
		super(x, y, size, random, gameMap);
		foodValue = 100;
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
}
