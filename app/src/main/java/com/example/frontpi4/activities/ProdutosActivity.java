package com.example.frontpi4.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.frontpi4.R;
import com.example.frontpi4.dto.ProdutoDTO;
import com.example.frontpi4.dto.UsuarioDTO;
import com.example.frontpi4.helpers.ProdutoAdapter;
import com.example.frontpi4.helpers.SwipeToDeleteCallback;
import com.example.frontpi4.helpers.SwipeToDeleteCallbackProduto;
import com.example.frontpi4.helpers.UsuarioAdapter;
import com.example.frontpi4.services.RetrofitService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProdutosActivity extends AppCompatActivity {

    public static final String TAG = "ProdutoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_produtos);

        buscaDados();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabProduto);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProdutosActivity.this, CadastroDeProdutoActivity.class));
            }
        });
    }

    private void preencheRecyclerview(List<ProdutoDTO> lista){
        RecyclerView mRecyclerView = findViewById(R.id.rv_todos_produto);
        ProdutoAdapter mAdapter = new ProdutoAdapter(this, lista);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallbackProduto(mAdapter));
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    private void buscaDados(){
        //# Rercuperando token salvo na activity de login
        SharedPreferences sp = getSharedPreferences("dados", 0);
        String token = sp.getString("token",null);
        //#

        RetrofitService.getServico().buscaProdutos("Bearer "+token).enqueue(new Callback<List<ProdutoDTO>>() {
            @Override
            public void onResponse(Call<List<ProdutoDTO>> call, Response<List<ProdutoDTO>> response) {
                if (response.isSuccessful()) {
                    List<ProdutoDTO> lista = response.body();
                    preencheRecyclerview(lista);
                } else {
                    startActivity(new Intent(ProdutosActivity.this, LoginActivity.class));
                    onFailure(call, new Exception());
                }
            }

            @Override
            public void onFailure(Call<List<ProdutoDTO>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}
