package com.example.wip.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.wip.R;
import com.example.wip.data.FiestasDataSource;
import com.example.wip.databinding.FragmentGalleryBinding;
import com.example.wip.layouts.ListaFragments;
import com.example.wip.modelo.Fiesta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    private View root;
    private List<Fiesta> fiestas = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        /*GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textGallery;
        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);*/

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

    private void loadFragment(Fragment fragment){

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }
}