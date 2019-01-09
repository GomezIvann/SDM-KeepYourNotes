package com.example.ivan.proyectosdm.CreacionNotas;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ivan.proyectosdm.DataBase.Save;
import com.example.ivan.proyectosdm.MainActivity;
import com.example.ivan.proyectosdm.Notas.ArchivoAdapter;
import com.example.ivan.proyectosdm.Notas.Imagen;
import com.example.ivan.proyectosdm.Notas.Nota;
import com.example.ivan.proyectosdm.R;
import com.example.ivan.proyectosdm.RecyclerTouchListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;
import static android.support.v4.content.ContextCompat.checkSelfPermission;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentImagenes extends Fragment {
    private Nota nota;
    private FloatingActionButton fabSettings;
    final int COD_FOTO_SELECCION=10;
    final int COD_FOTO_CAPTURA=20;
    private boolean permisos;
    private Save save;

    private List<Imagen> imagenes = new ArrayList<Imagen>();
    // lista que contiene las nuevas imagenes insertadas pero sin guardar. Tendra la finalidad de, en caso de no darle a guardar, seran borradas del sistema.
    private List<Imagen> nuevasImagenes = new ArrayList<Imagen>();
    private RecyclerView mRVImagen;
    private ArchivoAdapter adapter;
    private GridLayoutManager glm;

    public FragmentImagenes() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        save = new Save(getContext());

        if(getArguments() != null){
            nota = (Nota) getArguments().getSerializable(MainActivity.OBJETO_NOTA);
            this.imagenes = new ArrayList<>(nota.getImagenes());
            File dir = new File(save.getPathImages());
            if (dir.exists()) {
                for (int i = 0; i<this.imagenes.size();i++) {
                   Imagen img = this.imagenes.get(i);
                   if(img != null)
                       img.setBitmap(save.getImagen(img.getNombre()));
                }
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fragment_adjuntos, container, false);
        mRVImagen = (RecyclerView) v.findViewById(R.id.rvImagenes);
        if(nota == null) {
            nota = new Nota("asd", "asd", 0);
            if (imagenes == null) {
                this.imagenes = new ArrayList<Imagen>();
            }
        }
        cargarImagenes();
        fabSettings = (FloatingActionButton) v.findViewById(R.id.fabAdjuntos);
        mRVImagen.setZ(0);
        fabSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validaPermisos();
            }
        });
        mRVImagen.addOnItemTouchListener(new RecyclerTouchListener(getContext(), mRVImagen, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getActivity(), VisualizarImagen.class);
                Imagen imgAdapter = null;
                for (int i1 = 0; i1 < imagenes.size(); i1++) {
                    Imagen imagen = imagenes.get(i1);
                    if(adapter.getImagenes().get(position).getNombre().equals(imagen.getNombre())){
                        imgAdapter = adapter.getImagenes().get(position);
                    }
                }

                intent.putExtra("imagen", imgAdapter);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, final int position) {
                Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    v.vibrate(50);
                }
                borrarImagen(position);
            }
        }));
        return v;
    }

    public void borrarImagen(final int i){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.CustomDialogTheme);
        builder.setTitle("¿Desea borrar esta imagen?");
        builder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                for (int i1 = 0; i1 < imagenes.size(); i1++) {
                    Imagen imagen = imagenes.get(i1);
                    if(adapter.getImagenes().get(i).getNombre().equals(imagen.getNombre())){
                        imagen.borrarFoto();
                    }
                }
                cargarImagenes();
            }
        });
        builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.create().show();
    }

    private boolean validaPermisos() {

        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            cargarOpcionesImagen();
            return true;
        }

        if((checkSelfPermission(getContext(),CAMERA)==PackageManager.PERMISSION_GRANTED)&&
                (checkSelfPermission(getContext(),WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)){
            cargarOpcionesImagen();
            return true;
        }
        requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
        if(permisos){
            cargarOpcionesImagen();
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100){
            if(grantResults.length==2 && grantResults[0]==PackageManager.PERMISSION_GRANTED
                    && grantResults[1]==PackageManager.PERMISSION_GRANTED){
                permisos = true;
            }else{
                permisos = false;
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle(R.string.nopermisos);
                dialog.setMessage(R.string.nopermisos2);
                dialog.create().show();
            }
        }
    }

    private void cargarOpcionesImagen() {
        final CharSequence[] opciones={"Tomar Foto","Cargar Imagen","Cancelar"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(getContext());
        alertOpciones.setTitle(R.string.seleccioneopcion);
        alertOpciones.setItems(opciones,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(opciones[which].equals("Tomar Foto")){
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(Intent.createChooser(intent, ""), COD_FOTO_CAPTURA);
                        }
                        else if(opciones[which].equals("Cargar Imagen")){
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent, ""), COD_FOTO_SELECCION);
                        }else{
                            dialog.dismiss();
                        }
                    }
                });
        alertOpciones.create().show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*Puede que salga de la galeria o camara dando para atras, evitamos que rompa la app*/
        if (data!=null) {
            String fileName = null;
            Imagen imagen = null;
            Uri miPath=data.getData();
            Bitmap bitmap = null;

            if (resultCode==RESULT_OK)
            {
                if(requestCode == COD_FOTO_SELECCION)
                {
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),miPath);
                    } catch (IOException e) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                        dialog.setTitle("Aviso: Error");
                        dialog.setMessage("Vuelve a seleccionar la foto");
                        dialog.create().show();
                    }
                    fileName = save.setFileName();
                    imagen = new Imagen(fileName,bitmap);
                    imagenes.add(imagen);
                    cargarImagenes();

                    nuevasImagenes.add(imagen);
                    if (!save.existImage(fileName))
                        save.saveImage(bitmap,fileName);

                }
                else if(requestCode == COD_FOTO_CAPTURA){
                    Bundle extras = data.getExtras();
                    bitmap = (Bitmap) extras.get("data");
                    fileName = save.setFileName();
                    imagen = new Imagen(fileName,bitmap);
                    imagenes.add(imagen);
                    cargarImagenes();

                    // Como es una imagen nueva la guardamos en la galeria
                    save.saveOnGallery(bitmap, fileName);

                    nuevasImagenes.add(imagen);
                    if (!save.existImage(fileName))
                        save.saveImage(bitmap, fileName);

                }
            }
        }
    }

    public void cargarImagenes(){
        ArrayList<Imagen> aux = new ArrayList<Imagen>();
        for (int i = 0; i < imagenes.size(); i++) {
            Imagen imagen = imagenes.get(i);
            if(!imagen.isBorrado()){
                aux.add(imagen);
            }
        }
        glm = new GridLayoutManager(getContext(), 1);
        mRVImagen.setLayoutManager(glm);
        adapter = new ArchivoAdapter(aux);
        mRVImagen.setAdapter(adapter);
    }

    public List<Imagen> getImagenes(){
        if(this.imagenes.size() == 0)
            return new ArrayList<Imagen>();
        else
            return this.imagenes;
    }

    public List<Imagen> getNuevasImagenes(){
        if(this.nuevasImagenes.size() == 0)
            return new ArrayList<Imagen>();
        else
            return this.nuevasImagenes;
    }





}
