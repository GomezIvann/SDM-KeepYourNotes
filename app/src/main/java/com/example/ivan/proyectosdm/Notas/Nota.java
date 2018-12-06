package com.example.ivan.proyectosdm.Notas;

public class Nota<T> {
    private String Titulo;
    private T Contenido;

    public Nota(String titulo, T contenido) {
        Titulo = titulo;
        Contenido = contenido;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public T getContenido() {
        return Contenido;
    }

    public void setContenido(T contenido) {
        Contenido = contenido;
    }
}
