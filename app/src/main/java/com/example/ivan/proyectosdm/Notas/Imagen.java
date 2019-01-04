package com.example.ivan.proyectosdm.Notas;


public class Imagen {

    private Long id;
    private Long nota_id;
    private String nombre;

    public Imagen(Long id, Long nota_id, String nombre) {
        this.id = id;
        this.nota_id = nota_id;
        this.nombre = nombre;
    }
    public Imagen(Long nota_id, String nombre) {
            this.nota_id = nota_id;
            this.nombre = nombre;
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
}
