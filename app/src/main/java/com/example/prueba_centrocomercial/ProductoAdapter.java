package com.example.prueba_centrocomercial;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prueba_centrocomercial.Entidades.Producto;
import com.example.prueba_centrocomercial.Entidades.Tienda;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {

    private List<Producto> productos;
    private DatabaseReference productosRef;
    private Context context;

    private boolean islogin;
    private  String idUsuario;

    public ProductoAdapter (List<Producto> productoList, Context context){
        this.productos=productoList;
        this.context=context;

    }



    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);
        return new ProductoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto producto = productos.get(position);
        holder.tvNombre.setText(producto.getNombre());
        holder.tvDescripcion.setText(producto.getDescripcion());
        // Agrega más configuraciones de vistas según necesites
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }
    public void setProductos(List<Producto> productos) {
        this.productos= productos;
        notifyDataSetChanged(); // Notificar al RecyclerView que los datos han cambiado
    }

    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNombre;
        public TextView tvDescripcion;

        public ProductoViewHolder(View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreProducto);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcionProducto);
            // Vincula aquí otras vistas según necesites
        }
    }
}

