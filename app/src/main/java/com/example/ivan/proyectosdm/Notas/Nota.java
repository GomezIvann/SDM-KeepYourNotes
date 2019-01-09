package com.example.ivan.proyectosdm.Notas;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Nota<T> implements Serializable {
    private Long id;
    private String titulo;
    private String contenido;
    private List<Imagen> imagenes;
    private int color;
    private Context context;
    private String coordenadas = "";

    public Nota(String titulo, String contenido, int color) {
        this.titulo = titulo;
        this.contenido = contenido;
        this.color = color;
        this.imagenes = new ArrayList<Imagen>();
    }

    public Nota(String titulo, String contenido, int color, long id) {
        this(titulo, contenido, color);
        this.id = id;
    }

    protected Nota(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        titulo = in.readString();
        contenido = in.readString();
        color = in.readInt();
        coordenadas = in.readString();
    }

    public void addImagen (Imagen img){
        imagenes.add(img);
    }

    public void remove (int img) {
        imagenes.get(img).borrarFoto();
    }

    public void setImagenes(List<Imagen> imagenes) {
        for (int i = 0;i<imagenes.size();i++){
            Imagen img = imagenes.get(i);
            int pos = containsIgm(img);
            if(pos != -1){
                if(img.isBorrado()){
                    this.imagenes.get(pos).borrarFoto();
                }
                imagenes.remove(img);
            }
        }
        this.imagenes.addAll(imagenes);
    }

    public void asociarIds(String nombre, long id){
        for (int i = 0; i < imagenes.size(); i++) {
            Imagen imagen = imagenes.get(i);
            if(imagen.getNombre().equals(nombre)){
                imagen.setId(id);
            }
        }

    }

    private int containsIgm(Imagen imagn){
        for (int i = 0; i < imagenes.size(); i++) {
            Imagen img = imagenes.get(i);
            if (img.getNombre().equals(imagn.getNombre())) {
                return i;
            }
        }
        return -1;
    }

    public String getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
    }

    public Imagen getImagen(int index) { return imagenes.get(index); }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public List<Imagen> getImagenes() {
        return imagenes;
    }

    public void setImagener(List<Imagen> imagenes){
        this.imagenes = imagenes;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

//        @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(this.titulo);
//        dest.writeString(this.contenido);
//        dest.writeInt(this.color);
//    }
//
//    public static final Creator<Nota> CREATOR = new Creator<Nota>() {
//        @Override
//        public Nota createFromParcel(Parcel in) {
//            return new Nota(in);
//        }
//
//        @Override
//        public Nota[] newArray(int size) {
//            return new Nota[size];
//        }
//    };
}
