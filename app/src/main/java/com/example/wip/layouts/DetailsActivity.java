package com.example.wip.layouts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wip.ImageDetailsActivity;
import com.example.wip.R;
import com.example.wip.data.FiestasDataSource;
import com.example.wip.data.UploadedImagesDataSource;
import com.example.wip.modelo.Fiesta;
import com.example.wip.utils.ParserFiestas;
import com.example.wip.utils.adapters.ImageAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private static int RESULT_LOAD_IMAGE = 1;
    private static final int READ_GALLERY_PERMISSION_REQUEST_CODE = 1;

    private String urlDetails;
    private String details;
    private boolean isFavorite;
    ImageView btnFavorite;
    private Fiesta fiesta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ((TextView) findViewById(R.id.details)).setMovementMethod(new ScrollingMovementMethod());

        btnFavorite = findViewById(R.id.btnFav);
        btnFavorite.setOnClickListener(view -> toggleFav());

        Intent intent = getIntent();
        urlDetails = intent.getStringExtra(ListaFragments.ARG_FIESTAS);
        getDetails();
        fiesta = intent.getParcelableExtra(ListaFragments.FIESTA_SELECCIONADA);

        FiestasDataSource fds = new FiestasDataSource(getApplicationContext());
        fds.open();

        List<Fiesta> partiesSaved = fds.getFilteredValorations(fiesta.getName());
        fds.close();

        isFavorite = partiesSaved.size() != 0;
        if (isFavorite) fiesta.setId(partiesSaved.get(0).getId());

        updateFavIcon();
        if (isFavorite)
            loadImagesAndEnableUploadImg();
    }

    private void loadSavedImages() {
        UploadedImagesDataSource dataSource = new UploadedImagesDataSource(getApplicationContext());
        dataSource.open();

        List<String> uploadedImages = dataSource.getFilteredValorations(fiesta.getId());

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4));

        ImageAdapter adapter = new ImageAdapter(uploadedImages, (imagePath, imageView) -> {
            Intent i = new Intent(getApplicationContext(), ImageDetailsActivity.class);
            i.putExtra("imagePath", imagePath);
            startActivity(i);
        });
        recyclerView.setAdapter(adapter);
    }

    private void getDetails() {
        if (urlDetails.equals("")) setDetails(getResources().getString(R.string.no_details));
        else try {
            //Conseguimos el HTML con la librería "Ion"
            String url = "https://fiestas.net/" + urlDetails;
            Ion.with(getApplicationContext()).load(url).asString().withResponse().setCallback((e, result) -> {
                try {
                    // Una vez conseguido el html, lo parseamos para conseguir un array de fiestas
                    String resultado = result.getResult();
                    details = ParserFiestas.ParseDetails(resultado);
                    setDetails(details);
                } catch (Exception ex) {
                    Snackbar.make(findViewById(R.id.layoutMain), R.string.error, Snackbar.LENGTH_LONG).show();
                    ex.printStackTrace();
                }
            });

        } catch (Exception e) {
            Snackbar.make(findViewById(R.id.layoutMain), R.string.error, Snackbar.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void setDetails(String details) {
        TextView detailsView = findViewById(R.id.details);
        detailsView.setText(details);
    }

    private void loadImagesAndEnableUploadImg() {

        ImageView btnAddImage = findViewById(R.id.btnAddImage);
        btnAddImage.setOnClickListener(view -> {
            // Comprobamos si tenemos permiso para acceder a la galeria
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // Si no tenemos permiso, lo solicitamos
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_GALLERY_PERMISSION_REQUEST_CODE);
            } else {
                // Si tenemos permiso, continuamos
                showUploadView();
            }
        });

        findViewById(R.id.recyclerView).setVisibility(View.VISIBLE);
        btnAddImage.setVisibility(View.VISIBLE);

        loadSavedImages();
    }

    private void updateFavIcon() {
        if (isFavorite)
            btnFavorite.setImageDrawable((ContextCompat.getDrawable(this, R.drawable.ic_baseline_favorite_24)));
        else
            btnFavorite.setImageDrawable((ContextCompat.getDrawable(this, R.drawable.ic_baseline_favorite_border_24)));

    }

    private void toggleFav() {
        FiestasDataSource fsd = new FiestasDataSource(getApplicationContext());
        fsd.open();
        if (!isFavorite) {
            fiesta.setFavorite(true);
            fiesta.setId((int) fsd.insertFiesta(fiesta));
            Toast.makeText(this, "This is one of you favourite partys", Toast.LENGTH_LONG);
        } else {
            fiesta.setFavorite(false);
            fsd.deleteParty(fiesta.getName());
            Toast.makeText(this, "Sorry you don´t have fun at this party", Toast.LENGTH_LONG);
        }
        fsd.close();
        isFavorite = !isFavorite;
        updateFavIcon();

        loadImagesAndEnableUploadImg();
    }

    private void showUploadView() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {

            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            UploadedImagesDataSource dataSource = new UploadedImagesDataSource(getApplicationContext());
            dataSource.open();

            if (data.getClipData() != null) {
                ClipData mClipData = data.getClipData();
                List<String> imagesPaths = new ArrayList<>();
                for (int i = 0; i < mClipData.getItemCount(); i++) {
                    ClipData.Item item = mClipData.getItemAt(i);
                    Uri uri = item.getUri();
                    // Get the cursor
                    Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    imagesPaths.add(picturePath);


                    dataSource.insertImage(picturePath, fiesta.getId());
                }

                // Si subimos algo, volvemos a cargar el recycler
                loadSavedImages();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != READ_GALLERY_PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        //  Recorremos los permisos para comprobar si tenemos perimiso para acceder a la galeria
        for (int i = 0; i < permissions.length; i++) {
            if (permissions[i].equals(Manifest.permission.READ_EXTERNAL_STORAGE) && grantResults[i] == PackageManager.PERMISSION_GRANTED)
                showUploadView();
        }
    }
}