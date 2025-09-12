package org.example.trabalholfa.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.example.trabalholfa.model.Automato;
import org.example.trabalholfa.model.Estado;
import org.example.trabalholfa.model.Transicao;

public class AutomatoDrawer {

    private Automato automato;
    private Canvas canvas;

    public AutomatoDrawer(Automato automato, Canvas canvas) {
        this.automato = automato;
        this.canvas = canvas;
    }

    public void desenhar() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // desenhar transições
        gc.setStroke(Color.BLACK);
        for (Transicao t : automato.getTransicoes()) {
            double x1 = t.getOrigem().getX();
            double y1 = t.getOrigem().getY();
            double x2 = t.getDestino().getX();
            double y2 = t.getDestino().getY();

            gc.strokeLine(x1, y1, x2, y2);
            gc.fillText(t.getSimbolo(), (x1 + x2) / 2, (y1 + y2) / 2);
        }

        // desenhar estados
        for (Estado e : automato.getEstados()) {
            double r = 30;
            gc.setStroke(Color.BLACK);
            gc.strokeOval(e.getX() - r / 2, e.getY() - r / 2, r, r);
            gc.fillText(e.getNome(), e.getX() - 5, e.getY() + 5);

            if (e.isAceitacao()) {
                gc.strokeOval(e.getX() - r / 2 - 4, e.getY() - r / 2 - 4, r + 8, r + 8);
            }
            if (e.isInicial()) {
                gc.strokeLine(e.getX() - 50, e.getY(), e.getX() - r / 2, e.getY());
            }
        }
    }
}
