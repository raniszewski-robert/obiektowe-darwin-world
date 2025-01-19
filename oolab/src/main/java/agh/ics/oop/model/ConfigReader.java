package agh.ics.oop.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigReader {
    public String[] readFile(String fileName) {
        List<String> list = new ArrayList<String>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            line = reader.readLine();
            return line.split(",");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
