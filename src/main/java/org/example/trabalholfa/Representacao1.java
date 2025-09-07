package org.example.trabalholfa;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.*;

public class Representacao1 {

    @FXML
    private TextField txtExpressao, txtAlfabeto;

    @FXML
    private TextArea txtSaida;

    @FXML
    public void executarER(ActionEvent actionEvent) {
        String expressao = txtExpressao.getText();
        String alfabetoEntrada = txtAlfabeto.getText();
        String regexPermitido = "^[0-9a-zA-Z*+|().&]+$";

        if (expressao == null || expressao.trim().isEmpty())
            txtSaida.setText("Digite uma expressão.");
        else {
            if (alfabetoEntrada == null || alfabetoEntrada.trim().isEmpty())
                txtSaida.setText("Digite o alfabeto.");
            else {
                String[] blocos, simbolos;
                Set<String> alfabeto = new TreeSet<>();
                String exp, normalizar, core, palavra;
                StringBuilder regexFinal;
                List<String> exemplos;
                boolean aceita;

                simbolos = alfabetoEntrada.split(",");
                for (String s : simbolos) {
                    s = s.trim();
                    if (!s.isEmpty())
                        alfabeto.add(s);
                }

                if (!expressao.matches(regexPermitido)) {
                    txtSaida.setText("Erro: expressão contém símbolos inválidos!");
                    return;
                }

                exp = expressao.replaceAll("[*+|().&]", "");
                for (char c : exp.toCharArray()) {
                    if (!alfabeto.contains(String.valueOf(c))) {
                        txtSaida.setText("Erro: expressão contém símbolo '" + c + "' que não pertence ao alfabeto!");
                        return;
                    }
                }

                normalizar = expressao.replace("+", "|");

                blocos = normalizar.split("\\.");
                regexFinal = new StringBuilder("^");

                for (String bloco : blocos)
                {
                    bloco = bloco.trim();
                    if (!bloco.isEmpty())
                    {
                        if (bloco.endsWith("*"))
                        {
                            core = bloco.substring(0, bloco.length() - 1).trim();
                            if (core.contains("|") && !(core.startsWith("(") && core.endsWith(")")))
                                core = "(" + core + ")";
                            regexFinal.append(core).append("*");
                        }
                        else
                        {
                            if (bloco.contains("|") && !(bloco.startsWith("(") && bloco.endsWith(")")))
                                bloco = "(" + bloco + ")";
                            regexFinal.append(bloco);
                        }
                    }
                }
                regexFinal.append("$");
                exemplos = gerarPalavras(new ArrayList<>(alfabeto), 5);

                StringBuilder sb = new StringBuilder();
                sb.append("Expressão válida!\n");
                sb.append("A Palavra vazia é simbolizada &\n");
                sb.append("Regex final: ").append(regexFinal.toString()).append("\n\n");

                sb.append("Testes de palavras:\n");
                for (String w : exemplos) {
                    palavra = w.equals("&") ? "" : w;
                    aceita = palavra.matches(regexFinal.toString());
                    sb.append(String.format("%-6s -> %s\n", w, aceita ? "ACEITA" : "REJEITADA"));
                }

                txtSaida.setText(sb.toString());
            }
        }
    }

    private List<String> gerarPalavras(List<String> alfabeto, int maxLen) {
        List<String> resultado = new ArrayList<>();
        resultado.add("&");
        gerar("", alfabeto, maxLen, resultado);
        return resultado;
    }

    private void gerar(String prefixo, List<String> alfabeto, int maxLen, List<String> resultado) {
        if (prefixo.length() > 0)
            resultado.add(prefixo);

        if (prefixo.length() != maxLen)
            for (String s : alfabeto)
                gerar(prefixo + s, alfabeto, maxLen, resultado);

    }

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
