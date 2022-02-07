package com.marcowilly.organizze.model;

import com.google.firebase.database.Exclude;
public final class Usuario {

    private String idUsuario, nome, email, senha;
    private double receitaTotal = 0.0;
    private double despesaTotal = 0.0;

    public Usuario(final String nome, final String email, final String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public Usuario(final String email, final String senha) {
        this.email = email;
        this.senha = senha;
    }

    public Usuario(){}

    public Double getReceitaTotal() {
        return receitaTotal;
    }

    public void setReceitaTotal(Double receitaTotal) {
        this.receitaTotal = receitaTotal;
    }

    public double getDespesaTotal() {
        return despesaTotal;
    }

    public void setDespesaTotal(double despesaTotal) {
        this.despesaTotal = despesaTotal;
    }

    @Exclude
    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
