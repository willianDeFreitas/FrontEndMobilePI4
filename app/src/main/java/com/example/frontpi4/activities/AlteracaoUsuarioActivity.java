package com.example.frontpi4.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.frontpi4.R;
import com.example.frontpi4.dto.UsuarioDTO;
import com.example.frontpi4.services.RetrofitService;

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

        et_email = findViewById(R.id.et_altera_usuario_email);
        et_nome = findViewById(R.id.et_altera_usuario_nome);

        et_email.setText(email);
        et_nome.setText(nome);
    }

    public void alterar(View view) {
        String nome = ((EditText)findViewById(R.id.et_altera_usuario_nome)).getText().toString();
        String email = ((EditText)findViewById(R.id.et_altera_usuario_email)).getText().toString();
        String senha = ((EditText)findViewById(R.id.et_altera_usuario_password)).getText().toString();

        UsuarioDTO usuarioDTO;

        if ( senha.isEmpty()){
            usuarioDTO = new UsuarioDTO(email, nome);
        } else {
            usuarioDTO = new UsuarioDTO(email,nome,senha);
        }
        String token = getToken();

        RetrofitService.getServico().alteraUsuario(usuarioDTO, id, "Bearer "+token).enqueue(new Callback<UsuarioDTO>() {
            @Override
            public void onResponse(Call<UsuarioDTO> call, Response<UsuarioDTO> response) {
                if(response.code() == 200){
                    Toast.makeText(AlteracaoUsuarioActivity.this, "Usuário alterado com sucesso", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(AlteracaoUsuarioActivity.this, ListaUsuariosActivity.class));
                    onFailure(call, new Exception());
                } else {
                    Toast.makeText(AlteracaoUsuarioActivity.this, "Erro: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UsuarioDTO> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private String getToken() {
        SharedPreferences sp = getSharedPreferences("dados",0);
        return sp.getString("token",null);
    }
}
