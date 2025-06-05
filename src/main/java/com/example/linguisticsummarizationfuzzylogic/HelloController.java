package com.example.linguisticsummarizationfuzzylogic;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class HelloController {

    private static LinguisticRepository linguisticRepository;
    private static EntityRepository entityRepository;
    private static ElectoralDistricts electoralDistricts;
    private static ExcelDataController excelDataController;
    private static QuantifiersRepository quantifiersRepository;
    private static Path tempFile;

    @FXML
    private TreeView<String> treeView;
    @FXML
    private TreeView<String> rightTreeView;
    @FXML
    private Button button1;
    @FXML
    private Button button2;
    @FXML
    private CheckBox checkBox1;
    @FXML
    private CheckBox checkBox2;
    @FXML
    private CheckBox checkBox3;
    @FXML
    private CheckBox checkBox4;
    @FXML
    private CheckBox checkBox5;
    @FXML
    private CheckBox checkBox6;


    @FXML
    public void initialize() {
        // Ukryty root
        TreeItem<String> rootItem = new TreeItem<>("Root");

        linguisticRepository = new LinguisticRepository();
        try {
            linguisticRepository.loadFromCSV("src/main/resources/com/example/linguisticsummarizationfuzzylogic/features.csv");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        for (LinguisticVariable variable : linguisticRepository.getLinguisticVariables()) {
            // Używamy zwykłego TreeItem dla LinguisticVariable (bez checkboxa)
            TreeItem<String> variableItem = new TreeItem<>(variable.getName());
            variableItem.setExpanded(false); // <- zawinięte

            for (LinguisticTerm term : variable.getTerms()) {
                CheckBoxTreeItem<String> termItem = new CheckBoxTreeItem<>(term.getLabel());
                termItem.setSelected(term.isEnabled());

                // Synchronizacja stanu z modelem danych
                termItem.selectedProperty().addListener((obs, oldVal, newVal) -> {
                    term.setEnabled(newVal);
                    System.out.println("Term '" + term.getLabel() + "' enabled: " + term.isEnabled());
                });

                variableItem.getChildren().add(termItem);
            }

            rootItem.getChildren().add(variableItem);
        }

        treeView.setRoot(rootItem);
        treeView.setShowRoot(false);

        // WAŻNE: ustaw cellFactory aby checkbox był widoczny TYLKO dla CheckBoxTreeItem
        treeView.setCellFactory(tv -> new CheckBoxTreeCell<>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                TreeItem<String> treeItem = getTreeItem();
                // Jeśli to NIE CheckBoxTreeItem – usuń checkbox
                if (!(treeItem instanceof CheckBoxTreeItem)) {
                    setGraphic(null); // usuwa checkbox
                }
            }
        });

        TreeItem<String> rootItem2 = new TreeItem<>("Root");

        entityRepository = new EntityRepository();
        electoralDistricts = new ElectoralDistricts();
        excelDataController = new ExcelDataController(electoralDistricts);
        excelDataController.loadDataFromExcel("src/main/resources/com/example/linguisticsummarizationfuzzylogic/wybory2020.xlsx");

        try {
            entityRepository.loadFromCSV("src/main/resources/com/example/linguisticsummarizationfuzzylogic/entities.csv", electoralDistricts);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        for (Entity entity : entityRepository.getEntities()) {
            // Używamy zwykłego TreeItem dla Entity (bez checkboxa)
            TreeItem<String> entityItem = new TreeItem<>(entity.getName());
            entityItem.setExpanded(false); // <- zawinięte

            for (EntityValue value : entity.getValues()) {
                CheckBoxTreeItem<String> valueItem = new CheckBoxTreeItem<>(value.getValue());
                valueItem.setSelected(value.isEnabled());

                // Synchronizacja stanu z modelem danych
                valueItem.selectedProperty().addListener((obs, oldVal, newVal) -> {
                    value.setEnabled(newVal);
                    System.out.println("Value '" + value.getValue() + "' enabled: " + value.isEnabled());
                });

                entityItem.getChildren().add(valueItem);
            }

            rootItem2.getChildren().add(entityItem);
        }

        rightTreeView.setRoot(rootItem2);
        rightTreeView.setShowRoot(false); // jeśli chcesz ukryć "Root"
        rightTreeView.setCellFactory(tv -> new CheckBoxTreeCell<>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                TreeItem<String> treeItem = getTreeItem();
                if (!(treeItem instanceof CheckBoxTreeItem)) {
                    setGraphic(null);
                }
            }
        });
    }

    @FXML
    private void onAdvancedButtonClicked() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("advanced-view.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle("Zaawansowany edytor CSV");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);  // blokuje główne okno
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onGenerateSummariesButtonClicked() {
        try {
            // Załaduj FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("summaries-view.fxml"));
            Parent root = loader.load();

            quantifiersRepository = new QuantifiersRepository();
            try {
                quantifiersRepository.loadFromCSV("src/main/resources/com/example/linguisticsummarizationfuzzylogic/quantifiers_absolute.csv",
                        "src/main/resources/com/example/linguisticsummarizationfuzzylogic/quantifiers_relative.csv");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            // Pobierz kontroler i przekaż dane
            LinguisticSummaryService linguisticSummaryService = new LinguisticSummaryService(electoralDistricts, linguisticRepository, quantifiersRepository, entityRepository);
            linguisticSummaryService.prepareData();
            System.out.println("Przygotowane dane - liczba electoralDistricts: " + electoralDistricts.getDistricts().size());

            if(checkBox3.isSelected())linguisticSummaryService.generateZadeh();
            if(checkBox4.isSelected())linguisticSummaryService.generateYager();
            if(checkBox5.isSelected())linguisticSummaryService.generateKacprzyk();
            if(checkBox6.isSelected())linguisticSummaryService.generateComparativeSummaries();

            List<LinguisticSummary> summaries = linguisticSummaryService.getLinguisticSummaries();
            System.out.println("Liczba wygenerowanych podsumowań po generowaniu: " + summaries.size());

            SummaryTableController controller = loader.getController();
            controller.setSummaries(summaries);


            controller.setSummaries(summaries);

            // Stwórz i pokaż nowe okno
            Stage stage = new Stage();
            stage.setTitle("Wygenerowane podsumowania");
            stage.setScene(new Scene(root, 1800, 400));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
