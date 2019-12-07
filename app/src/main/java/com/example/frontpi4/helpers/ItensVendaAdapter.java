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
import com.example.frontpi4.activities.CadastroPedidoDeVendaActivity;
import com.example.frontpi4.dto.ItemVendaDTO;
import com.example.frontpi4.dto.ProdutoDTO;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;




public class ItensVendaAdapter extends RecyclerView.Adapter<ItensVendaAdapter.ItensVendaHolder> {

    private LayoutInflater mInflater;
    private Context context;
    private List<ItemVendaDTO> lista;
    private Integer recentlyDeletedItemPosition;
    private ItemVendaDTO recentlyDeletedItem;

    Singleton singleton = Singleton.getInstance();


    public List<ItemVendaDTO> getLista() {
        return Collections.unmodifiableList(lista);
    }

    public Context getContext() {
        return context;
    }

    public ItensVendaAdapter(Context context, List<ItemVendaDTO> lista) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.lista = lista;

    }

    @NonNull
    @Override
    public ItensVendaHolder onCreateViewHolder(@NonNull ViewGroup parent, int ViewType) {
        View mItemView = mInflater.inflate(R.layout.recyclerview_layout_item_todos_itensvenda, parent, false);
        return new ItensVendaHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ItensVendaHolder holder, int position) {


        int produtoId =Integer.parseInt(lista.get(position).getProdutoId().toString());
        double qtdItemV = lista.get(position).getQtdItemV();
        double valorIvendaV = lista.get(position).getValorItemV();
        String produtoNome ="";

        if (position % 2 == 0) {
            Drawable d = context.getResources().getDrawable(R.drawable.lista);
            holder.produto.setBackground(d);
            holder.qtd.setBackground(d);
            holder.valor.setBackground(d);
        }

        List<ProdutoDTO> listaDeProdutos = ((CadastroPedidoDeVendaActivity) getContext()).getListaDeProdutos();

        for (ProdutoDTO idProduto:listaDeProdutos) {
            if(idProduto.getId() == produtoId){
                 produtoNome = idProduto.getNome();
            }
        }

        holder.produto.setText( produtoNome);
        holder.qtd.setText(" " + qtdItemV + "m³");
        holder.valor.setText("R$ " + valorIvendaV);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ItensVendaHolder extends RecyclerView.ViewHolder {
        final ItensVendaAdapter itensVendaAdapter;
        public final TextView produto;
        public final TextView qtd;
        public final TextView valor;

        public ItensVendaHolder(@NonNull View itemView, ItensVendaAdapter itensVendaAdapter) {
            super(itemView);

            this.itensVendaAdapter = itensVendaAdapter;
            produto = itemView.findViewById(R.id.tv_recyclerview_itemvenda_produto);
            qtd = itemView.findViewById(R.id.tv_recyclerview_itemvenda_qtd);
            valor = itemView.findViewById(R.id.tv_recyclerview_itemvenda_valor);

        }


    }


    public void deleteTask(int position) {
        List<ItemVendaDTO> listaPersistencia = new ArrayList<ItemVendaDTO>();

        recentlyDeletedItem = lista.get(position);
        recentlyDeletedItemPosition = position;
        lista.remove(position);
        notifyItemRemoved(position);
        showUndoDialog();

    }

    private void showUndoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Deseja apagar este item venda?");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Sim",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((CadastroPedidoDeVendaActivity) getContext()).calcularValorTotalVenda();
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
        Toast.makeText(context, "Item da venda foi apagado com sucesso", Toast.LENGTH_SHORT).show();
    }


    private void undoDelete() {
        lista.add(recentlyDeletedItemPosition,
                recentlyDeletedItem);
        notifyItemInserted(recentlyDeletedItemPosition);
    }
}