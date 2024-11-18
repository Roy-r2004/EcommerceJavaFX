module org.example.ecommercejavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens org.example.ecommercejavafx.models to javafx.base;

    // Allow JavaFX to access the `controllers` package reflectively
    opens org.example.ecommercejavafx.controllers to javafx.fxml;

    // Open the main package in case it is required for FXML
    opens org.example.ecommercejavafx to javafx.fxml;

    // Export the main package so other modules can access it
    exports org.example.ecommercejavafx;
}
