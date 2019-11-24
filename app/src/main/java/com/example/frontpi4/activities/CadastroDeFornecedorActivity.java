package com.example.frontpi4.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.frontpi4.R;
import com.example.frontpi4.dto.ClienteDTO;
import com.example.frontpi4.dto.FornecedorDTO;
import com.example.frontpi4.services.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadastroDeFornecedorActivity extends AppCompatActivity {

    public static final String TAG = "CadastroDeFornecedorActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_de_fornecedor);

    }

    public void cadastrarFornecedor(View view) {
        String nome = ((EditText) findViewById(R.id.et_cadastro_fornecedor_nome)).getText().toString();
        String email = ((EditText) findViewById(R.id.et_cadastro_fornecedor_email)).getText().toString();
        String cnpj = ((EditText) findViewById(R.id.et_cadastro_fornecedor_cnpj)).getText().toString();
        String end = ((EditText) findViewById(R.id.et_cadastro_fornecedor_end)).getText().toString();
        String telefone = ((EditText) findViewById(R.id.et_cadastro_fornecedor_telefone)).getText().toString();

        FornecedorDTO fornecedorDTO = null;
        String token = "";

        if (nome != "" && email != "" && cnpj != "" && end != "" && telefone != "") {

            fornecedorDTO = new FornecedorDTO(nome,cnpj,end,email,telefone);
            token = getToken();
        }

        RetrofitService.getServico().cadastraFornecedor(fornecedorDTO, "Bearer " + token).enqueue(new Callback<FornecedorDTO>() {
            @Override
            public void onResponse(Call<FornecedorDTO> call, Response<FornecedorDTO> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CadastroDeFornecedorActivity.this, "Fornecedor cadastrado com ID: " + response.body().getId(), Toast.LENGTH_LONG).show();
                    startActivity(new Intent(CadastroDeFornecedorActivity.this, FornecedoresActivity.class));
                } else {
                    Toast.makeText(CadastroDeFornecedorActivity.this, "Problemas ao cadastratrar fornecedor ", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<FornecedorDTO> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private String getToken() {
        SharedPreferences sp = getSharedPreferences("dados", 0);
        return sp.getString("token", null);
    }
}