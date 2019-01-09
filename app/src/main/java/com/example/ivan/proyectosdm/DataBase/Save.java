package com.example.ivan.proyectosdm.DataBase;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.example.ivan.proyectosdm.Notas.Imagen;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Save {
    private Context context;
    private String NameOfFolder = "/NotasPics";
    private String NameOfFile = "IMG";

    public Save(Context context){
        this.context = context;
    }

    public void saveImage(Bitmap imageToSave, String fileName){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        try {
            FileOutputStream outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(byteArray);
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public void deleteImagen(Imagen img){
        String file_path = getPathImages();
        File dir = new File(file_path);
        File file = new File(dir, img.getNombre());
        if (file.exists())
            file.delete();
    }

    public String getPathImages() {
        return context.getFilesDir().getPath();
    }

    public String setFileName(){
        return NameOfFile + getCurrentDateAndTime()+ ".jpg";
    }

    private String getCurrentDateAndTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public boolean existImage(String fileName){
        String file_path = getPathImages();
        File dir = new File(file_path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, fileName);
        return file.exists();
    }

    public Bitmap getImagen(String nameOfFile){
        Bitmap bitmap = null;
        if (existImage(nameOfFile)){
            try {
                FileInputStream fileInputStream =
                        new FileInputStream(getPathImages() +"/"+ nameOfFile);
                bitmap = BitmapFactory.decodeStream(fileInputStream);
            } catch (IOException io) {
                io.printStackTrace();
            }
        }
        return bitmap;
    }

    public void saveOnGallery(Bitmap ImageToSave,String fileName){
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
        }

        catch(FileNotFoundException e) {
            UnableToSave();
        }
        catch(IOException e) {
            UnableToSave();
        }
    }

    private void MakeSureFileWasCreatedThenMakeAvabile(File file){
        MediaScannerConnection.scanFile(context,
                new String[] { file.toString() } , null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri) {
                    }
                });
    }

    private void UnableToSave() {
        Toast.makeText(context, "No se ha podido guardar la imagen.", Toast.LENGTH_SHORT).show();
    }

}
