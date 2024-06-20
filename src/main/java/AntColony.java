import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class AntColony{
    Random random;
    long seed;
    ArrayList<Object> objects;
    private final Display display;
    public final GameMap gameMap;
    float targetFPS = 60;
    final int scale = 3;
    long tick = 0;
    boolean isPaused = false;
    private Timer timer;

    public AntColony(String[] args) {
        random = new Random();
        seed = random.nextInt();
//        seed = 2;
        random.setSeed(seed);

        objects = new ArrayList<>();
        gameMap = new GameMap(random, objects, scale);
        display = new Display(args, this);

        gameMap.generateMap(display.getWidth(), display.getHeight());

            // Timer z pętlą symulacji na innym wątku
        timer = new Timer();
        timer.schedule(new MainLoop(), 0, (long) 1000/(int)targetFPS);
    }


    public class MainLoop extends TimerTask {
        @Override
        public void run() {
            if (isPaused)
                return;
            if (--gameMap.foodTimer <= 0) {
                gameMap.foodTimer = gameMap.foodCooldown;
                gameMap.generateFoodField(20);
            }
            gameMap.decreaseScentValues();
            for (int i = 0; i < objects.size(); i++) objects.get(i).action();
            display.repaint();
            tick++;
        }
    }

    public void updateFPS()
    {
        if (timer != null) timer.cancel();
        timer = new Timer();
        timer.schedule(new MainLoop(), 0, (long) 1000/(int)targetFPS);
    }
    public static void main(String[] args) {
        AntColony simulation = new AntColony(args);
    }
}