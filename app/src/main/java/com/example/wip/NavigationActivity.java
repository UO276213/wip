package com.example.wip;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;

import com.example.wip.databinding.ActivityNavigationBinding;
import com.example.wip.databinding.FragmentGalleryBinding;
import com.example.wip.databinding.FragmentHomeBinding;
import com.example.wip.layouts.CalendarFragment;
import com.example.wip.layouts.ListaFragments;
import com.example.wip.layouts.MainActivity;
import com.example.wip.layouts.MapsFragment;
import com.example.wip.modelo.Fiesta;
import com.example.wip.ui.gallery.GalleryFragment;
import com.example.wip.utils.ParserFiestas;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.util.ArrayList;

public class NavigationActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityNavigationBinding binding;
    public static final String FIESTAS = "fiestas";
    private String lugar = "asturias";//cambiar esto en un futuro
    public static ArrayList<Fiesta> fiestas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarNavigation.toolbar);
        binding.appBarNavigation.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.memories)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        /*BottomNavigationView nav = findViewById(R.id.bottom_navigation);
        nav.setOnItemSelectedListener(onItemSelectedListener);*/

        Intent intent = getIntent();
        lugar = intent.getStringExtra(MainActivity.COMUNIDAD);
        getData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
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
                        //loadFragment(ListaFragments.newInstance(fiestas));//Pantalla por defecto

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

    /*private final NavigationBarView.OnItemSelectedListener onItemSelectedListener = item -> {
            switch (item.getItemId()) {
                case R.id.list_fragment:
                    loadFragment(ListaFragments.newInstance(fiestas));
                    return true;
                case R.id.map_fragment:
                    loadFragment(MapsFragment.newInstance(fiestas));
                    return true;
                case R.id.calendar_fragment:
                    loadFragment(CalendarFragment.newInstance(fiestas));
                    return true;
            }


        return false;
    };

    private void loadActivity(Class<?> activityClass) {
        Intent itent = new Intent(NavigationActivity.this, activityClass);
        itent.putParcelableArrayListExtra(FIESTAS, fiestas);
        startActivity(itent);
    }

    private void loadFragment(Fragment fragment){

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment_content_navigation, fragment);
        transaction.commit();
    }*/
}