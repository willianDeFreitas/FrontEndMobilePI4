package com.example.frontpi4.activities;

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

import com.example.frontpi4.R;
import com.example.frontpi4.dto.ClienteDTO;
import com.example.frontpi4.dto.ItemVendaDTO;
import com.example.frontpi4.dto.ProdutoDTO;
import com.example.frontpi4.dto.UsuarioDTO;
import com.example.frontpi4.dto.VendaDTO;
import com.example.frontpi4.helpers.Singleton;
import com.example.frontpi4.services.RetrofitService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadastroPedidoDeVendaActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    public static final String TAG = "CadastroPedidoDevendaActivity";

    Spinner spin_cliente;
    Spinner spin_produto;
    List<ClienteDTO> listaDeClientes;
    List<ProdutoDTO> listaDeProdutos;
    List<ItemVendaDTO> listaDeItemVenda = new ArrayList<>();

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

        RetrofitService.getServico().buscaClientes("Bearer "+token).enqueue(new Callback<List<ClienteDTO>>() {
            @Override
            public void onResponse(Call<List<ClienteDTO>> call, Response<List<ClienteDTO>> response) {
                listaDeClientes = response.body();
                List<String> listaNomesDeClientes = new ArrayList<>();
                for (ClienteDTO cliente : listaDeClientes) {
                   listaNomesDeClientes.add(cliente.getNome());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        CadastroPedidoDeVendaActivity.this,
                        android.R.layout.simple_spinner_item,
                        listaNomesDeClientes);
                spin_cliente.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<ClienteDTO>> call, Throwable t) {
                Log.e("Cliente: ", t.getMessage());
            }
        });

        RetrofitService.getServico().buscaProdutos("Bearer "+token).enqueue(new Callback<List<ProdutoDTO>>() {
            @Override
            public void onResponse(Call<List<ProdutoDTO>> call, Response<List<ProdutoDTO>> response) {
                listaDeProdutos = response.body();
                List<String> listaNomeDeProdutos = new ArrayList<>();
                for (ProdutoDTO produto : listaDeProdutos) {
                    listaNomeDeProdutos.add(produto.getNome());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        CadastroPedidoDeVendaActivity.this,
                        android.R.layout.simple_spinner_item,
                        listaNomeDeProdutos);
                spin_produto.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<ProdutoDTO>> call, Throwable t) {
                Log.e("Produto: ", t.getMessage());
            }
        });
    }

    public void criaListaPedidoVenda (View view) {
        String conferido = "N";
        int produtoSelct = spin_produto.getSelectedItemPosition();
        String itemSelecionado = listaDeProdutos.get(produtoSelct).getNome();
        Long idProd = listaDeProdutos.get(produtoSelct).getId();
        String qtdItemVStr = ((EditText)findViewById(R.id.et_cadastro_pedido_venda_quantidade)).getText().toString();
        String valorItemVStr = ((EditText)findViewById(R.id.et_cadastro_pedido_venda_valor)).getText().toString();

        Double qtdItemV = Double.parseDouble(qtdItemVStr);
        Double valorItemV = Double.parseDouble(valorItemVStr);

        ItemVendaDTO itemVendaDTO = new ItemVendaDTO(null, qtdItemV, valorItemV, idProd, null, conferido);
        listaDeItemVenda.add(itemVendaDTO);

        Singleton singleton = Singleton.getInstance();
        singleton.setListaDeItemVenda(listaDeItemVenda);

        //editText.setEnabled(true);
        //editText.setEnabled(false);
    }

    public void cadastrar(View view) {
        Date data = new Date(System.currentTimeMillis());
        int clientId = 0;
        String email = ((EditText)findViewById(R.id.et_cadastro_usuario_email)).getText().toString();
        Double totalV = 0.0;

        VendaDTO vendaDTO =  new VendaDTO(data, totalV, clientId, listaDeItemVenda);

        String token = getToken();

        RetrofitService.getServico().cadastraPedidoDeVenda(vendaDTO, "Bearer "+token).enqueue(new Callback<VendaDTO>() {
            @Override
            public void onResponse(Call<VendaDTO> call, Response<VendaDTO> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CadastroPedidoDeVendaActivity.this, "Pedido de venda cadastrado com ID: " + response.body().getId(), Toast.LENGTH_LONG).show();
                    startActivity(new Intent(CadastroPedidoDeVendaActivity.this, MainActivity.class));
                } else {
                    Toast.makeText(CadastroPedidoDeVendaActivity.this, "Problemas ao cadastrar Pedido ", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<VendaDTO> call, Throwable t) {
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

    private String getToken() {
        SharedPreferences sp = getSharedPreferences("dados",0);
        return sp.getString("token",null);
    }
}
