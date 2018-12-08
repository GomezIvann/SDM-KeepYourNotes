package com.example.ivan.proyectosdm.Notas;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Nota<T> implements Serializable {
    private Long id;
    private String titulo;
    private String contenido;
    private String color;

    public Nota(String titulo, String contenido, String color) {
        this.titulo = titulo;
        this.contenido = contenido;
        this.color = color;
    }

    public Nota(String titulo, String contenido, String color, long id) {
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
        color = in.readString();
    }
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(this.titulo);
//        dest.writeString(this.contenido);
//        dest.writeString(this.color);
//    }
}
