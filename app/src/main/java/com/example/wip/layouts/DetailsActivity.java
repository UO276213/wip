package com.example.wip.layouts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wip.R;
import com.example.wip.data.FiestasDataSource;
import com.example.wip.modelo.Fiesta;
import com.example.wip.utils.ParserFiestas;
import com.google.android.material.snackbar.Snackbar;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.util.Random;

public class DetailsActivity extends AppCompatActivity {

    private String urlDetails;
    private String details;
    private boolean isFavorite;
    private ImageButton btnFavorite;
    private Fiesta fiesta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent = getIntent();
        urlDetails=intent.getStringExtra(ListaFragments.ARG_FIESTAS);
        getDetails();
        fiesta= intent.getParcelableExtra(ListaFragments.FIESTA_SELECCIONADA);
        Log.d("FIESTA", fiesta.toString());
        btnFavorite = findViewById(R.id.btnFav);
        isFavorite = fiesta.isFavorite();
        isFavorite();
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeItFav();
            }
        });
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

    private void isFavorite(){
        if (isFavorite){
            btnFavorite.setImageDrawable((ContextCompat.getDrawable(this,R.drawable.ic_baseline_favorite_24)));
        }else{
            btnFavorite.setImageDrawable((ContextCompat.getDrawable(this,R.drawable.ic_baseline_favorite_border_24)));
        }
    }

    private void makeItFav(){
        FiestasDataSource fsd= new FiestasDataSource(getApplicationContext());
        fsd.open();
        if(!isFavorite){
            isFavorite=true;
            fiesta.setFavorite(true);
            fsd.insertFiesta(fiesta);
            Toast.makeText(this, "This is one of you favourite partys",Toast.LENGTH_LONG);
        }else{
            isFavorite=false;
            fiesta.setFavorite(false);
            fsd.deleteParty(fiesta);
            Toast.makeText(this, "Sorry you don´t have fun at this party",Toast.LENGTH_LONG);
        }
        fsd.close();
        isFavorite();
    }




}