package com.example.frontpi4.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.frontpi4.R;

public class TelaPrincipalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.action_cadastro_de_usuario:
                //navegar para activity(new Intent(desta classe/activity, para outra Activity/classe)
                startActivity(new Intent(this, CadastroDeUsuarioActivity.class));
                return true;
            case R.id.action_lista_usuarios:
                startActivity(new Intent(this, ListaUsuariosActivity.class));
                return true;
            case R.id.action_sair:
                finish();
                startActivity(new Intent(this, MainActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
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