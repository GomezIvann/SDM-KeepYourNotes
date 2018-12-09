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
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITULO = "title";
    public static final String COLUMN_CONTENIDO = "content";
    public static final String COLUMN_COLOR = "color";



    /**
     * Sentencia de creacion de la tabla notas
     */
    private static final String DATABASE_CREATE = "create table " + TABLE_NOTES
            + "( " + COLUMN_ID + " " +
            "integer primary key autoincrement, " + COLUMN_TITULO
            + " text not null, " + COLUMN_CONTENIDO + " text not null, " + COLUMN_COLOR +
            " integer not null" + ");";

    /**
     * Sentencia para borrar la tabla notes
     */
    private static final String DATABASE_DROP = "DROP TABLE IF EXISTS " + TABLE_NOTES;

    public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                      int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DATABASE_DROP);
        this.onCreate(db);
    }
}
