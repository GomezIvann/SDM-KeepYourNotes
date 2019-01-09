package com.example.ivan.proyectosdm.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ivan.proyectosdm.Notas.Imagen;
import com.example.ivan.proyectosdm.Notas.Nota;

import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la tabla de valoracion.
 * Se encarga de abrir y cerrar la conexion, asi como hacer las consultas relacionadas con la tabla valoracion
 *
 */
public class NotesDataSource {
    /**
     * Referencia para manejar la base de datos. Este objeto lo obtenemos a partir de MyDBHelper
     * y nos proporciona metodos para hacer operaciones
     * CRUD (create, read, update and delete)
     */
    private SQLiteDatabase database;
    /**
     * Referencia al helper que se encarga de crear y actualizar la base de datos.
     */
    private MyDBHelper dbHelper;
    /**
     * Columnas de la tabla notas
     */
    private final String[] allColumns = { MyDBHelper.COLUMN_ID, MyDBHelper.COLUMN_TITULO,
            MyDBHelper.COLUMN_CONTENIDO, MyDBHelper.COLUMN_COLOR, MyDBHelper.COLUMN_COORDENADA};

    /**
     * Constructor
     * @param context
     */
    public NotesDataSource(Context context) {
        dbHelper = new MyDBHelper(context, null, null, 1);
    }
    /**
     * Abre una conexion para escritura con la base de datos.
     * Esto lo hace a traves del helper con la llamada a getWritableDatabase. Si la base de
     * datos no esta creada, el helper se encargara de llamar a onCreate
     *
     * @throws SQLException
     */
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        dbHelper.onUpgrade(database, 0, 1); //decomentad esta linea si cambias la base de datos o no esta en la ultima version MUCHO CUIDADO BORRA LA BASE DEL MOVIL
    }

    /**
     * Cierra la conexion con la base de datos
     */
    public void close() {
        dbHelper.close();
    }

    /*
     * Crea una nota, guarda las imagenes adjuntas (si las hay), la inserta y devuelve su id
     */
    public long createNote(Nota note) {
        ContentValues values = new ContentValues();
        values.put(MyDBHelper.COLUMN_TITULO, note.getTitulo());
        values.put(MyDBHelper.COLUMN_CONTENIDO, note.getContenido());
        values.put(MyDBHelper.COLUMN_COLOR, note.getColor());
        values.put(MyDBHelper.COLUMN_COORDENADA, note.getCoordenadas());
        long insertId = database.insert(MyDBHelper.TABLE_NOTES, null, values);
        createImages(note, insertId);
        return insertId;
    }


    /**
     * Crea las imagenes asociadas a una nota
     * @param note
     * @param insertId
     */
    public void createImages(Nota note, long insertId) {
        Imagen img = null;
        ContentValues values = null;

        for( int i = 0; i < note.getImagenes().size(); i++ ) {
            img = note.getImagen(i);
            if(!img.isBorrado()){
                values = new ContentValues();
                values.put(MyDBHelper.COLUMN_ID_NOTA, insertId);
                values.put(MyDBHelper.COLUMN_IMG_NOMBRE, img.getNombre());
                database.insert(MyDBHelper.TABLE_IMAGES, null, values);
            }
        }
    }
    
    /* 
     * Método para eliminar una nota de la base de datos
     *
     * @return devuelve un valor booleano indicando el éxito o no de la operación
     */
    public void deleteNote(Nota note, Context context) {
        deleteImagesFromNote(note, context);
        database.delete(MyDBHelper.TABLE_NOTES, MyDBHelper.COLUMN_ID + "=" + note.getId(), null);
    }

    /**
     * Elimina las imagenes asociadas a una nota y del sistema
     *
     * @param note
     */
    public void deleteImagesFromNote(Nota note, Context context){
        List<Imagen> imagenes = note.getImagenes();
        Save save = new Save(context);
        Cursor c = database.rawQuery(" SELECT _id , name FROM " +MyDBHelper.TABLE_IMAGES+" WHERE note_id="+note.getId(),null);
        c.moveToFirst();
        long id = 0;
        String name = "";
        while (!c.isAfterLast()) {
            id = c.getLong(0);
            name = c.getString(1);
            note.asociarIds(name,id);
            c.moveToNext();
        }
        c.close();

        //Elimina las imagenes del sistema
        for (int i = 0; i < imagenes.size(); i++) {
            Imagen imagen = imagenes.get(i);
            if(imagen.isBorrado()){
                deleteImage(imagen.getId());
                save.deleteImagen(imagen);
            }
        }
    }

    /**
     * Elimina una imagen de la base de datos
     *
     * @param _idFila
     */
    public void deleteImage(long _idFila){
        database.delete(MyDBHelper.TABLE_IMAGES, MyDBHelper.COLUMN_ID + "=" + _idFila, null);
    }

    /**
     * Método que me actualiza una nota
     */
    public void updateNote(Nota note, Context context) {
        ContentValues values = new ContentValues();
        values.put(MyDBHelper.COLUMN_TITULO, note.getTitulo());
        values.put(MyDBHelper.COLUMN_CONTENIDO, note.getContenido());
        values.put(MyDBHelper.COLUMN_COLOR, note.getColor());
        values.put(MyDBHelper.COLUMN_COORDENADA, note.getCoordenadas());
        database.update(MyDBHelper.TABLE_NOTES, values, MyDBHelper.COLUMN_ID+"="+note.getId(), null);

        //actualizamos tambien sus fotos (borrar y reinsertar)
        deleteImagesFromNote(note, context);
        createImages(note, note.getId());
    }

    /**
     * Obtiene todas las notas anadidas por los usuarios. Es análogo a lo que hemos visto en ri
     *
     * @return Lista de objetos de tipo Valoration
     */
    public List<Nota> getAllNotes() {
        List<Nota> noteList = new ArrayList<Nota>();
        Cursor cursor = database.query(MyDBHelper.TABLE_NOTES, allColumns,
                null, null, null, null, null);
        cursor.moveToFirst();

        long id = 0;
        String titulo, contenido = "",coordenada = "";
        int color = 0;
        List<Imagen> imagenes = null;
        while (!cursor.isAfterLast()) {
            id = cursor.getLong(0);
            titulo = cursor.getString(1);
            contenido = cursor.getString(2);
            color = cursor.getInt(3);
            coordenada = cursor.getString(4);
            final Nota note = new Nota(titulo,contenido,color,id);
            note.setCoordenadas(coordenada);
            note.setImagenes(getImagesFromNote(id));
            noteList.add(note);
            cursor.moveToNext();
        }
        cursor.close();
        return noteList;
    }

    private List<Imagen> getImagesFromNote(Long note_id) {
        List<Imagen> imagesList = new ArrayList<Imagen>();
        Cursor c = database.rawQuery(" SELECT _id , name FROM " +MyDBHelper.TABLE_IMAGES+" WHERE note_id="+note_id,null);
        c.moveToFirst();

        long id = 0;
        String name = "";
        List<Imagen> imagenes = null;
        while (!c.isAfterLast()) {
            id = c.getLong(0);
            name = c.getString(1);
            final Imagen img = new Imagen(id, note_id, name,null);

            imagesList.add(img);
            c.moveToNext();
        }
        c.close();
        return imagesList;
    }

    public List<Nota> searchNotes(String text) {
        List<Nota> noteList = new ArrayList<Nota>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + MyDBHelper.TABLE_NOTES +
            " WHERE " + MyDBHelper.COLUMN_TITULO + " LIKE '%" + text + "%' OR " +
                MyDBHelper.COLUMN_CONTENIDO + " LIKE '%" + text + "%'", null);
        cursor.moveToFirst();

        long id = 0;
        String titulo, contenido = "",coordenada = "";
        int color = 0;
        List<Imagen> imagenes = null;
        while (!cursor.isAfterLast()) {
            id = cursor.getLong(0);
            titulo = cursor.getString(1);
            contenido = cursor.getString(2);
            color = cursor.getInt(3);
            coordenada = cursor.getString(4);
            final Nota note = new Nota(titulo,contenido,color,id);
            note.setCoordenadas(coordenada);
            note.setImagenes(getImagesFromNote(id));
            noteList.add(note);
            cursor.moveToNext();
        }
        cursor.close();
        return noteList;
    }

}
