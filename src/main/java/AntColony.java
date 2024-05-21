import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class AntColony extends JPanel {
    Random random;
    ArrayList<Object> objects;
    Display display;
    GameMap gameMap;
    final int targetFPS = 60;
    final int scale = 2; // max 4? Może wywalić error jak będzie więcej
    long tick = 0;

    public AntColony(String[] args) {
        random = new Random(3);
        objects = new ArrayList<>();
        gameMap = new GameMap(random, objects, scale);
        display = new Display(args, this);

        gameMap.generateMap(display.getWidth(), display.getHeight());

//        for (int i = 0; i < 100 ; i++) {
//            int wid = random.nextInt(10, gameMap.getWidth() - 10);
//            int hei = random.nextInt(10, gameMap.getHeight() - 10);
//            objects.add(new Ant(wid, hei, 1, random, gameMap));
//        }

//         Timer z pętlą symulacji na innym wątku
        Timer timer = new Timer();
        timer.schedule(new MainLoop(), 0, (long) 1000/targetFPS);
    }

    private class MainLoop extends TimerTask {
        @Override
        public void run() {
            if (tick % 100000 == 0)
                gameMap.generateFoodField(25);

//            gameMap.spreadScentValues(1,255);//rozprzestrzenianie się zapachu na sąsiednie pola
            gameMap.decreaseScentValues(1, 255);
            for (int i = 0; i < objects.size(); i++) objects.get(i).action();
            display.repaint();
            tick++;
        }
    }

    public static void main(String[] args) {
        AntColony simulation = new AntColony(args);
    }
}