package com.example.ivan.proyectosdm.CreacionNotas;

import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
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

    private TextView mTextMessage;
    private FragmentTituloContenido fragment = new FragmentTituloContenido();
    private FragmentColor fragment2 = new FragmentColor();
    private FragmentAdjuntos fragment3 = new FragmentAdjuntos();
    private Nota notaAModificar;
    private NoteDataSource nds;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        nds.open();
        if (id == R.id.cancelar) {
            //sacar mensaje diciendo que si quiere salir sin guardar los cambios
            nds.close();
            return true;
        }
        else if (id == R.id.Guardar) {
            // dos casos, que sea una modificacion o que sea un guardado
            if (notaAModificar == null) {
                String titulo = fragment.getTitulo().getText().toString();
                String descripcion = fragment.getDescripcion().getText().toString();
                String color = fragment2.getColor();
                Nota nota = new Nota(titulo, descripcion, color);
                nds.createNote(nota); // creamos el objeto y lo añadimos a la bbdd
                Toast.makeText(getApplicationContext(), "Se han guardado los cambios", Toast.LENGTH_SHORT).show();
            }
            else {
                // editar nota HACER
                nds.updateNote(notaAModificar); //actualizamos el objeto en la bbdd
            }
            nds.close();
            return true;
        }
        nds.close();
        return super.onOptionsItemSelected(item);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
                    setTitle("Adjunto");
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
        notaAModificar = b.getParcelable(MainActivity.OBJETO_NOTA);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if(b != null) {
//            Nota nota = (Nota) notaAEditar.getSerializable(MainActivity.OBJETO_NOTA);
//            if(nota.getTitulo() != null){
//                Toast.makeText(getApplicationContext(),"asdasd",Toast.LENGTH_LONG).show();
//            }
            fragment.setArguments(b);
            fragment2.setArguments(b);
        }
        setTitle("Nota");
        FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
        fragmentTransaction1.replace(R.id.frame, fragment,"Nota" );
        fragmentTransaction1.commit();
    }

}
