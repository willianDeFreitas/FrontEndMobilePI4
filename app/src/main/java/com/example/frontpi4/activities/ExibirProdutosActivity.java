package com.example.frontpi4.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.frontpi4.R;
import com.example.frontpi4.dto.ProdutoDTO;
import com.example.frontpi4.services.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ExibirProdutosActivity extends Activity {

    public static final String TAG = "ExibirProdutoActivity";

    TextView et_qtd, et_vol;
    EditText et_nome,et_preco, et_categoria ;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exibir_produto);

        Intent intent = getIntent();

        id = intent.getIntExtra("id", -1);
        String nome = intent.getStringExtra("nome");
        String preco = intent.getStringExtra("preco");
        String qtd = intent.getStringExtra("qtd");
        String  vol = intent.getStringExtra("vol");
        String categoriaId = intent.getStringExtra("catId");


        et_nome = findViewById(R.id.et_nome_produto);
        et_preco = findViewById(R.id.et_preco_produto);
        et_qtd = findViewById(R.id.tv_quantidade_produto);
        et_categoria = findViewById(R.id.et_categoria_produto);

        et_nome.setText(nome);
        et_preco.setText(preco);
        et_qtd.setText(qtd);
        et_categoria.setText(categoriaId);

    }

    public void alterarProduto(View view) {
        String nome = ((EditText)findViewById(R.id.et_nome_produto)).getText().toString();
        String p = ((EditText)findViewById(R.id.et_preco_produto)).getText().toString();
        String q = ((TextView)findViewById(R.id.tv_quantidade_produto)).getText().toString();
        String catId = ((EditText)findViewById(R.id.et_categoria_produto)).getText().toString();
        String vol = " ";

        ProdutoDTO produtoDTO;

        double preco=Double.parseDouble(p);
        double qtd=Double.parseDouble(q);
        Long categoria=Long.valueOf(catId);

        produtoDTO = new ProdutoDTO(nome,preco,qtd,vol,categoria);

        String token = getToken();

        RetrofitService.getServico().alteraProduto(produtoDTO, id, "Bearer "+token).enqueue(new Callback<ProdutoDTO>() {
            @Override
            public void onResponse(Call<ProdutoDTO> call, Response<ProdutoDTO> response) {
                if(response.code() == 200){
                    Toast.makeText(ExibirProdutosActivity.this, "Produto alterado com sucesso", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ExibirProdutosActivity.this, ProdutosActivity.class));
                    onFailure(call, new Exception());
                } else {
                    Toast.makeText(ExibirProdutosActivity.this, "Erro: " + response.code(), Toast.LENGTH_LONG).show();
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
}
