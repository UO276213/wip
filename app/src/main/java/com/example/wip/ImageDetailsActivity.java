package com.example.wip;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.wip.data.UploadedImagesDataSource;
import com.example.wip.data.records.ImagePartyRecord;
import com.example.wip.layouts.DetailsActivity;

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
    }

    private void deleteImagePartyRecord(ImagePartyRecord ImagePartyRecord) {
        UploadedImagesDataSource dataSource = new UploadedImagesDataSource(getApplicationContext());
        dataSource.open();
        dataSource.deleteImageParty(ImagePartyRecord.getId());
        dataSource.close();

        finish();
    }
}