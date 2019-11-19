package com.example.frontpi4.dto;

public class ClienteDTO {

    private Long id;
    private String nome;
    private String email;
    private String cpf;
    private String end;
    private String tel;

    public ClienteDTO(Long id, String nome, String cpf, String end, String email, String tel) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.end = end;
        this.email = email;
        this.tel = tel;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}