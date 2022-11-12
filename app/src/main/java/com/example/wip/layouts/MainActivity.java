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

import com.example.wip.R;

public class MainActivity extends AppCompatActivity {

    private String comunidad="";
    public static final String COMUNIDAD = "comunidad";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cargarFietas();
        loadSpinner();
        loadButtons();
    }

    private void cargarFietas() {
        // Si ya hay una comunidad selecciona, se carga directamente
        SharedPreferences settings = getApplicationContext().getSharedPreferences(COMUNIDAD, 0);
        comunidad = settings.getString(COMUNIDAD,"");
        if(!comunidad.equals("")){
            buscarFiestas();
        }
    }

    private void loadButtons() {
        Button searchBtn = findViewById(R.id.search);
        searchBtn.setOnClickListener(view -> buscarFiestas());

        Button loadFromGalleryBtn = findViewById(R.id.buttonLoadPicture);
        loadFromGalleryBtn.setOnClickListener(view -> {
            Intent itent = new Intent(MainActivity.this, ImageGalleryActivity.class);
            startActivity(itent);
        });

    }

    private void buscarFiestas() {
        Intent itent = new Intent(MainActivity.this, FragmentActivity.class);
        itent.putExtra(COMUNIDAD, comunidad);
        startActivity(itent);
    }

    private void loadSpinner() {
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> SpinerAdapter;
        String[] arrayItems = getResources().getStringArray(R.array.comunidades);
        String[] actualValues=getResources().getStringArray(R.array.comunidades_valores);
        comunidad=actualValues[0];

        SpinerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, arrayItems);
        spinner.setAdapter(SpinerAdapter);

        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                comunidad=actualValues[ arg2];
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