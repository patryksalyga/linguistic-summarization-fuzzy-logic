module com.example.linguisticsummarizationfuzzylogic {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.linguisticsummarizationfuzzylogic to javafx.fxml;
    exports com.example.linguisticsummarizationfuzzylogic;
}