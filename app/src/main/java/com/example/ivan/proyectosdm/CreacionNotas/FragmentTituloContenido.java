package com.example.ivan.proyectosdm.CreacionNotas;


import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ivan.proyectosdm.MainActivity;
import com.example.ivan.proyectosdm.Notas.Nota;
import com.example.ivan.proyectosdm.R;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTituloContenido extends Fragment implements TextToSpeech.OnInitListener {
    private Nota nota;
    private EditText titulo;
    private EditText descripcion;
    private FloatingActionButton btnHablar;

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
        btnHablar = (FloatingActionButton) v.findViewById(R.id.btnHablar);

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

        textoPorVoz();
        return v;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("t",titulo.getText().toString());
        outState.putString("d",descripcion.getText().toString());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
           nota = (Nota) getArguments().getSerializable(MainActivity.OBJETO_NOTA);
        }
    }



    public void textoPorVoz() {

        btnHablar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Ejemplo");
                startActivityForResult(intent, 0);
            }
        });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK){
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            // añade el texto en el campo de texto sobre el que está el cursor del usuario
            if ( descripcion.hasFocus() ){
                if ( descripcion.getText().toString().equals("") )
                    descripcion.setText(matches.get(0));
                else
                    descripcion.setText(descripcion.getText()+" "+matches.get(0));
            }
            else {
                if (titulo.getText().toString().equals(""))
                    titulo.setText(matches.get(0));
                else
                    titulo.setText(titulo.getText() + " " + matches.get(0));
            }
        }
    }

    @Override
    public void onInit(int status) { }

    public EditText getTitulo() {
        return titulo;
    }

    public EditText getDescripcion() {
        return descripcion;
    }
}
