package com.example.wip.ui.layouts.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.wip.R;
import com.example.wip.data.UploadedImagesDataSource;
import com.example.wip.data.records.ImagePartyRecord;

public class ImageDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_details);

        ImageView imageView = findViewById(R.id.selectedImage); // init a ImageView
        Intent intent = getIntent(); // get Intent which was set from adapter of Previous Activity
        ImagePartyRecord imagePartyRecord = intent.getParcelableExtra(DetailsActivity.EXTRA_PARTY_IMAGE_RECORD);

        Bitmap bmp = BitmapFactory.decodeFile(imagePartyRecord.getImagePath());
        imageView.setImageBitmap(bmp);

        ImageButton deleteBtn = findViewById(R.id.deleteBtn); // init a ImageView
        deleteBtn.setOnClickListener(view -> deleteImagePartyRecord(imagePartyRecord));

        EditText imageTitle = findViewById(R.id.edit_image_title);

        String title = imagePartyRecord.getTitle();
        imageTitle.setText(title != null ? title: getResources().getString(R.string.image_uploaded_default_title));

        imageTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateImageTitle(imagePartyRecord, s.toString());
            }
        });
    }

    private void updateImageTitle(ImagePartyRecord imagePartyRecord, String title) {
        UploadedImagesDataSource dataSource = new UploadedImagesDataSource(getApplicationContext());
        dataSource.open();
        dataSource.updateImageTitle(imagePartyRecord.getId(), title);
        dataSource.close();
    }

    private void deleteImagePartyRecord(ImagePartyRecord imagePartyRecord) {
        UploadedImagesDataSource dataSource = new UploadedImagesDataSource(getApplicationContext());
        dataSource.open();
        dataSource.deleteImageParty(imagePartyRecord.getId());
        dataSource.close();

        finish();
    }
}