import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Object {
    protected int x;
    protected int y;
    protected int xz;
    protected int yz;
    protected int size;
    protected int mapWidth;
    protected int mapHeight;

    Object(int mapWidth, int mapHeight, int x, int y, int size, Random random)
    {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.x = x;
        this.y = y;
        this.size = size;
    }
    void move(){}
    Color getColor(){ return new Color(255,255,255);}

    int getX(){ return x;}
    int getY(){ return y;}
    int getSize(){ return size;}
}
