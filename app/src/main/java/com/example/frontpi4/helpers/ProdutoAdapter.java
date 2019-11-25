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

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.DrawableUtils;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontpi4.R;
import com.example.frontpi4.activities.AlteracaoProdutoActivity;
import com.example.frontpi4.activities.AlteracaoUsuarioActivity;
import com.example.frontpi4.activities.ExibirProdutosActivity;
import com.example.frontpi4.dto.ProdutoDTO;
import com.example.frontpi4.dto.UsuarioDTO;
import com.example.frontpi4.services.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProdutoAdapter extends RecyclerView.Adapter<ProdutoAdapter.ProdutoHolder> {
    //UsuarioAdapter.UsuarioHolder siginifica que será criada uma classe interna UsuarioHolder dentro de UsuarioAdapter
    private LayoutInflater mInflater;//objeto que "infla" o layout do item de lista do recyclerview
    private Context context;//activity que está exibindo o recyclerview
    private List<ProdutoDTO> lista;//fonte dos dados da lista a ser exibida

    private Integer recentlyDeletedItemPosition;
    private ProdutoDTO recentlyDeletedItem;

    public Context getContext() {
        return context;
    }

    public ProdutoAdapter(Context context, List<ProdutoDTO> lista) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public  ProdutoHolder onCreateViewHolder(@NonNull ViewGroup parent, int ViewType) {
        View mItemView = mInflater.inflate(R.layout.recyclerview_layout_item_todos_produtos, parent, false);
        return new ProdutoHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdutoHolder holder, int position) {
        String nome = lista.get(position).getNome();
        double p = lista.get(position).getPreco();
        String q =  lista.get(position).getVol();

        int preco = (int)p;

        if(position%2 == 0) {
            Drawable d = context.getResources().getDrawable(R.drawable.lista);
            holder.nome.setBackground(d);
            holder.qtd.setBackground(d);
            holder.preco.setBackground(d);
        }
        holder.nome.setText(nome);
        holder.qtd.setText(q);
        holder.preco.setText("R$ "+preco);

    }

    @Override
    public int getItemCount(){
        if(lista != null){
            return lista.size();
        } else {
            return 0;
        }
    }

    public class ProdutoHolder extends RecyclerView.ViewHolder {
        final ProdutoAdapter produtoAdapter;
        public final TextView nome;
        public final TextView preco;
        public final TextView qtd;

        public ProdutoHolder(@NonNull View itemView, ProdutoAdapter produtoAdapter) {
            super(itemView);

            this.produtoAdapter = produtoAdapter;
            nome = itemView.findViewById(R.id.tv_recyclerview_nome_produto);
            preco= itemView.findViewById(R.id.tv_recyclerview_preco_produto);
            qtd= itemView.findViewById(R.id.tv_recyclerview_qtd_produto);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    ProdutoDTO produto = lista.get(getLayoutPosition());
                    String nome = produto.getNome();
                    String id = produto.getId().toString();
                    int idInt = Integer.parseInt(id);
                    double preco = produto.getPreco();
                    double qtd = produto.getQtd();
                    String vol = produto.getVol();
                    Long categoriaId = produto.getCategoriaId();



                    Intent intent = new Intent(context, ExibirProdutosActivity.class);
                    intent.putExtra("id",idInt);
                    intent.putExtra("nome",nome);
                    intent.putExtra("preco",""+preco);
                    intent.putExtra("qtd",""+qtd);
                    intent.putExtra("vol",vol);
                    intent.putExtra("catId",""+ categoriaId);

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
        builder.setMessage("Deseja apagar este produto?");
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

        RetrofitService.getServico().deletaProduto(recentlyDeletedItem.getId(), "Bearer " + token).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Toast.makeText(context, "Produto foi apagado com sucesso", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Falha ao apagar o produto: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void undoDelete() {
        lista.add(recentlyDeletedItemPosition,
                recentlyDeletedItem);
        notifyItemInserted(recentlyDeletedItemPosition);
    }
}