package com.example.wip.layouts;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.wip.R;
import com.example.wip.modelo.Fiesta;
import com.example.wip.utils.ParserFiestas;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String FIESTAS = "fiestas";
    private String lugar = "asturias";//cambiar esto en un futuro
    private ArrayList<Fiesta> fiestas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnItemSelectedListener(onItemSelectedListener);

        getData();
    }

    //Recoge el html de la página solicitada y  llama a un nuevo layout pasandole las fiestas
    public void getData() {
        try {
            //Conseguimos el HTML con la librería "Ion"
            String url = "https://fiestas.net/" + lugar + "/";
            Ion.with(getApplicationContext()).load(url).asString().withResponse().setCallback(new FutureCallback<Response<String>>() {
                @Override
                public void onCompleted(Exception e, Response<String> result) {
                    try {
                        // Una vez conseguido el html, lo parseamos para conseguir un array de fiestas
                        String resultado = result.getResult();
                        fiestas = ParserFiestas.ParseFiestas(resultado);
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

    private final NavigationBarView.OnItemSelectedListener onItemSelectedListener = item -> {
        switch (item.getItemId()) {
            case R.id.list_fragment:
                loadFragment(ListaFragments.newInstance(fiestas));
                return true;
            case R.id.map_fragment:
                loadFragment(MapsFragment.newInstance(fiestas));
                return true;
            case R.id.calendar_fragment:
                loadActivity(CalendarActivity.class);
                return true;
        }
        return false;
    };

    private void loadActivity(Class<?> activityClass) {
        Intent itent = new Intent(MainActivity.this, activityClass);
        itent.putParcelableArrayListExtra(FIESTAS, fiestas);
        startActivity(itent);
    }

    private void loadFragment(Fragment fragment){

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }

}