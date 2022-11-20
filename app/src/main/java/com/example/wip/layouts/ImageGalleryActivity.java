package com.example.wip.layouts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ClipData;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.content.Intent;
import android.provider.MediaStore;

import com.example.wip.data.FiestasDataSource;
import com.example.wip.data.UploadedImagesDataSource;
import com.example.wip.utils.adapters.ImageAdapter;
import com.example.wip.ImageDetailsActivity;
import com.example.wip.R;

import java.util.ArrayList;
import java.util.List;


public class ImageGalleryActivity extends AppCompatActivity {

    private static int RESULT_LOAD_IMAGE = 1;
    private static final int READ_GALLERY_PERMISSION_REQUEST_CODE = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery);

        // Comprobamos si tenemos permiso para acceder a la galeria
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Si no tenemos permiso, lo solicitamos
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_GALLERY_PERMISSION_REQUEST_CODE);
        } else {
            // Si tenemos permiso, continuamos
            showUploadView();
        }
    }

    /**
     * Lanza la actividad que permite escoger imagenes de la galeria
     */
    private void showUploadView() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }


    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {

            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            UploadedImagesDataSource dataSource= new UploadedImagesDataSource(getApplicationContext());
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


                    dataSource.insertImage(picturePath);
                }

                RecyclerView recyclerView = findViewById(R.id.recyclerView);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4));

                ImageAdapter adapter = new ImageAdapter(dataSource.getAllValorations(), (imagePath, imageView) -> {
                    Intent intent = new Intent(getApplicationContext(), ImageDetailsActivity.class);
                    intent.putExtra("imagePath", imagePath);
                    startActivity(intent); // start Intent
                });
                recyclerView.setAdapter(adapter);
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