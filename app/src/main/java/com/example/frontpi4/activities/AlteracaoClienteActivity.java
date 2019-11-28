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
import com.example.frontpi4.services.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlteracaoClienteActivity extends AppCompatActivity {

    public static final String TAG = "AlteracaoClienteActivity";

    EditText et_nome, et_email, et_telefone, et_cpf, et_end;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alteracao_cliente);

        Intent intent = getIntent();

        id = intent.getIntExtra("id", -1);
        String nome = intent.getStringExtra("nome");
        String email = intent.getStringExtra("email");
        String telefone = intent.getStringExtra("tel");
        String  cpf = intent.getStringExtra("cpf");
        String end = intent.getStringExtra("end");

        et_email = findViewById(R.id.et_altera_cliente_email);
        et_nome = findViewById(R.id.et_altera_cliente_nome);
        et_cpf = findViewById(R.id.et_altera_cliente_cpf);
        et_end = findViewById(R.id.et_altera_cliente_end);
        et_telefone = findViewById(R.id.et_altera_cliente_telefone);

        et_email.setText(email);
        et_nome.setText(nome);
        et_cpf.setText(cpf);
        et_end.setText(end);
        et_telefone.setText(telefone);
    }

    public void alterarCliente(View view) {
        String nome = ((EditText)findViewById(R.id.et_altera_cliente_nome)).getText().toString();
        String email = ((EditText)findViewById(R.id.et_altera_cliente_email)).getText().toString();
        String cpf = ((EditText)findViewById(R.id.et_altera_cliente_cpf)).getText().toString();
        String end = ((EditText)findViewById(R.id.et_altera_cliente_end)).getText().toString();
        String telefone = ((EditText)findViewById(R.id.et_altera_cliente_telefone)).getText().toString();

        ClienteDTO clienteDTO = new ClienteDTO(nome,email, cpf, end, telefone);


        String token = getToken();

        RetrofitService.getServico().alteraCliente(clienteDTO, id, "Bearer "+token).enqueue(new Callback<ClienteDTO>() {
            @Override
            public void onResponse(Call<ClienteDTO> call, Response<ClienteDTO> response) {
                if(response.code() == 200){
                    Toast.makeText(AlteracaoClienteActivity.this, "Cliente alterado com sucesso", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(AlteracaoClienteActivity.this, ClientesActivity.class));
                    onFailure(call, new Exception());
                } else {
                    Toast.makeText(AlteracaoClienteActivity.this, "Erro: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ClienteDTO> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private String getToken() {
        SharedPreferences sp = getSharedPreferences("dados",0);
        return sp.getString("token",null);
    }
}
