package com.example.frontpi4.dto;

public class FornecedorDTO {

    private int id;
    private String nome;
    private String email;
    private String cnpj;
    private String end;
    private String telefone;

    public FornecedorDTO(String nome, String cnpj, String end, String email, String telefone) {

        this.nome = nome;
        this.cnpj = cnpj;
        this.end = end;
        this.email = email;
        this.telefone = telefone;
    }

    public String getcnpj() {
        return cnpj;
    }

    public void setcnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
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

    public String gettelefone() {
        return telefone;
    }

    public void settelefone(String tel) {
        this.telefone = telefone;
    }
}