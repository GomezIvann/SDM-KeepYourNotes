package com.example.ivan.proyectosdm;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.ivan.proyectosdm.CreacionNotas.CrearNota;
import com.example.ivan.proyectosdm.DataBase.NotesDataSource;
import com.example.ivan.proyectosdm.Notas.Nota;
import com.example.ivan.proyectosdm.Notas.NotaAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String OBJETO_NOTA = "nota";

    private List<Nota> notas;
    private RecyclerView mRVNotas;
    private NotaAdapter adapter;
    private GridLayoutManager glm;
    private NotesDataSource nds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nds = new NotesDataSource(getApplicationContext());

        mRVNotas = (RecyclerView) findViewById(R.id.rvNotas);
        glm = new GridLayoutManager(this, 1);
        mRVNotas.setLayoutManager(glm);
        adapter = new NotaAdapter(dataset());
        mRVNotas.setAdapter(adapter);

        mRVNotas.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRVNotas, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent mIntent = new Intent(MainActivity.this, CrearNota.class);
                Nota n = notas.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable(MainActivity.OBJETO_NOTA, n);
                mIntent.putExtras(bundle);
                startActivity(mIntent);
            }

            @Override
            public void onLongClick(View view, int position) {
                borrarNotaSeleccionada(position);
            }
        }));
    }

    public void borrarNotaSeleccionada(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.CustomDialogTheme);
        builder.setTitle("¿Deseas borrar la nota?");
        final Nota notaABorrar = notas.get(position);

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                nds.open();
                nds.deleteNote(notaABorrar);
                nds.close();
                onResume();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        builder.create().show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter = new NotaAdapter(dataset());
        mRVNotas.setAdapter(adapter);
    }

    public void load(){
        mRVNotas.setAdapter(adapter);
    }

    private ArrayList<Nota> dataset() {
        nds.open();
        notas = nds.getAllNotes();
        nds.close();
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
}
