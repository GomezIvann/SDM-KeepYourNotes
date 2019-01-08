package com.example.ivan.proyectosdm.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * MyDHelper
 */
public class MyDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "notes.db";
    private static final int DATABASE_VERSION = 1;
    
    public static final String TABLE_NOTES = "notes";
    public static final String TABLE_IMAGES = "images";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITULO = "title";
    public static final String COLUMN_CONTENIDO = "content";
    public static final String COLUMN_COLOR = "color";
    public static final String COLUMN_ID_NOTA = "note_id";
    public static final String COLUMN_IMG_NOMBRE = "name";
    public static final String COLUMN_COORDENADA = "coord";



    /**
     * Sentencia de creacion de la tabla de las notas
     */
    private static final String DATABASE_CREATE_NOTES = "create table " + TABLE_NOTES
            + "( " + COLUMN_ID + " " + "integer primary key autoincrement, "
            + COLUMN_TITULO + " text not null, "
            + COLUMN_CONTENIDO + " text not null, "
            + COLUMN_COLOR + " integer not null, "
            + COLUMN_COORDENADA +" text not null );";

    /**
     * Sentencia de creacion de la tabla de las imagenes
     */
    private static final String DATABASE_CREATE_IMAGES = "create table " + TABLE_IMAGES
            + "( " + COLUMN_ID + " " +
            "integer primary key autoincrement, " + COLUMN_ID_NOTA
            + " integer not null, " + COLUMN_IMG_NOMBRE + " text not null );";

    /**
    /*
     * Sentencia para borrar las tablas
     */
    private static final String DATABASE_DROP_NOTES = "DROP TABLE IF EXISTS " + TABLE_NOTES;
    private static final String DATABASE_DROP_IMAGES = "DROP TABLE IF EXISTS " + TABLE_IMAGES;


    public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                      int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_NOTES);
        db.execSQL(DATABASE_CREATE_IMAGES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DATABASE_DROP_NOTES);
        db.execSQL(DATABASE_DROP_IMAGES);
        this.onCreate(db);
    }
}
