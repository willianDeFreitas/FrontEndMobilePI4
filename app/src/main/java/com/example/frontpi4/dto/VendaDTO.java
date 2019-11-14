package com.example.frontpi4.dto;

import java.util.Date;
import java.util.List;

public class VendaDTO {
    private int id;
    private int clienteId;
    private Date data;
    private Double totalV;
    private List<ItemVendaDTO> itensVenda = null;

    public VendaDTO(Date data, Double totalV, int clienteId, List<ItemVendaDTO> itensVenda) {
        this.data = data;
        this.totalV = totalV;
        this.clienteId = clienteId;
        this.itensVenda = itensVenda;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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