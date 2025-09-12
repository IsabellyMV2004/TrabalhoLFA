package org.example.trabalholfa;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class Representacao3 {
    @FXML
    private TextArea txtSaida, txtGramatica;


    public void executarGR(ActionEvent actionEvent) {

    }

    @FXML
    public void voltar(ActionEvent actionEvent) throws Exception {
        Parent root = FXMLLoader.load(
                getClass().getResource("/org/example/trabalholfa/main-view.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
