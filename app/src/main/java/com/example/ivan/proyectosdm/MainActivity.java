package com.example.ivan.proyectosdm;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.ivan.proyectosdm.CreacionNotas.CrearNota;
import com.example.ivan.proyectosdm.DataBase.NoteDataSource;
import com.example.ivan.proyectosdm.Notas.Nota;
import com.example.ivan.proyectosdm.Notas.NotaAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String OBJETO_NOTA = "nota";

    private List<Nota> notas;
    private RecyclerView mRVNotas;
    private NotaAdapter adapter;
    private GridLayoutManager glm;
    private NoteDataSource ndb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ndb = new NoteDataSource(getApplicationContext());

        mRVNotas = (RecyclerView) findViewById(R.id.rvNotas);
        glm = new GridLayoutManager(this, 1);
        mRVNotas.setLayoutManager(glm);
        adapter = new NotaAdapter(dataset());
        mRVNotas.setAdapter(adapter);
    }

    private ArrayList<Nota> dataset() {
        ndb.open();
        notas = ndb.getAllNotes();
        ndb.close();
        return new ArrayList<Nota>(notas);
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
        Nota n = new Nota(((TextView) findViewById(R.id.txTitle)).getText().toString(), ((TextView) findViewById(R.id.txContent)).getText().toString(), getString(R.string.amarillo));
        Bundle bundle = new Bundle();
        bundle.putSerializable(MainActivity.OBJETO_NOTA, n);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
    }
}
