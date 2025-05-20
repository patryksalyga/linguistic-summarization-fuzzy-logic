package com.example.linguisticsummarizationfuzzylogic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hi!");
        stage.setScene(scene);
        stage.show();

        // Load data from xlsx file controller
        ElectoralDistricts electoralDistricts = new ElectoralDistricts();
        ExcelDataController excelDataController = new ExcelDataController(electoralDistricts);
        excelDataController.loadDataFromExcel("src/main/resources/com/example/linguisticsummarizationfuzzylogic/wybory2020.xlsx");
        System.out.println(electoralDistricts.getDistricts().get(0).toString());


    }

    public static void main(String[] args) {
        launch();
    }
}