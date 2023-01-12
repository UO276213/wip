package com.example.wip.ui.layouts.activities;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.wip.BuildConfig;
import com.example.wip.ui.layouts.fragments.ListaFragments;
import com.example.wip.R;
import com.example.wip.data.FiestasDataSource;
import com.example.wip.data.UploadedImagesDataSource;
import com.example.wip.data.records.ImagePartyRecord;
import com.example.wip.modelo.Fiesta;
import com.example.wip.utils.parsers.ParserFiestas;
import com.example.wip.utils.adapters.ImageAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    public static final String EXTRA_PARTY_IMAGE_RECORD = "extra_party_image_record";
    private static final int READ_GALLERY_PERMISSION_REQUEST_CODE = 1;
    private static int RESULT_LOAD_IMAGE = 1;
    ImageView btnFavorite;
    private String urlDetails;
    private String details;
    private boolean isFavorite;
    private Fiesta fiesta;
    private ImageButton btnAddImage;
    private TextView textMemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
//        ((TextView) findViewById(R.id.details)).setMovementMethod(new ScrollingMovementMethod());

        btnFavorite = findViewById(R.id.btnFav);
        btnFavorite.setOnClickListener(view -> toggleFav());

        btnAddImage = findViewById(R.id.btnAddImage);
        btnAddImage.setVisibility(View.INVISIBLE);

        textMemo = findViewById(R.id.textMemorias);

        Intent intent = getIntent();
        urlDetails = intent.getStringExtra(ListaFragments.ARG_FIESTAS);
        fiesta = intent.getParcelableExtra(ListaFragments.FIESTA_SELECCIONADA);
        getDetails();

        FiestasDataSource fds = new FiestasDataSource(getApplicationContext());
        fds.open();

        List<Fiesta> partiesSaved = fds.getFilteredValorations(fiesta.getName());
        fds.close();

        isFavorite = partiesSaved.size() != 0;
        if (isFavorite) fiesta.setId(partiesSaved.get(0).getId());

        updateFavIcon();

        if (isFavorite)
            enableUploadImg();

        loadImageGoogle(fiesta);
    }

    private void loadSavedImages() {
        UploadedImagesDataSource dataSource = new UploadedImagesDataSource(getApplicationContext());
        dataSource.open();

        List<ImagePartyRecord> uploadedImages = dataSource.getFiltredUploadedPartyImages(fiesta.getId());

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4));

            ImageAdapter adapter = new ImageAdapter(uploadedImages, imagePartyRecord -> {
                Intent i = new Intent(getApplicationContext(), ImageDetailsActivity.class);
                i.putExtra(EXTRA_PARTY_IMAGE_RECORD, imagePartyRecord);
                startActivity(i);
            });
            recyclerView.setAdapter(adapter);
        }

    private void getDetails() {
        if (urlDetails.equals("")) {
            setDefauldDetails();//Cuando no hay detalles
        }
        else try {
            //Conseguimos el HTML con la librería "Ion"
            String url = "https://fiestas.net/" + urlDetails;
            Ion.with(getApplicationContext()).load(url).asString().withResponse().setCallback((e, result) -> {
                // Una vez conseguido el html, lo parseamos para conseguir un array de fiestas
                String resultado = result.getResult();
                details = ParserFiestas.ParseDetails(resultado);
                setDetails(details);
            });

        } catch (Exception e) {
            Snackbar.make(findViewById(R.id.layoutMain), R.string.error, Snackbar.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void setDefauldDetails() {
        //setDetails(getResources().getString(R.string.no_details));
        details = fiesta.getName()+"\n"+fiesta.getDate()+"\n"+fiesta.getPlace();
        setDetails(details);
    }

    private void setDetails(String details) {
        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        String[] detaillsDivided = details.split("\n");
        int number = 0;
        for (String d : detaillsDivided) {
            TextView tv = new TextView(this.getApplicationContext());
            tv.setText(d);
            tv = getTextViewType(tv, number);
            linearLayout.addView(tv);
            number++;
        }
    }

    private TextView getTextViewType(TextView tv, int number) {
        String text = String.valueOf(tv.getText());
        List<String> days = Arrays.asList("lunes", "martes", "miércoles", "jueves", "viernes", "sábado", "domingo", "del");
        if (number == 0) {//titulo
            tv.setTextSize(30);
            tv.setTextColor(Color.parseColor("#63666A"));
        } else if (number == 2) {//proximas actividades
            tv.setTextSize(20);
            tv.setTextColor(Color.parseColor("#000000"));
        } else if (!text.equals(""))//fechas u horas
            if (Character.isDigit(text.charAt(0)) || days.contains(text.split(",| ")[0])) {
                tv.setTextColor(Color.parseColor("#0000FF"));
            } else {
                tv.setTextColor(Color.parseColor("#000000"));
                tv.setTextSize(17);
            }
        return tv;
    }

    private void enableUploadImg() {
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
    }

    private void updateFavIcon() {
        if (isFavorite){
            textMemo.setText("Sigue creando recuerdos!");
            btnFavorite.setImageDrawable((ContextCompat.getDrawable(this, R.drawable.ic_baseline_favorite_24)));
        } else{
            btnFavorite.setImageDrawable((ContextCompat.getDrawable(this, R.drawable.ic_baseline_favorite_border_24)));
        }


    }

    private void toggleFav() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        FiestasDataSource fsd = new FiestasDataSource(getApplicationContext());
        fsd.open();
        if (!isFavorite) {
            fiesta.setFavorite(true);
            fiesta.setId((int) fsd.insertFiesta(fiesta));
            textMemo.setText("Sigue creando recuerdos!");
            Toast.makeText(this, getResources().getString(R.string.add_favourite_label), Toast.LENGTH_LONG).show();
            enableUploadImg();
            recyclerView.setVisibility(View.VISIBLE);

        } else {
            fiesta.setFavorite(false);
            fsd.deleteParty(fiesta.getName());
            Toast.makeText(this, getResources().getString(R.string.delete_saved_party_label), Toast.LENGTH_LONG).show();

            btnAddImage.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        fsd.close();
        isFavorite = !isFavorite;
        updateFavIcon();

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
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

    @Override
    protected void onPostResume() {
        super.onPostResume();

        // Si la fiesta es de las favoritas, cargamos las imágenes asociadas
        if (isFavorite)
            loadSavedImages();
    }

    private void loadImageGoogle(Fiesta fiesta) {

        String BASE_URL = "https://customsearch.googleapis.com/customsearch/v1?cx=b0098e7778c834105&num=5&searchType=image&key=" + BuildConfig.GOOGLE_SEARCH_API_KEY;
        try {
            String url = BASE_URL + "&q=fiesta+" + (fiesta.getPlace() + " " + fiesta.getName()).replace(" ", "+");
            //Conseguimos el HTML con la librería "Ion"

            Ion.with(getApplicationContext()).load(url).asJsonObject().withResponse().success(response -> {
                // Una vez conseguido el html, lo parseamos para conseguir un array de fiestas
                JsonObject resultado = response.getResult();
                JsonArray items = resultado.get("items").getAsJsonArray();


                ImageSlider slider = findViewById(R.id.slider);
                List<SlideModel> slideModels = new ArrayList<>();
                for (JsonElement item : items) {
                    String link = item.getAsJsonObject().get("link").getAsString();
                    Log.d("IMAGEURL", link);
                    slideModels.add(new SlideModel(link, ScaleTypes.CENTER_CROP));
                }

                slider.setImageList(slideModels);

            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}