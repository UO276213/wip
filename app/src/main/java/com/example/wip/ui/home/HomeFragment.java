package com.example.wip.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wip.R;
import com.example.wip.layouts.CalendarFragment;
import com.example.wip.layouts.ListaFragments;
import com.example.wip.layouts.MainActivity;
import com.example.wip.layouts.MapsFragment;
import com.example.wip.modelo.Fiesta;
import com.example.wip.utils.ParserFiestas;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.util.ArrayList;

public class HomeFragment extends Fragment {


    private static final String ARG_FIESTAS = "arg_fiestas";
    private RecyclerView recyclerView;
    private ArrayList<Fiesta> fiestas = new ArrayList<>();
    private View root;
    private String lugar = "asturias";//cambiar esto en un futuro

    @Override
    public void onResume() {
        super.onResume();

        BottomNavigationView nav = getActivity().findViewById(R.id.bottom_navigation2);

        nav.setOnItemSelectedListener(onItemSelectedListener);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       /*HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);*/

        //binding = FragmentHomeBinding.inflate(inflater, container, false);
        root = inflater.inflate(R.layout.activity_fragment, container, false);


        Intent intent = getActivity().getIntent();
        lugar = intent.getStringExtra(MainActivity.COMUNIDAD);
        getData();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

  /*  @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addToLayout();
    }

    private void addToLayout() {
        //Este codigo será igual en las diferentes pantallas, recogemos el array de fiestas
        recyclerView=(RecyclerView) root.findViewById(R.id.recycler_view_list);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        addAdapter();
    }
    //Añade el adapter del recycledView
    private void addAdapter() {
        ListaFiestasAdapter lpAdapter = new ListaFiestasAdapter(fiestas, new ListaFiestasAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Fiesta fiesta) {
                //startCategory(category);
            }
        });
        recyclerView.setAdapter(lpAdapter);
    }*/

    public void getData() {
        try {
            //Conseguimos el HTML con la librería "Ion"
            String url = "https://fiestas.net/" + lugar + "/";
            Ion.with(root.getContext()).load(url).asString().withResponse().setCallback(new FutureCallback<Response<String>>() {
                @Override
                public void onCompleted(Exception e, Response<String> result) {
                    try {
                        // Una vez conseguido el html, lo parseamos para conseguir un array de fiestas
                        String resultado = result.getResult();
                        fiestas = ParserFiestas.ParseFiestas(resultado);

                        loadFragment(ListaFragments.newInstance(fiestas));//Pantalla por defecto

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

    public final NavigationBarView.OnItemSelectedListener onItemSelectedListener = item -> {
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


    private void loadFragment(Fragment fragment) {

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }

}