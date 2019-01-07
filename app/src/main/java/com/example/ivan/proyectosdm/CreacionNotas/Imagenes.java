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

import com.example.ivan.proyectosdm.Notas.Imagen;
import com.example.ivan.proyectosdm.Notas.Nota;
import com.example.ivan.proyectosdm.R;

import java.util.ArrayList;
import java.util.List;

public class Imagenes extends AppCompatActivity {
    private Imagen img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagenes);
        Bundle b = getIntent().getExtras();


        img = (Imagen) b.getSerializable(FragmentAdjuntos.OBJETO_IMAGEN);
        ImageView imgV = (ImageView) findViewById(R.id.imageView3);
        imgV.setImageBitmap(img.getBitmap());
        setTitle(img.getNombre());
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
