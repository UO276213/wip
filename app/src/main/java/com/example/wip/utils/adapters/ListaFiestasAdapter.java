package com.example.wip.utils.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wip.BuildConfig;
import com.example.wip.R;
import com.example.wip.modelo.Fiesta;
import com.example.wip.utils.parsers.UnderLine;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListaFiestasAdapter extends RecyclerView.Adapter<ListaFiestasAdapter.ListaFiestasAdapterViewHolder> {
    private ArrayList<Fiesta> fiestas;
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

    public interface OnItemClickListener {
        void onItemClick(Fiesta fiesta);
    }

    private final OnItemClickListener listenerPlace;
    private final OnItemClickListener listenerDetails;
    private Context context;


    public ListaFiestasAdapter(ArrayList<Fiesta> fiestasList,
                               OnItemClickListener listenerPlace, OnItemClickListener listenerDetails, Context context) {

        this.fiestas = fiestasList;
        this.listenerPlace = listenerPlace;
        this.listenerDetails = listenerDetails;
        this.context = context;
    }

    @NonNull
    @Override
    public ListaFiestasAdapter.ListaFiestasAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Creamos la vista con el layout para el elemento
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.fiestas_recycler_view, parent, false);

        return new ListaFiestasAdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaFiestasAdapter.ListaFiestasAdapterViewHolder holder, int position) {
        //Extrae de la lista el elemento indicado por posicion
        Fiesta fiesta = fiestas.get(position);
        //llama al metodo de nuestro holder para asignar valores a los componentes
        //ademas, pasamos el listener del elemento onClick
        holder.bindUser(fiesta, listenerPlace, listenerDetails);
    }

    @Override
    public int getItemCount() {
        return fiestas.size();
    }

    protected class ListaFiestasAdapterViewHolder extends RecyclerView.ViewHolder {

        private ImageView img;
        private TextView name;
        private TextView lugar;
        private TextView fecha;
        private OnItemClickListener listenerPlace;
        private OnItemClickListener listenerDetails;

        public ListaFiestasAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imageView2);
            name = (TextView) itemView.findViewById(R.id.nombre_fiesta);
            lugar = (TextView) itemView.findViewById(R.id.lugar_fiesta);
            fecha = (TextView) itemView.findViewById(R.id.fecha_fiesta);
        }

        public void bindUser(final Fiesta fiesta, final OnItemClickListener listenerPlace, final OnItemClickListener listenerDetails) {
            getDefaultData(fiesta);
            getURLData(fiesta);
            this.listenerDetails = listenerDetails;
            this.listenerPlace = listenerPlace;
        }

        private void getDefaultData(Fiesta fiesta) {
            loadImageGoogle(fiesta, img);
            name.setText(fiesta.getName());
            lugar.setText(fiesta.getPlace());
            fecha.setText(fiesta.getDate());
        }

        private void getURLData(Fiesta fiesta) {
            if (!fiesta.getTownURL().equals("")) {
                lugar.setText(UnderLine.underLine(fiesta.getPlace()));
                lugar.setTextColor(Color.parseColor("#000000"));
                lugar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listenerPlace.onItemClick(fiesta);
                    }
                });
            }
            if (!fiesta.getDetails().equals("")) {
                name.setTextColor(Color.parseColor("#0000FF"));
            }
            name.setText(UnderLine.underLine(fiesta.getName()));
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listenerDetails.onItemClick(fiesta);
                }
            });
        }

    }

    private void loadImageGoogle(Fiesta fiesta, ImageView imageView) {


        String BASE_URL = "https://customsearch.googleapis.com/customsearch/v1?cx=b0098e7778c834105&num=5&searchType=image&key=" + BuildConfig.GOOGLE_SEARCH_API_KEY;
        try {
            String url = BASE_URL + "&q=fiesta+" + (fiesta.getPlace() + " " + fiesta.getName()).replace(" ", "+");
            //Conseguimos el HTML con la librería "Ion"

            Ion.with(context).load(url).asJsonObject().withResponse().done((error, response) -> {
                if (error == null){
                    // Una vez conseguido el html, lo parseamos para conseguir un array de fiestas
                    JsonObject resultado = response.getResult();
                    JsonArray items = resultado.get("items").getAsJsonArray();

                    ArrayList<String> imgsUrls = new ArrayList<>();

                    String link = items.get(0).getAsJsonObject().get("link").getAsString();
                    Picasso.get().load(link).error(R.drawable.image_not_available).into(imageView);
                }


            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}