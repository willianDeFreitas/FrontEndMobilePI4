package com.example.frontpi4.dto;

public class ItemVendaDTO{

    private String conferido;
    private int id;
    private int produtoId;
    private Double qtdItemV;
    private Double valorItemV;
    private int vendaId;

    public ItemVendaDTO(int id, Double qtdItemV, Double valorItemV, int produtoId, int vendaId, String conferido) {
        this.qtdItemV = qtdItemV;
        this.valorItemV = valorItemV;
        this.produtoId = produtoId;
        this.vendaId = vendaId;
        this.conferido = conferido;
    }

    public String getConferido() {
        return conferido;
    }

    public void setConferido(String conferido) {
        this.conferido = conferido;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(int produtoId) {
        this.produtoId = produtoId;
    }

    public int getVendaId() {
        return vendaId;
    }

    public void setVendaId(int vendaId) {
        this.vendaId = vendaId;
    }

    public Double getQtdItemV() {
        return qtdItemV;
    }

    public void setQtdItemV(Double qtdItemV) {
        this.qtdItemV = qtdItemV;
    }

    public Double getValorItemV() {
        return valorItemV;
    }

    public void setValorItemV(Double valorItemV) {
        this.valorItemV = valorItemV;
    }
}