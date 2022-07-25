module com.example.finalProject {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires javafx.media;

    opens com.example.finalproject.welcome to javafx.fxml;
    exports com.example.finalproject;
    exports com.example.finalproject.welcome;
    exports com.example.finalproject.account;

    opens com.example.finalproject.account to javafx.fxml;

    opens com.example.finalproject to javafx.fxml;
}