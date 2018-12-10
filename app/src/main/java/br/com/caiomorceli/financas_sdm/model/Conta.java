package br.com.caiomorceli.financas_sdm.model;

import java.io.Serializable;

public class Conta implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String descricao;
    private double saldo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
}
