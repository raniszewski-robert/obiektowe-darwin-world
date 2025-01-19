package agh.ics.oop.model;

import javafx.scene.control.Label;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CSVLogger {
    private PrintWriter writer;

    public CSVLogger() throws IOException {

        File directory = new File("Statistics");
        if (!directory.exists()) {
            directory.mkdir();
        }

        // Generowanie unikalnej nazwy pliku z timestampem
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "Statistics/simulation_statistics_" + timestamp + ".csv";

        // Tworzenie nowego pliku
        writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));

        // Nagłówki CSV
        writer.println("Day;AnimalsCount;PlantCount;AverageEnergy;FreeSpaces;DominantGenotype;AverageAge;AverageOffspring");
        writer.flush();
    }

    public void logStatistics(int day, int animalsCount, int plantCount, double avgEnergy, int freeSpaces,
                              String dominantGenotype, String avgAge, double avgOffspring) {
        writer.printf("%d;%d;%d;%.2f;%d;%s;%s;%.2f%n",
                day, animalsCount, plantCount, avgEnergy, freeSpaces,
                dominantGenotype != null ? dominantGenotype : "None",
                avgAge, avgOffspring);
        writer.flush();
    }

    public void close() {
        if (writer != null) {
            writer.close();
        }
    }
}
