import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Ant_Colony extends JPanel {
    private Random random;
    private ArrayList<Object> objects;
    private Display display;
    private GameMap gameMap;

    public Ant_Colony(String[] args)
    {
        objects = new ArrayList<>();
        gameMap = new GameMap();
        display = new Display(args, objects, gameMap);
        random = new Random(3);
        int size = 2;//random.nextInt(1,20);


        gameMap.generateMap(random, display.getWidth(), display.getHeight());
        for (int i = 0; i < 1 ; i++) {
            int wid = random.nextInt(10, gameMap.getWidth() - 10);
            int hei = random.nextInt(10, gameMap.getHeight() - 10);
            objects.add(new Testowy_Kwadrat(250, 250, size, random, gameMap));
        }

//         Timer z pętlą symulacji na innym wątku
        Timer timer = new Timer();
        timer.schedule(new MainLoop(), 0, (long) 2000);
    }

    private class MainLoop extends TimerTask {
        @Override
        public void run() {
            objects.forEach(Object::move);
            display.repaint();
            for (int i = 0; i < gameMap.getWidth() ; i++){
                for (int j = 0; j < gameMap.getHeight() ; j++){//zmniejsza zapach
                    if(gameMap.tiles[i][j].scentValue>0) {
                        gameMap.tiles[i][j].scentValue -= 1;
                    }
                }
            }

        }
    }

    public static void main(String[] args) {
        Ant_Colony simulation = new Ant_Colony(args);
    }
}