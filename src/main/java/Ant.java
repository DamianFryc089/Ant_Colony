import java.awt.*;
import java.util.Random;

public class Ant extends Object{
    static int antCounter = 0;
    boolean carryFood = false;
    int lifeTime = 99999999; // póki co nieskończoność
    int k, b; //kierunek w krórym idzie mrówka i pamięć mrówki(NIE DOTYKAĆ!!!)
    int p=100;//cierpliwość mrówki(określa kiedy jej się znudzi łarzenie za zapachem innej mówki)
    int f=10;//jak dlugo mrówka będzie miała focha
    int a=50; // 2/a szansa na to że mrówka losowo zmieni kierunek(tylko w tedy gdy mrówka nie wyczuwa zapachu)
    Ant(int x, int y, int size, Random random, GameMap gameMap) {
        super(x, y, size, random, gameMap);
        antCounter++;
        k = random.nextInt(0,4);
    }

    @Override
    void action(){
        gameMap.takeObject(this);
        lifeTime--;
        if (lifeTime <= 0) {
            death();
            return;
        }
//        gameMap.takeObject(this);     // zamiast tego samego powyżej, aby były widoczne zwłoki mrówek, dopóki inna mrówka po nich nie przejdzie

        int tab[] = {-1,-1,-1,-1};
        if(x+1<gameMap.getWidth()){tab[0]=gameMap.tiles[x+1][y].getScentValue();}
        if(x-1>0){tab[1]=gameMap.tiles[x-1][y].getScentValue();}
        if(y+1<gameMap.getHeight()){tab[2]=gameMap.tiles[x][y+1].getScentValue();}
        if(y-1>0){tab[3]=gameMap.tiles[x][y-1].getScentValue();}
        gameMap.tiles[x][y].increaseScentValue(30);
        int z;
        switch (k)
        {
            case 0:
                z=logika(tab[0], tab[3], tab[2]);
                if(z==0){x+=1;}
                if(z==1){y-=1;k=3;}
                if(z==2){y+=1;k=2;}
                break;
            case 1:
                z=logika(tab[1], tab[2], tab[3]);
                if(z==0){x-=1;}
                if(z==1){y+=1;k=2;}
                if(z==2){y-=1;k=3;}
                break;
            case 2:
                z=logika(tab[2], tab[0], tab[1]);
                if(z==0){y+=1;}
                if(z==1){x+=1;k=0;}
                if(z==2){x-=1;k=1;}
                break;
            case 3:
                z=logika(tab[3], tab[1], tab[0]);
                if(z==0){y-=1;}
                if(z==1){x-=1;k=1;}
                if(z==2){x+=1;k=0;}
                break;
        }
        if(x+1>gameMap.getWidth()||x<1||gameMap.getHeight()<y+1||y<1){x= gameMap.getWidth()/2;y= gameMap.getHeight()/2;}
        gameMap.placeObject(this);
    }
    int logika(int s, int r, int l){ //s-prosto r-prawo l-lewo
        if(b>p)
        {
            s*=-1;
            r*=-1;
            l*=-1;
            b++;
            if(b>p+f){b=0;}
        }
        if( s > r && s > l )
        {b++;return 0;}
        if( s < r || s < l)
        {
            b++;
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
            int z = random.nextInt(0, a+1);
            if(z<a-1){return 0;}//90% szans że mrówka pujdzie prosto 10% że zmieni kierunek
            else
            {
                if( s == r && s == l)
                {
                    if(z==a-1){return 1;}
                    if(z==a){return 2;}
                }
                if( s == r ){return 1;}
                if( s == l){return 2;}
            }

        }
        return 3;
    }
    @Override
    Color getColor() {return new Color(0,0,0);}

    @Override
    void death() {
        super.death();
        antCounter--;
    }
}
