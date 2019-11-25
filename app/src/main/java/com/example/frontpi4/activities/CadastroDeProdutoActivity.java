package com.example.frontpi4.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.frontpi4.R;
import com.example.frontpi4.dto.ProdutoDTO;
import com.example.frontpi4.services.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadastroDeProdutoActivity extends AppCompatActivity {

    public static final String TAG = "CadastroDeProdutoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_de_produto);

    }

    public void cadastrarProduto(View view) {
        String nome = ((EditText)findViewById(R.id.et_cadastro_nome_produto)).getText().toString();
        String p = ((EditText)findViewById(R.id.et_cadastro_preco_produto)).getText().toString();
        String q = ((TextView)findViewById(R.id.et_cadastro_quantidade_produto)).getText().toString();
        String catId = ((EditText)findViewById(R.id.et_cadastro_categoria_produto)).getText().toString();
        String vol = ((TextView)findViewById(R.id.et_cadastro_volume_produto)).getText().toString();;

        double preco=Double.parseDouble(p);
        double qtd=Double.parseDouble(q);
        Long categoria=Long.valueOf(catId);



        ProdutoDTO produtoDTO = null;
        String token = "";

        if (nome != "" && preco != 0 && qtd != 0 && categoria != 0 && vol != "" ) {

            produtoDTO = new ProdutoDTO(nome,preco,qtd,vol,categoria);;
            token = getToken();
        }

        RetrofitService.getServico().cadastraProduto(produtoDTO, "Bearer "+token).enqueue(new Callback<ProdutoDTO>() {
            @Override
            public void onResponse(Call<ProdutoDTO> call, Response<ProdutoDTO> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CadastroDeProdutoActivity.this, "Produto cadastrado com ID: " + response.body().getId(), Toast.LENGTH_LONG).show();
                    startActivity(new Intent(CadastroDeProdutoActivity.this, ProdutosActivity.class));
                } else {
                    Toast.makeText(CadastroDeProdutoActivity.this, "Problemas ao cadastratrar produto ", Toast.LENGTH_LONG).show();
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