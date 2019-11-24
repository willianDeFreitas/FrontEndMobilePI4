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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.frontpi4.R;
import com.example.frontpi4.dto.ClienteDTO;
import com.example.frontpi4.dto.UsuarioDTO;
import com.example.frontpi4.services.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadastroDeClienteActivity extends AppCompatActivity {

    public static final String TAG = "CadastroDeClienteActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_de_cliente);

    }

    public void cadastrarCliente(View view) {
        String nome = ((EditText) findViewById(R.id.et_cadastro_cliente_nome)).getText().toString();
        String email = ((EditText) findViewById(R.id.et_cadastro_cliente_email)).getText().toString();
        String cpf = ((EditText) findViewById(R.id.et_cadastro_cliente_cpf)).getText().toString();
        String end = ((EditText) findViewById(R.id.et_cadastro_cliente_end)).getText().toString();
        String telefone = ((EditText) findViewById(R.id.et_cadastro_cliente_telefone)).getText().toString();

        ClienteDTO clienteDTO = null;
        String token = "";

        if (nome != "" && email != "" && cpf != "" && end != "" && telefone != "") {

            clienteDTO = new ClienteDTO(nome, email, cpf, end, telefone);
            token = getToken();
        }

        RetrofitService.getServico().cadastraCliente(clienteDTO, "Bearer " + token).enqueue(new Callback<ClienteDTO>() {
            @Override
            public void onResponse(Call<ClienteDTO> call, Response<ClienteDTO> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CadastroDeClienteActivity.this, "Cliente cadastrado com ID: " + response.body().getId(), Toast.LENGTH_LONG).show();
                    startActivity(new Intent(CadastroDeClienteActivity.this, ClientesActivity.class));
                } else {
                    Toast.makeText(CadastroDeClienteActivity.this, "Problemas ao cadastratrar cliente ", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ClienteDTO> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private String getToken() {
        SharedPreferences sp = getSharedPreferences("dados", 0);
        return sp.getString("token", null);
    }
}