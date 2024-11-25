module main {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires fontawesomefx;

    opens main.controllers to javafx.fxml;
    opens main.models.entities to com.google.gson;
    opens main.enums to com.google.gson;
    opens main.models.tcp to com.google.gson;
    exports main;
}
