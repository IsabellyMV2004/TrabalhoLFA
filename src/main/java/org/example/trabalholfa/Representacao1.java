package org.example.trabalholfa;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Representacao1 {

    @FXML
    private TextField txtExpressao, txtAlfabeto;

    @FXML
    private TextArea txtSaida;

    @FXML
    public void executarER(ActionEvent actionEvent) {
        String expressao = txtExpressao.getText();
        String alfabeto = txtAlfabeto.getText();
        String regexPermitido = "^[0-9a-zA-Z*+|().]+$";

        if (expressao == null || expressao.trim().isEmpty())
            txtSaida.setText("Digite uma expressão.");
        else {
            if (!expressao.matches(regexPermitido))
                txtSaida.setText("Erro: expressão contém símbolos inválidos!");
            else {
                String normalizar = expressao.replace("+", "|");
                String[] blocos = normalizar.split("\\.");
                StringBuilder regexFinal = new StringBuilder("^");

                for (String bloco : blocos)
                {
                    bloco = bloco.trim();
                    if (!bloco.isEmpty())
                    {
                        if (bloco.endsWith("*"))
                        {
                            String core = bloco.substring(0, bloco.length() - 1).trim();
                            if (core.contains("|") && !(core.startsWith("(") && core.endsWith(")")))
                                core = "(" + core + ")";

                            regexFinal.append(core).append("*");
                        } else
                        {
                            if (bloco.contains("|") && !(bloco.startsWith("(") && bloco.endsWith(")")))
                                bloco = "(" + bloco + ")";

                            regexFinal.append(bloco);
                        }
                    }
                }
                regexFinal.append("$");

                StringBuilder sb = new StringBuilder();
                sb.append("Expressão válida!\n");
                sb.append("Regex final: ").append(regexFinal.toString()).append("\n\n");

                //String[] exemplos = testarPalavras();
                String[] exemplos = {"a", "b", "aa", "ab", "aba", "aaa", "bbb", "abab"};
                sb.append("Testes de palavras:\n");
                for (String w : exemplos) {
                    boolean aceita = w.matches(regexFinal.toString());
                    sb.append(String.format("%-6s -> %s\n", w, aceita ? "ACEITA" : "REJEITADA"));
                }

                txtSaida.setText(sb.toString());
            }
        }
    }

      /*  public String[] testarPalavras(){
            String[] palavras;

            return palavras;
        }
*/

    @FXML
    public void voltar(ActionEvent actionEvent) throws Exception {
        javafx.scene.Parent root = javafx.fxml.FXMLLoader.load(
                getClass().getResource("/org/example/trabalholfa/main-view.fxml"));
        javafx.scene.Scene scene = new javafx.scene.Scene(root);

        javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
