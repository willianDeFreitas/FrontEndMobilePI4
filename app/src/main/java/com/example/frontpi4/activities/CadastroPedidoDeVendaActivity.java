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
import android.widget.ProgressBar;
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
    Button btnLimpaListaDeItensDeVenda;
    int qtdItemVenda = 0;
    ProgressBar pbCarregando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_pedido_de_venda);

        btnConfirmarPedidoVenda = findViewById(R.id.bt_cadastro_pedido_venda_confirma_pedido);
        btnConfirmarPedidoVenda.setEnabled(false);

        btnLimpaListaDeItensDeVenda = findViewById(R.id.bt_cadastro_pedido_venda_limpa_lista_itens);
        btnLimpaListaDeItensDeVenda.setEnabled(false);

        pbCarregando = findViewById(R.id.pb_cadastro_pedido_venda_carregando);
        pbCarregando.setVisibility(View.VISIBLE);

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
                if (response.isSuccessful()) {
                    listaDeClientes = response.body();
                    List<String> listaNomesDeClientes = new ArrayList<>();
                    listaNomesDeClientes.add("-Selecione o cliente-");
                    for (ClienteDTO cliente : listaDeClientes) {
                        listaNomesDeClientes.add(cliente.getNome());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            CadastroPedidoDeVendaActivity.this,
                            R.layout.spinner_item,
                            listaNomesDeClientes);

                    spin_cliente.setAdapter(adapter);
                } else {
                    Toast.makeText(CadastroPedidoDeVendaActivity.this, "Nenhum Cliente encontrado", Toast.LENGTH_LONG).show();
                    pbCarregando.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<ClienteDTO>> call, Throwable t) {
                Log.e("Cliente: ", t.getMessage());
            }
        });

        RetrofitService.getServico().buscaProdutos("Bearer "+token).enqueue(new Callback<List<ProdutoDTO>>() {
            @Override
            public void onResponse(Call<List<ProdutoDTO>> call, Response<List<ProdutoDTO>> response) {
                if (response.isSuccessful()) {
                    listaDeProdutos = response.body();
                    List<String> listaNomeDeProdutos = new ArrayList<>();
                    listaNomeDeProdutos.add("-Selecione o produto-");

                    for (ProdutoDTO produto : listaDeProdutos) {
                        listaNomeDeProdutos.add(produto.getNome());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            CadastroPedidoDeVendaActivity.this,
                            R.layout.spinner_item,
                            listaNomeDeProdutos);
                    spin_produto.setAdapter(adapter);
                    pbCarregando.setVisibility(View.INVISIBLE);
                } else {
                    Toast.makeText(CadastroPedidoDeVendaActivity.this, "Nenhum produto encontrado", Toast.LENGTH_LONG).show();
                    pbCarregando.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<ProdutoDTO>> call, Throwable t) {
                Log.e("Produto: ", t.getMessage());
            }
        });
    }

    public void registraItemVenda (View view) {
        String conferido = "N";
        int clienteSelct = spin_cliente.getSelectedItemPosition();
        int produtoSelct = spin_produto.getSelectedItemPosition();
        String qtdItemVStr = ((EditText)findViewById(R.id.et_cadastro_pedido_venda_quantidade)).getText().toString();
        String valorItemVStr = ((EditText)findViewById(R.id.et_cadastro_pedido_venda_valor)).getText().toString();

        if (clienteSelct <= 0) {
            Toast.makeText(CadastroPedidoDeVendaActivity.this, "Selecione um CLIENTE", Toast.LENGTH_LONG).show();
            return;
        }

        if (produtoSelct <= 0) {
            Toast.makeText(CadastroPedidoDeVendaActivity.this, "Selecione um PRODUTO", Toast.LENGTH_LONG).show();
            return;
        }

        /*faz um ajuste para poder pegar a posição correta do produto no array*/
        produtoSelct -= 1;

        Long idProd = listaDeProdutos.get(produtoSelct).getId();

        if ("".equals(qtdItemVStr)) {
            Toast.makeText(CadastroPedidoDeVendaActivity.this, "Insira uma QUANTIDADE", Toast.LENGTH_LONG).show();
            return;
        }

        if ("".equals(valorItemVStr)) {
            Toast.makeText(CadastroPedidoDeVendaActivity.this, "Insira o VALOR DO ITEM", Toast.LENGTH_LONG).show();
            return;
        }

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
        btnLimpaListaDeItensDeVenda.setEnabled(true);
        spin_cliente.setEnabled(false);
    }

    public void confirmarPedidoVenda(View view) {
        pbCarregando.setVisibility(View.VISIBLE);
        Double totalV = 0.0;
        Date data = new Date();
        String dataform = formataData.format(data);
        int clienteSelct = spin_cliente.getSelectedItemPosition();

        /*faz um ajuste para poder pegar a posição correta do cliente no array*/
        clienteSelct -= 1;

        int clienteId = listaDeClientes.get(clienteSelct).getId();

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
                    singleton.getListaDeItemVenda().clear();
                    singleton.setQtdItemVenda(0);
                    finish();
                    startActivity(new Intent(CadastroPedidoDeVendaActivity.this, TelaPrincipalActivity.class));
                } else {
                    Toast.makeText(CadastroPedidoDeVendaActivity.this, "Problemas ao cadastrar Pedido ", Toast.LENGTH_LONG).show();
                    pbCarregando.setVisibility(View.INVISIBLE);
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

    public void limpaListaDeItensDeVenda(View view) {
        singleton.getListaDeItemVenda().clear();
        singleton.setQtdItemVenda(0);
        qtdItemVenda = 0;
        btnConfirmarPedidoVenda.setEnabled(false);
        btnLimpaListaDeItensDeVenda.setEnabled(false);
        spin_cliente.setEnabled(true);
        Toast.makeText(CadastroPedidoDeVendaActivity.this, "Lista de itens foi apagada", Toast.LENGTH_LONG).show();
    }
}