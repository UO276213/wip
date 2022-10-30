package com.example.wip.layouts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.wip.R;
import com.example.wip.modelo.Fiesta;

import java.util.ArrayList;

public class ListaFragments extends Fragment {

    private static final String ARG_FIESTAS = "arg_fiestas";

    private ArrayList<Fiesta> fiestas;

    public ListaFragments() {
    }

    /**
     * @return A new instance of fragment ListaFragments.
     */
    public static ListaFragments newInstance(ArrayList<Fiesta> fiestas) {
        ListaFragments fragment = new ListaFragments();
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addToLayout();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_lista, container, false);
    }

    //Recorre un array de fiestas y las va añadiendo al layout
    private void addToLayout() {
        //Este codigo será igual en las diferentes pantallas, recogemos el array de fiestas
        LinearLayout layout = getView().findViewById(R.id.layoutLista);
        for (Fiesta fiesta : fiestas) {
            //Aquí ya es diferente, depende de lo que queramos añadir
            TextView texto = new TextView(getContext());
            texto.setText(fiesta.toString());
            layout.addView(texto);
        }
    }
}