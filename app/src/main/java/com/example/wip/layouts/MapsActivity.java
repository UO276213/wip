package com.example.wip.layouts;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
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

public class MapsActivity extends FragmentActivity implements
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private GoogleMap mMap;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in {@link
     * #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean permissionDenied = false;

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

        // Intentamos localizar al usuario
        enableMyLocation();


        // Obtenemos las fiestas para mostrar marcadores en el MAPS
        addPartyMarkers();
    }

    private void addPartyMarkers() {
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

    /**
     * En caso de tener permisos de ubicación , mostraremos la ubicación del usuario.
     * Si no tenemos permiso se solicita
     */
    @SuppressLint("MissingPermission")
    private void enableMyLocation() {
        // Comprobamos si tenemos permisos de ubicación
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            // Si los tenemos, activamos el botón de centrar la ubicación actual
            mMap.setOnMyLocationButtonClickListener(this);
            mMap.setOnMyLocationClickListener(this);
            mMap.setMyLocationEnabled(true);
        } else {
            // Solicitamos permisos
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        // Recorremos los permisos para comprobar si podemos geolocalizar al usuario
        for (int i = 0; i < permissions.length; i++) {
            if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[i] == PackageManager.PERMISSION_GRANTED ||
                    permissions[i].equals(Manifest.permission.ACCESS_COARSE_LOCATION) &&
                            grantResults[i] == PackageManager.PERMISSION_GRANTED)
                enableMyLocation();

        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }
}