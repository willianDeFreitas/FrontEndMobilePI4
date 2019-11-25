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
import com.example.frontpi4.dto.ItemVendaDTO;
import com.example.frontpi4.services.RetrofitService;

import java.math.BigDecimal;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfereSaidaActivity extends AppCompatActivity {

    public static final String TAG = "ConfereSaidaActivity";

    EditText et_idProd, et_idItemVenda, et_idVenda;
    int id;
    String qtdVendida;
    BigDecimal qtdVendidaD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confere_saida);

        Intent intent = getIntent();

        id = intent.getIntExtra("id", -1);
        qtdVendida = intent.getStringExtra("qtd");
        qtdVendidaD = BigDecimal.valueOf(Double.parseDouble(qtdVendida));

        Long idProd = intent.getLongExtra("idVenda", -1);
        Long idVenda = intent.getLongExtra("idProd",-1);

        et_idProd = findViewById(R.id.et_confere_saida_idproduto);
        et_idVenda = findViewById(R.id.et_confere_saida_idvenda);

        et_idProd.setText(idProd.toString());
        et_idVenda.setText(idVenda.toString());
    }

    public void conferirVenda(View view) {
        String conferido = "S";
        String qtdItemVendaStr = ((EditText)findViewById(R.id.et_confere_saida_qtd)).getText().toString();
        String idVenda = ((EditText)findViewById(R.id.et_confere_saida_idvenda)).getText().toString();
        String idProd = ((EditText)findViewById(R.id.et_confere_saida_idproduto)).getText().toString();

        BigDecimal qtdItemV = BigDecimal.valueOf(Double.parseDouble(qtdItemVendaStr));
        Long idV = Long.parseLong(idVenda);
        Long idP = Long.parseLong(idProd);

        ItemVendaDTO itemVendaDTO;

        if (qtdVendidaD.compareTo(qtdItemV) != 0.0){
            Toast.makeText(ConfereSaidaActivity.this, "Quantidade incorreta do produto", Toast.LENGTH_LONG).show();
            return;
        }

        itemVendaDTO = new ItemVendaDTO(idP,idV, conferido);
        String token = getToken();

        RetrofitService.getServico().confereItemVenda(itemVendaDTO, id, "Bearer "+token).enqueue(new Callback<ItemVendaDTO>() {
            @Override
            public void onResponse(Call<ItemVendaDTO> call, Response<ItemVendaDTO> response) {
                if(response.code() == 200){
                    Toast.makeText(ConfereSaidaActivity.this, "Conferido com sucesso", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ConfereSaidaActivity.this, ConferenciaDeSaidaActivity.class));
                    onFailure(call, new Exception());
                } else {
                    Toast.makeText(ConfereSaidaActivity.this, "Erro: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ItemVendaDTO> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private String getToken() {
        SharedPreferences sp = getSharedPreferences("dados",0);
        return sp.getString("token",null);
    }
}
