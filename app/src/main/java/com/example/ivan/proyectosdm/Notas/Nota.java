package com.example.ivan.proyectosdm.Notas;

public class Nota<T> {
    private String Titulo;
    private String Contenido;
    private String color;

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getContenido() {
        return Contenido;
    }

    public void setContenido(String contenido) {
        Contenido = contenido;
    }

    public Nota(String titulo, String contenido, String color) {
        Titulo = titulo;
        Contenido = contenido;
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
