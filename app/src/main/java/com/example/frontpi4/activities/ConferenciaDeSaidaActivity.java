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
import com.example.frontpi4.dto.ItemVendaDTO;
import com.example.frontpi4.dto.UsuarioDTO;
import com.example.frontpi4.helpers.ConferenciaSaidaAdapter;
import com.example.frontpi4.helpers.SwipeToDeleteCallback;
import com.example.frontpi4.helpers.UsuarioAdapter;
import com.example.frontpi4.services.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConferenciaDeSaidaActivity extends AppCompatActivity {

    public static final String TAG = "ConferenciaSaidaActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conferencia_de_saida);
        buscaDados();
    }

    private void buscaDados(){
        //# Rercuperando token salvo na activity de login
        SharedPreferences sp = getSharedPreferences("dados", 0);
        String token = sp.getString("token",null);
        //#

        RetrofitService.getServico().buscaItensVendaNaoConferido("Bearer "+token).enqueue(new Callback<List<ItemVendaDTO>>() {
            @Override
            public void onResponse(Call<List<ItemVendaDTO>> call, Response<List<ItemVendaDTO>> response) {
                if (response.isSuccessful()) {
                    List<ItemVendaDTO> lista = response.body();
                    preencheRecyclerview(lista);
                } else {
                    startActivity(new Intent(ConferenciaDeSaidaActivity.this, TelaPrincipalActivity.class));
                    onFailure(call, new Exception());
                }
            }

            @Override
            public void onFailure(Call<List<ItemVendaDTO>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void preencheRecyclerview(List<ItemVendaDTO> lista){
        RecyclerView mRecyclerView = findViewById(R.id.rv_conferencia_saida);
        ConferenciaSaidaAdapter mAdapter = new ConferenciaSaidaAdapter(this, lista);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}