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
import android.widget.ProgressBar;

import com.example.frontpi4.R;
import com.example.frontpi4.dto.FornecedorDTO;
import com.example.frontpi4.dto.UsuarioDTO;
import com.example.frontpi4.helpers.FornecedorAdapter;
import com.example.frontpi4.helpers.SwipeToDeleteCallback;
import com.example.frontpi4.helpers.SwipeToDeleteCallbackFornecedor;
import com.example.frontpi4.helpers.UsuarioAdapter;
import com.example.frontpi4.services.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FornecedoresActivity extends AppCompatActivity {

    public static final String TAG = "FornecedoresActivity";
    ProgressBar pbCarregando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_fornecedores);

        pbCarregando = findViewById(R.id.pb_liata_de_fornecedor_carregando);
        pbCarregando.setVisibility(View.VISIBLE);

        buscaDados();
    }

    private void preencheRecyclerview(List<FornecedorDTO> lista){
        RecyclerView mRecyclerView = findViewById(R.id.rv_todos_fornecedores);
        FornecedorAdapter mAdapter = new FornecedorAdapter(this, lista);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallbackFornecedor(mAdapter));
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    private void buscaDados(){
        //# Rercuperando token salvo na activity de login
        SharedPreferences sp = getSharedPreferences("dados", 0);
        String token = sp.getString("token",null);
        //#

        RetrofitService.getServico().buscaFornecedores("Bearer "+token).enqueue(new Callback<List<FornecedorDTO>>() {
            @Override
            public void onResponse(Call<List<FornecedorDTO>> call, Response<List<FornecedorDTO>> response) {
                if (response.isSuccessful()) {
                    List<FornecedorDTO> lista = response.body();
                    preencheRecyclerview(lista);
                    pbCarregando.setVisibility(View.INVISIBLE);
                } else {
                    startActivity(new Intent(FornecedoresActivity.this, LoginActivity.class));
                    onFailure(call, new Exception());
                }
            }

            @Override
            public void onFailure(Call<List<FornecedorDTO>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void adicionarFornecedor(View view) {
        startActivity(new Intent(this, CadastroDeFornecedorActivity.class));
    }
}
