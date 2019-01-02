package com.example.ivan.proyectosdm.CreacionNotas;

import android.content.DialogInterface;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ivan.proyectosdm.DataBase.NoteDataSource;
import com.example.ivan.proyectosdm.MainActivity;
import com.example.ivan.proyectosdm.Notas.Nota;
import com.example.ivan.proyectosdm.R;

public class CrearNota extends AppCompatActivity {

    public static final String OBJETO_NOTA = "nota";

    private TextView mTextMessage;
    private FragmentTituloContenido fragment = new FragmentTituloContenido();
    private FragmentColor fragment2 = new FragmentColor();
    private FragmentAdjuntos fragment3 = new FragmentAdjuntos();
    private Nota notaAModificar;
    private NoteDataSource nds;
    private Nota notaActual; //nota que hay en el momento de girar la pantalla
    private int currentTab;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.Guardar) {
            // dos casos, que sea una modificacion o que sea un guardado
            nds.open();
            if (notaAModificar == null) {
                String titulo = fragment.getTitulo().getText().toString();
                String descripcion = fragment.getDescripcion().getText().toString();
                int color = fragment2.getColor();
                Nota nota = new Nota(titulo, descripcion, color);
                nds.createNote(nota); // creamos el objeto y lo añadimos a la bbdd
                Toast.makeText(getApplicationContext(),
                        "La nota se ha guardado correctamente", Toast.LENGTH_SHORT).show();
                finish();
            }
            else {
                notaAModificar.setTitulo(fragment.getTitulo().getText().toString());
                notaAModificar.setContenido(fragment.getDescripcion().getText().toString());
                notaAModificar.setColor(fragment2.getColor());
                nds.updateNote(notaAModificar); //actualizamos el objeto en la bbdd
                Toast.makeText(getApplicationContext(),
                        "La nota ha sido modificada correctamente", Toast.LENGTH_SHORT).show();
            }
            nds.close();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            currentTab = item.getItemId();
            switch (item.getItemId()) {
                case R.id.navigation_nota:
                    setTitle("Nota");
                    FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction1.replace(R.id.frame, fragment,"Nota" );
                    fragmentTransaction1.commit();
                    return true;
                case R.id.navigation_color:
                    setTitle("Color");
                    FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction2.replace(R.id.frame, fragment2,"Color" );
                    fragmentTransaction2.commit();
                    return true;
                case R.id.navigation_adjunto:
                    setTitle("Archivos adjuntos");
                    FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction3.replace(R.id.frame, fragment3,"Adjunto" );
                    fragmentTransaction3.commit();
                    return true;
            }
            return false;
        }


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_nota);

        nds = new NoteDataSource(getApplicationContext());

        Bundle b = getIntent().getExtras();

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if(b != null) {
            notaAModificar = (Nota) b.getSerializable(MainActivity.OBJETO_NOTA);
            fragment.setArguments(b);
            fragment2.setArguments(b);
        }
        setTitle("Nota");
        FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
        fragmentTransaction1.replace(R.id.frame, fragment,"Nota" );
        fragmentTransaction1.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String titulo = fragment.getTitulo().getText().toString();
        String descripcion = fragment.getDescripcion().getText().toString();
        int color = fragment2.getColor();
        notaActual = new Nota(titulo, descripcion, color);
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
                setTitle("Nota");
                FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction1.replace(R.id.frame, fragment,"Nota" );
                fragmentTransaction1.commit();
                break;
            case R.id.navigation_color:
                setTitle("Color");
                FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction2.replace(R.id.frame, fragment2,"Color" );
                fragmentTransaction2.commit();
                break;
            case R.id.navigation_adjunto:
                setTitle("Archivos adjuntos");
                FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction3.replace(R.id.frame, fragment3,"Adjunto" );
                fragmentTransaction3.commit();
                break;
        }
        notaActual = (Nota) savedInstanceState.getSerializable(OBJETO_NOTA);
        TextView mContent = (TextView) findViewById(R.id.descripcion);
        TextView mTitle = (TextView) findViewById(R.id.titulo);
        mContent.setText(notaActual.getContenido());
        mTitle.setText(notaActual.getTitulo());
    }
}
