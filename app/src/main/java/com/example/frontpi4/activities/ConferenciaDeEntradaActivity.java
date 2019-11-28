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
import com.example.frontpi4.dto.ItemCompraDTO;
import com.example.frontpi4.dto.ItemVendaDTO;
import com.example.frontpi4.helpers.ConferenciaEntradaAdapter;
import com.example.frontpi4.helpers.ConferenciaSaidaAdapter;
import com.example.frontpi4.services.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConferenciaDeEntradaActivity extends AppCompatActivity {

    public static final String TAG = "ConferenciaEntradaActivity";

    ProgressBar pbCarregando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conferencia_de_entrada);

        pbCarregando = findViewById(R.id.pb_conferencia_de_saida_carregando);
        pbCarregando.setVisibility(View.INVISIBLE);

        buscaDados();
    }

    private void buscaDados() {
        pbCarregando.setVisibility(View.VISIBLE);
        //# Rercuperando token salvo na activity de login
        SharedPreferences sp = getSharedPreferences("dados", 0);
        String token = sp.getString("token",null);
        //#

        RetrofitService.getServico().buscaItensCompraNaoConferido("Bearer "+token).enqueue(new Callback<List<ItemCompraDTO>>() {
            @Override
            public void onResponse(Call<List<ItemCompraDTO>> call, Response<List<ItemCompraDTO>> response) {
                if (response.isSuccessful()) {
                    List<ItemCompraDTO> lista = response.body();

                    if (!lista.isEmpty()) {
                        preencheRecyclerview(lista);
                    } else {
                        Toast.makeText(ConferenciaDeEntradaActivity.this, "Não há conferências de entrada", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(ConferenciaDeEntradaActivity.this, TelaPrincipalActivity.class));
                    }

                    pbCarregando.setVisibility(View.INVISIBLE);
                } else {
                    startActivity(new Intent(ConferenciaDeEntradaActivity.this, TelaPrincipalActivity.class));
                    onFailure(call, new Exception());
                    pbCarregando.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<ItemCompraDTO>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void preencheRecyclerview(List<ItemCompraDTO> lista) {
        RecyclerView mRecyclerView = findViewById(R.id.rv_conferencia_entrada);
        ConferenciaEntradaAdapter mAdapter = new ConferenciaEntradaAdapter(this, lista);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
