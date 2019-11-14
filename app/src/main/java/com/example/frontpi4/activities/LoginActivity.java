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
import com.example.frontpi4.dto.LoginDTO;
import com.example.frontpi4.services.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void logar (View view){
        String email = ((EditText)findViewById(R.id.et_login_email)).getText().toString();
        String senha = ((EditText)findViewById(R.id.et_login_password)).getText().toString();

        LoginDTO dtoLogin = new LoginDTO();
        dtoLogin.setEmail(email);
        dtoLogin.setSenha(senha);

        RetrofitService.getServico().login(dtoLogin).enqueue(new Callback<LoginDTO>() {
            @Override
            public void onResponse(Call<LoginDTO> call, Response<LoginDTO> response) {
                if (response.isSuccessful()){
                    String token = response.body().getToken();
                    Toast.makeText(LoginActivity.this, "Usuário Logado", Toast.LENGTH_SHORT).show();

                    SharedPreferences sp = getSharedPreferences("dados", 0);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("token",token);
                    editor.apply();

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                } else {
                    Toast.makeText(LoginActivity.this, "Login Falhou", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, LoginActivity.class));
                    onFailure(call, new Exception());
                }

            }

            @Override
            public void onFailure(Call<LoginDTO> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());

            }
        });

    }
}