module org.example.ecommercejavafx {
    // Required JavaFX modules
    requires javafx.controls;
    requires javafx.fxml;

    // Additional dependencies
    requires java.sql;
    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    // Open specific packages for JavaFX FXML reflection
    opens org.example.ecommercejavafx.controllers to javafx.fxml;
    opens org.example.ecommercejavafx.models to javafx.base;

    // Open the main package in case FXML files reference it
    opens org.example.ecommercejavafx to javafx.fxml;

    // Export main application package
    exports org.example.ecommercejavafx;
    // Export controllers if they need to be accessed by other modules
    exports org.example.ecommercejavafx.controllers;
}
