package com.example.ivan.proyectosdm.Notas;

import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ivan.proyectosdm.R;

import java.util.ArrayList;
import java.util.List;

public class NotaAdapter extends Adapter<NotaAdapter.NotaViewHolder> {

    private List<Nota> notas;
    private int posicion;

    public NotaAdapter(List<Nota> notas) {
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
        notaViewHolder.background.setBackgroundColor(nota.getColor());
        posicion = i;
    }

    @Override
    public int getItemCount() {
        return notas.size();
    }

    public int getPosicion() { return posicion; }

    class NotaViewHolder extends RecyclerView.ViewHolder{

        TextView mTxTitulo;
        TextView mTxContenido;
        ConstraintLayout background;

        public NotaViewHolder(View itemView) {
            super(itemView);
            mTxTitulo = (TextView) itemView.findViewById(R.id.txTitle);
            mTxContenido = (TextView) itemView.findViewById(R.id.txContent);
            background = (ConstraintLayout) itemView.findViewById(R.id.idItemLayout);
        }
    }


}
