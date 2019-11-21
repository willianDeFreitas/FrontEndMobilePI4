package com.example.frontpi4.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.frontpi4.R;
import com.example.frontpi4.dto.ClienteDTO;
import com.example.frontpi4.dto.ItemVendaDTO;
import com.example.frontpi4.dto.ProdutoDTO;
import com.example.frontpi4.dto.VendaDTO;
import com.example.frontpi4.helpers.Singleton;
import com.example.frontpi4.services.RetrofitService;

import java.text.SimpleDateFormat;
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
    Singleton singleton = Singleton.getInstance();
    SimpleDateFormat formataData = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    Button btnConfirmarPedidoVenda;
    int qtdItemVenda = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_pedido_de_venda);

        btnConfirmarPedidoVenda = findViewById(R.id.bt_cadastro_pedido_venda_confirma_pedido);
        btnConfirmarPedidoVenda.setEnabled(false);

        spin_cliente = findViewById(R.id.spin_cadastro_pedido_venda_cliente);
        spin_produto = findViewById(R.id.spin_cadastro_pedido_venda_produto);

        buscaDadosSpinners();

        spin_cliente.setOnItemSelectedListener(this);
        spin_produto.setOnItemSelectedListener(this);
    }

    public void buscaDadosSpinners(){
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
                        R.layout.spinner_item,
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
                        R.layout.spinner_item,
                        listaNomeDeProdutos);
                spin_produto.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<ProdutoDTO>> call, Throwable t) {
                Log.e("Produto: ", t.getMessage());
            }
        });
    }

    public void registraItemVenda (View view) {
        String conferido = "N";
        int produtoSelct = spin_produto.getSelectedItemPosition();
        Long idProd = listaDeProdutos.get(produtoSelct).getId();
        String qtdItemVStr = ((EditText)findViewById(R.id.et_cadastro_pedido_venda_quantidade)).getText().toString();
        String valorItemVStr = ((EditText)findViewById(R.id.et_cadastro_pedido_venda_valor)).getText().toString();

        Double qtdItemV = Double.parseDouble(qtdItemVStr);
        Double valorItemV = Double.parseDouble(valorItemVStr);

        ItemVendaDTO itemVendaDTO = new ItemVendaDTO(null, qtdItemV, valorItemV, idProd, null, conferido);
        listaDeItemVenda.add(itemVendaDTO);

        singleton.setListaDeItemVenda(listaDeItemVenda);

        qtdItemVenda++;
        singleton.setQtdItemVenda(qtdItemVenda);
        qtdItemVenda = singleton.getQtdItemVenda();
        Toast.makeText(CadastroPedidoDeVendaActivity.this, "Item de venda: "+ qtdItemVenda +" salvo", Toast.LENGTH_LONG).show();

        btnConfirmarPedidoVenda.setEnabled(true);
        spin_cliente.setEnabled(false);
    }

    public void confirmarPedidoVenda(View view) {
        Double totalV = 0.0;
        Date data = new Date();
        String dataform = formataData.format(data);
        int clienteSelct = spin_cliente.getSelectedItemPosition();
        Long clienteId = listaDeClientes.get(clienteSelct).getId();

        for (ItemVendaDTO itemVendaDTO : singleton.getListaDeItemVenda()){
            Double vlrItemVenda = itemVendaDTO.getValorItemV();
            totalV += vlrItemVenda;
        }

        VendaDTO vendaDTO =  new VendaDTO(dataform, totalV, clienteId, singleton.getListaDeItemVenda());

        String token = getToken();

        RetrofitService.getServico().cadastraPedidoDeVenda(vendaDTO, "Bearer "+token).enqueue(new Callback<VendaDTO>() {
            @Override
            public void onResponse(Call<VendaDTO> call, Response<VendaDTO> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CadastroPedidoDeVendaActivity.this, "Pedido de venda inserido com sucesso", Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(new Intent(CadastroPedidoDeVendaActivity.this, TelaPrincipalActivity.class));
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

    public void cancelaPedidoDeVenda(View view) {
        finish();
        startActivity(new Intent(CadastroPedidoDeVendaActivity.this, TelaPrincipalActivity.class));
    }
}