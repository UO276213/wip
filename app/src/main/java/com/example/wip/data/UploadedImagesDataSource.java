package com.example.wip.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class UploadedImagesDataSource {

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
    private final String[] allColumns = {MyDBHelper.COLUMNA_ID_IMAGEN, MyDBHelper.COLUMNA_PATH};

    /**
     * Constructor.
     *
     * @param context
     */
    public UploadedImagesDataSource(Context context) {
        //el último parámetro es la versión
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

    /**
     * Recibe la imagen y crea el registro en la base de datos.
     *
     * @param path
     * @param fiestaId
     * @return
     */
    public long insertImage(String path, int fiestaId) {
        // Establecemos los valores que se insertaran
        ContentValues values = new ContentValues();
        int id = new Random().nextInt();

        values.put(MyDBHelper.COLUMNA_ID_IMAGEN, id);
        values.put(MyDBHelper.COLUMNA_ID_PARTY, fiestaId);
        values.put(MyDBHelper.COLUMNA_PATH, path);

        // Insertamos la valoracion
        long insertId =
                database.insert(MyDBHelper.TABLA_IMAGENES_SUBIDAS, null, values);

        return insertId;
    }

    /**
     * Obtiene todas las valoraciones añdadidas por los usuarios.
     *
     * @return L
     */
    public List<String> getAllValorations() {
        // Lista que almacenara el resultado
        List<String> images = new ArrayList<>();
        //hacemos una query porque queremos devolver un cursor

        Cursor cursor = database.query(MyDBHelper.TABLA_IMAGENES_SUBIDAS, allColumns,
                null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            images.add(cursor.getString(1));
            cursor.moveToNext();
        }

        cursor.close();
        // Una vez obtenidos todos los datos y cerrado el cursor, devolvemos la
        // lista.

        return images;
    }

    public List<String> getFilteredValorations(int idImagen) {
        // Lista que almacenara el resultado
        List<String> resultado = new ArrayList<>();
        //hacemos una query porque queremos devolver un cursor

        Cursor cursor = database.rawQuery("Select * " +
                " FROM " + MyDBHelper.TABLA_IMAGENES_SUBIDAS +
                " WHERE " + MyDBHelper.TABLA_IMAGENES_SUBIDAS + "." + MyDBHelper.COLUMNA_ID_PARTY + " = " + idImagen + ";", null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            resultado.add(cursor.getString(2));
            cursor.moveToNext();
        }

        cursor.close();
        // Una vez obtenidos todos los datos y cerrado el cursor, devolvemos la
        // lista.

        return resultado;
    }

    public int deleteImage(String imagePath) {
        String[] argumentos = {imagePath};
        return database.delete(MyDBHelper.TABLA_IMAGENES_SUBIDAS, "image_path = ?", argumentos);
    }

}

