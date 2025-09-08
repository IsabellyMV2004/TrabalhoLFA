/*package org.example.trabalholfa;

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

        if (expressao == null || expressao.trim().isEmpty()) {
            sb.append("Digite uma expressão.\n");
            flag = false;
        }
        if (alfabetoEntrada == null || alfabetoEntrada.trim().isEmpty()) {
            sb.append("Digite o alfabeto.\n");
            flag = false;
        }

        if (flag) {
            String[] blocos, simbolos;
            Set<String> alfabeto = new TreeSet<>();
            List<String> formatar = new ArrayList<>();
            String exp, normalizar, b;
            StringBuilder regex;
            List<String> exemplos;
            int i = 0, cont;

            // Processa o alfabeto
            simbolos = alfabetoEntrada.split(",");
            for (String s : simbolos) {
                s = s.trim();
                if (!s.isEmpty() && !s.equals("&"))
                    alfabeto.add(s);
            }

            // Verifica símbolos inválidos
            if (!expressao.matches(permitido)) {
                sb.append("Erro: expressão contém símbolos inválidos!");
                flag = false;
            } else {
                // Verifica se ER contém símbolos fora do alfabeto
                exp = expressao.replaceAll("[*+|().&]", "");
                boolean erroAlfabeto = false;
                for (char c : exp.toCharArray()) {
                    if (!alfabeto.contains(String.valueOf(c)) && !erroAlfabeto) {
                        sb.append("Erro: expressão contém símbolos que não pertencem ao alfabeto!");
                        flag = false;
                        erroAlfabeto = true;
                    }
                }

                if (flag) {
                    // Normaliza a expressão (substitui + por |)
                    normalizar = expressao.replace("+", "|");
                    blocos = normalizar.split("\\.");

                    // Formata blocos (coloca parênteses quando necessário)
                    for (String bloco : blocos) {
                        bloco = bloco.trim();
                        if (!bloco.isEmpty()) {
                            if (bloco.endsWith("*")) {
                                String core = bloco.substring(0, bloco.length() - 1).trim();
                                if (core.contains("|") && !(core.startsWith("(") && core.endsWith(")")))
                                    core = "(" + core + ")";
                                formatar.add(core + "*");
                            } else {
                                if (bloco.contains("|") && !(bloco.startsWith("(") && bloco.endsWith(")")))
                                    bloco = "(" + bloco + ")";
                                formatar.add(bloco);
                            }
                        }
                    }

                    // Monta regex final
                    regex = new StringBuilder("^");
                    while (i < formatar.size()) {
                        b = formatar.get(i);
                        cont = 1;
                        while (i + cont < formatar.size() && formatar.get(i + cont).equals(b))
                            cont++;

                        if (cont > 1 && !b.endsWith("*"))
                            regex.append(b).append("{").append(cont).append("}");
                        else
                            for (int k = 0; k < cont; k++)
                                regex.append(b);

                        i += cont;
                    }
                    regex.append("$");

                    // Gera exemplos
                    exemplos = gerarPalavras(new ArrayList<>(alfabeto), 5);

                    sb.append("Expressão válida!\n");
                    sb.append("A Palavra vazia é simbolizada &\n");
                    sb.append("Regex final: ").append(regex.toString()).append("\n\n");

                    sb.append("Testes de palavras:\n");
                    for (String w : exemplos) {
                        boolean aceita;
                        if (w.equals("&")) {
                            // Verifica se & aparece explicitamente na expressão
                            if (expressao.contains("&")) {
                                // & literal na expressão
                                aceita = w.matches(regex.toString());
                            } else {
                                // palavra vazia real
                                aceita = "".matches(regex.toString());
                            }
                        } else {
                            aceita = w.matches(regex.toString());
                        }
                        sb.append(String.format("%-6s -> %s\n", w, aceita ? "ACEITA" : "REJEITADA"));
                    }

                }
            }
        }

        txtSaida.setText(sb.toString());
    }

    private List<String> gerarPalavras(List<String> alfabeto, int maxLen) {
        List<String> resultado = new ArrayList<>();
        resultado.add("&"); // representa palavra vazia
        Collections.sort(alfabeto);
        for (int len = 1; len <= maxLen; len++)
            gerarPorTamanho(new ArrayList<>(), alfabeto, len, resultado);

        return resultado;
    }

    private void gerarPorTamanho(List<String> partes, List<String> alfabeto, int alvoLen, List<String> resultado) {
        if (partes.size() == alvoLen) {
            StringBuilder sb = new StringBuilder();
            for (String p : partes)
                sb.append(p);
            resultado.add(sb.toString());
        } else
            for (int i = 0; i < alfabeto.size(); i++) {
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
}*/

/*
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

        if (expressao == null || expressao.trim().isEmpty()) {
            sb.append("Digite uma expressão.\n");
            flag = false;
        }
        if (alfabetoEntrada == null || alfabetoEntrada.trim().isEmpty()) {
            sb.append("Digite o alfabeto.\n");
            flag = false;
        }

        if (flag) {
            // Processa o alfabeto
            String[] simbolos = alfabetoEntrada.split(",");
            Set<String> alfabeto = new TreeSet<>();
            for (String s : simbolos) {
                s = s.trim();
                if (!s.isEmpty() && !s.equals("&"))
                    alfabeto.add(s);
            }

            // Verifica símbolos inválidos
            if (!expressao.matches(permitido)) {
                sb.append("Erro: expressão contém símbolos inválidos!");
                flag = false;
            } else {
                // Verifica se ER contém símbolos fora do alfabeto
                String exp = expressao.replaceAll("[*+|().&]", "");
                boolean erroAlfabeto = false;
                for (char c : exp.toCharArray()) {
                    if (!alfabeto.contains(String.valueOf(c)) && !erroAlfabeto) {
                        sb.append("Erro: expressão contém símbolos que não pertencem ao alfabeto!");
                        flag = false;
                        erroAlfabeto = true;
                    }
                }

                if (flag) {
                    // Substitui + por |
                    String normalizar = expressao.replace("+", "|");

                    // Remove concatenação explícita '.'
                    normalizar = normalizar.replace(".", "");

                    // Substitui & por ε temporariamente
                   // boolean contemVazio = normalizar.contains("&");
                   // normalizar = normalizar.replace("&", "");

                    // Monta regex final
                    String regexFinal = "^" + normalizar + "$";
                 //   if (contemVazio) {
                  //      regexFinal = "^(|"+normalizar+")$"; // permite palavra vazia
                 //   }

                    // Gera exemplos de palavras
                    List<String> exemplos = gerarPalavras(new ArrayList<>(alfabeto), 5);

                    sb.append("Expressão válida!\n");
                    sb.append("A palavra vazia é simbolizada por &\n");
                    sb.append("Regex final: ").append(regexFinal).append("\n\n");

                    sb.append("Testes de palavras:\n");
                    for (String w : exemplos) {
                       // String teste = w.equals("&") ? "&" : w;
                       /// boolean aceita = teste.matches(regexFinal);
                        boolean aceita = w.matches(regexFinal);
                        sb.append(String.format("%-6s -> %s\n", w, aceita ? "ACEITA" : "REJEITADA"));
                    }
                }
            }
        }

        txtSaida.setText(sb.toString());
    }

    private List<String> gerarPalavras(List<String> alfabeto, int maxLen) {
        List<String> resultado = new ArrayList<>();
        resultado.add("&"); // representa palavra vazia
        Collections.sort(alfabeto);
        for (int len = 1; len <= maxLen; len++)
            gerarPorTamanho(new ArrayList<>(), alfabeto, len, resultado);

        return resultado;
    }

    private void gerarPorTamanho(List<String> partes, List<String> alfabeto, int alvoLen, List<String> resultado) {
        if (partes.size() == alvoLen) {
            StringBuilder sb = new StringBuilder();
            for (String p : partes)
                sb.append(p);
            resultado.add(sb.toString());
        } else
            for (String s : alfabeto) {
                partes.add(s);
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
}*/

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

        if (expressao == null || expressao.trim().isEmpty()) {
            sb.append("Digite uma expressão.\n");
            txtSaida.setText(sb.toString());
            return;
        }
        if (alfabetoEntrada == null || alfabetoEntrada.trim().isEmpty()) {
            sb.append("Digite o alfabeto.\n");
            txtSaida.setText(sb.toString());
            return;
        }

        // Processa o alfabeto
        String[] simbolos = alfabetoEntrada.split(",");
        Set<String> alfabeto = new TreeSet<>();
        for (String s : simbolos) {
            s = s.trim();
            if (!s.isEmpty() && !s.equals("&"))
                alfabeto.add(s);
        }

        // Verifica símbolos inválidos
        if (!expressao.matches(permitido)) {
            sb.append("Erro: expressão contém símbolos inválidos!");
            txtSaida.setText(sb.toString());
            return;
        }

        // Verifica se ER contém símbolos fora do alfabeto
        String expSemOperadores = expressao.replaceAll("[*+|().]", "");
        for (char c : expSemOperadores.toCharArray()) {
            if (!alfabeto.contains(String.valueOf(c)) && c != '&') {
                sb.append("Erro: expressão contém símbolos que não pertencem ao alfabeto!");
                txtSaida.setText(sb.toString());
                return;
            }
        }

        // Normaliza a expressão: + → | , remove '.' (concat explícita) ; mantém & como ε
        String normalizar = expressao.replace("+", "|").replace(".", "");

        String regexFinal;
        try {
            regexFinal = construirRegex(normalizar);
            // adiciona início e fim
            regexFinal = "^" + regexFinal + "$";
        } catch (Exception e) {
            sb.append("Erro ao interpretar a expressão: ").append(e.getMessage());
            txtSaida.setText(sb.toString());
            return;
        }

        // Gera exemplos para teste
        List<String> exemplos = gerarPalavras(new ArrayList<>(alfabeto), 6); // reduzir para 3 para exemplo
        exemplos.add(0, ""); // palavra vazia para teste

        sb.append("Expressão válida!\n");
        sb.append("A palavra vazia é simbolizada &\n");
        sb.append("Regex final: ").append(regexFinal).append("\n\n");
        sb.append("Testes de palavras:\n");

        for (String w : exemplos) {
            boolean aceita = w.matches(regexFinal);
            sb.append(String.format("%-6s -> %s\n", w.isEmpty() ? "&" : w, aceita ? "ACEITA" : "REJEITADA"));
        }

        txtSaida.setText(sb.toString());
    }

    private String construirRegex(String expr) throws Exception {
        expr = expr.trim();
        if (expr.isEmpty() || expr.equals("&")) return ""; // palavra vazia -> regex vazia
        return parseExpr(expr);
    }

    // parseExpr: trata uniões top-level (separadas por | ao nível 0)
    private String parseExpr(String expr) throws Exception {
        List<String> termos = new ArrayList<>();
        int nivel = 0;
        StringBuilder atual = new StringBuilder();

        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if (c == '(') nivel++;
            else if (c == ')') nivel--;

            if (c == '|' && nivel == 0) {
                termos.add(atual.toString());
                atual.setLength(0);
            } else {
                atual.append(c);
            }
        }
        // sempre adiciona o último (pode ser empty quando expr termina com '|' ou começa com '|')
        termos.add(atual.toString());

        if (termos.size() > 1) {
            List<String> partes = new ArrayList<>();
            for (String t : termos) {
                // cada termo vira uma concatenação (pode retornar "" para &)
                String p = parseConcat(t);
                partes.add(p); // p pode ser "" (epsilon) — mantenha assim para gerar alternativa vazia
            }
            // se todas as partes ficaram vazias, retorna vazio
            boolean allEmpty = true;
            for (String p : partes) if (!p.isEmpty()) { allEmpty = false; break; }
            if (allEmpty) return "";

            // monta união com grupo non-capturing; se existirem termos vazios, o join produzirá "": (?:|a|b)
            return "(?:" + String.join("|", partes) + ")";
        }

        // só um termo -> trata concatenação
        return parseConcat(termos.get(0));
    }

    // separa unidades (símbolos, parênteses, com possível Kleene *) e concatena
    private String parseConcat(String termo) throws Exception {
        termo = termo.trim();
        if (termo.isEmpty()) return ""; // vazio => ε
        List<String> unidades = new ArrayList<>();
        int i = 0;
        while (i < termo.length()) {
            char c = termo.charAt(i);
            if (c == '(') {
                int start = i;
                int nivel = 0;
                do {
                    char ch = termo.charAt(i);
                    if (ch == '(') nivel++;
                    else if (ch == ')') nivel--;
                    i++;
                    if (i > termo.length()) throw new Exception("Parênteses desbalanceados");
                } while (i < termo.length() && nivel > 0);
                String unit = termo.substring(start, i); // inclui parênteses
                // checa '*' após ')'
                if (i < termo.length() && termo.charAt(i) == '*') {
                    unit += "*";
                    i++;
                }
                unidades.add(unit);
            } else if (Character.isLetterOrDigit(c) || c == '&') {
                String unit = String.valueOf(c);
                i++;
                if (i < termo.length() && termo.charAt(i) == '*') {
                    unit += "*";
                    i++;
                }
                unidades.add(unit);
            } else if (c == '.') {
                // caso o usuário tenha esquecido a normalização; ignora concat explicita
                i++;
            } else {
                throw new Exception("Caractere inesperado na concatenação: '" + c + "'");
            }
        }

        StringBuilder sb = new StringBuilder();
        for (String u : unidades) {
            sb.append(parseUnidade(u));
        }
        return sb.toString();
    }

    private String parseUnidade(String unidade) throws Exception {
        unidade = unidade.trim();
        if (unidade.equals("&")) return ""; // ε

        boolean isStar = unidade.endsWith("*");
        String core = isStar ? unidade.substring(0, unidade.length() - 1) : unidade;
        String coreParsed;

        if (core.startsWith("(") && core.endsWith(")")) {
            // não reaplicar parênteses — parseExpr já produz (?:...) para uniões
            coreParsed = parseExpr(core.substring(1, core.length() - 1));
        } else {
            coreParsed = core; // símbolo único (a, b, etc)
        }

        if (isStar) {
            if (coreParsed.isEmpty()) return ""; // (ε)* = ε
            // se for um único símbolo alfanumérico: usar a*
            if (coreParsed.length() == 1 && Character.isLetterOrDigit(coreParsed.charAt(0))) {
                return coreParsed + "*";
            } else {
                // se já é um grupo non-capturing, apenas acrescenta '*'
                if (coreParsed.startsWith("(?:") && coreParsed.endsWith(")")) {
                    return coreParsed + "*";
                } else {
                    return "(?:" + coreParsed + ")*";
                }
            }
        } else {
            return coreParsed;
        }
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
        javafx.scene.Parent root = javafx.fxml.FXMLLoader.load(
                getClass().getResource("/org/example/trabalholfa/main-view.fxml"));
        javafx.scene.Scene scene = new javafx.scene.Scene(root);
        javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}

