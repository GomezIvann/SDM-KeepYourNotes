package com.example.ivan.proyectosdm;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.ivan.proyectosdm.CreacionNotas.CrearNota;
import com.example.ivan.proyectosdm.Notas.Nota;
import com.example.ivan.proyectosdm.Notas.NotaAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String OBJETO_NOTA = "nota";

    private List<String> notas;
    private RecyclerView mRVNotas;
    private NotaAdapter adapter;
    private GridLayoutManager glm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRVNotas = (RecyclerView) findViewById(R.id.rvNotas);

        glm = new GridLayoutManager(this, 1);
        mRVNotas.setLayoutManager(glm);
        adapter = new NotaAdapter(dataset());
        mRVNotas.setAdapter(adapter);
    }

    private ArrayList<Nota> dataset() {
        //Notas de prueba: aquí se haría la lectura de la bbdd
        ArrayList<Nota> datos = new ArrayList<Nota>();
        datos.add(new Nota("Ejemplo1", "Contenido de ejemplo",""));
        datos.add(new Nota("Ejemplo2", "Contenido de ejemplo 2",""));
        return datos;
    }

    /**
     * Metodo que se ejecuta cada vez que seleccionas un item del menu
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId(); // coge el id del menu item que has seleccionado
        //Acciones de cada opcion de menu
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {//este metodo te añade el menú al layout
        getMenuInflater().inflate(R.menu.menukeep,menu); //aqui simplemente metes el id del menu que has creado para poder mostrarlo
        return super.onCreateOptionsMenu(menu);
    }

    public void nuevaNota(View view) {
        Intent mIntent = new Intent(MainActivity.this, CrearNota.class);
        startActivity(mIntent);
    }

    public void editarNota(View view) {
        Intent mIntent = new Intent(MainActivity.this, CrearNota.class);
        Nota n = new Nota(findViewById(R.id.txTitle).toString(), findViewById(R.id.txContent).toString(), "yellow");
        mIntent.putExtra(OBJETO_NOTA, n);
        startActivity(mIntent);
    }
}
