package org.example.trabalholfa.model;

import java.util.ArrayList;
import java.util.List;

public class Automato {
    private List<Estado> estados;
    private List<Transicao> transicoes;

    public Automato() {
        this.estados = new ArrayList<>();
        this.transicoes = new ArrayList<>();
    }

    public void adicionarEstado(Estado e) {
        estados.add(e);
    }

    public void adicionarTransicao(Transicao t) {
        transicoes.add(t);
    }

    public List<Estado> getEstados() {
        return estados;
    }

    public List<Transicao> getTransicoes() {
        return transicoes;
    }
}
