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
import com.example.frontpi4.activities.ConfereEntradaActivity;
import com.example.frontpi4.dto.ItemCompraDTO;

import java.util.List;

public class ConferenciaEntradaAdapter extends RecyclerView.Adapter<ConferenciaEntradaAdapter.ItemCompraHolder> {

    private LayoutInflater mInflater;//objeto que "infla" o layout do item de lista do recyclerview
    private Context context;//activity que está exibindo o recyclerview
    private List<ItemCompraDTO> lista;//fonte dos dados da lista a ser exibida

    public Context getContext() {
        return context;
    }

    public ConferenciaEntradaAdapter(Context context, List<ItemCompraDTO> lista) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public ItemCompraHolder onCreateViewHolder(@NonNull ViewGroup parent, int ViewType) {
        View mItemView = mInflater.inflate(R.layout.recyclerview_layout_item_conferencia_entrada, parent, false);
        return new ItemCompraHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ConferenciaEntradaAdapter.ItemCompraHolder holder, int position) {
        Long idItemCompra = lista.get(position).getId();
        Long idCompra = lista.get(position).getCompraId();
        Long idProd = lista.get(position).getProdutoId();
        String produto = lista.get(position).getProduto();
        String fornecedor = lista.get(position).getFornecedor();
        String idItemCompraStr = idItemCompra.toString();
        String idCompraStr = idCompra.toString();
        String idProdutoStr = idProd.toString();

        if(position%2 == 0) {
            Drawable d = context.getResources().getDrawable(R.drawable.lista);
            holder.idItemCompra.setBackground(d);
        }

        holder.idItemCompra.setText("Compra: " + idCompraStr + " Item de compra: " + idItemCompraStr +
                "\n Produto: " + idProdutoStr + "-" + produto +
                "\n Fornecedor: " + fornecedor);
    }

    @Override
    public int getItemCount(){
        if(lista != null){
            return lista.size();
        } else {
            return 0;
        }
    }

    public class ItemCompraHolder extends RecyclerView.ViewHolder {
        final ConferenciaEntradaAdapter conferenciaEntradaAdapter;
        public final TextView idItemCompra;

        public ItemCompraHolder(@NonNull View itemView, ConferenciaEntradaAdapter conferenciaEntradaAdapter) {
            super(itemView);

            this.conferenciaEntradaAdapter = conferenciaEntradaAdapter;
            idItemCompra = itemView.findViewById(R.id.tv_recyclerview_conferencia_entrada);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    ItemCompraDTO itemCompraDTO = lista.get(getLayoutPosition());

                    String id = itemCompraDTO.getId().toString();
                    int idInt = Integer.parseInt(id);

                    Long idProd = itemCompraDTO.getProdutoId();
                    Long idCompra = itemCompraDTO.getCompraId();

                    String qtdItemC = itemCompraDTO.getQtdItemC().toString();
                    String produto = itemCompraDTO.getProduto();
                    String fornecedor = itemCompraDTO.getFornecedor();

                    Intent intent = new Intent(context, ConfereEntradaActivity.class);

                    intent.putExtra("idItemCompra",idInt);
                    intent.putExtra("idProd",idProd);
                    intent.putExtra("idCompra",idCompra);
                    intent.putExtra("qtdNegociada", qtdItemC);
                    intent.putExtra("fornecedor", fornecedor);
                    intent.putExtra("produto", produto);

                    context.startActivity(intent);
                }
            });
        }
    }
}