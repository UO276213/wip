package com.example.wip.layouts;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.example.wip.R;
import com.example.wip.modelo.Fiesta;

import java.util.ArrayList;

public class ListaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        addToLayout();
    }

    //Recorre un array de fiestas y las va añadiendo al layout
    private void addToLayout() {
        //Este codigo será igual en las diferentes pantallas, recogemos el array de fiestas
        Intent intent = getIntent();
        ArrayList<Fiesta> fiestas = intent.getParcelableArrayListExtra(MainActivity.FIESTAS);
        LinearLayout layout = findViewById(R.id.layoutLista);
        for (Fiesta fiesta : fiestas) {
            //Aquí ya es diferente, depende de lo que queramos añadir
            TextView texto = new TextView(getApplicationContext());
            texto.setText(fiesta.toString());
            layout.addView(texto);
        }
    }
}