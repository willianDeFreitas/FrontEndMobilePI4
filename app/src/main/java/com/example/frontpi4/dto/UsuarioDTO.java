package com.example.frontpi4.dto;

public class UsuarioDTO {

    private String email;
    private int id;
    private String nome;
    private String senha;
    private int funcao;

    public UsuarioDTO(String email, String nome) {
        this.email = email;
        this.nome = nome;
    }

    public UsuarioDTO(String email, String nome, String senha) {
        this.email = email;
        this.nome = nome;
        this.senha = senha;
        this.funcao = funcao;
    }

    public UsuarioDTO(String email, String nome, String senha, int funcao) {
        this.email = email;
        this.nome = nome;
        this.senha = senha;
        this.funcao = funcao;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getFuncao() {
        return funcao;
    }

    public void setFuncao(int funcao) {
        this.funcao = funcao;
    }

}