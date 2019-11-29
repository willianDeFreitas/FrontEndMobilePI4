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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ExibirProdutosActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String TAG = "ExibirProdutoActivity";

    TextView et_qtd, et_vol;
    EditText et_nome,et_preco, et_categoria ;
   int id;

    Spinner funcao;
    int funcao_selecionada;


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
        et_vol = findViewById(R.id.tv_volume_produto);

        et_nome.setText(nome);
        et_preco.setText(preco);
        et_qtd.setText(qtd);
        et_vol.setText(vol);
//        et_categoria.setText(categoriaId);

        funcao = findViewById(R.id.sp_exibir_categoria_produto);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.funcao_array_categoria,  R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        funcao.setAdapter(adapter);
        funcao.setOnItemSelectedListener(this);
        funcao.setSelection(Integer.parseInt(categoriaId));


    }

    public void alterarProduto(View view) {
        String nome = ((EditText)findViewById(R.id.et_nome_produto)).getText().toString();
        String p = ((EditText)findViewById(R.id.et_preco_produto)).getText().toString();
        String q = ((TextView)findViewById(R.id.tv_quantidade_produto)).getText().toString();
        String vol = ((TextView)findViewById(R.id.tv_volume_produto)).getText().toString();;

        ProdutoDTO produtoDTO;

        double preco=Double.parseDouble(p);
        double qtd=Double.parseDouble(q);
        Long categoria=Long.valueOf(funcao_selecionada);
        Long idProduto= Long.valueOf(id);
        produtoDTO = new ProdutoDTO(nome,preco,qtd,vol,categoria);

        String token ="" ;


        if (nome != "" && preco != 0 && qtd != 0 && categoria != 0 && vol != "" ) {
            produtoDTO = new ProdutoDTO(nome,preco,qtd,vol,categoria);
            token = getToken();
        }

        RetrofitService.getServico().alteraProduto(produtoDTO, idProduto, "Bearer "+token).enqueue(new Callback<ProdutoDTO>() {
            @Override
            public void onResponse(Call<ProdutoDTO> call, Response<ProdutoDTO> response) {
                if(response.code() == 200){
                    Toast.makeText(ExibirProdutosActivity.this, "Produto alterado com sucesso", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ExibirProdutosActivity.this, ProdutosActivity.class));
                    onFailure(call, new Exception());
                } else {
                    Toast.makeText(ExibirProdutosActivity.this, "Erro: Usuario não autorizado " + response.code(), Toast.LENGTH_LONG).show();
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
        String e = sp.getString("email",null);
        return sp.getString("token",null);
    }
    private String getEmail() {
        SharedPreferences sp = getSharedPreferences("dados",0);

        return sp.getString("email",null);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        funcao_selecionada = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
