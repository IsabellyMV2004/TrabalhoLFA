package org.example.trabalholfa;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.regex.*;
public class Representacao1 {

    public void executar(){
        String expressao, expr;
        String regex = "^[0-9a-zA-Z*+|().]+$";

        /*if(expressao.matches(regex)){  //
            expr = expressao.replace(".", "");
            expr = "^" + expr + "$";

        }
        else{ // vai retornar uma mensagem de erro

        }*/
    }

    public void executarER(ActionEvent actionEvent) {
    }

    @FXML
    public void voltar(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/org/example/trabalholfa/main-view.fxml"));
        Scene scene = new Scene(root);

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
