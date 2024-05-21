module org.example.javafx_app {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.javafx_app to javafx.fxml;
    exports org.example.javafx_app;
}