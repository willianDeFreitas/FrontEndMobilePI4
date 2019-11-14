package com.example.frontpi4.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.frontpi4.R;
import com.example.frontpi4.dto.UsuarioDTO;
import com.example.frontpi4.helpers.SwipeToDeleteCallback;
import com.example.frontpi4.helpers.UsuarioAdapter;
import com.example.frontpi4.services.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListaUsuariosActivity extends AppCompatActivity {

    public static final String TAG = "ListaUsuariosActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_usuarios);
        buscaDados();
    }

    private void preencheRecyclerview(List<UsuarioDTO> lista){
        RecyclerView mRecyclerView = findViewById(R.id.rv_todos_usuarios);
        UsuarioAdapter mAdapter = new UsuarioAdapter(this, lista);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(mAdapter));
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    private void buscaDados(){
        //# Rercuperando token salvo na activity de login
        SharedPreferences sp = getSharedPreferences("dados", 0);
        String token = sp.getString("token",null);
        //#

        RetrofitService.getServico().todosUsuarios("Bearer "+token).enqueue(new Callback<List<UsuarioDTO>>() {
            @Override
            public void onResponse(Call<List<UsuarioDTO>> call, Response<List<UsuarioDTO>> response) {
                if (response.isSuccessful()) {
                    List<UsuarioDTO> lista = response.body();
                    preencheRecyclerview(lista);
                } else {
                    startActivity(new Intent(ListaUsuariosActivity.this, LoginActivity.class));
                    onFailure(call, new Exception());
                }
            }

            @Override
            public void onFailure(Call<List<UsuarioDTO>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}