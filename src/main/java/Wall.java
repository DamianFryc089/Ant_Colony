import java.awt.*;
import java.util.Random;

public class Wall extends Object{
	Wall(int x, int y, int size, Random random, GameMap gameMap) {
		super(x, y, size, random, gameMap);
	}

	Color getColor(){ return new Color(120, 120, 120);}
}
