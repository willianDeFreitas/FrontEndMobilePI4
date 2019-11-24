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
import com.example.frontpi4.dto.FornecedorDTO;
import com.example.frontpi4.dto.UsuarioDTO;
import com.example.frontpi4.services.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlteracaoFornecedorActivity extends AppCompatActivity {

    public static final String TAG = "AlteracaoFornecedorActivity";

    EditText et_nome, et_email, et_telefone, et_cnpj, et_end;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alteracao_fornecedor);

        Intent intent = getIntent();

        id = intent.getIntExtra("id", -1);
        String nome = intent.getStringExtra("nome");
        String email = intent.getStringExtra("email");
        String telefone = intent.getStringExtra("telefone");
        String  cnpj = intent.getStringExtra("cnpj");
        String end = intent.getStringExtra("end");

        et_nome = findViewById(R.id.et_altera_fornecedor_nome);
        et_email = findViewById(R.id.et_altera_fornecedor_email);
        et_cnpj = findViewById(R.id.et_altera_fornecedor_cnpj);
        et_end = findViewById(R.id.et_altera_fornecedor_end);
        et_telefone = findViewById(R.id.et_altera_fornecedor_telefone);

        et_email.setText(email);
        et_nome.setText(nome);
        et_cnpj.setText(cnpj);
        et_end.setText(end);
        et_telefone.setText(telefone);
    }

    public void alterarFornecedor(View view) {
        String nome = ((EditText)findViewById(R.id.et_altera_fornecedor_nome)).getText().toString();
        String email = ((EditText)findViewById(R.id.et_altera_fornecedor_email)).getText().toString();
        String cnpj = ((EditText)findViewById(R.id.et_altera_fornecedor_cnpj)).getText().toString();
        String end = ((EditText)findViewById(R.id.et_altera_fornecedor_end)).getText().toString();
        String telefone = ((EditText)findViewById(R.id.et_altera_fornecedor_telefone)).getText().toString();

        FornecedorDTO fornecedorDTO;


        fornecedorDTO = new FornecedorDTO(nome, cnpj,end, email,telefone);

        String token = getToken();

        RetrofitService.getServico().alteraFornecedor(fornecedorDTO, id, "Bearer "+token).enqueue(new Callback<FornecedorDTO>() {
            @Override
            public void onResponse(Call<FornecedorDTO> call, Response<FornecedorDTO> response) {
                if(response.code() == 200){
                    Toast.makeText(AlteracaoFornecedorActivity.this, "Fornecedor alterado com sucesso", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(AlteracaoFornecedorActivity.this, FornecedoresActivity.class));
                    onFailure(call, new Exception());
                } else {
                    Toast.makeText(AlteracaoFornecedorActivity.this, "Erro: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<FornecedorDTO> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private String getToken() {
        SharedPreferences sp = getSharedPreferences("dados",0);
        return sp.getString("token",null);
    }
}
