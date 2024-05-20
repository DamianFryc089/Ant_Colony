import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class AntColony extends JPanel {
    private Random random;
    private ArrayList<Object> objects;
    private Display display;
    private GameMap gameMap;
    private final int targetFPS = 60;
    private final int scale = 1; // max 4? Może wywalić error jak będzie więcej

    public AntColony(String[] args) {
        random = new Random(3);
        objects = new ArrayList<>();
        gameMap = new GameMap(random, objects, scale);
        display = new Display(args, objects, gameMap);
        int size = 1;//random.nextInt(1,20);

        gameMap.generateMap(display.getWidth(), display.getHeight());
        for (int i = 0; i < 100 ; i++) {
            int wid = random.nextInt(10, gameMap.getWidth() - 10);
            int hei = random.nextInt(10, gameMap.getHeight() - 10);
            objects.add(new Ant(wid, hei, size, random, gameMap));
        }

//         Timer z pętlą symulacji na innym wątku
        Timer timer = new Timer();
        timer.schedule(new MainLoop(), 0, (long) 1000/targetFPS);
    }

    private class MainLoop extends TimerTask {
        @Override
        public void run() {
            gameMap.decreaseScentValues(10);
            objects.forEach(Object::move);
            display.repaint();
        }
    }

    public static void main(String[] args) {
        AntColony simulation = new AntColony(args);
    }
}