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
import com.example.frontpi4.dto.CompraDTO;
import com.example.frontpi4.dto.FornecedorDTO;
import com.example.frontpi4.dto.ItemCompraDTO;
import com.example.frontpi4.dto.ProdutoDTO;
import com.example.frontpi4.helpers.Singleton;
import com.example.frontpi4.services.RetrofitService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadastroPedidoDeCompraActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    public static final String TAG = "CadastroPedidoDeCompraActivity";

    Spinner spin_fornecedor;
    Spinner spin_produto;
    List<FornecedorDTO> listaDeFornecedores;
    List<ProdutoDTO> listaDeProdutos;
    List<ItemCompraDTO> listaDeItemCompra = new ArrayList<>();
    Singleton singleton = Singleton.getInstance();
    SimpleDateFormat formataData = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    Button btnConfirmarPedidoCompra;
    Button btnLimpaListaDeItensDeCompra;
    int qtdItemCompra = 0;
    ProgressBar pbCarregando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_pedido_de_compra);

        btnConfirmarPedidoCompra = findViewById(R.id.bt_cadastro_pedido_compra_confirma_pedido);
        btnConfirmarPedidoCompra.setEnabled(false);

        btnLimpaListaDeItensDeCompra = findViewById(R.id.bt_cadastro_pedido_compra_limpa_lista_itens);
        btnLimpaListaDeItensDeCompra.setEnabled(false);

        pbCarregando = findViewById(R.id.pb_cadastro_pedido_compra_carregando);
        pbCarregando.setVisibility(View.VISIBLE);

        spin_fornecedor = findViewById(R.id.spin_cadastro_pedido_compra_fornecedor);
        spin_produto = findViewById(R.id.spin_cadastro_pedido_compra_produto);

        buscaDadosSpinners();

        spin_fornecedor.setOnItemSelectedListener(this);
        spin_produto.setOnItemSelectedListener(this);
    }

    public void buscaDadosSpinners(){
        //# Rercuperando token salvo na activity de login
        SharedPreferences sp = getSharedPreferences("dados", 0);
        String token = sp.getString("token",null);
        //#

        RetrofitService.getServico().buscaFornecedores("Bearer "+token).enqueue(new Callback<List<FornecedorDTO>>() {
            @Override
            public void onResponse(Call<List<FornecedorDTO>> call, Response<List<FornecedorDTO>> response) {
                if (response.isSuccessful()) {
                    listaDeFornecedores = response.body();
                    List<String> listaNomesDeFornecedores = new ArrayList<>();
                    listaNomesDeFornecedores.add("-Selecione o fornecedor-");
                    for (FornecedorDTO fornecedorDTO : listaDeFornecedores) {
                        listaNomesDeFornecedores.add(fornecedorDTO.getNome());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            CadastroPedidoDeCompraActivity.this,
                            R.layout.spinner_item,
                            listaNomesDeFornecedores);

                    spin_fornecedor.setAdapter(adapter);
                } else {
                    Toast.makeText(CadastroPedidoDeCompraActivity.this, "Nenhum Fornecedor encontrado", Toast.LENGTH_LONG).show();
                    pbCarregando.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<FornecedorDTO>> call, Throwable t) {
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
                            CadastroPedidoDeCompraActivity.this,
                            R.layout.spinner_item,
                            listaNomeDeProdutos);
                    spin_produto.setAdapter(adapter);
                    pbCarregando.setVisibility(View.INVISIBLE);
                } else {
                    Toast.makeText(CadastroPedidoDeCompraActivity.this, "Nenhum produto encontrado", Toast.LENGTH_LONG).show();
                    pbCarregando.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<ProdutoDTO>> call, Throwable t) {
                Log.e("Produto: ", t.getMessage());
            }
        });
    }

    public void registraItemCompra (View view) {
        String conferido = "N";
        int fornecedorSelct = spin_fornecedor.getSelectedItemPosition();
        int produtoSelct = spin_produto.getSelectedItemPosition();
        String qtdItemVStr = ((EditText)findViewById(R.id.et_cadastro_pedido_compra_quantidade)).getText().toString();
        String valorItemVStr = ((EditText)findViewById(R.id.et_cadastro_pedido_compra_valor)).getText().toString();

        if (fornecedorSelct <= 0) {
            Toast.makeText(CadastroPedidoDeCompraActivity.this, "Selecione um FORNECEDOR", Toast.LENGTH_LONG).show();
            return;
        }

        if (produtoSelct <= 0) {
            Toast.makeText(CadastroPedidoDeCompraActivity.this, "Selecione um PRODUTO", Toast.LENGTH_LONG).show();
            return;
        }

        /*faz um ajuste para poder pegar a posição correta do produto no array*/
        produtoSelct -= 1;

        Long idProd = listaDeProdutos.get(produtoSelct).getId();

        if ("".equals(qtdItemVStr)) {
            Toast.makeText(CadastroPedidoDeCompraActivity.this, "Insira uma QUANTIDADE", Toast.LENGTH_LONG).show();
            return;
        }

        if ("".equals(valorItemVStr)) {
            Toast.makeText(CadastroPedidoDeCompraActivity.this, "Insira o VALOR DO ITEM", Toast.LENGTH_LONG).show();
            return;
        }

        Double qtdItemC = Double.parseDouble(qtdItemVStr);
        Double valorItemC = Double.parseDouble(valorItemVStr);

        ItemCompraDTO itemCompraDTO = new ItemCompraDTO(null, qtdItemC, valorItemC, idProd, null, conferido);
        listaDeItemCompra.add(itemCompraDTO);

        singleton.setListaDeItemCompra(listaDeItemCompra);

        qtdItemCompra++;
        singleton.setQtdItemCompra(qtdItemCompra);
        qtdItemCompra = singleton.getQtdItemCompra();
        Toast.makeText(CadastroPedidoDeCompraActivity.this, "Item de compra: "+ qtdItemCompra + " salvo", Toast.LENGTH_LONG).show();

        btnConfirmarPedidoCompra.setEnabled(true);
        btnLimpaListaDeItensDeCompra.setEnabled(true);
        spin_fornecedor.setEnabled(false);
    }

    public void confirmarPedidoCompra(View view) {
        pbCarregando.setVisibility(View.VISIBLE);
        Double totalC = 0.0;
        Date data = new Date();
        String dataform = formataData.format(data);
        int fornecedorSelct = spin_fornecedor.getSelectedItemPosition();

        /*faz um ajuste para poder pegar a posição correta do cliente no array*/
        fornecedorSelct -= 1;

        int fornecedorId = listaDeFornecedores.get(fornecedorSelct).getId();

        for (ItemCompraDTO itemCompraDTO : singleton.getListaDeItemCompra()){
            Double vlrItemCompra = itemCompraDTO.getValorItemC();
            totalC += vlrItemCompra;
        }


        CompraDTO compraDTO =  new CompraDTO(dataform, totalC, fornecedorId, singleton.getListaDeItemCompra());

        String token = getToken();

        RetrofitService.getServico().cadastraPedidoDeCompra(compraDTO, "Bearer "+token).enqueue(new Callback<CompraDTO>() {
            @Override
            public void onResponse(Call<CompraDTO> call, Response<CompraDTO> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CadastroPedidoDeCompraActivity.this, "Pedido de compra inserido com sucesso", Toast.LENGTH_LONG).show();
                    singleton.getListaDeItemCompra().clear();
                    //singleton.setQtdItemCompra(0);
                    finish();
                    startActivity(new Intent(CadastroPedidoDeCompraActivity.this, TelaPrincipalActivity.class));
                } else {
                    Toast.makeText(CadastroPedidoDeCompraActivity.this, "Problemas ao cadastrar Pedido ", Toast.LENGTH_LONG).show();
                    pbCarregando.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<CompraDTO> call, Throwable t) {
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

    public void cancelaPedidoDeCompra(View view) {
        finish();
        startActivity(new Intent(CadastroPedidoDeCompraActivity.this, TelaPrincipalActivity.class));
    }

    public void limpaListaDeItensDeCompra(View view) {
        singleton.getListaDeItemCompra().clear();
        singleton.setQtdItemCompra(0);
        qtdItemCompra = 0;
        btnConfirmarPedidoCompra.setEnabled(false);
        btnLimpaListaDeItensDeCompra.setEnabled(false);
        spin_fornecedor.setEnabled(true);
        Toast.makeText(CadastroPedidoDeCompraActivity.this, "Lista de itens foi apagada", Toast.LENGTH_LONG).show();
    }
}
