package com.example.wip.layouts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wip.R;
import com.example.wip.modelo.Fiesta;
import com.example.wip.utils.ParserFiestas;
import com.google.android.material.snackbar.Snackbar;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String FIESTAS = "fiestas";
    private String lugar = "asturias";//cambiar esto en un futuro

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    //Action del botón
    //Recoge el html de la página solicitada y  llama a un nuevo layout pasandole las fiestas
    public void getData(View buttom) {
        try {
            //Conseguimos el HTML con la librería "Ion"
            String url = "https://fiestas.net/" + lugar + "/";
            Ion.with(getApplicationContext()).load(url).asString().withResponse().setCallback(new FutureCallback<Response<String>>() {
                @Override
                public void onCompleted(Exception e, Response<String> result) {
                    try {
                        // Una vez conseguido el html, lo parseamos para conseguir un array de fiestas
                        String resultado = result.getResult();
                        ArrayList<Fiesta> fiestas = ParserFiestas.ParseFiestas(resultado);
                        //Cargamos el nuevo layout
                        getFiestasLayout(fiestas, buttom);
                    } catch (Exception ex) {
                        Snackbar.make(findViewById(R.id.layoutMain), R.string.error, Snackbar.LENGTH_LONG).show();
                        ex.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            Snackbar.make(findViewById(R.id.layoutMain), R.string.error, Snackbar.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    //Carga el layout, pasandole el array de las fiestas
    private void getFiestasLayout(ArrayList<Fiesta> fiestas, View buttom) {
        Class<?> tipoLayout = null;
        //Depende del botón, se invocará a un layout diferente
        //TODO añadir aqui el mapa y el calendario
        switch (buttom.getId()) {
            case R.id.buttonLista:
                tipoLayout = ListaActivity.class;
                break;
            case R.id.buttonMaps:
                tipoLayout = MapsActivity.class;
                break;
        }
        try {
            //Le pasamos el array al layout y lo invocamos
            Intent itent = new Intent(MainActivity.this, tipoLayout);
            itent.putParcelableArrayListExtra(FIESTAS, fiestas);
            startActivity(itent);

        } catch (Exception e) {
            Snackbar.make(findViewById(R.id.layoutMain), R.string.error, Snackbar.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


}