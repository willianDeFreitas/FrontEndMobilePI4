package com.example.cruddeusuarios.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cruddeusuarios.R;
import com.example.cruddeusuarios.dto.DtoUser;
import com.example.cruddeusuarios.services.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadastroDeUsuarioActivity extends AppCompatActivity {

    public static final String TAG = "CadastroDeUsuarioActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_de_usuario);
    }

    public void cadastrar(View view) {
        String nome = ((EditText)findViewById(R.id.et_cadastro_usuario_nome)).getText().toString();
        String telefone = ((EditText)findViewById(R.id.et_cadastro_usuario_telefone)).getText().toString();
        String email = ((EditText)findViewById(R.id.et_cadastro_usuario_email)).getText().toString();
        String senha = ((EditText)findViewById(R.id.et_cadastro_usuario_password)).getText().toString();

        DtoUser dtoUser =  new DtoUser(email, nome, senha, telefone);

        RetrofitService.getServico().cadastraUsuario(dtoUser).enqueue(new Callback<DtoUser>() {
            @Override
            public void onResponse(Call<DtoUser> call, Response<DtoUser> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CadastroDeUsuarioActivity.this, "Usuário cadastrado com ID: " + response.body().getId(), Toast.LENGTH_LONG).show();
                    startActivity(new Intent(CadastroDeUsuarioActivity.this, MainActivity.class));
                } else {
                    Toast.makeText(CadastroDeUsuarioActivity.this, "Problemas ao cadastratrar usuário ", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<DtoUser> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}
