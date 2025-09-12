package org.example.trabalholfa.view;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.*;
import java.util.*;

/**
 * Um Pane que funciona como editor de autômatos (estados = bolinhas, transições = linhas).
 * Uso no FXML: new AutomatonCanvas() — por isso tem construtor sem-args.
 *
 * Observação: saveToJFF/loadFromJFF gravam/lerem um formato simples custom (linhas começando com S; ou T;).
 * Você pode trocar a implementação por uma que use jflap-lib para gravar .jff.
 */
public class AutomatonCanvas extends Pane {

    public enum Mode { SELECT, ADD_STATE, ADD_TRANSITION }

    private Mode mode = Mode.SELECT;

    private final List<StateNode> states = new ArrayList<>();
    private final List<TransitionEdge> transitions = new ArrayList<>();

    // temporário para criação de transição
    private StateNode pendingTransitionSource = null;

    // estilo / constantes
    private static final double STATE_RADIUS = 28;

    public AutomatonCanvas() {
        setPrefSize(800, 600);
        setStyle("-fx-background-color: white; -fx-border-color: lightgray;");

        // clique no canvas: criar estado quando estiver no modo ADD_STATE
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onCanvasClick);
    }

    // -------- public API usado pelo Representacao2 --------
    public void setMode(Mode m) {
        this.mode = m;
        // limpar seleção temporária se trocar de modo
        if (m != Mode.ADD_TRANSITION) pendingTransitionSource = null;
        updateStateStyles();
    }

    public Mode getMode() { return mode; }

    /**
     * Salva em um formato simples custom:
     * Linhas:
     * S;nome;x;y;inicial;final
     * T;origemNome;destinoNome;simbolo
     *
     * Substitua este método por código que use jflap-lib para gerar .jff quando quiser.
     */
    public void saveToJFF(File f) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(f))) {
            for (StateNode s : states) {
                pw.printf("S;%s;%.3f;%.3f;%b;%b%n",
                        escape(s.name), s.getCenterX(), s.getCenterY(), s.initial, s.accepting);
            }
            for (TransitionEdge t : transitions) {
                pw.printf("T;%s;%s;%s%n", escape(t.from.name), escape(t.to.name), escape(t.label));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Le o formato simples custom acima. Substituir por parser de .jff usando jflap-lib quando quiser.
     */
    public void loadFromJFF(File f) {
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            clearAll();
            String line;
            // duas passadas: primeiro estados, depois transições
            List<String> tlines = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                if (line.startsWith("S;")) {
                    // S;nome;x;y;inicial;final
                    String[] parts = line.split(";", 6);
                    if (parts.length >= 6) {
                        String name = unescape(parts[1]);
                        double x = Double.parseDouble(parts[2]);
                        double y = Double.parseDouble(parts[3]);
                        boolean inicial = Boolean.parseBoolean(parts[4]);
                        boolean finalE = Boolean.parseBoolean(parts[5]);
                        addStateAtLoaded(name, x, y, inicial, finalE);
                    }
                } else if (line.startsWith("T;")) {
                    tlines.add(line);
                }
            }
            // processa transições
            for (String tline : tlines) {
                String[] p = tline.split(";", 4);
                if (p.length >= 4) {
                    String from = unescape(p[1]);
                    String to = unescape(p[2]);
                    String label = unescape(p[3]);
                    StateNode fnode = findStateByName(from);
                    StateNode tnode = findStateByName(to);
                    if (fnode != null && tnode != null) {
                        createTransition(fnode, tnode, label);
                    }
                }
            }
            redrawAll();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // -----------------------------------------------------

    private void onCanvasClick(MouseEvent ev) {
        // Se o clique for sobre um state node, esse evento será consumido por ele.
        if (mode == Mode.ADD_STATE && ev.getButton() == MouseButton.PRIMARY) {
            double x = ev.getX();
            double y = ev.getY();
            // nome automático q0..qn
            String name = "q" + states.size();
            addState(name, x, y);
        }
    }

    private void addState(String name, double centerX, double centerY) {
        StateNode s = new StateNode(name, centerX, centerY);
        states.add(s);
        // transitions devem ficar atrás dos estados: add(0, edge)
        getChildren().add(s);
        redrawAll();
    }

    private void addStateAtLoaded(String name, double centerX, double centerY, boolean inicial, boolean finalE) {
        StateNode s = new StateNode(name, centerX, centerY);
        s.initial = inicial;
        s.accepting = finalE;
        states.add(s);
        getChildren().add(s);
    }

    private void createTransition(StateNode from, StateNode to, String label) {
        // evita duplicatas exatas (pode permitir múltiplas com labels diferentes)
        TransitionEdge edge = new TransitionEdge(from, to, label);
        transitions.add(edge);
        // add antes dos estados (índice 0) para ficar atrás
        getChildren().add(0, edge);
        redrawAll();
    }

    private void redrawAll() {
        // atualiza linha de cada transição
        for (TransitionEdge t : transitions) t.update();
    }

    private void updateStateStyles() {
        for (StateNode s : states) s.updateStyle();
    }

    private void clearAll() {
        // remove tudo do pane (transitions e states)
        getChildren().clear();
        states.clear();
        transitions.clear();
        pendingTransitionSource = null;
    }

    private StateNode findStateByName(String name) {
        for (StateNode s : states) if (s.name.equals(name)) return s;
        return null;
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace(";", "\\;");
    }
    private static String unescape(String s) {
        if (s == null) return "";
        return s.replace("\\;", ";").replace("\\\\", "\\");
    }

    // -------------------- Inner classes --------------------

    /**
     * Visual de um estado: StackPane com Circle e Text. O layoutX/Y do node definem o canto superior esquerdo,
     * mas nós trabalhamos com centro (getCenterX/Y).
     */
    private class StateNode extends Group {
        String name;
        Circle circle;
        Text label;
        boolean initial = false;
        boolean accepting = false;

        double dragOffsetX, dragOffsetY;

        StateNode(String name, double centerX, double centerY) {
            this.name = name;
            this.circle = new Circle(STATE_RADIUS);
            this.circle.setStroke(Color.BLACK);
            this.circle.setFill(Color.WHITE);

            this.label = new Text(name);
            this.label.setFont(Font.font(14));
            this.label.setY(5); // ajuste vertical relativo ao centro

            // compor visual: circle e label centralizados
            // Para Group, precisamos controlar transform/position: usaremos layoutX/layoutY no próprio Group
            this.getChildren().addAll(circle, label);
            // posicionar o label no centro (aprox)
            label.setX(-label.getLayoutBounds().getWidth() / 2);

            // colocar com centro em (centerX, centerY)
            setLayoutX(centerX - STATE_RADIUS);
            setLayoutY(centerY - STATE_RADIUS);

            // mouse handlers:
            this.setOnMousePressed(this::onPressed);
            this.setOnMouseDragged(this::onDragged);
            this.setOnMouseClicked(this::onClicked);
            this.setOnMouseReleased(this::onReleased);

            updateStyle();
        }

        double getCenterX() {
            return getLayoutX() + STATE_RADIUS;
        }

        double getCenterY() {
            return getLayoutY() + STATE_RADIUS;
        }

        void setCenter(double cx, double cy) {
            setLayoutX(cx - STATE_RADIUS);
            setLayoutY(cy - STATE_RADIUS);
            redrawAll();
        }

        void updateStyle() {
            circle.setStrokeWidth(accepting ? 3.0 : 1.5);
            if (pendingTransitionSource == this) {
                circle.setStroke(Color.DARKORANGE);
            } else {
                circle.setStroke(Color.BLACK);
            }
            // desenho da seta inicial será feito nas transições (ou pode desenhar uma linha)
        }

        private void onPressed(MouseEvent ev) {
            // prepara arraste
            dragOffsetX = ev.getX();
            dragOffsetY = ev.getY();
            ev.consume();
        }

        private void onDragged(MouseEvent ev) {
            if (mode == Mode.SELECT || mode == Mode.ADD_STATE) {
                double parentX = ev.getSceneX() - getScene().getWindow().getX() - getParent().getLayoutX(); // avoid complexity
                // Simples: atualiza com delta
                double newLayoutX = getLayoutX() + (ev.getX() - dragOffsetX);
                double newLayoutY = getLayoutY() + (ev.getY() - dragOffsetY);
                setLayoutX(newLayoutX);
                setLayoutY(newLayoutY);
                redrawAll();
            }
            ev.consume();
        }

        private void onClicked(MouseEvent ev) {
            if (ev.getButton() == MouseButton.PRIMARY) {
                if (mode == Mode.ADD_TRANSITION) {
                    if (pendingTransitionSource == null) {
                        pendingTransitionSource = this;
                        updateStateStyles();
                    } else if (pendingTransitionSource != this) {
                        // pedir label para a transição
                        TextInputDialog d = new TextInputDialog("a");
                        d.setHeaderText("Rótulo da transição");
                        d.setContentText("Símbolo:");
                        Optional<String> res = d.showAndWait();
                        String labelText = res.orElse("a");
                        createTransition(pendingTransitionSource, this, labelText);
                        pendingTransitionSource = null;
                        updateStateStyles();
                    } else {
                        // clicou no mesmo: cancela
                        pendingTransitionSource = null;
                        updateStateStyles();
                    }
                } else if (mode == Mode.SELECT) {
                    // clique duplo para editar nome
                    if (ev.getClickCount() == 2) {
                        TextInputDialog d = new TextInputDialog(this.name);
                        d.setHeaderText("Editar nome do estado");
                        d.setContentText("Nome:");
                        d.showAndWait().ifPresent(newName -> {
                            this.name = newName;
                            label.setText(newName);
                            label.setX(-label.getLayoutBounds().getWidth() / 2);
                        });
                    }
                }
            } else if (ev.getButton() == MouseButton.SECONDARY) {
                // botão direito: alterna inicial / final
                // se SHIFT estiver pressionado -> alterna estado inicial, senão alterna aceitacao
                if (ev.isShiftDown()) {
                    this.initial = !this.initial;
                } else {
                    this.accepting = !this.accepting;
                }
                updateStyle();
                redrawAll();
            }
            ev.consume();
        }

        private void onReleased(MouseEvent ev) {
            // ao soltar, garante que linhas atualizem
            redrawAll();
            ev.consume();
        }
    }

    /**
     * Visual da transição: linha + texto no meio.
     */
    private class TransitionEdge extends Group {
        StateNode from, to;
        Line line;
        Text text;
        String label;

        TransitionEdge(StateNode from, StateNode to, String label) {
            this.from = from;
            this.to = to;
            this.label = label == null ? "" : label;
            this.line = new Line();
            this.line.setStroke(Color.BLACK);
            this.text = new Text(this.label);
            this.text.setFont(Font.font(12));
            getChildren().addAll(line, text);
            update();
        }

        void update() {
            double x1 = from.getCenterX();
            double y1 = from.getCenterY();
            double x2 = to.getCenterX();
            double y2 = to.getCenterY();

            // ajustar para sair/entrar na borda do círculo
            double dx = x2 - x1;
            double dy = y2 - y1;
            double dist = Math.sqrt(dx*dx + dy*dy);
            double ux = dx / (dist == 0 ? 1 : dist);
            double uy = dy / (dist == 0 ? 1 : dist);

            double startX = x1 + ux * STATE_RADIUS;
            double startY = y1 + uy * STATE_RADIUS;
            double endX = x2 - ux * STATE_RADIUS;
            double endY = y2 - uy * STATE_RADIUS;

            line.setStartX(startX);
            line.setStartY(startY);
            line.setEndX(endX);
            line.setEndY(endY);

            // label no meio
            text.setX((startX + endX) / 2 - text.getLayoutBounds().getWidth() / 2);
            text.setY((startY + endY) / 2 - 6);
        }
    }
}
