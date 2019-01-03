package com.example.ivan.proyectosdm.CreacionNotas;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.io.File;
import java.net.URI;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;
import static android.support.v4.content.ContextCompat.checkSelfPermission;
import static android.support.v4.content.FileProvider.getUriForFile;

import com.example.ivan.proyectosdm.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAdjuntos extends Fragment {
    private static final int MY_PERMISSIONS_REQUEST_CAMARA = 0;
    private boolean fabExpanded = false;
    private FloatingActionButton fabSettings;
    private LinearLayout layoutFabFoto;
    private LinearLayout layoutFabVideo;
    private LinearLayout layoutFabUbi;
    private final String CARPETA_RAIZ = "misImagenesPrueba/";
    private final String RUTA_IMAGEN = CARPETA_RAIZ + "misFotos";
    String path;
    final int COD_SELECCIONA=10;
    final int COD_FOTO=20;
    ImageView imagen;
    Button botonCargar;

    public FragmentAdjuntos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fabFoto = inflater.inflate(R.layout.layout_fab_foto, container, false);
        View fabVideo = inflater.inflate(R.layout.layout_fab_video, container, false);
        View fabUbi = inflater.inflate(R.layout.layout_fab_ubicacion, container, false);
        container.addView(fabFoto);
        container.addView(fabVideo);
        container.addView(fabUbi);
        View v = inflater.inflate(R.layout.fragment_fragment_adjuntos, container, false);
        // Inflate the layout for this fragment
        fabSettings = (FloatingActionButton) v.findViewById(R.id.fabAdjuntos);
        layoutFabFoto = (LinearLayout) fabFoto.findViewById(R.id.layoutFabFoto);
        layoutFabVideo = (LinearLayout) fabVideo.findViewById(R.id.layoutFabVideo);
        layoutFabUbi = (LinearLayout) fabUbi.findViewById(R.id.layoutFabUbi);
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
        closeSubMenusFab();
        layoutFabFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validaPermisos();
            }
        });
        layoutFabVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validaPermisos();
            }
        });
        return v;
    }

    private boolean validaPermisos() {

        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            cargarImagen();
            return true;
        }

        if((checkSelfPermission(getContext(),CAMERA)==PackageManager.PERMISSION_GRANTED)&&
                (checkSelfPermission(getContext(),WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)){
            cargarImagen();
            return true;
        }
        requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Toast.makeText(getContext(),"asdfbasdofib",Toast.LENGTH_LONG).show();
        if(requestCode==100){
            if(grantResults.length==2 && grantResults[0]==PackageManager.PERMISSION_GRANTED
                    && grantResults[1]==PackageManager.PERMISSION_GRANTED){
                cargarImagen();
            }else{
                Toast.makeText(getContext(),"acepta los permisos", Toast.LENGTH_LONG).show();
            }
        }

    }

    private void cargarImagen() {
        final CharSequence[] opciones={"Tomar Foto","Cargar Imagen","Cancelar"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(getContext());
        alertOpciones.setTitle("Seleccione una Opción");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar Foto")){
                    tomarFotografia();
                }else{
                    if (opciones[i].equals("Cargar Imagen")){
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, ""), COD_SELECCIONA);
                    }else{
                        dialogInterface.dismiss();
                    }
                }
            }
        });
        alertOpciones.show();

    }

    private void tomarFotografia() {
        File fileImagen=new File(Environment.getExternalStorageDirectory(),RUTA_IMAGEN);
        boolean isCreada=fileImagen.exists();
        String nombreImagen="";
        if(isCreada==false){
            isCreada=fileImagen.mkdirs();
        }

        if(isCreada==true){
            nombreImagen=(System.currentTimeMillis()/1000)+".jpg";
            Toast.makeText(getContext(),"4",Toast.LENGTH_LONG).show();
        }


        path=Environment.getExternalStorageDirectory()+
                File.separator+RUTA_IMAGEN+File.separator+nombreImagen;
//        Toast.makeText(getContext(),"5",Toast.LENGTH_LONG).show();
        File imagen=new File(path);
//        Toast.makeText(getContext(),"6",Toast.LENGTH_LONG).show();

        Intent intent=null;
        intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        Toast.makeText(getContext(),"7",Toast.LENGTH_LONG).show();
        ////
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N)
        {
            String authorities=getContext().getPackageName()+".provider";
//            Toast.makeText(getContext(),"8",Toast.LENGTH_LONG).show();
            Uri imageUri=FileProvider.getUriForFile(getContext(),authorities,imagen);
//            Toast.makeText(getContext(),"9",Toast.LENGTH_LONG).show();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//            Toast.makeText(getContext(),"10",Toast.LENGTH_LONG).show();
        }else
        {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen));
//            Toast.makeText(getContext(),"11",Toast.LENGTH_LONG).show();
        }
        startActivityForResult(intent,COD_FOTO);
//        Toast.makeText(getContext(),"12",Toast.LENGTH_LONG).show();

        ////
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){

            switch (requestCode){
                case COD_SELECCIONA:
                    Uri miPath=data.getData();
                    if(miPath != null){
                        Toast.makeText(getContext(),"hola",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getContext(),"sdf",Toast.LENGTH_LONG).show();

                    }
                    break;

                case COD_FOTO:
                    Toast.makeText(getContext(),"qweqwe",Toast.LENGTH_LONG).show();
                    MediaScannerConnection.scanFile(getContext(), new String[]{path}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("Ruta de almacenamiento","Path: "+path);
                                }
                            });

                    Bitmap bitmap= BitmapFactory.decodeFile(path);
                    if(bitmap != null){
                        Toast.makeText(getContext(),"hola",Toast.LENGTH_LONG).show();
                    }

                    break;
            }


        }
    }


    private void abrirCamaraFotos(){
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivity(i);
    }

    private void closeSubMenusFab(){
        layoutFabFoto.setVisibility(View.GONE);
        layoutFabVideo.setVisibility(View.GONE);
        layoutFabUbi.setVisibility(View.GONE);
        fabSettings.setImageResource(R.drawable.ic_add_white_24dp);
        fabExpanded = false;
    }

    //Opens FAB submenus
    private void openSubMenusFab(){
        layoutFabFoto.setVisibility(View.VISIBLE);
        layoutFabVideo.setVisibility(View.VISIBLE);
        layoutFabUbi.setVisibility(View.VISIBLE);
        //Change settings icon to 'X' icon
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
}
