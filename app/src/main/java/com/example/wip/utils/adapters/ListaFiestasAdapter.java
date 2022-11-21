package com.example.wip.utils.adapters;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wip.R;
import com.example.wip.modelo.Fiesta;
import com.example.wip.utils.UnderLine;

import java.util.ArrayList;

public class ListaFiestasAdapter extends RecyclerView.Adapter<ListaFiestasAdapter.ListaFiestasAdapterViewHolder>{
    private ArrayList<Fiesta> fiestas;
    private RecyclerView.RecycledViewPool viewPool= new RecyclerView.RecycledViewPool();

    public interface OnItemClickListener{
        void onItemClick(Fiesta fiesta);
    }

    private final OnItemClickListener listenerPlace;
    private final OnItemClickListener listenerDetails;


    public ListaFiestasAdapter(ArrayList<Fiesta> fiestasList, OnItemClickListener listenerPlace,OnItemClickListener listenerDetails) {

        this.fiestas=fiestasList;
        this.listenerPlace=listenerPlace;
        this.listenerDetails=listenerDetails;
    }

    @NonNull
    @Override
    public ListaFiestasAdapter.ListaFiestasAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Creamos la vista con el layout para el elemento
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.fiestas_recycler_view,parent,false);

        return new ListaFiestasAdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaFiestasAdapter.ListaFiestasAdapterViewHolder holder, int position) {
        //Extrae de la lista el elemento indicado por posicion
        Fiesta fiesta = fiestas.get(position);
        //llama al metodo de nuestro holder para asignar valores a los componentes
        //ademas, pasamos el listener del elemento onClick
        holder.bindUser(fiesta, listenerPlace,listenerDetails);
    }

    @Override
    public int getItemCount() {
        return fiestas.size();
    }
    protected class ListaFiestasAdapterViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView lugar;
        private TextView fecha;
        private OnItemClickListener listenerPlace;
        private OnItemClickListener listenerDetails;
        public ListaFiestasAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            name=(TextView) itemView.findViewById(R.id.nombre_fiesta);
            lugar=(TextView) itemView.findViewById(R.id.lugar_fiesta);
            fecha=(TextView) itemView.findViewById(R.id.fecha_fiesta);
        }

        public void bindUser(final Fiesta fiesta, final OnItemClickListener listenerPlace, final OnItemClickListener listenerDetails){
            getDefaultData(fiesta);
            getURLData(fiesta);
            this.listenerDetails=listenerDetails;
            this.listenerPlace=listenerPlace;
        }
        private void getDefaultData(Fiesta fiesta) {
            name.setText(fiesta.getName());
            lugar.setText(fiesta.getPlace());
            fecha.setText(fiesta.getDate());
        }

        private void getURLData(Fiesta fiesta) {
            if(!fiesta.getTownURL().equals("")) {
                lugar.setText(UnderLine.underLine(fiesta.getPlace()));
                lugar.setTextColor(Color.parseColor("#000000"));
                lugar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listenerPlace.onItemClick(fiesta);
                    }
                });
            }
            if(!fiesta.getDetails().equals("")) {
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
}