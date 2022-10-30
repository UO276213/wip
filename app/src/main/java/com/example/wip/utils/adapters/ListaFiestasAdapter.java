package com.example.wip.utils.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wip.R;
import com.example.wip.modelo.Fiesta;

import java.util.ArrayList;

public class ListaFiestasAdapter extends RecyclerView.Adapter<ListaFiestasAdapter.ListaFiestasAdapterViewHolder>{
    private ArrayList<Fiesta> fiestas;
    private RecyclerView.RecycledViewPool viewPool= new RecyclerView.RecycledViewPool();

    public interface OnItemClickListener{
        void onItemClick(Fiesta fiesta);
    }

    private final OnItemClickListener listener;


    public ListaFiestasAdapter(ArrayList<Fiesta> fiestasList, OnItemClickListener list) {

        this.fiestas=fiestasList;
        this.listener=list;
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
        holder.bindUser(fiesta, listener);
    }

    @Override
    public int getItemCount() {
        return fiestas.size();
    }
    protected class ListaFiestasAdapterViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView lugar;
        private TextView fecha;
        private RecyclerView recyclerView;
        public ListaFiestasAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            name=(TextView) itemView.findViewById(R.id.nombre_fiesta);
            lugar=(TextView) itemView.findViewById(R.id.lugar_fiesta);
            fecha=(TextView) itemView.findViewById(R.id.fecha_fiesta);
        }

        public void bindUser(final Fiesta fiesta, final OnItemClickListener listener){
            name.setText(fiesta.getName());
            lugar.setText(fiesta.getPlace());
            fecha.setText(fiesta.getDate());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(fiesta);
                }
            });
        }
    }
}