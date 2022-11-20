package com.example.wip.layouts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.wip.NavigationActivity;
import com.example.wip.R;
import com.example.wip.utils.ParserLugares;
import com.google.android.material.snackbar.Snackbar;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String comunidad="";
    public static final String COMUNIDAD = "comunidad";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cargarFietas();
        loadData();
        loadButton();
    }

    /**
     * Carga todas las comunidades españolas
     */
    private void loadData() {
        try {
            //Conseguimos el HTML con la librería "Ion"
            String url = "https://fiestas.net/";
            Ion.with(getApplicationContext()).load(url).asString().withResponse().setCallback(new FutureCallback<Response<String>>() {
                @Override
                public void onCompleted(Exception e, Response<String> result) {
                    try {
                        // Una vez conseguido el html, lo parseamos para conseguir un array de fiestas
                        String resultado = result.getResult();
                        ArrayList<String>[] returned = ParserLugares.parseComunidades(resultado);
                        loadSpinner(returned);

                    } catch (Exception ex) {
                        Snackbar.make(findViewById(R.id.layoutMain), R.string.error, Snackbar.LENGTH_LONG).show();
                        ex.printStackTrace();
                    }
                }
            });} catch (Exception e) {
            Snackbar.make(findViewById(R.id.layoutMain), R.string.error, Snackbar.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


    private void cargarFietas() {
        // Si ya hay una comunidad selecciona, se carga directamente
        SharedPreferences settings = getApplicationContext().getSharedPreferences(COMUNIDAD, 0);
        comunidad = settings.getString(COMUNIDAD,"");
        if(!comunidad.equals("")){
            buscarFiestas();
        }
    }

    private void loadButton() {
        Button button = findViewById(R.id.search);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscarFiestas();
            }
        });
    }

    private void buscarFiestas() {
        Intent itent = new Intent(MainActivity.this, NavigationActivity.class);
        itent.putExtra(COMUNIDAD, comunidad);
        startActivity(itent);
    }

    private void loadSpinner(ArrayList<String>[] resultado) {
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> SpinerAdapter;
        //String[] arrayItems = getResources().getStringArray(R.array.comunidades);
        //String[] actualValues=getResources().getStringArray(R.array.comunidades_valores);
        ArrayList<String> arrayItems=resultado[0];
        ArrayList<String> actualValues=resultado[1];
        comunidad= actualValues.get(0);

        SpinerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, arrayItems);
        spinner.setAdapter(SpinerAdapter);

        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                comunidad= actualValues.get(arg2);
                //Guardamos en memoria
                SharedPreferences settings = getApplicationContext().getSharedPreferences(COMUNIDAD, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString(COMUNIDAD, comunidad);
                editor.apply();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
    }
}