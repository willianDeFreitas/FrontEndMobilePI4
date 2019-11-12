package com.example.cruddeusuarios.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cruddeusuarios.R;
import com.example.cruddeusuarios.dto.DtoCliente;
import com.example.cruddeusuarios.dto.DtoProduto;
import com.example.cruddeusuarios.dto.DtoUser;
import com.example.cruddeusuarios.services.RetrofitService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadastroPedidoDeVendaActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    public static final String TAG = "CadastroPedidoDevendaActivity";
    Spinner spin_cliente;
    Spinner spin_produto;
    List<DtoCliente> listaDeClientes;
    List<DtoProduto> listaDeProdutos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_pedido_de_venda);
        spin_cliente = findViewById(R.id.spin_cadastro_pedido_venda_cliente);
        spin_produto = findViewById(R.id.spin_cadastro_pedido_venda_produto);
        buscaDados();
        spin_cliente.setOnItemSelectedListener(this);
        spin_produto.setOnItemSelectedListener(this);
    }

    public void buscaDados(){
        //# Rercuperando token salvo na activity de login
        SharedPreferences sp = getSharedPreferences("dados", 0);
        String token = sp.getString("token",null);
        //#

        RetrofitService.getServico().buscaClientes("Bearer "+token).enqueue(new Callback<List<DtoCliente>>() {
            @Override
            public void onResponse(Call<List<DtoCliente>> call, Response<List<DtoCliente>> response) {
                listaDeClientes = response.body();
                List<String> listaNomesDeClientes = new ArrayList<>();
                for (DtoCliente cliente : listaDeClientes) {
                    listaNomesDeClientes.add(cliente.getName());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        CadastroPedidoDeVendaActivity.this,
                        android.R.layout.simple_spinner_item,
                        listaNomesDeClientes);
                spin_cliente.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<DtoCliente>> call, Throwable t) {
                Log.e("Cliente: ", t.getMessage());
            }
        });

        RetrofitService.getServico().buscaProdutos("Bearer "+token).enqueue(new Callback<List<DtoProduto>>() {
            @Override
            public void onResponse(Call<List<DtoProduto>> call, Response<List<DtoProduto>> response) {
                listaDeProdutos = response.body();
                List<String> listaDeProdutos = new ArrayList<>();
                for (DtoProduto produto : listaDeProdutos) {
                    listaDeProdutos.add(produto.getName());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        CadastroPedidoDeVendaActivity.this,
                        android.R.layout.simple_spinner_item,
                        listaDeProdutos);
                spin_produto.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<DtoProduto>> call, Throwable t) {
                Log.e("Produto: ", t.getMessage());
            }
        });
    }



    public void cadastrar(View view) {
        String nome = ((EditText)findViewById(R.id.et_cadastro_usuario_nome)).getText().toString();
        String telefone = ((EditText)findViewById(R.id.et_cadastro_usuario_telefone)).getText().toString();
        String email = ((EditText)findViewById(R.id.et_cadastro_usuario_email)).getText().toString();
        String senha = ((EditText)findViewById(R.id.et_cadastro_usuario_password)).getText().toString();

        DtoUser dtoUser =  new DtoUser(email, nome, senha, telefone);

        RetrofitService.getServico().cadastraUsuario(dtoUser).enqueue(new Callback<DtoUser>() {
            @Override
            public void onResponse(Call<DtoUser> call, Response<DtoUser> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CadastroPedidoDeVendaActivity.this, "Pedido de venda cadastrado com ID: " + response.body().getId(), Toast.LENGTH_LONG).show();
                    startActivity(new Intent(CadastroPedidoDeVendaActivity.this, MainActivity.class));
                } else {
                    Toast.makeText(CadastroPedidoDeVendaActivity.this, "Problemas ao cadastratrar usuário ", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<DtoUser> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
