package org.example.trabalholfa;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.*;

public class Representacao1 {

    @FXML
    private TextField txtExpressao, txtAlfabeto;

    @FXML
    private TextArea txtSaida;

    @FXML
    public void executarER(ActionEvent actionEvent) {
        String expressao = txtExpressao.getText();
        String alfabetoEntrada;// = txtAlfabeto.getText();
        String permitido = "^[0-9a-zA-Z*+|().&]+$";
        boolean erro = false;
        StringBuilder sb = new StringBuilder();

        if (expressao == null || expressao.trim().isEmpty()) {
            sb.append("Digite uma expressão.\n");
            txtSaida.setText(sb.toString());
            erro = true;
        }

        if(!erro) {
            //String[] simbolos = alfabetoEntrada.split(",");
            String[] simbolos = definirAlfabeto(expressao);
            Set<String> alfabeto = new TreeSet<>();
            String exp,normalizar,regex;
            List<String> exemplos;
            boolean aceita;

            for (String s : simbolos) {
                s = s.trim();
                if (!s.isEmpty() && !s.equals("&"))
                    alfabeto.add(s);
            }

            if (!expressao.matches(permitido)) {
                sb.append("Erro: expressão contém símbolos inválidos!");
                txtSaida.setText(sb.toString());
            }
            else {
               /* exp = expressao.replaceAll("[*+|().]", "");
                for (char c : exp.toCharArray()) {
                    if (!alfabeto.contains(String.valueOf(c)) && c != '&') {
                        sb.append("Erro: expressão contém símbolos que não pertencem ao alfabeto!");
                        txtSaida.setText(sb.toString());
                        erro = true;
                    }
                }
                if(!erro) {*/
                    normalizar = expressao.replace("+", "|").replace(".", "");


                    try {
                        regex = construirRegex(normalizar);
                        regex = "^" + regex + "$";

                        exemplos = gerarPalavras(new ArrayList<>(alfabeto), 6);
                        exemplos.add(0, "");

                        sb.append("Expressão válida!\n");
                        sb.append("A palavra vazia é simbolizada &\n");
                        sb.append("Regex final: ").append(regex).append("\n\n");
                        sb.append("Testes de palavras:\n");

                        for (String w : exemplos)
                        {
                            aceita = w.matches(regex);
                            sb.append(String.format("%-6s -> %s\n", w.isEmpty() ? "&" : w, aceita ? "ACEITA" : "REJEITADA"));
                        }

                        txtSaida.setText(sb.toString());
                    } catch (Exception e) {
                        sb.append("Erro ao interpretar a expressão: ").append(e.getMessage());
                        txtSaida.setText(sb.toString());
                    }
               // }
            }
        }
    }

    private String[] definirAlfabeto(String expressao) {
        List<String> alfabeto = new ArrayList<>();
        String simbolos = "*+|().&";

        for (int i = 0; i < expressao.length(); i++) {
            char c = expressao.charAt(i);

            if (simbolos.indexOf(c) == -1 && !alfabeto.contains(String.valueOf(c)))
                alfabeto.add(String.valueOf(c));
        }

        return alfabeto.toArray(new String[0]);
    }

    private String construirRegex(String expr) throws Exception {
        expr = expr.trim();
        if (expr.isEmpty() || expr.equals("&"))
            return "";
        return parseExpr(expr);
    }

    private String parseExpr(String expr) throws Exception {
        List<String> termos = new ArrayList<>();
        List<String> partes;
        int nivel = 0, i;
        StringBuilder atual = new StringBuilder();
        char c;
        boolean vazio;

        for (i = 0; i < expr.length(); i++) {
            c = expr.charAt(i);
            if (c == '(') {
                nivel++;
            } else if (c == ')') {
                nivel--;
            }

            if (c == '|' && nivel == 0) {
                termos.add(atual.toString());
                atual.setLength(0);
            } else {
                atual.append(c);
            }
        }
        termos.add(atual.toString());

        if (termos.size() > 1) {
            partes = new ArrayList<>();
            for (String t : termos) {
                String p = parseConcat(t);
                partes.add(p);
            }

            vazio = true;
            i = 0;
            while (i < partes.size() && vazio) {
                String p = partes.get(i);
                if (!p.isEmpty()) {
                    vazio = false;
                }
                i++;
            }

            if (vazio)
                return "";

            return "(?:" + String.join("|", partes) + ")";
        }

        return parseConcat(termos.get(0));
    }


    private String parseConcat(String termo) throws Exception {
        termo = termo.trim();
        if (termo.isEmpty())
            return "";
        List<String> unidades = new ArrayList<>();
        int i = 0, inicio, nivel;
        char c, ch;
        String unit;
        StringBuilder sb;

        while (i < termo.length()) {
            c = termo.charAt(i);
            if (c == '(')
            {
                inicio = i;
                nivel = 0;
                do {
                    ch = termo.charAt(i);
                    if (ch == '(')
                        nivel++;
                    else if (ch == ')')
                        nivel--;
                    i++;
                    if (i > termo.length())
                        throw new Exception("Parênteses desbalanceados");
                } while (i < termo.length() && nivel > 0);
                unit = termo.substring(inicio, i);

                if (i < termo.length() && termo.charAt(i) == '*') {
                    unit += "*";
                    i++;
                }
                unidades.add(unit);
            } else if (Character.isLetterOrDigit(c) || c == '&') {
                unit = String.valueOf(c);
                i++;
                if (i < termo.length() && termo.charAt(i) == '*') {
                    unit += "*";
                    i++;
                }
                unidades.add(unit);
            }
            else if (c == '.')
                i++;
            else
                throw new Exception("Caractere inesperado na concatenação: '" + c + "'");
        }

        sb = new StringBuilder();
        for (String u : unidades) {
            sb.append(parseUnidade(u));
        }
        return sb.toString();
    }

    private String parseUnidade(String unidade) throws Exception {
        unidade = unidade.trim();
        if (unidade.equals("&"))
            return "";

        boolean estrela;
        String nucleo, analizado;

        estrela = unidade.endsWith("*");
        nucleo = estrela ? unidade.substring(0, unidade.length() - 1) : unidade;

        if (nucleo.startsWith("(") && nucleo.endsWith(")"))
            analizado = parseExpr(nucleo.substring(1, nucleo.length() - 1));
         else
            analizado = nucleo;


        if (estrela) {
            if (analizado.isEmpty())
                return "";
            if (analizado.length() == 1 && Character.isLetterOrDigit(analizado.charAt(0)))
                return analizado + "*";
            else
                if (analizado.startsWith("(?:") && analizado.endsWith(")"))
                    return analizado + "*";
                else
                    return "(?:" + analizado + ")*";
        } else
            return analizado;
    }

    private List<String> gerarPalavras(List<String> alfabeto, int maxLen) {
        List<String> resultado = new ArrayList<>();
        Collections.sort(alfabeto);
        for (int len = 1; len <= maxLen; len++)
            gerarPorTamanho(new ArrayList<>(), alfabeto, len, resultado);
        return resultado;
    }

    private void gerarPorTamanho(List<String> partes, List<String> alfabeto, int alvoLen, List<String> resultado) {
        if (partes.size() == alvoLen) {
            StringBuilder sb = new StringBuilder();
            for (String p : partes) sb.append(p);
            resultado.add(sb.toString());
        } else {
            for (String s : alfabeto) {
                partes.add(s);
                gerarPorTamanho(partes, alfabeto, alvoLen, resultado);
                partes.remove(partes.size() - 1);
            }
        }
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

