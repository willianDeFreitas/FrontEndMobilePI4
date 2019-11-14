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
import com.example.frontpi4.activities.AlteracaoUsuarioActivity;
import com.example.frontpi4.dto.UsuarioDTO;
import com.example.frontpi4.services.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.UsuarioHolder> {
    //UsuarioAdapter.UsuarioHolder siginifica que será criada uma classe interna UsuarioHolder dentro de UsuarioAdapter
    private LayoutInflater mInflater;//objeto que "infla" o layout do item de lista do recyclerview
    private Context context;//activity que está exibindo o recyclerview
    private List<UsuarioDTO> lista;//fonte dos dados da lista a ser exibida

    private Integer recentlyDeletedItemPosition;
    private UsuarioDTO recentlyDeletedItem;

    public Context getContext() {
        return context;
    }

    public UsuarioAdapter(Context context, List<UsuarioDTO> lista) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public  UsuarioHolder onCreateViewHolder(@NonNull ViewGroup parent, int ViewType) {
        View mItemView = mInflater.inflate(R.layout.recyclerview_layout_item_todos_usuarios, parent, false);
        return new UsuarioHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioHolder holder, int position) {
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

    public class UsuarioHolder extends RecyclerView.ViewHolder {
        final UsuarioAdapter usuarioAdapter;
        public final TextView nome;

        public UsuarioHolder(@NonNull View itemView, UsuarioAdapter usuarioAdapter) {
            super(itemView);

            this.usuarioAdapter = usuarioAdapter;
            nome = itemView.findViewById(R.id.tv_recyclerview_nome_usuario);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    UsuarioDTO user = lista.get(getLayoutPosition());
                    String nome = user.getNome();
                    int id = user.getId();
                    String email = user.getEmail();

                    Intent intent = new Intent(context, AlteracaoUsuarioActivity.class);
                    intent.putExtra("id",id);
                    intent.putExtra("nome",nome);
                    intent.putExtra("email",email);

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
        builder.setMessage("Deseja apagar este usuário?");
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

        RetrofitService.getServico().deletaUsuario(recentlyDeletedItem.getId(), "Bearer " + token).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Toast.makeText(context, "Usuário foi apagado com sucesso", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Falha ao apagar o usuário: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void undoDelete() {
        lista.add(recentlyDeletedItemPosition,
                recentlyDeletedItem);
        notifyItemInserted(recentlyDeletedItemPosition);
    }
}