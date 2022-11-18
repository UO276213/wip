package com.example.wip.layouts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.wip.R;
import com.example.wip.utils.ParserFiestas;
import com.google.android.material.snackbar.Snackbar;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

public class DetailsActivity extends AppCompatActivity {

    private String urlDetails;
    private String details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent = getIntent();
        urlDetails=intent.getStringExtra(ListaFragments.ARG_FIESTAS);
        getDetails();
    }

    private void getDetails() {
        try {
            //Conseguimos el HTML con la librería "Ion"
            String url = "https://fiestas.net/" + urlDetails;
            Ion.with(getApplicationContext()).load(url).asString().withResponse().setCallback(new FutureCallback<Response<String>>() {
                @Override
                public void onCompleted(Exception e, Response<String> result) {
                    try {
                        // Una vez conseguido el html, lo parseamos para conseguir un array de fiestas
                        String resultado = result.getResult();
                        details = ParserFiestas.ParseDetails(resultado);
                        setDetails(details);
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

    private void setDetails(String details) {
        TextView detailsView= findViewById(R.id.details);
        detailsView.setText(details);
    }

}