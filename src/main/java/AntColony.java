import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * The AntColony class represents a simulation of an ant colony.
 */
public class AntColony{
    Random random;
    long seed;
    ArrayList<Object> objects;
    private final Display display;
    public final GameMap gameMap;
    float targetFPS = 60;
    final int scale = 3;
    long tick = 0;
    long simulationEndTime;
    boolean isPaused = false;
    private Timer timer;

    /**
     * Creates an AntColony simulation with specified arguments such as:<br>
     * - window width, <br>
     * - window height,<br>
     * - simulation tick limit,<br>
     * - frequency of food appearing on the map,<br>
     * - rate of disappearance of food and ant scents,<br>
     * - number of walls on the map.
     *
     * @param args the arguments for the simulation configuration
     */
    public AntColony(String[] args) {

        int[] simulationArgs = handleArgs(args);
        simulationEndTime = simulationArgs[2];

        random = new Random();
        seed = random.nextInt();
//        seed = 2;
        random.setSeed(seed);

        objects = new ArrayList<>();
        gameMap = new GameMap(random, objects, scale);
        display = new Display(simulationArgs, this);

        gameMap.generateMap(display.getWidth(), display.getHeight(), simulationArgs);

            // Timer z pętlą symulacji na innym wątku
        timer = new Timer();
        timer.schedule(new MainLoop(), 0, (long) 1000/(int)targetFPS);
    }

    /**
     * The MainLoop class represents the main simulation loop.
     */
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
            if (simulationEndTime != 0 && tick >= simulationEndTime)
            {
                saveResults();
                System.exit(0);
            }

        }
    }
    /**
     * Saves the results of the simulation to a file "results.txt".
     * Will overwrite data if the file is already created.
     */
    private void saveResults()
    {
        try (FileWriter writer = new FileWriter("results.txt")) {
            String content = String.valueOf(Ant.antCounter);
            writer.write(content);
            System.out.println("Dane zostały zapisane do pliku.");
        } catch (IOException e) {
            System.err.println("Wystąpił błąd podczas zapisywania danych do pliku: " + e.getMessage());
        }
    }

    /**
     * Updates the max amount of frames per second (FPS) of the simulation.
     * The maximum frame rate is 1000, but with a larger simulation map the actual FPS will not exceed 200.
     */
    public void updateFPS()
    {
        if (timer != null) timer.cancel();
        timer = new Timer();
        timer.schedule(new MainLoop(), 0, (long) 1000/(int)targetFPS);
    }

    /**
     * Handles the command-line arguments and converts them into simulation parameters.
     * If user parameters are not passed or are invalid, default values will be used
     *
     * @param args the command-line arguments
     * @return an array of integers representing the simulation correct parameters
     */
    int[] handleArgs(String[] args) {
            // szerokość, wysokość, długość symulacji, odstęp tikowy pomiędzy nowym jedzeniem, szybkośc znikania zapachu, ilość ścian
        int[] argsConverted = {750, 750, 25000, 5000, 9995, 300};

            // szerokość
        if (args.length > 0){
            try{
                argsConverted[0] = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {}
            argsConverted[0] = Math.max(0, argsConverted[0]);
        }
            // wysokość
        if (args.length > 1){
            try{
                argsConverted[1] = Integer.parseInt(args[1]);
            } catch (NumberFormatException ignored) {}
            argsConverted[1] = Math.max(0, argsConverted[1]);
        }
            // długość symulacji
        if (args.length > 2){
            try{
                argsConverted[2] = Integer.parseInt(args[2]);
            } catch (NumberFormatException ignored) {}
            argsConverted[2] = Math.max(0, argsConverted[2]);
        }
            // odstęp tikowy pomiędzy nowym jedzeniem
        if (args.length > 3){
            try{
                argsConverted[3] = Integer.parseInt(args[3]);
            } catch (NumberFormatException ignored) {}
            argsConverted[3] = Math.max(100, argsConverted[3]);
            argsConverted[3] = Math.min(1000000, argsConverted[3]);
        }
            // szybkośc znikania zapachu
        if (args.length > 4){
            try{
                argsConverted[4] = Integer.parseInt(args[4]);
            } catch (NumberFormatException ignored) {}
            argsConverted[4] = Math.max(0, argsConverted[4]);
            argsConverted[4] = Math.min(10000, argsConverted[4]);
        }
            // ilość ścian
        if (args.length > 5){
            try{
                argsConverted[5] = Integer.parseInt(args[5]);
            } catch (NumberFormatException ignored) {}
            argsConverted[5] = Math.max(0, argsConverted[5]);
            argsConverted[5] = Math.min(1000000, argsConverted[5]);
        }
        return argsConverted;
    }

    /**
     * The main method to start the ant colony simulation.
     * Captures the passed arguments from Gradle and passes them to the simulation constructor
     *
     * @param args the command-line arguments for the simulation
     */
    public static void main(String[] args) {
        AntColony simulation = new AntColony(args);
    }
}