package com.example.frontpi4.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.frontpi4.R;
import com.example.frontpi4.dto.ItemVendaDTO;
import com.example.frontpi4.helpers.ConferenciaSaidaAdapter;
import com.example.frontpi4.services.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConferenciaDeSaidaActivity extends AppCompatActivity {

    public static final String TAG = "ConferenciaSaidaActivity";

    ProgressBar pbCarregando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conferencia_de_saida);

        pbCarregando = findViewById(R.id.pb_conferencia_de_saida_carregando);
        pbCarregando.setVisibility(View.INVISIBLE);

        buscaDados();
    }

    private void buscaDados(){
        pbCarregando.setVisibility(View.VISIBLE);
        //# Rercuperando token salvo na activity de login
        SharedPreferences sp = getSharedPreferences("dados", 0);
        String token = sp.getString("token",null);
        //#

        RetrofitService.getServico().buscaItensVendaNaoConferido("Bearer "+token).enqueue(new Callback<List<ItemVendaDTO>>() {
            @Override
            public void onResponse(Call<List<ItemVendaDTO>> call, Response<List<ItemVendaDTO>> response) {
                if (response.isSuccessful()) {
                    List<ItemVendaDTO> lista = response.body();

                    if (!lista.isEmpty()) {
                        preencheRecyclerview(lista);
                    } else {
                        Toast.makeText(ConferenciaDeSaidaActivity.this, "Não há conferências de saída", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(ConferenciaDeSaidaActivity.this, TelaPrincipalActivity.class));
                    }

                    pbCarregando.setVisibility(View.INVISIBLE);
                } else {
                    startActivity(new Intent(ConferenciaDeSaidaActivity.this, TelaPrincipalActivity.class));
                    onFailure(call, new Exception());
                    pbCarregando.setVisibility(View.INVISIBLE);
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