package com.example.ivan.proyectosdm.Notas;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ivan.proyectosdm.R;

import java.util.ArrayList;

public class NotaAdapter extends Adapter<NotaAdapter.NotaViewHolder> {

    private ArrayList<Nota> notas;

    public NotaAdapter(ArrayList<Nota> notas) {
        this.notas = notas;
    }

    @Override
    public NotaViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        return new NotaViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.item_nota, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(NotaViewHolder notaViewHolder, int i) {
        Nota nota = notas.get(i);
        notaViewHolder.mTxTitulo.setText(nota.getTitulo());
        notaViewHolder.mTxContenido.setText(nota.getContenido().toString());
    }

    @Override
    public int getItemCount() {
        return notas.size();
    }

    class NotaViewHolder extends RecyclerView.ViewHolder{

        TextView mTxTitulo;
        TextView mTxContenido;

        public NotaViewHolder(View itemView) {
            super(itemView);
            mTxTitulo = (TextView) itemView.findViewById(R.id.txTitle);
            mTxContenido = (TextView) itemView.findViewById(R.id.txContent);
        }
    }


}
