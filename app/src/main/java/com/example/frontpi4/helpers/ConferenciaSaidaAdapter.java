package com.example.frontpi4.helpers;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontpi4.R;
import com.example.frontpi4.activities.ConfereSaidaActivity;
import com.example.frontpi4.dto.ItemVendaDTO;

import java.util.List;

public class ConferenciaSaidaAdapter extends RecyclerView.Adapter<ConferenciaSaidaAdapter.ItemVendaHolder> {

    private LayoutInflater mInflater;//objeto que "infla" o layout do item de lista do recyclerview
    private Context context;//activity que está exibindo o recyclerview
    private List<ItemVendaDTO> lista;//fonte dos dados da lista a ser exibida

    public Context getContext() {
        return context;
    }

    public ConferenciaSaidaAdapter(Context context, List<ItemVendaDTO> lista) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public ItemVendaHolder onCreateViewHolder(@NonNull ViewGroup parent, int ViewType) {
        View mItemView = mInflater.inflate(R.layout.recyclerview_layout_item_conferencia_saida, parent, false);
        return new ItemVendaHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ConferenciaSaidaAdapter.ItemVendaHolder holder, int position) {
        Long idItemVenda = lista.get(position).getId();
        Long idVenda = lista.get(position).getVendaId();
        Long idProd = lista.get(position).getProdutoId();
        String produto = lista.get(position).getProduto();
        String cliente = lista.get(position).getCliente();
        String idItemVendaStr = idItemVenda.toString();
        String idVendaStr = idVenda.toString();
        String idProdutoStr = idProd.toString();

        if(position%2 == 0) {
            Drawable d = context.getResources().getDrawable(R.drawable.lista);
            holder.idItemVenda.setBackground(d);
        }

        holder.idItemVenda.setText("Venda: " + idVendaStr + " Item de Venda: " + idItemVendaStr +
                "\n Produto: " + idProdutoStr + "-" + produto +
                "\n Cliente: " + cliente);
    }

    @Override
    public int getItemCount(){
        if(lista != null){
            return lista.size();
        } else {
            return 0;
        }
    }

    public class ItemVendaHolder extends RecyclerView.ViewHolder {
        final ConferenciaSaidaAdapter conferenciaSaidaAdapter;
        public final TextView idItemVenda;

        public ItemVendaHolder(@NonNull View itemView, ConferenciaSaidaAdapter conferenciaSaidaAdapter) {
            super(itemView);

            this.conferenciaSaidaAdapter = conferenciaSaidaAdapter;
            idItemVenda = itemView.findViewById(R.id.tv_recyclerview_conferencia_saida);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    ItemVendaDTO itemVendaDTO = lista.get(getLayoutPosition());

                    String id = itemVendaDTO.getId().toString();
                    int idInt = Integer.parseInt(id);

                    Long idProd = itemVendaDTO.getProdutoId();
                    Long idVenda = itemVendaDTO.getVendaId();

                    String qtdItemV = itemVendaDTO.getQtdItemV().toString();
                    String produto = itemVendaDTO.getProduto();
                    String cliente = itemVendaDTO.getCliente();

                    Intent intent = new Intent(context, ConfereSaidaActivity.class);

                    intent.putExtra("idItemVenda",idInt);
                    intent.putExtra("idProd",idProd);
                    intent.putExtra("idVenda",idVenda);
                    intent.putExtra("qtdNegociada", qtdItemV);
                    intent.putExtra("cliente", cliente);
                    intent.putExtra("produto", produto);

                    context.startActivity(intent);
                }
            });
        }
    }
}