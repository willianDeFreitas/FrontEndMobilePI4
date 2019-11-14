package com.example.frontpi4.activities;

import androidx.appcompat.app.AppCompatActivity;

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

import com.example.frontpi4.R;
import com.example.frontpi4.dto.UsuarioDTO;
import com.example.frontpi4.services.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadastroDeUsuarioActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    public static final String TAG = "CadastroDeUsuarioActivity";
    Spinner funcao;
    int funcao_selecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_de_usuario);
        funcao = findViewById(R.id.sp_cadastro_usuario_fucao);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.funcao_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        funcao.setAdapter(adapter);
        funcao.setOnItemSelectedListener(this);
    }

    public void cadastrar(View view) {
        String nome = ((EditText)findViewById(R.id.et_cadastro_usuario_nome)).getText().toString();
        String email = ((EditText)findViewById(R.id.et_cadastro_usuario_email)).getText().toString();
        String senha = ((EditText)findViewById(R.id.et_cadastro_usuario_password)).getText().toString();
        //int teste = funcao_selecionada;

        UsuarioDTO usuarioDTO =  new UsuarioDTO(email, nome, senha);

        String token = getToken();

        RetrofitService.getServico().cadastraUsuario(usuarioDTO, "Bearer "+token).enqueue(new Callback<UsuarioDTO>() {
            @Override
            public void onResponse(Call<UsuarioDTO> call, Response<UsuarioDTO> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CadastroDeUsuarioActivity.this, "Usuário cadastrado com ID: " + response.body().getId(), Toast.LENGTH_LONG).show();
                    startActivity(new Intent(CadastroDeUsuarioActivity.this, MainActivity.class));
                } else {
                    Toast.makeText(CadastroDeUsuarioActivity.this, "Problemas ao cadastratrar usuário ", Toast.LENGTH_LONG).show();
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //funcao_selecionada = (int) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
