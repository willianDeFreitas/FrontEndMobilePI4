package com.example.frontpi4.dto;

public class ClienteDTO {

    private int id;
    private String nome;
    private String email;
    private String cpf;
    private String end;
    private String tel;

    public ClienteDTO( String nome, String email, String cpf, String end, String tel) {

        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.end = end;
        this.tel = tel;
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

     public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}