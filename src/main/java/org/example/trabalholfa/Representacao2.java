package org.example.trabalholfa;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import org.example.trabalholfa.view.AutomatonCanvas;

import java.io.File;

public class Representacao2 {

    @FXML private ToggleButton selectBtn;
    @FXML private ToggleButton stateBtn;
    @FXML private ToggleButton transBtn;
    @FXML private Button saveBtn;
    @FXML private Button loadBtn;
    @FXML private Pane canvasArea;

    private AutomatonCanvas automatonCanvas;

    @FXML
    public void initialize() {
        // Cria o canvas e adiciona dentro do Pane
        automatonCanvas = new AutomatonCanvas();
        automatonCanvas.prefWidthProperty().bind(canvasArea.widthProperty());
        automatonCanvas.prefHeightProperty().bind(canvasArea.heightProperty());
        canvasArea.getChildren().add(automatonCanvas);

        // Grupo de botões
        ToggleGroup tools = new ToggleGroup();
        selectBtn.setToggleGroup(tools);
        stateBtn.setToggleGroup(tools);
        transBtn.setToggleGroup(tools);
        selectBtn.setSelected(true);

        // Ações
        selectBtn.setOnAction(e -> automatonCanvas.setMode(AutomatonCanvas.Mode.SELECT));
        stateBtn.setOnAction(e -> automatonCanvas.setMode(AutomatonCanvas.Mode.ADD_STATE));
        transBtn.setOnAction(e -> automatonCanvas.setMode(AutomatonCanvas.Mode.ADD_TRANSITION));

        saveBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Salvar Autômato");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("JFLAP Files", "*.jff")
            );
            File file = fileChooser.showSaveDialog(saveBtn.getScene().getWindow());
            if (file != null) automatonCanvas.saveToJFF(file);
        });

        loadBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Carregar Autômato");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("JFLAP Files", "*.jff")
            );
            File file = fileChooser.showOpenDialog(loadBtn.getScene().getWindow());
            if (file != null) automatonCanvas.loadFromJFF(file);
        });
    }
}
