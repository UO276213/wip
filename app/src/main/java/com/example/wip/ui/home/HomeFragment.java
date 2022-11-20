package com.example.wip.ui.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.wip.NavigationActivity;
import com.example.wip.R;
import com.example.wip.databinding.FragmentHomeBinding;
import com.example.wip.layouts.CalendarFragment;
import com.example.wip.layouts.ListaFragments;
import com.example.wip.layouts.MapsFragment;
import com.example.wip.modelo.Fiesta;
import com.example.wip.utils.ParserFiestas;
import com.example.wip.utils.ParserLugares;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private View root;

    private static final String ARG_FIESTAS = "arg_fiestas";
    private ArrayList<Fiesta> fiestas = new ArrayList<>();

    private String comunidad = "";
    public static final String COMUNIDAD = "comunidad";
    NavigationActivity navigationActivity;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_home, container, false);
        binding = FragmentHomeBinding.inflate(inflater, container, false);


        // TODO Esto hay que modificarlo,
        //  porque el bottom_navigation tiuene que estar en varios fragmentos.

        navigationActivity = (NavigationActivity) getActivity();


//        getActivity().findViewById(R.id.bottom_navigation)

//        cargarFietas();
        loadData();
        loadButton();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

//    private void cargarFietas() {
//        // Si ya hay una comunidad selecciona, se carga directamente
//        SharedPreferences settings = getContext().getSharedPreferences(COMUNIDAD, 0);
//        comunidad = settings.getString(COMUNIDAD, "");
//        if (!comunidad.equals("")) {
//            buscarFiestas();
//        }
//    }

    private void loadButton() {
        Button button = root.findViewById(R.id.search);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });
    }

//    private void buscarFiestas() {
//        getData();
//
//    }

    private void loadSpinner(ArrayList<String>[] resultado) {
        Spinner spinner = root.findViewById(R.id.spinner);
        ArrayAdapter<String> SpinerAdapter;
        ArrayList<String> arrayItems = resultado[0];
        ArrayList<String> actualValues = resultado[1];
        comunidad = actualValues.get(0);

        SpinerAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, arrayItems);
        spinner.setAdapter(SpinerAdapter);

        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                comunidad = actualValues.get(arg2);
                //Guardamos en memoria
                SharedPreferences settings = getContext().getSharedPreferences(COMUNIDAD, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString(COMUNIDAD, comunidad);
                editor.apply();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    public void getData() {
        try {
            //Conseguimos el HTML con la librería "Ion"
            String url = "https://fiestas.net" + comunidad;
            Ion.with(root.getContext()).load(url).asString().withResponse().setCallback(new FutureCallback<Response<String>>() {
                @Override
                public void onCompleted(Exception e, Response<String> result) {
                    try {
                        // Una vez conseguido el html, lo parseamos para conseguir un array de fiestas
                        String resultado = result.getResult();
                        fiestas = ParserFiestas.ParseFiestas(resultado);
                        navigationActivity.fiestas = fiestas;
//                        loadFragment(ListaFragments.newInstance(fiestas));//Pantalla por defecto

                    } catch (Exception ex) {
                        Snackbar.make(root.findViewById(R.id.layoutMain), R.string.error, Snackbar.LENGTH_LONG).show();
                        ex.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            Snackbar.make(root.findViewById(R.id.layoutMain), R.string.error, Snackbar.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    /**
     * Carga todas las comunidades españolas
     */
    private void loadData() {
        try {
            //Conseguimos el HTML con la librería "Ion"
            String url = "https://fiestas.net/";
            Ion.with(getContext()).load(url).asString().withResponse().setCallback(new FutureCallback<Response<String>>() {
                @Override
                public void onCompleted(Exception e, Response<String> result) {
                    try {
                        // Una vez conseguido el html, lo parseamos para conseguir un array de fiestas
                        String resultado = result.getResult();
                        ArrayList<String>[] returned = ParserLugares.parseComunidades(resultado);
                        loadSpinner(returned);

                    } catch (Exception ex) {
                        Snackbar.make(root.findViewById(R.id.layoutMain), R.string.error, Snackbar.LENGTH_LONG).show();
                        ex.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            Snackbar.make(root.findViewById(R.id.layoutMain), R.string.error, Snackbar.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


    private void loadFragment(Fragment fragment) {

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment_content_navigation, fragment);
        transaction.commit();
    }

}