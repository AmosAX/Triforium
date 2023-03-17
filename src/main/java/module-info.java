module com.example.triforium {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.triforium to javafx.fxml;
    exports com.example.triforium;
}