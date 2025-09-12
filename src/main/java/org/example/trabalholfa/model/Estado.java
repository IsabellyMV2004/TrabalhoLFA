package org.example.trabalholfa.model;

public class Estado {
    private String nome;
    private double x;
    private double y;
    private boolean inicial;
    private boolean aceitacao;

    public Estado(String nome, double x, double y) {
        this.nome = nome;
        this.x = x;
        this.y = y;
        this.inicial = false;
        this.aceitacao = false;
    }

    public String getNome() {
        return nome;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setPosicao(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public boolean isInicial() {
        return inicial;
    }

    public void setInicial(boolean inicial) {
        this.inicial = inicial;
    }

    public boolean isAceitacao() {
        return aceitacao;
    }

    public void setAceitacao(boolean aceitacao) {
        this.aceitacao = aceitacao;
    }
}
