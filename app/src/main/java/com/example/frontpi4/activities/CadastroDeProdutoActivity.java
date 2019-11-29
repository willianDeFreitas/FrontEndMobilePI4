package com.example.frontpi4.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.frontpi4.R;
import com.example.frontpi4.dto.ProdutoDTO;
import com.example.frontpi4.services.RetrofitService;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadastroDeProdutoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String TAG = "CadastroDeProdutoActivity";
    Spinner funcao;
    int funcao_selecionada;
    SimpleDateFormat formataData = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_de_produto);

        funcao = findViewById(R.id.sp_cadastro_categoria_produto);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.funcao_array_categoria, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        funcao.setAdapter(adapter);
        funcao.setOnItemSelectedListener(this);
    }

    public void cadastrarProduto(View view) {
        String nome = ((EditText)findViewById(R.id.et_cadastro_nome_produto)).getText().toString();
        String p = ((EditText)findViewById(R.id.et_cadastro_preco_produto)).getText().toString();
        String q = ((TextView)findViewById(R.id.et_cadastro_quantidade_produto)).getText().toString();
        String vol = ((TextView)findViewById(R.id.et_cadastro_volume_produto)).getText().toString();

        if ("".equals(nome)) {
            Toast.makeText(CadastroDeProdutoActivity.this, "Preencha o NOME do produto", Toast.LENGTH_LONG).show();
            return;
        }

        if ("".equals(p)) {
            Toast.makeText(CadastroDeProdutoActivity.this, "Preencha o PREÇO do produto", Toast.LENGTH_LONG).show();
            return;
        }

        if ("".equals(q)) {
            Toast.makeText(CadastroDeProdutoActivity.this, "Preencha o QUANTIDADE do produto", Toast.LENGTH_LONG).show();
            return;
        }

        if ("".equals(vol)) {
            Toast.makeText(CadastroDeProdutoActivity.this, "Preencha o VOLUME do produto", Toast.LENGTH_LONG).show();
            return;
        }

        double preco=Double.parseDouble(p);
        double qtd=Double.parseDouble(q);
        Long categoria=Long.valueOf(funcao_selecionada);

        Date data = new Date();
        String dataform = formataData.format(data);

        ProdutoDTO produtoDTO = null;
        String token = "";

        if (nome != "" && preco != 0 && qtd != 0 && categoria != 0 && vol != "" ) {
            produtoDTO = new ProdutoDTO(dataform, nome, preco, qtd, vol, categoria);
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


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        funcao_selecionada = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
