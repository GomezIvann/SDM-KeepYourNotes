package com.example.ivan.proyectosdm.CreacionNotas;

import android.content.pm.ActivityInfo;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ivan.proyectosdm.DataBase.NotesDataSource;
import com.example.ivan.proyectosdm.DataBase.Save;
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
    private FragmentMapas fragment4 = new FragmentMapas();
    private Nota notaAModificar;
    private NotesDataSource nds;
    private Nota notaActual; //nota que hay en el momento de girar la pantalla
    private int currentTab;
    private MenuItem menuItem;
    private boolean saved;

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
                if(titulo.equals("")){
                    titulo = fragment4.getDireccion();
                }
                if (color == 0)
                    color = colorAleatorio();
                Nota nota = new Nota(titulo, descripcion, color);
                List<Imagen> imagenes = fragment3.getImagenes();
                nota.setImagenes(imagenes);
                nota.setContext(getApplicationContext());
                nota.setCoordenadas(fragment4.getCoordenada());
                nds.createNote(nota); // creamos el objeto y lo añadimos a la bbdd
                Toast.makeText(getApplicationContext(),
                        R.string.nuevaNota, Toast.LENGTH_SHORT).show();
                saved = true;
                finish();
            }
            else {
                String titulo1 = fragment.getTitulo().getText().toString();
                if(titulo1.equals("")){
                    titulo1 = fragment4.getDireccion();
                }
                notaAModificar.setTitulo(titulo1);
                notaAModificar.setContenido(fragment.getDescripcion().getText().toString());
                if (fragment2.getColor() != 0)
                    notaAModificar.setColor(fragment2.getColor());
                notaAModificar.setContext(getApplicationContext());
                notaAModificar.setImagenes(fragment3.getImagenes());
                notaAModificar.setCoordenadas(fragment4.getCoordenada());
                nds.updateNote(notaAModificar, getApplicationContext()); //actualizamos el objeto en la bbdd
                Toast.makeText(getApplicationContext(),
                        R.string.modificacionnota, Toast.LENGTH_SHORT).show();
                saved = true;
                finish();
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
                    return true;
                case R.id.navitacion_mapa:
                    crearFragmentMapa();
                    return true;
            }
            return false;
        }


    };

    private void crearFragmentAdjuntos() {
        setTitle(R.string.imagenes);
        FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
        fragmentTransaction3.replace(R.id.frame, fragment3,"Adjunto" );
        fragmentTransaction3.commit();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    private void crearFragmentColor() {
        setTitle(R.string.color);
        FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
        fragmentTransaction2.replace(R.id.frame, fragment2,"Color" );
        fragmentTransaction2.commit();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    private void crearFragmentNota() {
        setTitle(R.string.nota);
        FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
        fragmentTransaction1.replace(R.id.frame, fragment,"Nota" );
        fragmentTransaction1.commit();
    }

    private void crearFragmentMapa() {
        setTitle(R.string.mapa);
        FragmentTransaction fragmentTransaction4 = getSupportFragmentManager().beginTransaction();
        fragmentTransaction4.replace(R.id.frame, fragment4,"Mapa" );
        fragmentTransaction4.commit();

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
            fragment4.setArguments(b);
        }
        crearFragmentNota();
    }

    @Override
    public void onBackPressed() {
        Save save = new Save(getApplicationContext());
        if (!saved){
            for (int i = 0; i < fragment3.getNuevasImagenes().size(); i++)
                save.deleteImagen(fragment3.getNuevasImagenes().get(i));
        }
        super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String titulo = fragment.getTitulo().getText().toString();
        String descripcion = fragment.getDescripcion().getText().toString();
        int color = fragment2.getColor();
        notaActual = new Nota(titulo, descripcion, color);
        notaActual.setImagenes(fragment3.getImagenes());
        notaActual.setCoordenadas(fragment4.getCoordenada());
        if (notaAModificar != null) {
            notaActual.setId(notaAModificar.getId());
           // notaActual.setImagenes(fragment3.getNuevasImagenes());
        }
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
            case R.id.navitacion_mapa:
                crearFragmentMapa();
                break;
        }
        notaActual = (Nota) savedInstanceState.getSerializable(OBJETO_NOTA);
        TextView mContent = (TextView) findViewById(R.id.descripcion);
        TextView mTitle = (TextView) findViewById(R.id.titulo);
        mContent.setText(notaActual.getContenido());
        mTitle.setText(notaActual.getTitulo());
        fragment2.setArguments(savedInstanceState);
        fragment3.setArguments(savedInstanceState);
        fragment4.setArguments(savedInstanceState);
    }
}
