package com.example.ivan.proyectosdm.Notas;


import android.graphics.Bitmap;

import java.io.Serializable;

public class Imagen implements Serializable {
    private Long id;
    private Long nota_id;
    private String nombre;
    private transient Bitmap bitmap;
    private boolean borrado;

    public Imagen(Long id, Long nota_id, String nombre, Bitmap bitmap) {
        this.id = id;
        this.nota_id = nota_id;
        this.nombre = nombre;
        this.bitmap = bitmap;
        this.borrado = false;
    }
    public Imagen(String nombre, Bitmap bitmap) {
            this.nombre = nombre;
            this.bitmap = bitmap;
            this.borrado = false;

    }

    public Imagen(Long id, String nombre) {
            this.nombre = nombre;
            this.id = id;
    }

    public String getNombre(){
        return nombre;
    }

    public Long getNota_id() {
        return nota_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void borrarFoto(){
        borrado = true;
    }

    public boolean isBorrado(){
        return borrado;
    }
}
