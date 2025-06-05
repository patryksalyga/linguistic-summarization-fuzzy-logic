package com.example.linguisticsummarizationfuzzylogic;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import com.example.linguisticsummarizationfuzzylogic.LinguisticSummary;

import java.util.List;

public class SummaryTableController {

    @FXML
    private TableView<LinguisticSummary> summaryTable;

    @FXML
    private TableColumn<LinguisticSummary, String> textColumn;

    @FXML
    private TableColumn<LinguisticSummary, Double> overallColumn;
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

    public void setSummaries(List<LinguisticSummary> summaries) {
        summaryTable.setItems(FXCollections.observableArrayList(summaries));
    }

    private ReadOnlyObjectWrapper<Double> fx(double value) {
        return new ReadOnlyObjectWrapper<>(value);
    }
}
