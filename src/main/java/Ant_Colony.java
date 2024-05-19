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
        int size = 1;//random.nextInt(1,20);


        gameMap.generateMap(random, display.getWidth(), display.getHeight());
        for (int i = 0; i < 100 ; i++) {
            int wid = random.nextInt(10, gameMap.getWidth() - 10);
            int hei = random.nextInt(10, gameMap.getHeight() - 10);
            objects.add(new Testowy_Kwadrat(wid, hei, size, random, gameMap));
        }

//         Timer z pętlą symulacji na innym wątku
        Timer timer = new Timer();
        timer.schedule(new MainLoop(), 0, (long) 1000/60);
    }

    private class MainLoop extends TimerTask {
        @Override
        public void run() {
            objects.forEach(Object::move);
            display.repaint();
        }
    }

    public static void main(String[] args) {
        Ant_Colony simulation = new Ant_Colony(args);
    }
}