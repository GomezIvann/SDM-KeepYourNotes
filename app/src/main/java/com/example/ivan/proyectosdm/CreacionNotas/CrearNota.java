package com.example.ivan.proyectosdm.CreacionNotas;

import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ivan.proyectosdm.R;

public class CrearNota extends AppCompatActivity {

    private TextView mTextMessage;
    private FragmentTituloContenido fragment = new FragmentTituloContenido();
    private FragmentColor fragment2 = new FragmentColor();
    private FragmentAdjuntos fragment3 = new FragmentAdjuntos();
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.cancelar) {
            //sacar mensaje diciendo que si quiere salir sin guardar los cambios
            return true;
        }
        else if (id == R.id.Guardar) {
            String Titulo = fragment.getTitulo().getText().toString();
            String Descripcion = fragment.getDescripcion().getText().toString();
            String Colro = fragment2.getColor();
            Nota nota = new Nota(Titulo, Descripcion, Colro);
            Toast.makeText(getApplicationContext(), "Se han guardado los cambios", Toast.LENGTH_SHORT).show();
            return true;
        }

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
                    FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction2.replace(R.id.frame, fragment2,"Nota" );
                    fragmentTransaction2.commit();
                    return true;
                case R.id.navigation_adjunto:
                    FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction3.replace(R.id.frame, fragment3,"Nota" );
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

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        setTitle("Nota");
        FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
        fragmentTransaction1.replace(R.id.frame, fragment,"Nota" );
        fragmentTransaction1.commit();
    }

}
