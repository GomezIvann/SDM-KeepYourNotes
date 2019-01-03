package com.example.ivan.proyectosdm.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.ivan.proyectosdm.Notas.Imagen;
import com.example.ivan.proyectosdm.Notas.Nota;

import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la tabla de valoracion.
 * Se encarga de abrir y cerrar la conexion, asi como hacer las consultas relacionadas con la tabla valoracion
 *
 */
public class NoteDataSource {
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
            MyDBHelper.COLUMN_CONTENIDO, MyDBHelper.COLUMN_COLOR };
    
    /**
     * Constructor
     * @param context
     */
    public NoteDataSource(Context context) {
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
        Imagen img = null;
        values.put(MyDBHelper.COLUMN_TITULO, note.getTitulo());
        values.put(MyDBHelper.COLUMN_CONTENIDO, note.getContenido());
        values.put(MyDBHelper.COLUMN_COLOR, note.getColor());

        long insertId = database.insert(MyDBHelper.TABLE_NOTES, null, values);

        for( int i = 0; i < note.getNumImagenes(); i++ ) {
            img = note.getImagen(i);
            values = new ContentValues();
            values.put(MyDBHelper.COLUMN_ID_NOTA, insertId);
            values.put(MyDBHelper.COLUMN_IMG_NOMBRE, img.getNombre());
            database.insert(MyDBHelper.TABLE_IMAGES, null, values);
        }

        return insertId;
    }
    
    /* 
     * Método para eliminar una nota de la base de datos
     *
     * @return devuelve un valor booleano indicando el éxito o no de la operación
     */
    public void deleteNote(long _idFila) {
        database.delete(MyDBHelper.TABLE_IMAGES, MyDBHelper.COLUMN_ID_NOTA + "=" + _idFila, null);
        database.delete(MyDBHelper.TABLE_NOTES, MyDBHelper.COLUMN_ID + "=" + _idFila, null);
    }

    /**
     * Método que me actualiza una nota
     */
    public void updateNote(Nota note) {
        ContentValues values = new ContentValues();
        values.put(MyDBHelper.COLUMN_TITULO, note.getTitulo());
        values.put(MyDBHelper.COLUMN_CONTENIDO, note.getContenido());
        values.put(MyDBHelper.COLUMN_COLOR, note.getColor());

        database.update(MyDBHelper.TABLE_NOTES, values, MyDBHelper.COLUMN_ID+"="+note.getId(), null);
    }

    /**
     * Método que me actualiza una imagen
     */
    public void updateImage(Imagen img) {
        ContentValues values = new ContentValues();
        values.put(MyDBHelper.COLUMN_ID_NOTA, img.getNota_id());
        values.put(MyDBHelper.COLUMN_IMG_NOMBRE, img.getNombre());

        database.update(MyDBHelper.TABLE_IMAGES, values, MyDBHelper.COLUMN_ID+"="+img.getId(), null);
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
        String titulo, contenido = "";
        int color = 0;
        List<Imagen> imagenes = null;
        while (!cursor.isAfterLast()) {
            id = cursor.getLong(0);
            titulo = cursor.getString(1);
            contenido = cursor.getString(2);
            color = cursor.getInt(3);
            final Nota note = new Nota(titulo,contenido,color,id);

            note.setImagenes(getImagesFromNote(note.getId()));

            noteList.add(note);
            cursor.moveToNext();
        }
        cursor.close();
        return noteList;
    }

    private List<Imagen> getImagesFromNote(Long note_id) {
        List<Imagen> imagesList = new ArrayList<Imagen>();
        Cursor c = database.rawQuery(" SELECT _id, name FROM Images WHERE _id="+note_id,null);
        c.moveToFirst();

        long id = 0;
        String name = "";
        int color = 0;
        List<Imagen> imagenes = null;
        while (!c.isAfterLast()) {
            id = c.getLong(0);
            name = c.getString(1);
            final Imagen img = new Imagen(id, note_id, name);

            imagesList.add(img);
            c.moveToNext();
        }
        c.close();
        return imagesList;
    }

}
