module org.example.trabalholfa {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.example.trabalholfa to javafx.fxml;
    exports org.example.trabalholfa;
}
