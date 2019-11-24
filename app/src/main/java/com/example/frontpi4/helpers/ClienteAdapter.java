package com.example.frontpi4.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontpi4.R;
import com.example.frontpi4.activities.AlteracaoClienteActivity;
import com.example.frontpi4.activities.AlteracaoUsuarioActivity;
import com.example.frontpi4.dto.ClienteDTO;
import com.example.frontpi4.services.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.ClienteHolder> {

    private LayoutInflater mInflater;//objeto que "infla" o layout do item de lista do recyclerview
    private Context context;//activity que está exibindo o recyclerview
    private List<ClienteDTO> lista;//fonte dos dados da lista a ser exibida

    private Integer recentlyDeletedItemPosition;
    private ClienteDTO recentlyDeletedItem;

    public Context getContext() {
        return context;
    }

    public ClienteAdapter(Context context, List<ClienteDTO> lista) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public  ClienteHolder onCreateViewHolder(@NonNull ViewGroup parent, int ViewType) {
        View mItemView = mInflater.inflate(R.layout.recyclerview_layout_item_todos_clientes, parent, false);
        return new ClienteHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ClienteHolder holder, int position) {
        String nome = lista.get(position).getNome();
        holder.nome.setText(nome);
    }

    @Override
    public int getItemCount(){
        if(lista != null){
            return lista.size();
        } else {
            return 0;
        }
    }

    public class ClienteHolder extends RecyclerView.ViewHolder {
        final ClienteAdapter clienteAdapter;
        public final TextView nome;

        public ClienteHolder(@NonNull View itemView, ClienteAdapter clienteAdapter) {
            super(itemView);

            this.clienteAdapter = clienteAdapter;
            nome = itemView.findViewById(R.id.tv_recyclerview_nome_clientes);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    ClienteDTO clinte = lista.get(getLayoutPosition());
                    String nome = clinte.getNome();
                    int id = clinte.getId();
                    String email = clinte.getEmail();
                    String cpf= clinte.getCpf();
                    String end = clinte.getEnd();
                    String tel = clinte.getTel();

                    Intent intent = new Intent(context, AlteracaoClienteActivity.class);
                    intent.putExtra("id",id);
                    intent.putExtra("nome",nome);
                    intent.putExtra("email",email);
                    intent.putExtra("cpf",cpf);
                    intent.putExtra("end", end);
                    intent.putExtra("tel", tel);
                    context.startActivity(intent);
                }
            });
        }


    }

    public void deleteTask(int position) {
        recentlyDeletedItem = lista.get(position);
        recentlyDeletedItemPosition = position;
        lista.remove(position);
        notifyItemRemoved(position);
        showUndoDialog();
    }

    private void showUndoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Deseja apagar este cliente?");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Sim",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        callAPIServiceDelete();
                    }
                });

        builder.setNegativeButton(
                "Não",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        undoDelete();
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void callAPIServiceDelete() {
        SharedPreferences sp = context.getSharedPreferences("dados", 0);
        String token = sp.getString("token", null);

        RetrofitService.getServico().deletaCliente(recentlyDeletedItem.getId(), "Bearer " + token).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Toast.makeText(context, "Cliente foi apagado com sucesso", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Falha ao apagar o Cliente: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void undoDelete() {
        lista.add(recentlyDeletedItemPosition,
                recentlyDeletedItem);
        notifyItemInserted(recentlyDeletedItemPosition);
    }
}