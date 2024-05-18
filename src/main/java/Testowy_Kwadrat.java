import java.awt.*;
import java.util.Random;

public class Testowy_Kwadrat extends Object{
    Testowy_Kwadrat(int mapWidth, int mapHeight, int x, int y, int size, Random random)
    {
        super(mapWidth, mapHeight, x, y, size, random);
        xz = random.nextInt(1,4);
        xz = xz - 2 * xz * random.nextInt(0,2);

        yz = random.nextInt(1,4);
        yz = yz - 2 * yz * random.nextInt(0,2);
    }
    @Override
    void move(){
        x += xz;
        if (x + size >= mapWidth || x < 0) {xz*=-1; x+=2*xz;}
        y += yz;
        if (y + size >= mapHeight || y < 0) {yz*=-1; y+=2*yz;}
    }
    @Override
    Color getColor() {return new Color(0,255,0);}

        // Epilepsja
//    @Override
//    Color getColor() {return new Color(new Random().nextInt(0,255),new Random().nextInt(0,255),new Random().nextInt(0,255));}
}
