import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Ant_Colony extends JPanel {
    private final int mapWidth;
    private final int mapHeight;
    private Random random;
    private ArrayList<Object> objects;
    private Display display;

    public Ant_Colony(String[] args)
    {
        objects = new ArrayList<>();
        display = new Display(args, objects);
        mapWidth = display.getWidth();
        mapHeight = display.getHeight();
        random = new Random(3);

        for (int i = 0; i < 100 ; i++) {
            int size = random.nextInt(1,20);
            int wid = random.nextInt(0, mapWidth -size);
            int hei = random.nextInt(0, mapHeight -size);
            objects.add(new TestowyKwadrat(mapWidth, mapHeight, wid, hei, size, random));
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