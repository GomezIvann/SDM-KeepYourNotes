package com.example.ivan.proyectosdm.CreacionNotas;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
public class FragmentAdjuntos extends Fragment {
    private Nota nota;
    private boolean fabExpanded = false;
    private FloatingActionButton fabSettings;
    private LinearLayout layoutFabFoto;
    private LinearLayout layoutFabVideo;
    private final String CARPETA_RAIZ = "misImagenesPrueba/";
    private final String RUTA_IMAGEN = CARPETA_RAIZ + "misFotos";
    String path;
    final int COD_FOTO_SELECCION=10;
    final int COD_FOTO_CAPTURA=20;
    final int COD_VIDEO_SELECCION=30;
    final int COD_VIDEO_CAPTURA=40;
    private boolean permisos;
    private Save save = new Save();
    public static final String OBJETO_IMAGEN = "imagen";


    private List<Imagen> imagenes = new ArrayList<Imagen>();
    private RecyclerView mRVImagen;
    private ArchivoAdapter adapter;
    private GridLayoutManager glm;

    public FragmentAdjuntos() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            nota = (Nota) getArguments().getSerializable(MainActivity.OBJETO_NOTA);
            this.imagenes = new ArrayList<>(nota.getImagenes());
            Save save = new Save();
            File dir = new File(save.getImagen());
            if (dir.exists()) {
                for (int i = 0; i<this.imagenes.size();i++) {
                    Imagen img = this.imagenes.get(i);
                   if(img != null){
                       File file = new File(dir, img.getNombre());
                       if (file.exists()) {
                           String filePath = file.getPath();
                           Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                           img.setBitmap(bitmap);
                       }

                   }
                }
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fabFoto = inflater.inflate(R.layout.layout_fab_foto, container, false);
        View fabVideo = inflater.inflate(R.layout.layout_fab_video, container, false);
        View v = inflater.inflate(R.layout.fragment_fragment_adjuntos, container, false);
        mRVImagen = (RecyclerView) v.findViewById(R.id.rvImagenes);
        if(nota == null){
            nota = new Nota("asd","asd",0);
            this.imagenes = new ArrayList<Imagen>();
        }
        //
        cargarImagenes();
        fabSettings = (FloatingActionButton) v.findViewById(R.id.fabAdjuntos);
        layoutFabFoto = (LinearLayout) fabFoto.findViewById(R.id.layoutFabFoto);
        layoutFabVideo = (LinearLayout) fabVideo.findViewById(R.id.layoutFabVideo);
        fabSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabExpanded){
                    closeSubMenusFab();
                } else {
                    openSubMenusFab();
                }
            }
        });
        mRVImagen.setZ(0);
        layoutFabFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validaPermisos(0);
            }
        });
        layoutFabFoto.setZ(1);
        layoutFabVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validaPermisos(1);
            }
        });
        layoutFabVideo.setZ(1);
        container.addView(fabFoto);
        container.addView(fabVideo);
        closeSubMenusFab();
        mRVImagen.addOnItemTouchListener(new RecyclerTouchListener(getContext(), mRVImagen, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getActivity(), Imagenes.class);
                Bundle bundle = new Bundle();
                Imagen img = null;
                for (int i1 = 0; i1 < imagenes.size(); i1++) {
                    Imagen imagene = imagenes.get(i1);
                    if(adapter.getImagenes().get(position).getNombre().equals(imagene.getNombre())){
                        img = adapter.getImagenes().get(position);
                    }
                }
//                bundle.putSerializable(OBJETO_IMAGEN, img);
                intent.putExtra("BitmapImage", img.getBitmap());
                intent.putExtra("title",img.getNombre());
//                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, final int position) {
                Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    v.vibrate(200);
                }
                borrarImagen(position);
            }
        }));
        return v;
    }

    public void borrarImagen(final int i){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.CustomDialogTheme);
        builder.setTitle("¿Deseas borrar la nota?");

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                for (int i1 = 0; i1 < imagenes.size(); i1++) {
                    Imagen imagene = imagenes.get(i1);
                    if(adapter.getImagenes().get(i).getNombre().equals(imagene.getNombre())){
                        imagene.borrarFoto();
                    }
                }

                imagenes.get(i).borrarFoto();
                Log.d("LONG",imagenes.size()+"");
                cargarImagenes();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        builder.create().show();
    }

    private boolean validaPermisos(int seleccion) {

        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            if(seleccion == 0) {
                cargarImagen();
            }else{
                cargarVideo();
            }
            return true;
        }

        if((checkSelfPermission(getContext(),CAMERA)==PackageManager.PERMISSION_GRANTED)&&
                (checkSelfPermission(getContext(),WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)){
            if(seleccion == 0) {
                cargarImagen();
            }else{
                cargarVideo();
            }
            return true;
        }
        requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
        if(permisos){
            if(seleccion == 0) {
                cargarImagen();
            }else{
                cargarVideo();
            }
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
                dialog.setTitle("Aviso: Permisos Desactivados");
                dialog.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");
                dialog.create().show();
            }
        }
    }

    private void cargarVideo(){
        final CharSequence[] opciones={"Grabar Vídeo","Cargar Vídeo","Cancelar"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(getContext());
        alertOpciones.setTitle("Seleccione una Opción");
        alertOpciones.setItems(opciones,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(opciones[which].equals("Grabar Vídeo")){
                            closeSubMenusFab();
                            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                            startActivityForResult(Intent.createChooser(intent, ""), COD_VIDEO_CAPTURA);
                        }
                        else if(opciones[which].equals("Cargar Vídeo")){
                            Intent intent = new Intent();
                            intent.setType("video/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent, ""), COD_VIDEO_SELECCION);
                        }else{
                            dialog.dismiss();
                        }
                    }
                });
        alertOpciones.create().show();
    }

    private void cargarImagen() {
        final CharSequence[] opciones={"Tomar Foto","Cargar Imagen","Cancelar"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(getContext());
        alertOpciones.setTitle("Seleccione una Opción");
        alertOpciones.setItems(opciones,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(opciones[which].equals("Tomar Foto")){
                            closeSubMenusFab();
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
        if (resultCode==RESULT_OK){
            if(requestCode == COD_FOTO_SELECCION){
                Uri miPath=data.getData();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),miPath);
                } catch (IOException e) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setTitle("Aviso: Error");
                    dialog.setMessage("Vuelve a seleccionar la foto");
                    dialog.create().show();
                }
                String fileName = save.setFileName();
                Imagen imagen = new Imagen(fileName,bitmap);
                imagenes.add(imagen);
                cargarImagenes();

            }else if(requestCode == COD_FOTO_CAPTURA){
                Bundle extras = data.getExtras();
                Bitmap bitmap1 = (Bitmap) extras.get("data");
                String fileName1 = save.setFileName();
                Imagen imagen1 = new Imagen(fileName1,bitmap1);
                imagenes.add(imagen1);
                cargarImagenes();
            }else if(requestCode == COD_VIDEO_CAPTURA){
                MediaScannerConnection.scanFile(getContext(), new String[]{path}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("Ruta de almacenamiento","Path: "+path);
                            }
                });
                Uri uri = Uri.parse(path);
            }else if(requestCode == COD_VIDEO_SELECCION){
                Uri miPath=data.getData();
                if(miPath == null) {
                    AlertDialog.Builder dialog1 = new AlertDialog.Builder(getContext());
                    dialog1.setTitle("Aviso: Error");
                    dialog1.setMessage("Vuelve a seleccionar el vídeo");
                    dialog1.create().show();
                }
            }
        }
    }

    public void cargarImagenes(){
        ArrayList<Imagen> aux = new ArrayList<Imagen>();
        for (int i = 0; i < imagenes.size(); i++) {
            Imagen imagene = imagenes.get(i);
            if(!imagene.isBorrado()){
                aux.add(imagene);
            }
        }
        glm = new GridLayoutManager(getContext(), 1);
        mRVImagen.setLayoutManager(glm);
        adapter = new ArchivoAdapter(aux);
        mRVImagen.setAdapter(adapter);
    }

    private void closeSubMenusFab(){
        layoutFabFoto.setVisibility(View.GONE);
        layoutFabVideo.setVisibility(View.GONE);
        fabSettings.setImageResource(R.drawable.ic_add_white_24dp);
        fabExpanded = false;
    }

    private void openSubMenusFab(){
        layoutFabFoto.setVisibility(View.VISIBLE);
        layoutFabVideo.setVisibility(View.VISIBLE);
        fabSettings.setImageResource(R.drawable.ic_close_black_24dp);
        fabExpanded = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        closeSubMenusFab();
    }

    @Override
    public void onResume() {
        super.onResume();
        closeSubMenusFab();
    }

    public List<Imagen> getImagenes(){
        if(this.imagenes.size() == 0){
            return null;
        }else{
            return this.imagenes;
        }
    }
}
