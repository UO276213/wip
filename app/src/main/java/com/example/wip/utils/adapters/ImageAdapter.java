package com.example.wip.utils.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.wip.R;
import com.example.wip.data.records.ImagePartyRecord;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImagesViewHolder> {

    private List<ImagePartyRecord> imagesPartiesRecords;
    private final OnItemClickListener listener;

    public ImageAdapter(List<ImagePartyRecord> imagesPartiesRecords, OnItemClickListener listener) {
        this.imagesPartiesRecords = imagesPartiesRecords;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ImagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_image, parent, false);
        return new ImagesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagesViewHolder holder, int position) {
        holder.bindUser(imagesPartiesRecords.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return imagesPartiesRecords.size();
    }

    public interface OnItemClickListener {
        void onItemClick(ImagePartyRecord imagePartyRecord);
    }

    protected class ImagesViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        public ImagesViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.grid_item_image);
        }

        public void bindUser(final ImagePartyRecord imagePartyRecord, OnItemClickListener listener) {

            BitmapFactory.Options options = new BitmapFactory.Options();
            // evita la asignación de memoria,
            // con lo cual se muestra null para el objeto de mapa de bits
            options.inJustDecodeBounds = true;

            // Asigna ciertos valores a options
            BitmapFactory.decodeFile(imagePartyRecord.getImagePath(), options);

            // TODO Obtner el tamño del imageView

            options.inSampleSize = calculateInSampleSize(options, 200, 200);

            options.inJustDecodeBounds = false;
            Bitmap bmp = BitmapFactory.decodeFile(imagePartyRecord.getImagePath(), options);

            imageView.setImageBitmap(bmp);
            imageView.setOnClickListener(view -> listener.onItemClick(imagePartyRecord));
        }

        /*
        Calcula el cociente necesario para convertir la imagen de su tamaño original a la del ImageView
        https://developer.android.com/topic/performance/graphics/load-bitmap

         */
        public int calculateInSampleSize(
                BitmapFactory.Options options, int reqWidth, int reqHeight) {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {

                final int halfHeight = height / 2;
                final int halfWidth = width / 2;

                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while ((halfHeight / inSampleSize) >= reqHeight
                        && (halfWidth / inSampleSize) >= reqWidth) {
                    inSampleSize *= 2;
                }
            }

            return inSampleSize;
        }
    }
}
