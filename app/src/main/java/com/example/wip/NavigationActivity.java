package com.example.wip;

import android.os.Bundle;
import android.view.Menu;

import com.example.wip.databinding.ActivityNavigationBinding;
import com.example.wip.layouts.CalendarFragment;
import com.example.wip.layouts.ListaFragments;
import com.example.wip.layouts.MapsFragment;
import com.example.wip.modelo.Fiesta;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;

public class NavigationActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityNavigationBinding binding;
    public static final String FIESTAS = "fiestas";
    private String lugar = "asturias";//cambiar esto en un futuro
    public ArrayList<Fiesta> fiestas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarNavigation.toolbar);
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

        BottomNavigationView nav = findViewById(R.id.bottom_navigation);
        nav.setOnItemSelectedListener(onItemSelectedListener);

    }

    private final NavigationBarView.OnItemSelectedListener onItemSelectedListener = item -> {
        switch (item.getItemId()) {
            case R.id.list_fragment:
                loadFragment(new ListaFragments());
                return true;
            case R.id.map_fragment:
                loadFragment(new MapsFragment());
                return true;
            case R.id.calendar_fragment:
                loadFragment(new CalendarFragment());
                return true;
        }
        return false;
    };

    private void loadFragment(Fragment fragment) {

        FragmentTransaction transaction =  getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment_content_navigation, fragment);
        transaction.commit();
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
}