package com.example.frontpi4.dto;

public class ItemCompraDTO {

    private String conferido;
    private Long id;
    private Long produtoId;
    private Double qtdItemC;
    private Double valorItemC;
    private Long compraId;
    private String produto;
    private String fornecedor;

    public ItemCompraDTO(Long id, Double qtdItemC, Double valorItemC, Long produtoId, Long compraId, String conferido) {
        this.qtdItemC = qtdItemC;
        this.valorItemC = valorItemC;
        this.produtoId = produtoId;
        this.compraId = compraId;
        this.conferido = conferido;
    }

    public ItemCompraDTO(Long produtoId, Long compraId, String conferido) {
        this.produtoId = produtoId;
        this.compraId = compraId;
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

    public Double getQtdItemC() {
        return qtdItemC;
    }

    public void setQtdItemC(Double qtdItemC) {
        this.qtdItemC = qtdItemC;
    }

    public Double getValorItemC() {
        return valorItemC;
    }

    public void setValorItemC(Double valorItemC) {
        this.valorItemC = valorItemC;
    }

    public Long getCompraId() {
        return compraId;
    }

    public void setCompraId(Long compraId) {
        this.compraId = compraId;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }
}