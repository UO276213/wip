package com.example.wip.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.wip.data.records.ImagePartyRecord;

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
    private final String[] allColumns = {MyDBHelper.COLUMNA_ID_IMAGEN, MyDBHelper.COLUMNA_PATH, MyDBHelper.COLUMNA_IMG_TITLE};

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

    public long insertImage(String path, int fiestaId) {
        return insertImage(path, fiestaId, null);
    }

    /**
     * Recibe la imagen y crea el registro en la base de datos.
     *
     * @param path
     * @param fiestaId
     * @return
     */
    public long insertImage(String path, int fiestaId, String title) {
        // Establecemos los valores que se insertaran
        ContentValues values = new ContentValues();
        int id = new Random().nextInt();

        values.put(MyDBHelper.COLUMNA_ID_IMAGEN, id);
        values.put(MyDBHelper.COLUMNA_ID_PARTY, fiestaId);
        values.put(MyDBHelper.COLUMNA_PATH, path);
        values.put(MyDBHelper.COLUMNA_IMG_TITLE, title);

        // Insertamos la valoracion
        long insertId =
                database.insert(MyDBHelper.TABLA_IMAGENES_SUBIDAS, null, values);

        return insertId;
    }

    /**
     * Devuelve toda la tabla IMAGENES_SUBIDAS.
     */
    public List<ImagePartyRecord> getAllUploadedPartyImages() {
        // Lista que almacenara el resultado
        List<ImagePartyRecord> images = new ArrayList<>();
        //hacemos una query porque queremos devolver un cursor

        Cursor cursor = database.query(MyDBHelper.TABLA_IMAGENES_SUBIDAS, allColumns,
                null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(0);
            int idParty = cursor.getInt(1);
            String imagePath = cursor.getString(2);
            String title = null;
            if (!cursor.isNull(3))
                title = cursor.getString(3);
            images.add(new ImagePartyRecord(id, imagePath, idParty, title));
            cursor.moveToNext();
        }

        cursor.close();
        // Una vez obtenidos todos los datos y cerrado el cursor, devolvemos la
        // lista.

        return images;
    }

    /**
     * Devuelve lass filas que cumplan en filtro de IMAGENES_SUBIDAS.
     */
    public List<ImagePartyRecord> getFiltredUploadedPartyImages(int idImagen) {
        // Lista que almacenara el resultado
        List<ImagePartyRecord> images = new ArrayList<>();
        //hacemos una query porque queremos devolver un cursor

        Cursor cursor = database.rawQuery("Select * " +
                " FROM " + MyDBHelper.TABLA_IMAGENES_SUBIDAS +
                " WHERE " + MyDBHelper.TABLA_IMAGENES_SUBIDAS + "." + MyDBHelper.COLUMNA_ID_PARTY +
                " = " + idImagen + ";", null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(0);
            int idParty = cursor.getInt(1);
            String imagePath = cursor.getString(2);
            String title = null;
            if (!cursor.isNull(3))
                title = cursor.getString(3);
            images.add(new ImagePartyRecord(id, imagePath, idParty, title));
            cursor.moveToNext();
        }

        cursor.close();
        // Una vez obtenidos todos los datos y cerrado el cursor, devolvemos la
        // lista.

        return images;
    }

    public int deleteImage(String imagePath) {
        String[] argumentos = {imagePath};
        return database.delete(MyDBHelper.TABLA_IMAGENES_SUBIDAS, "image_path = ?", argumentos);
    }

    public int deleteImageParty(int id){
        String[] argumentos = {String.valueOf(id)};
        return database.delete(MyDBHelper.TABLA_IMAGENES_SUBIDAS, "id_imagen = ?", argumentos);
    }

    public int updateImageTitle(int idImg, String newTitle){
        String[] argumentos = {String.valueOf(idImg)};
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyDBHelper.COLUMNA_IMG_TITLE, newTitle );
        return database.update(MyDBHelper.TABLA_IMAGENES_SUBIDAS, contentValues, "id_imagen = ?", argumentos);
    }

}

