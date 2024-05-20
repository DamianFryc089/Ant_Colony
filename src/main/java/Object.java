import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Object {
    int x, y, size;
    Random random;
    GameMap gameMap;

    Object(int x, int y, int size, Random random, GameMap gameMap)
    {
        this.x = x;
        this.y = y;
        this.size = size;
        this.random = random;
        this.gameMap = gameMap;
        gameMap.placeObject(this);
    }
    void move(){}
    Color getColor(){ return new Color(255, 0, 0);}

    int getX(){ return x;}
    int getY(){ return y;}
    int getSize(){ return size;}
}
