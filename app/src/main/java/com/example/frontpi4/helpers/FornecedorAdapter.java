package com.example.frontpi4.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontpi4.R;
import com.example.frontpi4.activities.AlteracaoFornecedorActivity;
import com.example.frontpi4.activities.AlteracaoUsuarioActivity;
import com.example.frontpi4.dto.FornecedorDTO;
import com.example.frontpi4.dto.UsuarioDTO;
import com.example.frontpi4.services.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FornecedorAdapter extends RecyclerView.Adapter<FornecedorAdapter.FornecedorHolder> {
    //UsuarioAdapter.UsuarioHolder siginifica que será criada uma classe interna UsuarioHolder dentro de UsuarioAdapter
    private LayoutInflater mInflater;//objeto que "infla" o layout do item de lista do recyclerview
    private Context context;//activity que está exibindo o recyclerview
    private List<FornecedorDTO> lista;//fonte dos dados da lista a ser exibida

    private Integer recentlyDeletedItemPosition;
    private FornecedorDTO recentlyDeletedItem;

    public Context getContext() {
        return context;
    }

    public FornecedorAdapter(Context context, List<FornecedorDTO> lista) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public  FornecedorHolder onCreateViewHolder(@NonNull ViewGroup parent, int ViewType) {
        View mItemView = mInflater.inflate(R.layout.recyclerview_layout_item_todos_fornecedores, parent, false);
        return new FornecedorHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull FornecedorHolder holder, int position) {
        String nome = lista.get(position).getNome();
        String telefone = lista.get(position).gettelefone();
        if(position%2 == 0) {
            Drawable d = context.getResources().getDrawable(R.drawable.lista);
            holder.nome.setBackground(d);
            holder.telefone.setBackground(d);
        }
        holder.nome.setText(nome);
        holder.telefone.setText(telefone);
    }

    @Override
    public int getItemCount(){
        if(lista != null){
            return lista.size();
        } else {
            return 0;
        }
    }

    public class FornecedorHolder extends RecyclerView.ViewHolder {
        final FornecedorAdapter fornecedorAdapter;
        public final TextView nome;
        public final TextView telefone;

        public FornecedorHolder(@NonNull View itemView, FornecedorAdapter fornecedorAdapter) {
            super(itemView);

            this.fornecedorAdapter = fornecedorAdapter;
            nome = itemView.findViewById(R.id.tv_recyclerview_nome_fornecedor);
            telefone = itemView.findViewById(R.id.tv_recyclerview_telefone_fornecedor);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                   FornecedorDTO fornecedor = lista.get(getLayoutPosition());
                    String nome = fornecedor.getNome();
                    int id = fornecedor.getId();
                    String email = fornecedor.getEmail();
                    String cnpj= fornecedor.getcnpj();
                    String end = fornecedor.getEnd();
                    String telefone = fornecedor.gettelefone();

                    Intent intent = new Intent(context, AlteracaoFornecedorActivity.class);
                    intent.putExtra("id",id);
                    intent.putExtra("nome",nome);
                    intent.putExtra("email",email);
                    intent.putExtra("cnpj",cnpj);
                    intent.putExtra("end",end);
                    intent.putExtra("telefone",telefone);

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
        builder.setMessage("Deseja apagar este fornecedor?");
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

        RetrofitService.getServico().deletaFornecedor(recentlyDeletedItem.getId(), "Bearer " + token).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Toast.makeText(context, "Fornecedor foi apagado com sucesso", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Falha ao apagar o fornecedor: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void undoDelete() {
        lista.add(recentlyDeletedItemPosition,
                recentlyDeletedItem);
        notifyItemInserted(recentlyDeletedItemPosition);
    }
}