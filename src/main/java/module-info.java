module com.example.linguisticsummarizationfuzzylogic {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;


    opens com.example.linguisticsummarizationfuzzylogic to javafx.fxml;
    exports com.example.linguisticsummarizationfuzzylogic;
}