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
import com.example.cruddeusuarios.dto.DtoLogin;
import com.example.cruddeusuarios.services.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

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

        DtoLogin dtoLogin = new DtoLogin();
        dtoLogin.setEmail(email);
        dtoLogin.setPassword(senha);

        RetrofitService.getServico().login(dtoLogin).enqueue(new Callback<DtoLogin>() {
            @Override
            public void onResponse(Call<DtoLogin> call, Response<DtoLogin> response) {
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
            public void onFailure(Call<DtoLogin> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());

            }
        });

    }
}