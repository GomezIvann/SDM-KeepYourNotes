package com.example.ivan.proyectosdm.CreacionNotas;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ivan.proyectosdm.DataBase.Save;
import com.example.ivan.proyectosdm.Notas.Imagen;
import com.example.ivan.proyectosdm.Notas.Nota;
import com.example.ivan.proyectosdm.R;

import java.util.ArrayList;
import java.util.List;

public class VisualizarImagen extends AppCompatActivity {

    private Imagen imgAMostrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagenes);

        imgAMostrar = (Imagen) getIntent().getExtras().getSerializable("imagen");

        ImageView imgV = (ImageView) findViewById(R.id.imageView3);
        Save save = new Save(getApplicationContext());
        if(imgAMostrar != null)
            imgAMostrar.setBitmap(save.getImagen(imgAMostrar.getNombre()));
        setTitle(imgAMostrar.getNombre());
        imgV.setImageBitmap(imgAMostrar.getBitmap());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_imagenes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.Salir) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
