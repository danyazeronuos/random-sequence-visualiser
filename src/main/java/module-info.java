module com.danyazero.cs1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.danyazero.cs1 to javafx.fxml;
    exports com.danyazero.cs1;
    exports com.danyazero.cs1.model;
    exports com.danyazero.cs1.generators;
}