package com.example.wip.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.wip.modelo.Fiesta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class FiestasDataSource {

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
    private final String[] allColumns = {MyDBHelper.COLUMNA_ID_FIESTAS, MyDBHelper.COLUMNA_NOMBRE_FIESTA,
            MyDBHelper.COLUMNA_FECHA_FIESTA, MyDBHelper.COLUMNA_UBI_FIESTA, MyDBHelper.COLUMNA_URL_UBI_FIESTA, MyDBHelper.COLUMNA_DETAILS_FIESTA};

    /**
     * Constructor.
     *
     * @param context
     */
    public FiestasDataSource(Context context) {
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
     * Recibe la película y crea el registro en la base de datos.
     * @param partyToInsert
     * @return
     */
    public long insertFiesta(Fiesta partyToInsert) {
        // Establecemos los valores que se insertaran
        ContentValues values = new ContentValues();
        int id = new Random().nextInt();

        values.put(MyDBHelper.COLUMNA_ID_FIESTAS, id);
        values.put(MyDBHelper.COLUMNA_NOMBRE_FIESTA, partyToInsert.getName());
        values.put(MyDBHelper.COLUMNA_FECHA_FIESTA, partyToInsert.getDate());
        values.put(MyDBHelper.COLUMNA_UBI_FIESTA, partyToInsert.getPlace());
        values.put(MyDBHelper.COLUMNA_URL_UBI_FIESTA, partyToInsert.getTownURL());
        values.put(MyDBHelper.COLUMNA_DETAILS_FIESTA, partyToInsert.getDetails());

        // Insertamos la valoracion
        long insertId =
                database.insert(MyDBHelper.TABLA_FIESTAS, null, values);

        return insertId;
    }

    /**
     * Obtiene todas las valoraciones andadidas por los usuarios.
     *
     * @return Lista de objetos de tipo Pelicula
     */
    public List<Fiesta> getAllValorations() {
        // Lista que almacenara el resultado
        List<Fiesta> partyList = new ArrayList<Fiesta>();
        //hacemos una query porque queremos devolver un cursor

        Cursor cursor = database.query(MyDBHelper.TABLA_FIESTAS, allColumns,
                null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            final Fiesta fiesta = new Fiesta();
            cursor.getInt(0);
            fiesta.setId(cursor.getInt(0));
            fiesta.setName(cursor.getString(1));
            fiesta.setDate(cursor.getString(2));
            fiesta.setPlace(cursor.getString(3));
            fiesta.setTownURL(cursor.getString(4));
            fiesta.setDetails(cursor.getString(5));

            partyList.add(fiesta);
            cursor.moveToNext();
        }

        cursor.close();
        // Una vez obtenidos todos los datos y cerrado el cursor, devolvemos la
        // lista.

        return partyList;
    }


    /**
     * Obtiene todas las valoraciones andadidas por los usuarios con el filtro introducido en el SQL.
     *
     * @return Lista de objetos de tipo Pelicula
     */
   /* public List<Pelicula> getFilteredValorations(String filtro) {
        // Lista que almacenara el resultado
        List<Pelicula> peliculaList = new ArrayList<Pelicula>();
        //hacemos una query porque queremos devolver un cursor

        Cursor cursor = database.rawQuery("Select * " +
                " FROM " + MyDBHelper.TABLA_PELICULAS +
                " WHERE " + MyDBHelper.TABLA_PELICULAS + "." + MyDBHelper.COLUMNA_CATEGORIA_PELICULAS + " = \"" + filtro + "\"", null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            final Pelicula pelicula = new Pelicula();
            pelicula.setId(cursor.getInt(0));
            pelicula.setTitulo(cursor.getString(1));
            pelicula.setArgumento(cursor.getString(2));
            pelicula.setCategoria(new Categoria(cursor.getString(3), ""));
            pelicula.setDuracion(cursor.getString(4));
            pelicula.setFecha(cursor.getString(5));
            pelicula.setUrlCaratula("https://image.tmdb.org/t/p/original/" + cursor.getString(6));
            pelicula.setUrlFondo("https://image.tmdb.org/t/p/original/" + cursor.getString(7));
            pelicula.setUrlTrailer("https://youtu.be/" + cursor.getString(8));


            peliculaList.add(pelicula);
            cursor.moveToNext();
        }

        cursor.close();
        // Una vez obtenidos todos los datos y cerrado el cursor, devolvemos la
        // lista.

        return peliculaList;
    }*/

    public int deleteParty(Fiesta fiesta) {
        String[] argumentos = {String.valueOf(fiesta.getId())};
        return database.delete(MyDBHelper.TABLA_FIESTAS, "id_fiesta = ?", argumentos);
    }

}

