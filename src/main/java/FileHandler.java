import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class FileHandler {
    public static void save(AntColony simulation)
    {
        String content = "";

            // zapis wymiarów
        content += simulation.gameMap.getHeight() + "|" + simulation.gameMap.getWidth() + "\n";

            // zapis ziarna
        content += simulation.seed + "\n";
            // zapis ilości tików
        content += simulation.tick + "\n";


            // zapis zapachów
        for (int i = 0; i < simulation.gameMap.getHeight(); i++) {
            for (int j = 0; j < simulation.gameMap.getWidth(); j++) {
                content += simulation.gameMap.tiles[j][i].getAntScentValue() + ":" + simulation.gameMap.tiles[j][i].getFoodScentValue() + "|";
            }
            content += "\n";
        }

            // zapis obiektów
        for (int i = 0; i < simulation.objects.size(); i++) {
            content += simulation.objects.get(i).toString() + "\n";
        }

        try (FileWriter writer = new FileWriter("save.txt")) {
            writer.write(content);
            System.out.println("Dane zostały zapisane do pliku.");
        } catch (IOException e) {
            System.err.println("Wystąpił błąd podczas zapisywania danych do pliku: " + e.getMessage());
        }
    }

    public static void load(AntColony simulation)
    {

        try (BufferedReader reader = new BufferedReader(new FileReader("save.txt"))) {
            String line = reader.readLine();
            if (line == null) return;

            int height = Integer.parseInt(line.split("\\|")[0]);
            int width = Integer.parseInt(line.split("\\|")[1]);

            if (height != simulation.gameMap.getHeight() || width != simulation.gameMap.getWidth()) return;

            simulation.random = new Random(Long.parseLong(reader.readLine()));
            simulation.tick = Long.parseLong(reader.readLine());

            for (int i = 0; i < height; i++) {
                    // sczytanie z wiersza wartości
                String row = reader.readLine();
                String[] scentValuesS = row.split("[|:]");
                for (int j = 0; j < scentValuesS.length/2; j++) {
                    simulation.gameMap.tiles[j][i].setAntScentValue(Float.parseFloat(scentValuesS[j]));
                    simulation.gameMap.tiles[j][i].setFoodScentValue(Float.parseFloat(scentValuesS[j+1]));
                }
            }

            for (int i = 0; i < simulation.objects.size(); i++) {
                simulation.gameMap.takeObject(simulation.objects.get(i));
            }

            simulation.objects.clear();
            Ant.antCounter = 0;
            Food.foodCounter = 0;

            while ((line = reader.readLine()) != null) {
                String[] objectInfo = line.split("\\|");

                switch (objectInfo[0]){
                    case "class Ant":
                        new Ant(Integer.parseInt(objectInfo[1]),
                                Integer.parseInt(objectInfo[2]),
                                Integer.parseInt(objectInfo[3]),
                                simulation.random,
                                simulation.gameMap,

                                Boolean.parseBoolean(objectInfo[4]),
                                Float.parseFloat(objectInfo[5]),
                                Float.parseFloat(objectInfo[6]),
                                Float.parseFloat(objectInfo[7]),
                                Float.parseFloat(objectInfo[8]),
                                Float.parseFloat(objectInfo[9]),
                                Float.parseFloat(objectInfo[10]),

                                Integer.parseInt(objectInfo[11]),
                                Integer.parseInt(objectInfo[12]),

                                Integer.parseInt(objectInfo[13])
                        );
                        break;
                    case "class AntNest":
                        new AntNest(
                                Integer.parseInt(objectInfo[1]),
                                Integer.parseInt(objectInfo[2]),
                                Integer.parseInt(objectInfo[3]),
                                simulation.random,
                                simulation.gameMap,
                                Integer.parseInt(objectInfo[4])
                        );
                        break;
                    case "class Wall":
                        new Wall(
                                Integer.parseInt(objectInfo[1]),
                                Integer.parseInt(objectInfo[2]),
                                Integer.parseInt(objectInfo[3]),
                                simulation.random,
                                simulation.gameMap
                        );
                        break;
                    case "class Food":
                        new Food(
                                Integer.parseInt(objectInfo[1]),
                                Integer.parseInt(objectInfo[2]),
                                Integer.parseInt(objectInfo[3]),
                                simulation.random,
                                simulation.gameMap,
                                Integer.parseInt(objectInfo[3])
                        );
                        break;
                }
            }

            for (int i = 0; i < simulation.objects.size(); i++) {
                simulation.gameMap.placeObject(simulation.objects.get(i));
            }

        } catch (IOException e) {
            System.err.println("Błąd odczytu pliku: " + e.getMessage());
        }
    }
}
