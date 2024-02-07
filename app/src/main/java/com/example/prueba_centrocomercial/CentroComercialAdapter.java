package com.example.prueba_centrocomercial;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CentroComercialAdapter extends RecyclerView.Adapter<CentroComercialAdapter.ViewHolder> {

    private List<CentroComercial> centroComercialList;
    private  List<String> keys;
    private Context context;

    public void setCentroComercialIds( List<String> keys) {

        this.keys = keys;
       // System.out.println("tamanio en adapter: " + centroComercialIds.size());
    }

    public CentroComercialAdapter(List<CentroComercial> centroComercialList, Context context) {
        this.centroComercialList = centroComercialList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.centro_comercial_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CentroComercial centroComercial = centroComercialList.get(position);
        CentroComercial nuevo= new CentroComercial();
        nuevo.setId(keys.get(position));
        nuevo.setNombre(centroComercial.getNombre());
        nuevo.setUbicacion(centroComercial.getUbicacion());
        nuevo.setLogoUrl(centroComercial.getLogoUrl());


        holder.tvMallName.setText(centroComercial.getNombre());
        holder.tvLocation.setText(centroComercial.getUbicacion());

        // Cargar el logo del centro comercial utilizando Picasso (puedes usar Glide también)
        Picasso.get().load(centroComercial.getLogoUrl()).into(holder.ivLogo);

        // Configurar el clic del elemento
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemClick(centroComercial);
                }
            }
        });

        holder.ivOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lógica para mostrar el menú de opciones o eliminar el elemento
                showOptionsMenu(view, nuevo);
            }
        });
    }

    private void showOptionsMenu(View view, CentroComercial centroComercial) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.options_menu); // Reemplaza con tu propio menú de opciones

        // Manejar clic en los elementos del menú
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_delete:

                        deleteItem(centroComercial);
                        // Notifica al adaptador que el conjunto de datos ha cambiado
                        notifyDataSetChanged();
                        return true;
                    // Agrega más elementos de menú según tus necesidades
                    // ...
                    default:
                        return false;
                }
            }
        });

        // Mostrar el menú contextual
        popupMenu.show();
    }

    public void deleteItem(CentroComercial centroComercial) {
        if (centroComercial != null) {
            Log.d("CentroComercialAdapter", "HOLA ESTOY EN ELIMINAE: " + centroComercial.getId());

            // Obtiene la ID del centro comercial
            String centroComercialId = centroComercial.getId();

            // Elimina el elemento de tu lista de datos
            centroComercialList.remove(centroComercial);

            // Notifica al adaptador que el conjunto de datos ha cambiado
            notifyDataSetChanged();

            // Verifica que la ID del centro comercial no sea nula
            if (centroComercialId != null) {
                // Obtiene la referencia del elemento en la base de datos
                DatabaseReference itemRef = FirebaseDatabase.getInstance().getReference().child("centrosComerciales").child(centroComercialId);

                // Elimina el elemento de la base de datos
                itemRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Se eliminó correctamente de la base de datos
                            Toast.makeText(context, "Centro Comercial eliminado", Toast.LENGTH_SHORT).show();
                        } else {
                            // Manejar error en la eliminación
                            Toast.makeText(context, "Error al eliminar el Centro Comercial", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                Log.e("CentroComercialAdapter", "ID del centro comercial es nulo. No se puede eliminar de la base de datos.");
            }
        }
    }


    @Override
    public int getItemCount() {
        return centroComercialList.size();
    }

    public void setCentroComercialList(List<CentroComercial> newList) {
        centroComercialList.clear();
        centroComercialList.addAll(newList);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(CentroComercial centroComercial);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMallName;
        TextView tvLocation;
        ImageView ivLogo;
        ImageView ivOptions;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMallName = itemView.findViewById(R.id.tvMallName);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            ivLogo = itemView.findViewById(R.id.ivLogo);
            ivOptions = itemView.findViewById(R.id.ivOptions);
        }
    }
}
