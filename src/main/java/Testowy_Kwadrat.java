import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Testowy_Kwadrat extends Object{
    GameMap gameMap;
    int k; //kierunek w którym idzie mrówka
    Random random;
    Testowy_Kwadrat(int x, int y, int size, Random random, GameMap gameMap)
    {
        super(x, y, size, random, gameMap);
        this.gameMap=gameMap;
        this.random=random;
        k = 0;//random.nextInt(0,4);
    }
    @Override
    void move(){
        int tab[] = {-1,-1,-1,-1};
        if(x+1<gameMap.getWidth()){tab[0]=gameMap.tiles[x+1][y].scentValue;}
        if(x-1>0){tab[0]=gameMap.tiles[x-1][y].scentValue;}
        if(y+1<gameMap.getHeight()){tab[0]=gameMap.tiles[x][y+1].scentValue;}
        if(y-1>0){tab[0]=gameMap.tiles[x][y-1].scentValue;}
        gameMap.tiles[x][y].scentValue+=0;
        int z;
         switch (k)
        {
            case 0:
                z=nos(tab[0], tab[3], tab[2]);
                if(z==0){x+=10;}
                if(z==1){y-=10;k=3;}
                if(z==2){y+=10;k=2;}
                break;
            case 1:
                z=nos(tab[1], tab[2], tab[3]);
                if(z==0){x-=10;}
                if(z==1){y+=10;k=2;}
                if(z==2){y-=10;k=3;}
                break;
            case 2:
                z=nos(tab[2], tab[0], tab[1]);
                if(z==0){y+=10;}
                if(z==1){x+=10;k=0;}
                if(z==2){x-=10;k=1;}
                break;
            case 3:
                z=nos(tab[3], tab[1], tab[0]);
                if(z==0){y-=10;}
                if(z==1){x-=10;k=1;}
                if(z==2){x+=10;k=0;}
                break;
        }
        if(x+1>gameMap.getWidth()||x<1||gameMap.getHeight()<y+1||y<1){x=250;y=250;}
    }
    int nos(int s, int r, int l){ //s-prosto r-prawo l-lewo
        if( s > r && s > l )
        {return 0;}
        if( s < r || s < l)
        {
            if(r==l)
            {
                int z= random.nextInt(0,2);
                if(z==0){return 1;}
                else{return 2;}
            }
            if(r>l)
            {return 1;}
            else
            {return 2;}
        }
        if( s == l || s == r )
        {
            int z = random.nextInt(0, 21);
            System.out.println(z);
            if(z<19){return 0;}//80% szans że mrówka pujdzie prosto 20% że zmieni kierunek
            else
            {
                if( s == r && s == l)
                {
                    if(z==19){return 1;}
                    if(z==20){return 2;}
                }
                if( s == r ){return 1;}
                if( s == l){return 2;}
            }

        }
        return 3;
    }
    @Override
    Color getColor() {return new Color(0,0,0);}

        // Epilepsja
//    @Override
//    Color getColor() {return new Color(new Random().nextInt(0,255),new Random().nextInt(0,255),new Random().nextInt(0,255));}
}
