package com.example.wip.ui.layouts.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wip.R;
import com.example.wip.ui.layouts.activities.DetailsActivity;
import com.example.wip.modelo.Fiesta;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends Fragment implements
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final String ARG_FIESTAS = "arg_fiestas";

    private GoogleMap mMap;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            mMap.setOnMarkerClickListener(marker -> {

                Fiesta fiesta = (Fiesta) marker.getTag();

                Intent itent = new Intent(getContext(), DetailsActivity.class);
                itent.putExtra(ARG_FIESTAS, fiesta.getDetails());
                itent.putExtra(ListaFragments.FIESTA_SELECCIONADA, fiesta);
                startActivity(itent);

                return false;
            });

            // Intentamos localizar al usuario
            enableMyLocation();

            // Obtenemos las fiestas para mostrar marcadores en el MAPS
            addPartyMarkers();
        }
    };
    private ArrayList<Fiesta> fiestas;

    public MapsFragment() {
    }

    /**
     * @return A new instance of fragment MapsFragment.
     */
    public static MapsFragment newInstance(ArrayList<Fiesta> fiestas) {
        MapsFragment fragment = new MapsFragment();
        Bundle args = new Bundle();
        if (fiestas == null) fiestas = new ArrayList<>();
        args.putParcelableArrayList(ARG_FIESTAS, fiestas);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fiestas = getArguments().getParcelableArrayList(ARG_FIESTAS);
        }
    }

    private void addPartyMarkers() {

        // Servicio que nos permite traducir nombre de ubicaciones a coordenadas
        Geocoder geocoder = new Geocoder(getActivity().getApplicationContext());
        try {
            // Recorremos todas las fiestas para incluirlas con un marcador en el mapa
            for (Fiesta fiesta : fiestas) {
                List<Address> fiestaAddress = geocoder.getFromLocationName(fiesta.getPlace(), 1);

                if (!fiestaAddress.isEmpty()) {
                    LatLng coords = new LatLng(fiestaAddress.get(0).getLatitude(), fiestaAddress.get(0).getLongitude());


                    Marker marker = mMap.addMarker(new MarkerOptions().position(coords).title(fiesta.getName())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.party_marker_icon)));

                    marker.setTag(fiesta);

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
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
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