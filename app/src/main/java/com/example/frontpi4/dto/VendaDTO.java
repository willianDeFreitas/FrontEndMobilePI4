package com.example.frontpi4.dto;

import java.util.Date;
import java.util.List;

public class VendaDTO {
    private Long id;
    private Long clienteId;
    private Date data;
    private Double totalV;
    private List<ItemVendaDTO> itensVenda = null;

    public VendaDTO(Date data, Double totalV, Long clienteId, List<ItemVendaDTO> itensVenda) {
        this.data = data;
        this.totalV = totalV;
        this.clienteId = clienteId;
        this.itensVenda = itensVenda;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ItemVendaDTO> getItemVenda() {
        return itensVenda;
    }

    public void setItemVenda(List<ItemVendaDTO> itensVenda) {
        this.itensVenda = itensVenda;
    }

    public Double getTotalV() {
        return totalV;
    }

    public void setTotalV(Double totalV) {
        this.totalV = totalV;
    }

}