package com.example.wip.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.wip.R;
import com.example.wip.data.FiestasDataSource;
import com.example.wip.databinding.FragmentGalleryBinding;
import com.example.wip.layouts.CalendarFragment;
import com.example.wip.layouts.ListaFragments;
import com.example.wip.layouts.MapsFragment;
import com.example.wip.modelo.Fiesta;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    private View root;
    private ArrayList<Fiesta> fiestas = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        /*GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textGallery;
        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);*/

        BottomNavigationView nav = getActivity().findViewById(R.id.bottom_navigation2);

        nav.setOnItemSelectedListener(onItemSelectedListener);

        root = inflater.inflate(R.layout.activity_fragment, container, false);
        getFavPartys();
        loadFragment(ListaFragments.newInstance((ArrayList<Fiesta>) fiestas));
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void getFavPartys(){
        FiestasDataSource fiestasDataSource = new FiestasDataSource(getContext());
        fiestasDataSource.open();
        fiestas = fiestasDataSource.getAllValorations();
        fiestasDataSource.close();

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


    private void loadFragment(Fragment fragment){

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }
}