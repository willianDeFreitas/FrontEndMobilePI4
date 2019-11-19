package com.example.frontpi4.helpers;

import com.example.frontpi4.dto.ItemVendaDTO;

import java.util.ArrayList;
import java.util.List;

public class Singleton {
    private static Singleton _instance;
    private List<ItemVendaDTO> listaDeItemVenda;

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
}