package com.example.frontpi4.dto;

public class ItemVendaDTO{

    private String conferido;
    private Long id;
    private Long produtoId;
    private Double qtdItemV;
    private Double valorItemV;
    private Long vendaId;

    public ItemVendaDTO(Long id, Double qtdItemV, Double valorItemV, Long produtoId, Long vendaId, String conferido) {
        this.qtdItemV = qtdItemV;
        this.valorItemV = valorItemV;
        this.produtoId = produtoId;
        this.vendaId = vendaId;
        this.conferido = conferido;
    }

    public ItemVendaDTO(Long produtoId, Long vendaId, String conferido) {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public Long getVendaId() {
        return vendaId;
    }

    public void setVendaId(Long vendaId) {
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