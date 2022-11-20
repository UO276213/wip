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
    public static final String COLUMNA_URL_UBI_FIESTA = "url_ubi_fiesta";
    public static final String COLUMNA_DETAILS_FIESTA = "details_fiesta";


    /**
     * Nombre de la tabla películas_reparto y sus columnas
     * id_reparto;id_pelicula; (estas son Foreign keys); Personaje
     */
    public static final String TABLA_PELICULAS_REPARTO = "tabla_peliculas_reparto";

    public static final String COLUMNA_PERSONAJE = "nombre_personaje";

    /**
     * Nombre de la tabla reparto y sus columnas
     * id;nombre;imagen;URL_imdb
     */
    public static final String TABLA_REPARTO = "tabla_reparto";

    public static final String COLUMNA_ID_REPARTO = "id_reparto";
    public static final String COLUMNA_NOMBRE_ACTOR = "nombre_actor";
    public static final String COLUMNA_IMAGEN_ACTOR = "URL_imagen_actor";
    public static final String COLUMNA_URL_imdb = "URL_imdb_actor";


    /**
     * Script para crear la base datos en SQL
     */
    private static final String CREATE_TABLA_PELICULAS = "create table if not exists " + TABLA_FIESTAS
            + "( " +
            COLUMNA_ID_FIESTAS + " " + "integer primary key, " +
            COLUMNA_NOMBRE_FIESTA + " text not null, " +
            COLUMNA_FECHA_FIESTA + " text," +
            COLUMNA_UBI_FIESTA + " text," +
            COLUMNA_URL_UBI_FIESTA + " text," +
            COLUMNA_DETAILS_FIESTA + " text" +
            ");";

    /**
     * Script para borrar la base de datos (SQL)
     */
    private static final String DATABASE_DROP_PELICULAS = "DROP TABLE IF EXISTS " + TABLA_FIESTAS;


    public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                      int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //invocamos execSQL pq no devuelve ningún tipo de dataset
        db.execSQL(CREATE_TABLA_PELICULAS);


        Log.i("ONCREATE", "EJECUTO CREACION");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DATABASE_DROP_PELICULAS);
        this.onCreate(db);

    }
}