module com.danyazero.cs1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.danyazero.cs1 to javafx.fxml;
    exports com.danyazero.cs1;
    exports com.danyazero.cs1.model;
}