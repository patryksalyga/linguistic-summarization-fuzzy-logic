package com.example.linguisticsummarizationfuzzylogic;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

public class AdvancedController implements Initializable {

    @FXML
    private TextArea csvEditor1;

    @FXML
    private TextArea csvEditor2;

    @FXML
    private TextArea csvEditor3;


    private final String path1 = "src/main/resources/com/example/linguisticsummarizationfuzzylogic/features.csv";
    private final String path2 = "src/main/resources/com/example/linguisticsummarizationfuzzylogic/quantifiers_absolute.csv";
    private final String path3 = "src/main/resources/com/example/linguisticsummarizationfuzzylogic/quantifiers_relative.csv";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        csvEditor1.setText(loadFromFile(path1));
        csvEditor2.setText(loadFromFile(path2));
        csvEditor3.setText(loadFromFile(path3));
    }

    private String loadFromFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) return "";

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            System.err.println("Błąd wczytywania pliku: " + filePath);
            e.printStackTrace();
            return "";
        }
    }

    @FXML
    private void saveCsv1() {
        saveToFile(csvEditor1.getText(), path1);
    }

    @FXML
    private void saveCsv2() {
        saveToFile(csvEditor2.getText(), path2);
    }

    @FXML
    private void saveCsv3() {
        saveToFile(csvEditor3.getText(), path3);
    }

    private void saveToFile(String content, String filePath) {
        try (FileWriter writer = new FileWriter(filePath, false)) {
            writer.write(content);
            System.out.println("Zapisano do pliku: " + filePath);
        } catch (IOException e) {
            System.err.println("Błąd zapisu pliku " + filePath + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
