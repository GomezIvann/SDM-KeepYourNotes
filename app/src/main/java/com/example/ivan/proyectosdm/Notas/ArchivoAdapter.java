package com.example.ivan.proyectosdm.Notas;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ivan.proyectosdm.DataBase.Save;
import com.example.ivan.proyectosdm.R;

import java.io.File;
import java.util.List;

public class ArchivoAdapter extends Adapter<ArchivoAdapter.ArchivoViewHolder> {

    private List<Imagen> imagenes;
    private int posicion;

    public ArchivoAdapter(List<Imagen> imagenes) {
        this.imagenes = imagenes;
    }

    @Override
    public ArchivoViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        return new ArchivoViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.item_imagen, viewGroup, false));
    }




    @Override
    public void onBindViewHolder(ArchivoViewHolder notaViewHolder, int i) {
        Imagen imagen = imagenes.get(i);
        Save save = new Save();
        File dir = new File(save.getImagen());
        if (dir.exists()) {
            File file = new File(dir, imagen.getNombre());
            if (file.exists()) {
                String filePath = file.getPath();
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                notaViewHolder.image.setImageBitmap(bitmap);
            }
        }
        notaViewHolder.mTxTitulo.setText(imagen.getNombre());
        posicion = i;
    }

    @Override
    public int getItemCount() {
        return imagenes.size();
    }

    public int getPosicion() { return posicion; }

    class ArchivoViewHolder extends RecyclerView.ViewHolder{

        TextView mTxTitulo;
        ImageView image;

        public ArchivoViewHolder(View itemView) {
            super(itemView);
            mTxTitulo = (TextView) itemView.findViewById(R.id.txImageName);
            image = (ImageView) itemView.findViewById(R.id.imageView2);
        }
    }


}