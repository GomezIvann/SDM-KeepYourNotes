package com.example.ivan.proyectosdm.CreacionNotas;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.ivan.proyectosdm.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentColor extends Fragment {
    private Switch Amarillo;
    private Switch Azul;
    private Switch Verde;
    private Switch Naranja;
    private Switch Morado;
    private Switch Rojo;
    private Switch Blanco;
    private String color = "Amarillo";
    public FragmentColor() {
        // Required empty public constructor
    }

    public String getColor() {
        return color;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_color, container, false);
        Amarillo = v.findViewById(R.id.switchAmarillo);
        Azul = v.findViewById(R.id.switchAzul);
        Verde = v.findViewById(R.id.switchVerde);
        Naranja = v.findViewById(R.id.switchNaranja);
        Morado = v.findViewById(R.id.switchMorado);
        Rojo = v.findViewById(R.id.switchRojo);
        Blanco = v.findViewById(R.id.switchBlanco);
        Amarillo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    color = Amarillo.getText().toString();
                    Azul.setChecked(false);
                    Verde.setChecked(false);
                    Naranja.setChecked(false);
                    Morado.setChecked(false);
                    Rojo.setChecked(false);
                    Blanco.setChecked(false);
                    Amarillo.setChecked(true);
                }
            }
        });
        Azul.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    color = Azul.getText().toString();
                    Amarillo.setChecked(false);
                    Verde.setChecked(false);
                    Naranja.setChecked(false);
                    Morado.setChecked(false);
                    Rojo.setChecked(false);
                    Blanco.setChecked(false);
                    Azul.setChecked(true);
                }
            }
        });
        Verde.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    color = Verde.getText().toString();
                    Azul.setChecked(false);
                    Amarillo.setChecked(false);
                    Naranja.setChecked(false);
                    Morado.setChecked(false);
                    Rojo.setChecked(false);
                    Blanco.setChecked(false);
                    Verde.setChecked(true);
                }
            }
        });
        Naranja.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    color = Naranja.getText().toString();
                    Azul.setChecked(false);
                    Verde.setChecked(false);
                    Amarillo.setChecked(false);
                    Morado.setChecked(false);
                    Rojo.setChecked(false);
                    Blanco.setChecked(false);
                    Naranja.setChecked(true);
                }
            }
        });
        Morado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    color = Morado.getText().toString();
                    Azul.setChecked(false);
                    Verde.setChecked(false);
                    Naranja.setChecked(false);
                    Amarillo.setChecked(false);
                    Rojo.setChecked(false);
                    Blanco.setChecked(false);
                    Morado.setChecked(true);
                }
            }
        });
        Rojo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    color = Rojo.getText().toString();
                    Azul.setChecked(false);
                    Verde.setChecked(false);
                    Naranja.setChecked(false);
                    Morado.setChecked(false);
                    Amarillo.setChecked(false);
                    Blanco.setChecked(false);
                    Rojo.setChecked(true);
                }
            }
        });
        Blanco.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    color = Blanco.getText().toString();
                    Azul.setChecked(false);
                    Verde.setChecked(false);
                    Naranja.setChecked(false);
                    Morado.setChecked(false);
                    Rojo.setChecked(false);
                    Amarillo.setChecked(false);
                    Blanco.setChecked(true);
                }
            }
        });
        return v;
    }

}
