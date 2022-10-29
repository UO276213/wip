package com.example.wip.layouts;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.example.wip.R;
import com.example.wip.modelo.Fiesta;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

//        binding = ActivityMapsBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Movemos la cámara a Oviedo
        // TODO: Obtener las coordenadas por GPS
        LatLng oviedo = new LatLng(43, -5);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(oviedo));

        // Obtenemos las fiestas para mostrar marcadores en el MAPS
        Intent intent = getIntent();
        ArrayList<Fiesta> fiestas = intent.getParcelableArrayListExtra(MainActivity.FIESTAS);

        // Servicio que nos permite traducir nombre de ubicaciones a coordenadas
        Geocoder geocoder = new Geocoder(getApplicationContext());
        try {
            // Recorremos todas las fiestas para incluirlas con un marcador en el mapa
            for (Fiesta fiesta : fiestas) {
                List<Address> fiestaAddress = geocoder.getFromLocationName(fiesta.getPlace(), 1);

                if (!fiestaAddress.isEmpty()) {
                    LatLng coords = new LatLng(fiestaAddress.get(0).getLatitude(), fiestaAddress.get(0).getLongitude());

                    mMap.addMarker(new MarkerOptions().position(coords).title(fiesta.getName()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(coords));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}