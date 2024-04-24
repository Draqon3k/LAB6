module com.example.laboratorul6 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.laboratorul6 to javafx.fxml;
    exports com.example.laboratorul6;
}