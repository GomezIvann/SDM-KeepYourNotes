package com.example.ivan.proyectosdm.CreacionNotas;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.ivan.proyectosdm.Notas.Nota;
import com.example.ivan.proyectosdm.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTituloContenido extends Fragment {
    private Nota nota;
    private EditText titulo;
    private EditText descripcion;
    public FragmentTituloContenido() {

    }

    public void setNota(Nota nota) {
        this.nota = nota;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_titulo_contenido, container, false);
        titulo = (EditText) v.findViewById(R.id.titulo);
        descripcion = (EditText) v.findViewById(R.id.descripcion);
        if(savedInstanceState == null){
            if(nota != null){
                this.titulo.setText(nota.getTitulo());
                this.descripcion.setText(nota.getContenido());
            }else{
                titulo.setText("");
                descripcion.setText("");
            }
        }else{
            titulo.setText(savedInstanceState.getString("t"));
            descripcion.setText(savedInstanceState.getString("d"));
        }
        return v;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("t",titulo.getText().toString());
        outState.putString("d",descripcion.getText().toString());
    }

    public EditText getTitulo() {
        return titulo;
    }

    public EditText getDescripcion() {
        return descripcion;
    }
}
