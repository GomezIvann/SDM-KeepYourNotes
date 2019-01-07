package com.example.ivan.proyectosdm.CreacionNotas;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ivan.proyectosdm.DataBase.NotesDataSource;
import com.example.ivan.proyectosdm.MainActivity;
import com.example.ivan.proyectosdm.Notas.Imagen;
import com.example.ivan.proyectosdm.Notas.Nota;
import com.example.ivan.proyectosdm.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CrearNota extends AppCompatActivity {

    public static final String OBJETO_NOTA = "nota";

    private TextView mTextMessage;
    private FragmentTituloContenido fragment = new FragmentTituloContenido();
    private FragmentColor fragment2 = new FragmentColor();
    private FragmentAdjuntos fragment3 = new FragmentAdjuntos();
    private Nota notaAModificar;
    private NotesDataSource nds;
    private Nota notaActual; //nota que hay en el momento de girar la pantalla
    private int currentTab;
    private MenuItem menuItem;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menuItem = menu.findItem(R.id.Guardar);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.Guardar) {
            // dos casos, que sea una modificacion o que sea un guardado (nota nueva)
            nds.open();
            if (notaAModificar == null) {
                String titulo = fragment.getTitulo().getText().toString();
                String descripcion = fragment.getDescripcion().getText().toString();
                int color = fragment2.getColor();
                if (color == 0)
                    color = colorAleatorio();

                Nota nota = new Nota(titulo, descripcion, color);
                List<Imagen> imagenes = fragment3.getImagenes();
                nota.setImagenes(imagenes);
                nota.setContext(getApplicationContext());
                nds.createNote(nota); // creamos el objeto y lo añadimos a la bbdd
                finish();
            }
            else {
                notaAModificar.setTitulo(fragment.getTitulo().getText().toString());
                notaAModificar.setContenido(fragment.getDescripcion().getText().toString());
                notaAModificar.setColor(fragment2.getColor());
                notaAModificar.setContext(getApplicationContext());
                notaAModificar.setImagenes(fragment3.getImagenes());
                nds.updateNote(notaAModificar); //actualizamos el objeto en la bbdd
                Toast.makeText(getApplicationContext(),
                        "La nota ha sido modificada correctamente", Toast.LENGTH_SHORT).show();
            }
            nds.close();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private int colorAleatorio() {
        Random r = new Random();
        int i = r.nextInt(7);
        switch (i) {
            case 0:
                return ContextCompat.getColor(this, R.color.amarillo);
            case 1:
                return ContextCompat.getColor(this, R.color.azul);
            case 2:
                return ContextCompat.getColor(this, R.color.verdeClaro);
            case 3:
                return ContextCompat.getColor(this, R.color.naranja);
            case 4:
                return ContextCompat.getColor(this, R.color.morado);
            case 5:
                return ContextCompat.getColor(this, R.color.rojo);
            case 6:
                return ContextCompat.getColor(this, R.color.blanco);
            default:
                return 0;
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            currentTab = item.getItemId();
            switch (item.getItemId()) {
                case R.id.navigation_nota:
                    crearFragmentNota();
                    menuItem.setVisible(true);
                    return true;
                case R.id.navigation_color:
                    crearFragmentColor();
                    menuItem.setVisible(true);
                    return true;
                case R.id.navigation_adjunto:
                    crearFragmentAdjuntos();
//                    menuItem.setVisible(false);
                    return true;
            }
            return false;
        }


    };

    private void crearFragmentAdjuntos() {
        setTitle("Archivos adjuntos");
        FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
        fragmentTransaction3.replace(R.id.frame, fragment3,"Adjunto" );
        fragmentTransaction3.commit();
    }

    private void crearFragmentColor() {
        setTitle("Color");
        FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
        fragmentTransaction2.replace(R.id.frame, fragment2,"Color" );
        fragmentTransaction2.commit();
    }

    private void crearFragmentNota() {
        setTitle("Nota");
        FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
        fragmentTransaction1.replace(R.id.frame, fragment,"Nota" );
        fragmentTransaction1.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_nota);

        nds = new NotesDataSource(getApplicationContext());

        Bundle b = getIntent().getExtras();

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if(b != null) {
            notaAModificar = (Nota) b.getSerializable(MainActivity.OBJETO_NOTA);
            fragment.setArguments(b);
            fragment2.setArguments(b);
            fragment3.setArguments(b);
        }
        crearFragmentNota();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String titulo = fragment.getTitulo().getText().toString();
        String descripcion = fragment.getDescripcion().getText().toString();
        int color = fragment2.getColor();
        notaActual = new Nota(titulo, descripcion, color);
        notaActual.setImagenes(fragment3.getImagenes());
        if (notaAModificar != null)
            notaActual.setId(notaAModificar.getId());
        outState.putSerializable(OBJETO_NOTA, notaActual);
        outState.putInt("CurrentTab", currentTab);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentTab = savedInstanceState.getInt("CurrentTab");
        switch (currentTab) {
            case R.id.navigation_nota:
                crearFragmentNota();
                break;
            case R.id.navigation_color:
                crearFragmentColor();
                break;
            case R.id.navigation_adjunto:
                crearFragmentAdjuntos();
                break;
        }
        notaActual = (Nota) savedInstanceState.getSerializable(OBJETO_NOTA);
        TextView mContent = (TextView) findViewById(R.id.descripcion);
        TextView mTitle = (TextView) findViewById(R.id.titulo);
        mContent.setText(notaActual.getContenido());
        mTitle.setText(notaActual.getTitulo());
        fragment2.setArguments(savedInstanceState);
        fragment3.setArguments(savedInstanceState);
    }
}
