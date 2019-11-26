package com.example.frontpi4.dto;

public class ProdutoDTO {
    private Long id;
    private String nome;
    private Double preco;
    private Double qtd;
    private String vol;
    private Long categoriaId;
    private String data;

    public ProdutoDTO(String nome, Double preco, Double qtd, String vol, Long categoriaId){
        this.nome = nome;
        this.preco = preco;
        this.qtd = qtd;
        this.vol = vol;
        this.categoriaId = categoriaId;
    }

    public ProdutoDTO(Long id, String nome, Double preco, Double qtd, String vol, Long categoriaId, String data){
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.qtd = qtd;
        this.vol = vol;
        this.categoriaId = categoriaId;
        this.data = data;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Double getPreco() {
        return preco;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
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

    public String getVol() {
        return vol;
    }

    public void setVol(String vol) {
        this.vol = vol;
    }

    public Double getQtd() {
        return qtd;
    }

    public void setQtd(Double qtd) {
        this.qtd = qtd;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}