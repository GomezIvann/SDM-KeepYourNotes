package com.example.ivan.proyectosdm.DataBase;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.example.ivan.proyectosdm.Notas.Imagen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Save {
    private Context TheThis;
    private String NameOfFolder = "/NotasPics";
    private String NameOfFile = "IMG";

    public void SaveImage(Context context, Bitmap ImageToSave,String fileName) {

        TheThis = context;
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + NameOfFolder;
        File dir = new File(file_path);

        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, fileName);

        try {
            FileOutputStream fOut = new FileOutputStream(file);

            ImageToSave.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();
            MakeSureFileWasCreatedThenMakeAvabile(file);
            AbleToSave();
        }

        catch(FileNotFoundException e) {
            UnableToSave();
        }
        catch(IOException e) {
            UnableToSave();
        }
    }

    public void deleteImagen(Imagen img){
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + NameOfFolder;
        File dir = new File(file_path);
        File file = new File(dir, img.getNombre());
        file.delete();
    }

    public String setFileName(){
        return NameOfFile + getCurrentDateAndTime()+ ".jpg";
    }

    public String getImagen(){
        return Environment.getExternalStorageDirectory().getAbsolutePath() + NameOfFolder;
    }

    private void MakeSureFileWasCreatedThenMakeAvabile(File file){
        MediaScannerConnection.scanFile(TheThis,
                new String[] { file.toString() } , null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri) {
                    }
                });
    }

    private String getCurrentDateAndTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    private void UnableToSave() {
        Toast.makeText(TheThis, "No se ha podido guardar la imagen.", Toast.LENGTH_SHORT).show();
    }

    private void AbleToSave() {
        Toast.makeText(TheThis, "Imagen guardada en la galería.", Toast.LENGTH_SHORT).show();
    }



    public boolean existImage(String fileName){
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + NameOfFolder;
        File dir = new File(file_path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, fileName);
        return file.exists();
    }
}
