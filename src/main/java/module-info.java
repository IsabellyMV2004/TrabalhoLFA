module org.example.trabalholfa {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;

    opens org.example.trabalholfa to javafx.fxml;
    exports org.example.trabalholfa;
}
