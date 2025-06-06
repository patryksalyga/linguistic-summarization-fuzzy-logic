package com.example.linguisticsummarizationfuzzylogic;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class SummaryTableController {

    @FXML
    private TableView<LinguisticSummary> summaryTable;

    @FXML
    private TableColumn<LinguisticSummary, String> textColumn;

    @FXML private TableColumn<LinguisticSummary, Double> overallColumn;
    @FXML private TableColumn<LinguisticSummary, Double> t1Column;
    @FXML private TableColumn<LinguisticSummary, Double> t2Column;
    @FXML private TableColumn<LinguisticSummary, Double> t3Column;
    @FXML private TableColumn<LinguisticSummary, Double> t4Column;
    @FXML private TableColumn<LinguisticSummary, Double> t5Column;
    @FXML private TableColumn<LinguisticSummary, Double> t6Column;
    @FXML private TableColumn<LinguisticSummary, Double> t7Column;
    @FXML private TableColumn<LinguisticSummary, Double> t8Column;
    @FXML private TableColumn<LinguisticSummary, Double> t9Column;
    @FXML private TableColumn<LinguisticSummary, Double> t10Column;
    @FXML private TableColumn<LinguisticSummary, Double> t11Column;

    public void initialize() {
        textColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("text"));

        overallColumn.setCellValueFactory(data -> fx(data.getValue().getSummaryQualityEvaluator().getOverallQuality()));
        t1Column.setCellValueFactory(data -> fx(data.getValue().getSummaryQualityEvaluator().get("T1")));
        t2Column.setCellValueFactory(data -> fx(data.getValue().getSummaryQualityEvaluator().get("T2")));
        t3Column.setCellValueFactory(data -> fx(data.getValue().getSummaryQualityEvaluator().get("T3")));
        t4Column.setCellValueFactory(data -> fx(data.getValue().getSummaryQualityEvaluator().get("T4")));
        t5Column.setCellValueFactory(data -> fx(data.getValue().getSummaryQualityEvaluator().get("T5")));
        t6Column.setCellValueFactory(data -> fx(data.getValue().getSummaryQualityEvaluator().get("T6")));
        t7Column.setCellValueFactory(data -> fx(data.getValue().getSummaryQualityEvaluator().get("T7")));
        t8Column.setCellValueFactory(data -> fx(data.getValue().getSummaryQualityEvaluator().get("T8")));
        t9Column.setCellValueFactory(data -> fx(data.getValue().getSummaryQualityEvaluator().get("T9")));
        t10Column.setCellValueFactory(data -> fx(data.getValue().getSummaryQualityEvaluator().get("T10")));
        t11Column.setCellValueFactory(data -> fx(data.getValue().getSummaryQualityEvaluator().get("T11")));
    }

    public void setSummaries(List<LinguisticSummary> summaries, boolean isStandardSummaries) {
        if (isStandardSummaries) {
            summaryTable.setItems(FXCollections.observableArrayList(summaries));
            exportToCSV("src/main/resources/com/example/linguisticsummarizationfuzzylogic/summaries.csv", true, false, summaries);
        } else {
            summaryTable.getItems().addAll(summaries);
            exportToCSV("src/main/resources/com/example/linguisticsummarizationfuzzylogic/comparative_summaries.csv", false, false, summaries);
        }
    }



    private ReadOnlyObjectWrapper<Double> fx(double value) {
        return new ReadOnlyObjectWrapper<>(value);
    }

    private void exportToCSV(String filePath, boolean standardSummaries, boolean printToConsole, List<LinguisticSummary> summariesToExport) {
        StringBuilder sb = new StringBuilder();

        if (standardSummaries) {
            sb.append("Text,Overall,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11\n");

            for (LinguisticSummary summary : summariesToExport) {
                var evaluator = summary.getSummaryQualityEvaluator();
                sb.append("\"").append(summary.getText()).append("\"").append(",")
                        .append(evaluator.getOverallQuality()).append(",")
                        .append(evaluator.get("T1")).append(",")
                        .append(evaluator.get("T2")).append(",")
                        .append(evaluator.get("T3")).append(",")
                        .append(evaluator.get("T4")).append(",")
                        .append(evaluator.get("T5")).append(",")
                        .append(evaluator.get("T6")).append(",")
                        .append(evaluator.get("T7")).append(",")
                        .append(evaluator.get("T8")).append(",")
                        .append(evaluator.get("T9")).append(",")
                        .append(evaluator.get("T10")).append(",")
                        .append(evaluator.get("T11")).append("\n");
            }
        } else {
            sb.append("Text,Overall\n");

            for (LinguisticSummary summary : summariesToExport) {
                var evaluator = summary.getSummaryQualityEvaluator();
                sb.append("\"").append(summary.getText()).append("\"").append(",")
                        .append(evaluator.getOverallQuality()).append("\n");
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (printToConsole) {
            System.out.println(sb.toString());
        }
    }
}
