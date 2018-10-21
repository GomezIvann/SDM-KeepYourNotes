package com.example.ivan.proyectosdm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }

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
            //sacar mensaje diciendo que se han guardado los cambios
            Toast.makeText(getApplicationContext(), "Se han guardado los cambios", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void ColorRojo(View v){
        TextView txt = (TextView) findViewById(R.id.textView3);
        txt.setText("Color: Rojo");
    }
    public void ColorVerde(View v){
        TextView txt = (TextView) findViewById(R.id.textView3);
        txt.setText("Color: Verde");
    }
    public void ColorAzul(View v){
        TextView txt = (TextView) findViewById(R.id.textView3);
        txt.setText("Color: Azul");
    }
}
