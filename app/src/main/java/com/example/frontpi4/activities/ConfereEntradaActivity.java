package com.example.frontpi4.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frontpi4.R;
import com.example.frontpi4.dto.ItemCompraDTO;
import com.example.frontpi4.dto.ProdutoDTO;
import com.example.frontpi4.services.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfereEntradaActivity extends AppCompatActivity {

    public static final String TAG = "ConfereEntradaActivity";

    TextView tv_idCompra, tv_idItemCompra, tv_fornecedor, tv_idProd;
    int idItemCompra;
    Double qtdNegociada;
    Long idCompra, idProd;
    ProgressBar pbCarregando;
    String token;
    ProdutoDTO produtoDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confere_entrada);

        pbCarregando = findViewById(R.id.pb_confere_entrada_carregando);
        pbCarregando.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();

        idCompra = intent.getLongExtra("idCompra",-1);
        idItemCompra = intent.getIntExtra("idItemCompra", -1);

        idProd = intent.getLongExtra("idProd", -1);
        buscaProduto(idProd);

        String produto = intent.getStringExtra("produto");
        String fornecedor = intent.getStringExtra("fornecedor");

        tv_idCompra = findViewById(R.id.tv_confere_entrada_idcompra);
        tv_idItemCompra = findViewById(R.id.tv_confere_entrada_iditemcompra);
        tv_idProd = findViewById(R.id.tv_confere_entrada_idproduto);
        tv_fornecedor = findViewById(R.id.tv_confere_entrada_fornecedor);

        tv_idCompra.setText("Compra: " + idCompra.toString() );
        tv_idItemCompra.setText("Item de compra: " + String.valueOf(idItemCompra));
        tv_idProd.setText("Produto: \n" + idProd.toString() + "-" + produto);
        tv_fornecedor.setText("Fornecedor: \n" + fornecedor);

        qtdNegociada = Double.parseDouble(intent.getStringExtra("qtdNegociada"));
    }


    public void conferirCompra(View view) {
        pbCarregando.setVisibility(View.VISIBLE);
        Double qtdProduto = produtoDTO.getQtd();

        String qtdConferidaStr = ((EditText)findViewById(R.id.et_confere_entrada_qtd)).getText().toString();
        final Double qtdConferida = Double.parseDouble(qtdConferidaStr);

        if (qtdNegociada.compareTo(qtdConferida) != 0){
            Toast.makeText(ConfereEntradaActivity.this, "Quantidade incorreta do produto", Toast.LENGTH_LONG).show();
            pbCarregando.setVisibility(View.INVISIBLE);
            return;
        }

        qtdProduto -= qtdConferida;

        token = getToken();

        ItemCompraDTO itemCompraDTO;
        itemCompraDTO = new ItemCompraDTO(idProd,idCompra, "S");

        RetrofitService.getServico().confereItemCompra(itemCompraDTO, idItemCompra, "Bearer "+token).enqueue(new Callback<ItemCompraDTO>() {
            @Override
            public void onResponse(Call<ItemCompraDTO> call, Response<ItemCompraDTO> response) {
                if(response.code() == 200){
                    onFailure(call, new Exception());
                } else {
                    Toast.makeText(ConfereEntradaActivity.this, "Erro: " + response.code(), Toast.LENGTH_LONG).show();
                    pbCarregando.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ItemCompraDTO> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

        ProdutoDTO produtoDTOUpdate = new ProdutoDTO(qtdProduto);

        RetrofitService.getServico().alteraProduto(produtoDTOUpdate, idProd, "Bearer "+token).enqueue(new Callback<ProdutoDTO>() {
            @Override
            public void onResponse(Call<ProdutoDTO> call, Response<ProdutoDTO> response) {
                if(response.code() == 200){
                    Toast.makeText(ConfereEntradaActivity.this, "Conferido com sucesso", Toast.LENGTH_LONG).show();
                    pbCarregando.setVisibility(View.INVISIBLE);
                    finish();
                    startActivity(new Intent(ConfereEntradaActivity.this, ConferenciaDeEntradaActivity.class));
                } else {
                    Toast.makeText(ConfereEntradaActivity.this, "Erro: " + response.code(), Toast.LENGTH_LONG).show();
                    pbCarregando.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ProdutoDTO> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private String getToken() {
        SharedPreferences sp = getSharedPreferences("dados",0);
        return sp.getString("token",null);
    }

    public void buscaProduto(Long idProd) {
        //# Rercuperando token salvo na activity de login
        SharedPreferences sp = getSharedPreferences("dados", 0);
        String token = sp.getString("token",null);
        //#

        RetrofitService.getServico().buscaProdutoId(idProd, "Bearer "+token).enqueue(new Callback<ProdutoDTO>() {
            @Override
            public void onResponse(Call<ProdutoDTO> call, Response<ProdutoDTO> response) {
                if(response.code() == 200){
                    produtoDTO = response.body();
                } else {
                    Toast.makeText(ConfereEntradaActivity.this, "Erro: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ProdutoDTO> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}
