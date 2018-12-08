package com.example.ivan.proyectosdm.Notas;

public class Nota<T> {

    private Long id;
    private String titulo;
    private String contenido;
    private String color;

    public Nota(String titulo, String contenido, String color) {
        this.titulo = titulo;
        this.contenido = contenido;
        this.color = color;
    }

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
}
