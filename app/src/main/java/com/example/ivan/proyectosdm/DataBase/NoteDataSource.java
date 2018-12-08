package com.example.ivan.proyectosdm.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.ivan.proyectosdm.Notas.Nota;

import java.util.ArrayList;
import java.util.List;

/**
 * Ejemplo <b>SQLite</b>. Ejemplo de uso de SQLite.
 *
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
     * Columnas de la tabla
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
     * Crea una nota, la inserta y devuelve su id
     */
    public long createNote(Nota note) {
        ContentValues values = new ContentValues();
        values.put(MyDBHelper.COLUMN_TITULO, note.getTitulo());
        values.put(MyDBHelper.COLUMN_CONTENIDO, note.getContenido());
        values.put(MyDBHelper.COLUMN_COLOR, note.getColor());

        long insertId = database.insert(MyDBHelper.TABLE_NOTES, null, values);

        return insertId;
    }
    
    /* 
     * Método para eliminar un elemento de la tabla de la base de datos
     *
     * @return devuelve un valor booleano indicando el éxito o no de la operación
     */
    public boolean deleteNote(long _indiceFila) {
        return database.delete(MyDBHelper.TABLE_NOTES, MyDBHelper.COLUMN_ID + "=" + _indiceFila, null) > 0;
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
        String titulo, contenido, color = "";
        while (!cursor.isAfterLast()) {
            id = cursor.getLong(0);
            titulo = cursor.getString(1);
            contenido = cursor.getString(2);
            color = cursor.getString(3);
            final Nota note = new Nota(titulo,contenido,color);

            noteList.add(note);
            cursor.moveToNext();
        }
        cursor.close();
        return noteList;
    }

}
