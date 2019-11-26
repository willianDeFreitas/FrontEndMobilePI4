package com.example.frontpi4.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.frontpi4.R;
import com.example.frontpi4.dto.ItemVendaDTO;
import com.example.frontpi4.dto.ProdutoDTO;
import com.example.frontpi4.services.RetrofitService;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfereSaidaActivity extends AppCompatActivity {

    public static final String TAG = "ConfereSaidaActivity";

    TextView tv_idVenda, tv_idItemVenda, tv_cliente, tv_idProd;
    int idItemVenda;
    Double qtdNegociada;
    Long idVenda, idProd;
    ProgressBar pbCarregando;
    String token;
    ProdutoDTO produtoDTO;
    SimpleDateFormat formataData = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confere_saida);

        pbCarregando = findViewById(R.id.pb_confere_saida_carregando);
        pbCarregando.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();

        idVenda = intent.getLongExtra("idVenda",-1);
        idItemVenda = intent.getIntExtra("idItemVenda", -1);

        idProd = intent.getLongExtra("idProd", -1);
        buscaProduto(idProd);

        String produto = intent.getStringExtra("produto");
        String cliente = intent.getStringExtra("cliente");

        tv_idVenda = findViewById(R.id.tv_confere_saida_idvenda);
        tv_idItemVenda = findViewById(R.id.tv_confere_saida_iditemvenda);
        tv_idProd = findViewById(R.id.tv_confere_saida_idproduto);
        tv_cliente = findViewById(R.id.tv_confere_saida_cliente);

        tv_idVenda.setText("Venda: " + idVenda.toString() );
        tv_idItemVenda.setText("Item de venda: " + String.valueOf(idItemVenda));
        tv_idProd.setText("Produto: \n" + idProd.toString() + "-" + produto);
        tv_cliente.setText("Cliente: \n" + cliente);

        qtdNegociada = Double.parseDouble(intent.getStringExtra("qtdNegociada"));
    }

    public void conferirVenda(View view) {
        pbCarregando.setVisibility(View.VISIBLE);
        Double qtdProduto = produtoDTO.getQtd();
        Date data = new Date();
        String dataform = formataData.format(data);
        String qtdConferidaStr = ((EditText)findViewById(R.id.et_confere_saida_qtd)).getText().toString();
        final Double qtdConferida = Double.parseDouble(qtdConferidaStr);

        if (qtdNegociada.compareTo(qtdConferida) != 0){
            Toast.makeText(ConfereSaidaActivity.this, "Quantidade incorreta do produto", Toast.LENGTH_LONG).show();
            return;
        }

        qtdProduto -= qtdConferida;

        token = getToken();

        ItemVendaDTO itemVendaDTO;
        itemVendaDTO = new ItemVendaDTO(idProd,idVenda, "S");

        RetrofitService.getServico().confereItemVenda(itemVendaDTO, idItemVenda, "Bearer "+token).enqueue(new Callback<ItemVendaDTO>() {
            @Override
            public void onResponse(Call<ItemVendaDTO> call, Response<ItemVendaDTO> response) {
                if(response.code() == 200){
                    Toast.makeText(ConfereSaidaActivity.this, "Conferido com sucesso", Toast.LENGTH_LONG).show();
                    onFailure(call, new Exception());
                } else {
                    Toast.makeText(ConfereSaidaActivity.this, "Erro: " + response.code(), Toast.LENGTH_LONG).show();
                    pbCarregando.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ItemVendaDTO> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

        ProdutoDTO produtoDTOUpdate = new ProdutoDTO(produtoDTO.getId()
                , produtoDTO.getNome()
                , produtoDTO.getPreco()
                , qtdProduto
                , produtoDTO.getVol()
                , produtoDTO.getCategoriaId()
                , produtoDTO.getData());

        RetrofitService.getServico().alteraProduto(produtoDTOUpdate, idProd, "Bearer "+token).enqueue(new Callback<ProdutoDTO>() {
            @Override
            public void onResponse(Call<ProdutoDTO> call, Response<ProdutoDTO> response) {
                if(response.code() == 200){
                    Toast.makeText(ConfereSaidaActivity.this, "FOI!!" + response.code(), Toast.LENGTH_LONG).show();
                    pbCarregando.setVisibility(View.INVISIBLE);
                    finish();
                    startActivity(new Intent(ConfereSaidaActivity.this, ConferenciaDeSaidaActivity.class));
                } else {
                    Toast.makeText(ConfereSaidaActivity.this, "Erro: " + response.code(), Toast.LENGTH_LONG).show();
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
                    Toast.makeText(ConfereSaidaActivity.this, "Erro: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ProdutoDTO> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}