package com.example.frontpi4.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontpi4.R;
import com.example.frontpi4.dto.ItemCompraDTO;
import com.example.frontpi4.dto.ItemVendaDTO;

import java.util.List;

public class ItensCompraAdapter extends RecyclerView.Adapter<ItensCompraAdapter.ItensCompraHolder> {

    private LayoutInflater mInflater;
    private Context context;
    private List<ItemCompraDTO> lista;
    Singleton singleton = Singleton.getInstance();
    private Integer recentlyDeletedItemPosition;
    private ItemCompraDTO recentlyDeletedItem;

    public Context getContext() {
        return context;
    }

    public ItensCompraAdapter(Context context, List<ItemCompraDTO> lista) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public  ItensCompraHolder onCreateViewHolder(@NonNull ViewGroup parent, int ViewType) {
        View mItemView = mInflater.inflate(R.layout.recyclerview_layout_item_todos_itenscompra, parent, false);
        return new ItensCompraHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ItensCompraHolder holder, int position) {
        int produtoId =Integer.parseInt(lista.get(position).getProdutoId().toString());
        double qtdItemV = lista.get(position).getQtdItemC();
        double valorIvendaV = lista.get(position).getValorItemC();
        String produto = "";
        if(position%2 == 0) {
            Drawable d = context.getResources().getDrawable(R.drawable.lista);
            holder.produto.setBackground(d);
            holder.qtd.setBackground(d);
            holder.valor.setBackground(d);
        }

        switch (produtoId){
            case 1: produto= "Areia media";
                    break;
            case 2: produto = "Areia fina";
                break;
            case 3: produto = "Brita 1";
                break;
            case 4: produto = "Brita 0";
                break;
        }

        holder.produto.setText(produto);
        holder.qtd.setText(" "+qtdItemV+"m³");
        holder.valor.setText("R$ "+valorIvendaV);
    }

    @Override
    public int getItemCount(){
        if(lista != null){
            return lista.size();
        } else {
            return 0;
        }
    }

    public class ItensCompraHolder extends RecyclerView.ViewHolder {
        final ItensCompraAdapter itensVendaAdapter;
        public final TextView produto;
        public  final TextView qtd;
        public final TextView valor;

        public ItensCompraHolder(@NonNull View itemView, ItensCompraAdapter itensCompraAdapter) {
            super(itemView);

            this.itensVendaAdapter = itensCompraAdapter;
            produto = itemView.findViewById(R.id.tv_recyclerview_itemcompra_produto);
            qtd = itemView.findViewById(R.id.tv_recyclerview_itemcompra_qtd);
            valor = itemView.findViewById(R.id.tv_recyclerview_itemcompra_valor);

        }


    }

    public void deleteTask(int position) {
        recentlyDeletedItem = lista.get(position);
        recentlyDeletedItemPosition = position;
        lista.remove(position);
        singleton.setListaDeItemCompra(lista);
        notifyItemRemoved(position);
        showUndoDialog();
    }


    private void showUndoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Deseja apagar este item da compra?");
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

                    Toast.makeText(context, "Item da compra foi apagado com sucesso", Toast.LENGTH_SHORT).show();
                }


    private void undoDelete() {
        lista.add(recentlyDeletedItemPosition,
                recentlyDeletedItem);
        notifyItemInserted(recentlyDeletedItemPosition);
    }
}