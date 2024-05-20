import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

class AntNest extends Object {
	ArrayList<Object> objects;

	AntNest(int x, int y, int size, Random random, GameMap gameMap, ArrayList<Object> objects)
	{
		super(x, y, size, random, gameMap);
		this.objects = objects;
	}

	void spawnAnt()
	{

	}

	@Override
	Color getColor() {return new Color(119, 52, 29);}
}
