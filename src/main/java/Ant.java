import java.awt.*;
import java.util.Random;

public class Ant extends Object{
    int p=4;
    Ant(int x, int y, int size, Random random, GameMap gameMap) {
        super(x, y, size, random, gameMap);
    }

    @Override
    void move(){
        gameMap.takeObject(this);

        int tab[] = {0,0,0,0};
        int Tab[] = {4,4,4,4};
        int z=0,m=0;
            // Kod przed zmianą pobierania i zmiany wartości zapachu
//        gameMap.tiles[x][y].scentValue+=10;
//        if(x+1 < gameMap.getWidth()){tab[0]=gameMap.tiles[x+1][y].scentValue;}
//        if(x-1 > 0){tab[1]=gameMap.tiles[x-1][y].scentValue;}
//        if(y+1 < gameMap.getWidth()){tab[2]=gameMap.tiles[x][y+1].scentValue;}
//        if(y-1 > 0){tab[3]=gameMap.tiles[x][y-1].scentValue;}
        gameMap.tiles[x][y].increaseScentValue(10);
        if(x+1 < gameMap.getWidth()){tab[0]=gameMap.tiles[x+1][y].getScentValue();}
        if(x-1 > 0){tab[1]=gameMap.tiles[x-1][y].getScentValue();}
        if(y+1 < gameMap.getWidth()){tab[2]=gameMap.tiles[x][y+1].getScentValue();}
        if(y-1 > 0){tab[3]=gameMap.tiles[x][y-1].getScentValue();}
        for(int i=0; i<4; i++)
        {
            if(tab[i]>m&&i!=p){
                z=i;
                m=tab[i];
            }
        }
        int i=0;
        for(;i<4; i++)
        {
            if(tab[i]==m&&i!=p){
                Tab[i]=i;
            }
        }
        z=Tab[random.nextInt(0, i)];
        if(z==0&&x+1 < gameMap.getWidth()){x+=1;p=1;}
        if(z==1&&x-1 > 0){x-=1;p=0;}
        if(z==2&&y+1 < gameMap.getHeight()){y+=1;p=3;}
        if(z==3&&y-1 > 0){y-=1;p=2;}

        gameMap.placeObject(this);
    }
    @Override
    Color getColor() {return new Color(0,0,0);}

        // Epilepsja
//    @Override
//    Color getColor() {return new Color(new Random().nextInt(0,255),new Random().nextInt(0,255),new Random().nextInt(0,255));}
}
