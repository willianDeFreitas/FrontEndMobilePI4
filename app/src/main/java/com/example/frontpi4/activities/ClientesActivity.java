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
import android.widget.Toast;

import com.example.frontpi4.R;
import com.example.frontpi4.dto.ClienteDTO;
import com.example.frontpi4.dto.UsuarioDTO;
import com.example.frontpi4.helpers.ClienteAdapter;
import com.example.frontpi4.helpers.SwipeToDeleteCallback;
import com.example.frontpi4.helpers.SwipeToDeleteCallbackCliente;
import com.example.frontpi4.helpers.UsuarioAdapter;
import com.example.frontpi4.services.RetrofitService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class ClientesActivity extends AppCompatActivity {

    public static final String TAG = "ClientesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_clientes);
        buscaDados();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ClientesActivity.this, CadastroDeClienteActivity.class));
            }
        });
    }

    private void preencheRecyclerview(List<ClienteDTO> lista){
        RecyclerView mRecyclerView = findViewById(R.id.rv_todos_clientes);
        ClienteAdapter mAdapter = new ClienteAdapter(this, lista);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallbackCliente(mAdapter));
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }


    private void buscaDados(){
        //# Rercuperando token salvo na activity de login
        SharedPreferences sp = getSharedPreferences("dados", 0);
        String token = sp.getString("token",null);
        //#

        RetrofitService.getServico().buscaClientes("Bearer "+token).enqueue(new Callback<List<ClienteDTO>>() {
            @Override
            public void onResponse(Call<List<ClienteDTO>> call, Response<List<ClienteDTO>> response) {
                if (response.isSuccessful()) {
                    List<ClienteDTO> lista = response.body();
                    preencheRecyclerview(lista);
                } else {
                    startActivity(new Intent(ClientesActivity.this, LoginActivity.class));
                    onFailure(call, new Exception());
                }
            }

            @Override
            public void onFailure(Call<List<ClienteDTO>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

}
