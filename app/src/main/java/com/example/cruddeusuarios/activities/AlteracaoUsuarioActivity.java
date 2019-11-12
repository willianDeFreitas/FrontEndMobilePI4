package com.example.cruddeusuarios.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;

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

public class AlteracaoUsuarioActivity extends AppCompatActivity {

    public static final String TAG = "AlteracaoUsuarioActivity";

    EditText et_nome, et_email, et_telefone;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alteracao_usuario);
        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);
        String nome = intent.getStringExtra("nome");
        String email = intent.getStringExtra("email");
        String telefone = intent.getStringExtra("tel");

        et_email = findViewById(R.id.et_altera_usuario_email);
        et_nome = findViewById(R.id.et_altera_usuario_nome);
        et_telefone = findViewById(R.id.et_altera_usuario_telefone);

        et_email.setText(email);
        et_nome.setText(nome);
        et_telefone.setText(telefone);
    }

    public void alterar(View view) {
        String nome = ((EditText)findViewById(R.id.et_altera_usuario_nome)).getText().toString();
        String telefone = ((EditText)findViewById(R.id.et_altera_usuario_telefone)).getText().toString();
        String email = ((EditText)findViewById(R.id.et_altera_usuario_email)).getText().toString();
        String senha = ((EditText)findViewById(R.id.et_altera_usuario_password)).getText().toString();

        DtoUser dtoUser;

        if ( senha.isEmpty()){
            dtoUser = new DtoUser(email, nome, telefone);
        } else {
            dtoUser = new DtoUser(email,nome,senha, telefone);
        }
        String token = getToken();

        RetrofitService.getServico().alteraUsuario(dtoUser, id, "Bearer "+token).enqueue(new Callback<DtoUser>() {
            @Override
            public void onResponse(Call<DtoUser> call, Response<DtoUser> response) {
                if(response.code() == 200){
                    Toast.makeText(AlteracaoUsuarioActivity.this, "Usuário alterado com sucesso", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(AlteracaoUsuarioActivity.this, ListaUsuariosActivity.class));
                    onFailure(call, new Exception());
                } else {
                    Toast.makeText(AlteracaoUsuarioActivity.this, "Erro: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<DtoUser> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private String getToken() {
        SharedPreferences sp = getSharedPreferences("dados",0);
        return sp.getString("token",null);
    }
}
