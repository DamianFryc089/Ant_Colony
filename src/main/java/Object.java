import java.awt.*;
import java.util.Random;

public abstract class Object {
    protected int x, y, size;
    protected Random random;
    protected GameMap gameMap;

    Object(int x, int y, int size, Random random, GameMap gameMap)
    {
        this.x = x;
        this.y = y;
        this.size = size;
        this.random = random;
        this.gameMap = gameMap;
        gameMap.placeObject(this);
        gameMap.objects.add(this);
    }
    void death() {
        gameMap.takeObject(this);
        gameMap.objects.remove(this);
    }
    abstract void action();
    public Color getColor(){ return new Color(255, 0, 0);}
    public int getX(){ return x;}
    public int getY(){ return y;}
    public int getSize(){ return size;}

    @Override
    public String toString() {
        return this.getClass() + "|" + x + "|" + y + "|" + size;
    }
}
