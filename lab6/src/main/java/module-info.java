module com.example.lab6 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.lab6 to javafx.fxml;
    exports com.example.lab6;
    exports com.example.lab6.controller;
    opens com.example.lab6.controller to javafx.fxml;

    exports com.example.lab6.domain;
    opens com.example.lab6.domain to javafx.fxml;

    exports com.example.lab6.repository;
    opens com.example.lab6.repository to javafx.fxml;

    exports com.example.lab6.service;
    opens com.example.lab6.service to javafx.fxml;
}