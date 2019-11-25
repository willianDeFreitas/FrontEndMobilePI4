package com.example.frontpi4.dto;

import java.util.List;

public class CompraDTO {
    private Long id;
    private int fornecedorId;
    private String data;
    private Double totalC;
    private List<ItemCompraDTO> itensCompra = null;

    public CompraDTO(String data, Double totalC, int fornecedorId, List<ItemCompraDTO> itensCompra) {
        this.data = data;
        this.totalC = totalC;
        this.fornecedorId = fornecedorId;
        this.itensCompra = itensCompra;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTotalC() {
        return totalC;
    }

    public void setTotalC(Double totalC) {
        this.totalC = totalC;
    }

    public int getFornecedorId() {
        return fornecedorId;
    }

    public void setFornecedorId(int fornecedorId) {
        this.fornecedorId = fornecedorId;
    }

    public List<ItemCompraDTO> getItensCompra() {
        return itensCompra;
    }

    public void setItensCompra(List<ItemCompraDTO> itensCompra) {
        this.itensCompra = itensCompra;
    }
}