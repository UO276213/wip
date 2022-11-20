package com.example.wip.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDBHelper extends SQLiteOpenHelper {

    /**
     * Nombre y version de la base de datos
     */
    private static final String DATABASE_NAME = "fiestas.db";
    private static final int DATABASE_VERSION = 1;

    /**
     * Nombre de la tabla fiestas y sus columnas
     * id;name;date;place
     */
    public static final String TABLA_FIESTAS = "tabla_fiestas";

    public static final String COLUMNA_ID_FIESTAS = "id_fiesta";
    public static final String COLUMNA_NOMBRE_FIESTA = "name_fiesta";
    public static final String COLUMNA_FECHA_FIESTA = "date_fiesta";
    public static final String COLUMNA_UBI_FIESTA = "ubi_fiesta";

    public static final String TABLA_IMAGENES_SUBIDAS = "tabla_imagenes_subidas";

    public static final String COLUMNA_ID_IMAGEN = "id_imagen";
    public static final String COLUMNA_PATH = "image_path";
    public static final String COLUMNA_ID_PARTY = "id_fiesta";



    /**
     * Script para crear la base datos en SQL
     */
    private static final String CREATE_TABLA_PELICULAS = "create table if not exists " + TABLA_FIESTAS
            + "( " +
            COLUMNA_ID_FIESTAS + " " + "integer primary key, " +
            COLUMNA_NOMBRE_FIESTA + " text not null, " +
            COLUMNA_FECHA_FIESTA + " text," +
            COLUMNA_UBI_FIESTA + " text" +
            ");";

    /**
     * Script para crear la base datos en SQL
     */
    private static final String CREATE_TABLA_IMAGENES_SUBIDAS = "create table if not exists " + TABLA_IMAGENES_SUBIDAS
            + "( " +
            COLUMNA_ID_IMAGEN + " " + "integer primary key, " +
            COLUMNA_ID_PARTY + " integer, " +
            COLUMNA_PATH + " text not null " +
            ");";

    /**
     * Script para borrar la base de datos (SQL)
     */
    private static final String DATABASE_DROP_PELICULAS = "DROP TABLE IF EXISTS " + TABLA_FIESTAS;
    private static final String DATABASE_DROP_IMAGENES_SUBIDAS = "DROP TABLE IF EXISTS " + TABLA_IMAGENES_SUBIDAS;


    public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                      int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //invocamos execSQL pq no devuelve ningún tipo de dataset
        db.execSQL(CREATE_TABLA_PELICULAS);
        db.execSQL(CREATE_TABLA_IMAGENES_SUBIDAS);


        Log.i("ONCREATE", "EJECUTO CREACION");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DATABASE_DROP_PELICULAS);
        db.execSQL(DATABASE_DROP_IMAGENES_SUBIDAS);
        this.onCreate(db);
    }
}