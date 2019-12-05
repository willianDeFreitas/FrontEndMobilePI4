package com.example.frontpi4.helpers;

import com.example.frontpi4.dto.ItemCompraDTO;
import com.example.frontpi4.dto.ItemVendaDTO;

import java.util.List;

public class Singleton {
    private static Singleton _instance;
    private List<ItemVendaDTO> listaDeItemVenda;
    private List<ItemCompraDTO> listaDeItemCompra;
    private int qtdItemVenda;
    private int qtdItemCompra;
    private String nomeProduto;

    private Singleton() { }

    public static Singleton getInstance() {
        if (_instance == null) {
            synchronized (Singleton.class){
                if (_instance == null) {
                    _instance = new Singleton();
                }
            }
        }
        return _instance;
    }

    public List<ItemVendaDTO> getListaDeItemVenda() {
        return listaDeItemVenda;
    }

    public void setListaDeItemVenda(List<ItemVendaDTO> listaDeItemVenda) {
        this.listaDeItemVenda = listaDeItemVenda;
    }
    public void removerListaDeItemVenda(List<ItemVendaDTO> listaDeItemVenda) {
        this.listaDeItemVenda = listaDeItemVenda;
    }

    public int getQtdItemVenda() {
        return qtdItemVenda;
    }

    public void setQtdItemVenda(int qtdItemVenda) {
        this.qtdItemVenda = qtdItemVenda;
    }

    public List<ItemCompraDTO> getListaDeItemCompra() {
        return listaDeItemCompra;
    }

    public void setListaDeItemCompra(List<ItemCompraDTO> listaDeItemCompra) {
        this.listaDeItemCompra = listaDeItemCompra;
    }

    public int getQtdItemCompra() {
        return qtdItemCompra;
    }

    public void setQtdItemCompra(int qtdItemCompra) {
        this.qtdItemCompra = qtdItemCompra;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }
}