package com.example.prueba_centrocomercial;

import android.content.Context;
import android.location.GnssAntennaInfo;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class TiendaAdapter extends RecyclerView.Adapter<TiendaAdapter.ViewHolder> {

    private List<Tienda> tiendaList;
    private Context context;

    private OnItemClickListener listener;


    public TiendaAdapter(List<Tienda> tiendaList, Context context) {
        this.tiendaList = tiendaList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tienda, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tienda tienda = tiendaList.get(position);
        holder.tvNombre.setText(tienda.getNombre());
        holder.tvDescripcion.setText(tienda.getDescripcion());



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemClick(tienda);
                }
            }
        });

        holder.ivOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionsMenu(v,tienda);
            }
        });

    }

    @Override
    public int getItemCount() {
        return tiendaList.size();
    }

    public void setTiendas(List<Tienda> tiendas) {
        this.tiendaList = tiendas;
        notifyDataSetChanged(); // Notificar al RecyclerView que los datos han cambiado
    }
    public void setOnItemClickListener(TiendaAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
    public interface OnItemClickListener {
        void onItemClick(Tienda tienda);
    }
    private void showOptionsMenu(View view, Tienda tienda) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.options_menu_tienda); // Reemplaza con tu propio menú de opciones

        // Manejar clic en los elementos del menú
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_tienda_delete:

                        deleteItem(tienda);
                        // Notifica al adaptador que el conjunto de datos ha cambiado
                        notifyDataSetChanged();
                        return true;
                    case R.id.menu_tienda_modificar:
                        notifyDataSetChanged();
                        return true;
                    case R.id.menu_tienda_agregar_producto:
                        notifyDataSetChanged();
                        return true;

                    default:
                        return false;
                }
            }
        });

        // Mostrar el menú contextual
        popupMenu.show();
    }
    private void deleteItem(Tienda tienda) {
        // Obtén la referencia a la ubicación del elemento que deseas eliminar en la base de datos
        DatabaseReference tiendaRef = FirebaseDatabase.getInstance().getReference().child("tiendas").child(tienda.getId());

        // Llama al método removeValue() para eliminar el elemento
        tiendaRef.removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Eliminación exitosa
                        // Aquí puedes mostrar un mensaje de éxito o realizar alguna acción adicional si es necesario
                        Toast.makeText(context, "La tienda se eliminó correctamente", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error al eliminar la tienda
                        // Aquí puedes manejar el error, mostrar un mensaje de error o realizar alguna acción adicional si es necesario
                        Log.e("TiendaAdapter", "Error al eliminar la tienda: " + e.getMessage());
                        Toast.makeText(context, "Error al eliminar la tienda", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre;
        TextView tvDescripcion;
        ImageView ivOptions;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreTienda);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcionTienda);
            ivOptions = itemView.findViewById(R.id.ivOptions);
        }
    }

}
