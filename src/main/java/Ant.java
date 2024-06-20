import java.awt.*;
import java.util.Random;

public class Ant extends Object{
    static int antCounter = 0;
    public boolean carryFood = false;
    private int k, f=0;//kierunek w krórym idzie mrówka(NIE DOTYKAĆ!!!)
    long t=0;
    private int a=50; // 2/a szansa na to że mrówka losowo zmieni kierunek(tylko w tedy gdy mrówka nie wyczuwa zapachu)
    Ant(int x, int y, int size, Random random, GameMap gameMap) {
        super(x, y, size, random, gameMap);
        antCounter++;
        k = random.nextInt(0,4);
    }
    Ant(int x, int y, int size, Random random, GameMap gameMap, boolean carryFood, int k, int a)
    {
        super(x, y, size, random, gameMap);
        this.carryFood = carryFood;
        this.k = k;
        this.a = a;
        antCounter++;
        int xn=x-gameMap.getNestx();
        int yn=y-gameMap.getNesty();
    }

    @Override
    void action(){
        gameMap.takeObject(this);
        int tab[] = {-256,-256,-256,-256};
        if(x+1<gameMap.getWidth() && (gameMap.tiles[x+1][y].cellOccupant == null || gameMap.tiles[x+1][y].cellOccupant.getClass() == Ant.class)){tab[0]=gameMap.tiles[x+1][y].getScentValue();}
        if(x-1>0 && (gameMap.tiles[x-1][y].cellOccupant == null || gameMap.tiles[x-1][y].cellOccupant.getClass() == Ant.class)){tab[1]=gameMap.tiles[x-1][y].getScentValue();}
        if(y+1<gameMap.getHeight() && (gameMap.tiles[x][y+1].cellOccupant == null || gameMap.tiles[x][y+1].cellOccupant.getClass() == Ant.class)){tab[2]=gameMap.tiles[x][y+1].getScentValue();}
        if(y-1>0 && (gameMap.tiles[x][y-1].cellOccupant == null || gameMap.tiles[x][y-1].cellOccupant.getClass() == Ant.class)){tab[3]=gameMap.tiles[x][y-1].getScentValue();}
        if(carryFood){
            t++;
            gameMap.tiles[x][y].setScentValue(250);
            for(int i=0; i<4;i++){
                if(tab[i]!=-256&&tab[i]<10)tab[i]=0;
                //if(tab[i]>150)tab[i]=250;
                if(tab[i]!=256 && t>500)tab[i]=0;
            }
        }
        else {
            if(tab[0]<1&&tab[1]<1&&tab[2]<1&&tab[3]<1)
                gameMap.tiles[x][y].setScentValue(-30);
        }

        int z;
        switch (k)
        {
            case 0:
                z=logika(tab[0], tab[3], tab[2]);
                if(z==3){k=1;x-=1;}
                if(z==0){x+=1;}
                if(z==1){y-=1;k=3;f++;}
                if(z==2){y+=1;k=2;f--;}
                break;
            case 1:
                z=logika(tab[1], tab[2], tab[3]);
                if(z==3){k=0;x+=1;}
                if(z==0){x-=1;}
                if(z==1){y+=1;k=2;f++;}
                if(z==2){y-=1;k=3;f--;}
                break;
            case 2:
                z=logika(tab[2], tab[0], tab[1]);
                if(z==3){k=3;y-=1;}
                if(z==0){y+=1;}
                if(z==1){x+=1;k=0;f++;}
                if(z==2){x-=1;k=1;f--;}
                break;
            case 3:
                z=logika(tab[3], tab[1], tab[0]);
                if(z==3){k=2;y+=1;}
                if(z==0){y-=1;}
                if(z==1){x-=1;k=1;f++;}
                if(z==2){x+=1;k=0;f--;}
                break;
        }
        if(x+1>gameMap.getWidth()||x<1||gameMap.getHeight()<y+1||y<1){x= gameMap.getWidth()/2;y= gameMap.getHeight()/2;}
        gameMap.placeObject(this);
    }
    private int logika(int s, int r, int l){
        int xn=x-gameMap.getNestx();
        int yn=y-gameMap.getNesty();
        if(nose()){
            if(carryFood) {
                carryFood = false;
                AntNest.foodInNest++;
                t=0;
            }
            else{
                gameMap.tiles[x][y].setScentValue(250);
                carryFood=true;}
            return 3;}
        if( s==-256 && r==-256 && l==-256)return 3;//s-prosto r-prawo l-lewo
        if(f>4){
            r=-255;
            f=0;
//            if(l!=-256){f=0;return 2;}
//            if(s!=-256){f=0;return 0;}

        }
        if(f<-4){
            l=-255;
            f=0;
//            if(r!=-256){f=0;return 1;}
//            if(s!=-256){f=0;return 0;}

        }
        if( s > r && s > l ) return 0;
        if( s < r || s < l) {
            if (r == l) {
                if (carryFood) {
                    switch (k) {
                        case 0:
                            if (yn > 0) return 1;
                            if (yn < 0) return 2;
                            break;
                        case 1:
                            if (yn > 0) return 2;
                            if (yn < 0) return 1;
                            break;
                        case 2:
                            if (xn > 0) return 2;
                            if (xn < 0) return 1;
                            break;
                        case 3:
                            if (xn > 0) return 1;
                            if (xn < 0) return 2;
                            break;
                    }
                }
                int z = random.nextInt(0, 2);
                if (z == 0) return 1;
                else return 2;
            }
            if (r > l) return 1;
            else return 2;
        }
        if (s == l && s == r && carryFood) {
            switch (k) {
                case 0:
                    if (yn > 0) return 1;
                    if (yn < 0) return 2;
                    if(xn<0) return 0;
                    break;
                case 1:
                    if (yn > 0) return 2;
                    if (yn < 0) return 1;
                    if(xn>0) return 0;
                    break;
                case 2:
                    if (xn > 0) return 2;
                    if (xn < 0) return 1;
                    if(yn<0) return 0;
                    break;
                case 3:
                    if (xn > 0) return 1;
                    if (xn < 0) return 2;
                    if(yn>0) return 0;
                    break;
            }
        }
        if (s == l && s == r) {
            int z = random.nextInt(0, a + 1);
            if (z < a - 1) return 0;
            if (z == a - 1) return 1;
            if (z == a) return 2;
        }
        if (s == r) {
            return 1;
        }
        return 2;
    }
    private boolean nose()
    {
        if(!carryFood) {
            if (x + 1 < gameMap.getWidth() && gameMap.tiles[x + 1][y].cellOccupant != null && gameMap.tiles[x + 1][y].cellOccupant.getClass() == Food.class){
                ((Food) gameMap.tiles[x + 1][y].cellOccupant).decreaseFood();
                gameMap.tiles[x + 1][y].increaseScentValue(250);
                return true;}
            if (y + 1 < gameMap.getHeight() && gameMap.tiles[x][y + 1].cellOccupant != null && gameMap.tiles[x][y + 1].cellOccupant.getClass() == Food.class){
                ((Food) gameMap.tiles[x][y + 1].cellOccupant).decreaseFood();
                gameMap.tiles[x][y + 1].increaseScentValue(250);
                return true;}
            if (x - 1 > 0 && gameMap.tiles[x - 1][y].cellOccupant != null && gameMap.tiles[x - 1][y].cellOccupant.getClass() == Food.class){
                ((Food) gameMap.tiles[x - 1][y].cellOccupant).decreaseFood();
                gameMap.tiles[x - 1][y].increaseScentValue(250);
                return true;}
            if (y - 1 > 0 && gameMap.tiles[x][y - 1].cellOccupant != null && gameMap.tiles[x][y - 1].cellOccupant.getClass() == Food.class){
                ((Food) gameMap.tiles[x][y - 1].cellOccupant).decreaseFood();
                gameMap.tiles[x][y - 1].increaseScentValue(250);
                return true;}
        }
        else {
            if (x + 1 < gameMap.getWidth() && gameMap.tiles[x + 1][y].cellOccupant != null && gameMap.tiles[x + 1][y].cellOccupant.getClass() == AntNest.class)
                return true;
            if (y + 1 < gameMap.getHeight() && gameMap.tiles[x][y + 1].cellOccupant != null && gameMap.tiles[x][y + 1].cellOccupant.getClass() == AntNest.class)
                return true;
            if (x - 1 > 0 && gameMap.tiles[x - 1][y].cellOccupant != null && gameMap.tiles[x - 1][y].cellOccupant.getClass() == AntNest.class)
                return true;
            if (y - 1 > 0 && gameMap.tiles[x][y - 1].cellOccupant != null && gameMap.tiles[x][y - 1].cellOccupant.getClass() == AntNest.class)
                return true;
        }
        return false;
    }
    @Override
    public Color getColor() {return new Color(0,0,0);}

    @Override
    void death() {
        super.death();
        antCounter--;
    }

    @Override
    public String toString() {
        return super.toString() +
                "|" + carryFood +
                "|" + k +
                "|" + a;
    }
}
