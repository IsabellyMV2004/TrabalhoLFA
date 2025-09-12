/*package org.example.trabalholfa;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.util.*;

public class Representacao3 {
    @FXML
    private TextArea txtSaida, txtGramatica, txtPalavras;

    private Map<String, List<String>> regras = new HashMap<>();

    @FXML
    public void executarGR(ActionEvent actionEvent) {
        String gramatica = txtGramatica.getText();
        String palavrasTeste = txtPalavras.getText();
        StringBuilder sb = new StringBuilder();

        if (gramatica == null || gramatica.trim().isEmpty()) {
            txtSaida.setText("Digite uma gramática.");
            return;
        }

        try {
            // Parse da gramática
            regras.clear();
            String[] linhas = gramatica.split("\n");
            for (String linha : linhas) {
                linha = linha.trim();
                if (linha.isEmpty()) continue;

                String[] partes = linha.split("->");
                if (partes.length != 2) throw new Exception("Regra inválida: " + linha);

                String naoTerminal = partes[0].trim();
                String producoes = partes[1].trim();

                String[] lados = producoes.split("\\|");
                for (String prod : lados) {
                    regras.computeIfAbsent(naoTerminal, k -> new ArrayList<>())
                            .add(prod.trim());
                }
            }

            sb.append("Gramática carregada com sucesso!\n\n");

            if (palavrasTeste == null || palavrasTeste.trim().isEmpty()) {
                // Gerar 10 primeiras palavras aceitas
                sb.append("Primeiras palavras aceitas:\n");
                List<String> geradas = gerarPrimeirasAceitas("S", 10);
                for (String w : geradas) {
                    sb.append(w.isEmpty() ? "&" : w).append("\n");
                }
            } else {
                // Testar palavras fornecidas
                sb.append("Testes das palavras:\n");
                String[] lista = palavrasTeste.split("\n");
                for (String w : lista) {
                    String palavra = w.trim();
                    if (palavra.equals("&")) palavra = "";
                    boolean aceita = pertenceAGramatica(palavra);
                    sb.append(String.format("%-6s -> %s\n", w,
                            aceita ? "ACEITA" : "REJEITADA"));
                }
            }

            txtSaida.setText(sb.toString());
        } catch (Exception e) {
            txtSaida.setText("Erro ao processar a gramática: " + e.getMessage());
        }
    }

    // Verifica se a palavra pertence à gramática (backtracking simples)
    private boolean pertenceAGramatica(String palavra) {
        return deriva("S", palavra, 0);
    }

    private boolean deriva(String simbolo, String palavra, int pos) {
        if (!regras.containsKey(simbolo)) {
            // Terminal
            if (pos < palavra.length() && palavra.charAt(pos) == simbolo.charAt(0)) {
                return pos + 1 == palavra.length();
            }
            return false;
        }

        for (String prod : regras.get(simbolo)) {
            if (prod.equals("&")) {
                if (pos == palavra.length()) return true;
            } else {
                if (tentaDerivar(prod, palavra, pos)) return true;
            }
        }
        return false;
    }

    private boolean tentaDerivar(String producao, String palavra, int pos) {
        if (pos > palavra.length()) return false;

        int atual = pos;
        for (int i = 0; i < producao.length(); i++) {
            char c = producao.charAt(i);
            String s = String.valueOf(c);

            if (Character.isUpperCase(c)) {
                if (deriva(s, palavra, atual)) return true;
                return false;
            } else {
                if (atual >= palavra.length() || palavra.charAt(atual) != c) return false;
                atual++;
            }
        }
        return atual == palavra.length();
    }

    // Gera até 'limite' palavras aceitas pela gramática
    private List<String> gerarPrimeirasAceitas(String inicial, int limite) {
        List<String> aceitas = new ArrayList<>();
        Queue<String> fila = new LinkedList<>();
        fila.add(inicial);

        while (!fila.isEmpty() && aceitas.size() < limite) {
            String atual = fila.poll();

            if (!atual.matches(".*[A-Z].*")) { // não contém não-terminais
                aceitas.add(atual);
            } else {
                for (int i = 0; i < atual.length(); i++) {
                    char c = atual.charAt(i);
                    if (Character.isUpperCase(c)) {
                        String naoTerminal = String.valueOf(c);
                        if (regras.containsKey(naoTerminal)) {
                            for (String prod : regras.get(naoTerminal)) {
                                String novo = atual.substring(0, i) +
                                        (prod.equals("&") ? "" : prod) +
                                        atual.substring(i + 1);
                                fila.add(novo);
                            }
                        }
                        break; // só expande um NT por vez
                    }
                }
            }
        }

        Collections.sort(aceitas);
        return aceitas.size() > limite ? aceitas.subList(0, limite) : aceitas;
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
}*/




package org.example.trabalholfa;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.util.*;

public class Representacao3 {
    @FXML
    private TextArea txtSaida, txtGramatica, txtPalavras;

    private Map<String, List<String>> regras = new HashMap<>();

    @FXML
    public void executarGR(ActionEvent actionEvent) {
        String gramatica = txtGramatica.getText();
        String palavrasTeste = txtPalavras == null ? null : txtPalavras.getText();
        StringBuilder sb = new StringBuilder();

        if (gramatica == null || gramatica.trim().isEmpty())
            txtSaida.setText("Digite uma gramática.");
        else{
            try {
                String[] linhas, lados, lista;
                String nt, prodStr, prod, palavra;
                Set<String> alfabetoSet = new TreeSet<>();
                List<String> alfabeto, geradas;
                char ch;
                int limite = 10;
                int maxLen = 7;
                boolean aceita;

                regras.clear();
                linhas = gramatica.split("\\r?\\n");
                for (String linha : linhas) {
                    linha = linha.trim();
                    if (linha.isEmpty()) continue;
                    lados = linha.split("->");
                    if (lados.length != 2) throw new Exception("Regra inválida: " + linha);
                    nt = lados[0].trim();
                    prodStr = lados[1].trim();
                    String[] prods = prodStr.split("\\|");
                    for (String p : prods) {
                        prod = p.trim();
                        regras.computeIfAbsent(nt, k -> new ArrayList<>()).add(prod);
                    }
                }

                sb.append("Gramática carregada com sucesso!\n\n");

                // descobrir alfabeto (terminais) a partir das produções
                for (List<String> prods : regras.values())
                    for (String p : prods)
                        for (int i = 0; i < p.length(); ++i)
                        {
                            ch = p.charAt(i);
                            if (!Character.isUpperCase(ch) && ch != '&')
                                alfabetoSet.add(String.valueOf(ch));
                        }


                alfabeto = new ArrayList<>(alfabetoSet);

                if (palavrasTeste == null || palavrasTeste.trim().isEmpty()) {
                    geradas = gerarPrimeirasAceitas(alfabeto, limite, maxLen);
                    sb.append("Primeiras palavras aceitas:\n");
                    for (String w : geradas)
                        sb.append(w.isEmpty() ? "&" : w).append("\n");
                } else {
                    sb.append("Testes das palavras:\n");
                    lista = palavrasTeste.split("\\r?\\n");
                    for (String w : lista) {
                        palavra = w.trim();
                        if (palavra.equals("&")) palavra = "";
                        aceita = pertenceAGramatica(palavra);
                        sb.append(String.format("%-8s -> %s\n", w.isEmpty() ? "&" : w, aceita ? "ACEITA" : "REJEITADA"));
                    }
                }

                txtSaida.setText(sb.toString());
            } catch (Exception ex) {
                txtSaida.setText("Erro ao processar a gramática: " + ex.getMessage());
            }
        }
    }

    // Retorna true se palavra pertence à gramática
    private boolean pertenceAGramatica(String palavra) {
        Set<Integer> posicoes = derivePosicoes("S", palavra, 0);
        return posicoes.contains(palavra.length());
    }

    // Retorna conjunto de posições possiveis
    private Set<Integer> derivePosicoes(String simbolo, String palavra, int pos) {
        Set<Integer> resultado = new HashSet<>();
        List<String> prods = regras.get(simbolo);
        if (prods == null)
            return resultado; // NT desconhecido -> vazio

        for (String prod : prods) {
            if (prod.equals("&"))
                resultado.add(pos);
            else
                resultado.addAll(analisarPosicao(prod, palavra, pos));
        }
        return resultado;
    }

    private Set<Integer> analisarPosicao(String prod, String palavra, int pos) {
        Set<Integer> posicoes = new HashSet<>();
        int i = 0;
        boolean interromper = false;
        char ch;
        Set<Integer> proximo;

        posicoes.add(pos);
        while (i < prod.length() && !interromper) {
            ch = prod.charAt(i);
            proximo = new HashSet<>();

            for (int p : posicoes)
                if (p <= palavra.length())
                    if (Character.isUpperCase(ch))
                    {
                        Set<Integer> res = derivePosicoes(String.valueOf(ch), palavra, p);
                        proximo.addAll(res);
                    }
                    else
                        if (ch == '&')
                            proximo.add(p);
                        else
                            if (p < palavra.length() && palavra.charAt(p) == ch)
                                proximo.add(p + 1);


            posicoes = proximo;
            if (posicoes.isEmpty())
                interromper = true;

            i++;
        }

        return posicoes;
    }

    // Gera as primeiras palavras aceitas pela gramática
    private List<String> gerarPrimeirasAceitas(List<String> alfabeto, int limite, int maxLen) {
        List<String> aceitas = new ArrayList<>();
        List<String> alfabetoUsado = (alfabeto == null) ? new ArrayList<>() : alfabeto;
        int len = 1;
        boolean aceitaVazio = pertenceAGramatica("");

        if (aceitaVazio)
            aceitas.add("");

        while (len <= maxLen && aceitas.size() < limite)
        {
            gerarDeTamanho(alfabetoUsado, len, "", aceitas, limite);
            len++;
        }

        return aceitas;
    }

    // Função recursiva para gerar strings de tamanho fixo
    private void gerarDeTamanho(List<String> alfabeto, int faltam, String prefixo, List<String> aceitas, int limite) {
        boolean deveContinuar = aceitas.size() < limite;
        boolean aceita;
        int i = 0;
        if (faltam == 0 && deveContinuar)
        {
            aceita = pertenceAGramatica(prefixo);
            if (aceita)
                aceitas.add(prefixo);
        }
        else
            while (i < alfabeto.size() && deveContinuar)
            {
                String s = alfabeto.get(i);
                gerarDeTamanho(alfabeto, faltam - 1, prefixo + s, aceitas, limite);

                // Atualiza se ainda pode continuar
                deveContinuar = aceitas.size() < limite;
                i++;
            }
    }

    @FXML
    public void voltar(ActionEvent actionEvent) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/org/example/trabalholfa/main-view.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}

