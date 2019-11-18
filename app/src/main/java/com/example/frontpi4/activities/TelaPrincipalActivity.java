package com.example.frontpi4.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.frontpi4.R;

public class TelaPrincipalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);
    }

    public void clientes(View view) {
        startActivity(new Intent(this, ClientesActivity.class));
    }

    public void fornecedores(View view) {
        startActivity(new Intent(this, FornecedoresActivity.class));
    }

    public void vendas(View view) {
        startActivity(new Intent(this, CadastroPedidoDeVendaActivity.class));
    }

    public void compras(View view) {
        startActivity(new Intent(this, CadastroPedidoDeCompraActivity.class));
    }

    public void confEntrada(View view) {
        startActivity(new Intent(this, ConferenciaDeEntradaActivity.class));
    }

    public void confSaida(View view) {
        startActivity(new Intent(this, ConferenciaDeSaidaActivity.class));
    }

    public void produtos(View view) {
        startActivity(new Intent(this, ProdutosActivity.class));
    }
}