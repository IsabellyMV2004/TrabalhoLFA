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
        String permitido = "^[0-9a-zA-Z*+|().&]+$";

        StringBuilder sb = new StringBuilder();
        boolean flag = true;

        if (expressao == null || expressao.trim().isEmpty())
        {
            sb.append("Digite uma expressão.\n");
            flag = false;
        }
        if (alfabetoEntrada == null || alfabetoEntrada.trim().isEmpty())
        {
            sb.append("Digite o alfabeto.\n");
            flag = false;
        }

        if (flag)
        {
            String[] blocos, simbolos;
            Set<String> alfabeto = new TreeSet<>();
            List<String> formatar = new ArrayList<>();
            String exp, normalizar, palavra, b;
            StringBuilder regex;
            List<String> exemplos;
            boolean aceita;
            int i = 0, cont;

            simbolos = alfabetoEntrada.split(",");
            for (String s : simbolos)
            {
                s = s.trim();
                if (!s.isEmpty())
                    alfabeto.add(s);
            }

            if (!expressao.matches(permitido))
            {
                sb.append("Erro: expressão contém símbolos inválidos!");
                flag = false;
            }
            else
            {
                exp = expressao.replaceAll("[*+|().&]", "");
                boolean erroAlfabeto = false;
                for (char c : exp.toCharArray())
                    if (!alfabeto.contains(String.valueOf(c)) && !erroAlfabeto)
                    {
                        sb.append("Erro: expressão contém símbolos que não pertencem ao alfabeto!");
                        flag = false;
                        erroAlfabeto = true;
                    }

                if (flag)
                {
                    normalizar = expressao.replace("+", "|");
                    blocos = normalizar.split("\\.");

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
                                formatar.add(core + "*");
                            }
                            else
                            {
                                if (bloco.contains("|") && !(bloco.startsWith("(") && bloco.endsWith(")")))
                                    bloco = "(" + bloco + ")";
                                formatar.add(bloco);
                            }
                        }
                    }

                    regex = new StringBuilder("^");
                    while (i < formatar.size())
                    {
                        b = formatar.get(i);
                        cont = 1;
                        while (i + cont < formatar.size() && formatar.get(i + cont).equals(b))
                            cont++;

                        if (cont > 1 && !b.endsWith("*"))
                            regex.append(b).append("{").append(cont).append("}");
                        else
                            for (int k = 0;k < cont;k++)
                                regex.append(b);

                        i += cont;
                    }

                    regex.append("$");
                    exemplos = gerarPalavras(new ArrayList<>(alfabeto), 5);

                    sb.append("Expressão válida!\n");
                    sb.append("A Palavra vazia é simbolizada &\n");
                    sb.append("Regex final: ").append(regex.toString()).append("\n\n");

                    sb.append("Testes de palavras:\n");
                    for (String w : exemplos)
                    {
                        palavra = w.equals("&") ? "" : w;
                        aceita = palavra.matches(regex.toString());
                        sb.append(String.format("%-6s -> %s\n", w, aceita ? "ACEITA" : "REJEITADA"));
                    }
                }
            }
        }

        txtSaida.setText(sb.toString());
    }

    private List<String> gerarPalavras(List<String> alfabeto, int maxLen) {
        List<String> resultado = new ArrayList<>();
        resultado.add("&");
        Collections.sort(alfabeto);
        for (int len = 1;len <= maxLen;len++)
            gerarPorTamanho(new ArrayList<>(), alfabeto, len, resultado);

        return resultado;
    }

    private void gerarPorTamanho(List<String> partes, List<String> alfabeto, int alvoLen, List<String> resultado) {
        if (partes.size() == alvoLen)
        {
            StringBuilder sb = new StringBuilder();
            for (String p : partes)
                sb.append(p);
            resultado.add(sb.toString());
        }
        else
            for (int i=0;i < alfabeto.size();i++)
            {
                partes.add(alfabeto.get(i));
                gerarPorTamanho(partes, alfabeto, alvoLen, resultado);
                partes.remove(partes.size() - 1);
            }
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
