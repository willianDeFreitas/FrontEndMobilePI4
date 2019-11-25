package com.example.frontpi4.helpers;

import android.content.Context;
import android.content.Intent;
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

    private Integer recentlyDeletedItemPosition;
    private ItemVendaDTO recentlyDeletedItem;

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
        String idItemVendaStr = idItemVenda.toString();
        String idVendaStr = idVenda.toString();
        String idProdutoStr = idProd.toString();

        holder.idItemVenda.setText("Item de Venda: " + idItemVendaStr +
                "\n Venda: " + idVendaStr +
                "\n Produto: " + idProdutoStr);
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

                    Intent intent = new Intent(context, ConfereSaidaActivity.class);
                    intent.putExtra("id",idInt);
                    intent.putExtra("idProd",idProd);
                    intent.putExtra("idVenda",idVenda);
                    intent.putExtra("qtd", qtdItemV);

                    context.startActivity(intent);
                }
            });
        }


    }

//    public void deleteTask(int position) {
//        recentlyDeletedItem = lista.get(position);
//        recentlyDeletedItemPosition = position;
//        lista.remove(position);
//        notifyItemRemoved(position);
//        showUndoDialog();
//    }
//
//    private void showUndoDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setMessage("Deseja apagar este usuário?");
//        builder.setCancelable(true);
//
//        builder.setPositiveButton(
//                "Sim",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        callAPIServiceDelete();
//                    }
//                });
//
//        builder.setNegativeButton(
//                "Não",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        undoDelete();
//                        dialog.cancel();
//                    }
//                });
//
//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();
//    }

//    private void callAPIServiceDelete() {
//        SharedPreferences sp = context.getSharedPreferences("dados", 0);
//        String token = sp.getString("token", null);
//
//        RetrofitService.getServico().deletaUsuario(recentlyDeletedItem.getId(), "Bearer " + token).enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if(response.isSuccessful()){
//                    Toast.makeText(context, "Usuário foi apagado com sucesso", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Toast.makeText(context, "Falha ao apagar o usuário: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

//    private void undoDelete() {
//        lista.add(recentlyDeletedItemPosition,
//                recentlyDeletedItem);
//        notifyItemInserted(recentlyDeletedItemPosition);
//    }
}