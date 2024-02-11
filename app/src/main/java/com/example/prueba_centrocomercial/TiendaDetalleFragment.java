package com.example.prueba_centrocomercial;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
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

public class TiendaDetalleFragment extends Fragment {

    private TextView tvNombreTienda;
    private TextView tvDescripcionTienda;
    private TextView tvTelefonoTienda;
    private ImageView ivLlamar;
    private RecyclerView recyclerViewProductos;
    private ProductoAdapter productoAdapter;
    private DatabaseReference producRef;

    public TiendaDetalleFragment() {
        // Constructor vacío requerido
    }

    public static TiendaDetalleFragment newInstance(Tienda tienda) {
        TiendaDetalleFragment fragment = new TiendaDetalleFragment();
        Bundle args = new Bundle();
        args.putString("nombre", tienda.getNombre());
        args.putString("descripcion", tienda.getDescripcionUbicacion());
        args.putString("telefono", tienda.getTelefono());
        args.putString("idTienda",tienda.getId());
        args.putString("idusuario",tienda.getIdUsuarioEncargado());

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tienda_detalle, container, false);
        tvNombreTienda = view.findViewById(R.id.tvNombreTienda);
        tvDescripcionTienda = view.findViewById(R.id.tvDescripcionTienda);
        tvTelefonoTienda = view.findViewById(R.id.tvTelefonoTienda);
        ivLlamar = view.findViewById(R.id.ivLlamar);
        recyclerViewProductos = view.findViewById(R.id.recyclerViewProductos);
        String idTienda="";

        // Inicializar el adaptador para el RecyclerView
        productoAdapter = new ProductoAdapter(new ArrayList<Producto>(), getActivity());

        // Inicializar la referencia a la base de datos
        producRef = FirebaseDatabase.getInstance().getReference().child("productos");

        Bundle args = getArguments();
        if (args != null) {
            String nombre = args.getString("nombre", "");
            String descripcion = args.getString("descripcion", "");
            String telefono = args.getString("telefono", "");
            idTienda=args.getString("idTienda","");

            tvNombreTienda.setText(nombre);
            tvDescripcionTienda.setText(descripcion);
            tvTelefonoTienda.setText(telefono);

            // Configurar el listener para el icono de llamar
            ivLlamar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    llamar(telefono);
                }
            });

            // Configurar RecyclerView y adaptador para mostrar la lista de productos
            recyclerViewProductos.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerViewProductos.setAdapter(productoAdapter);
            obtenerListaDeProductos(idTienda);
        }

        return view;
    }


    // Método para iniciar una llamada telefónica
    private void llamar(String telefono) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + telefono));
        startActivity(intent);
    }
    private void obtenerListaDeProductos(String idCtienda) {
        // Hacer una consulta a la base de datos para obtener los datos de las tiendas del centro comercial específico
        producRef.orderByChild("idTienda").equalTo(idCtienda).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Producto> productos = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Producto Producto = dataSnapshot.getValue(Producto.class);
                    productos.add(Producto);
                }
                Log.d("TiendaDetalleFragment", "Cantidad de Productos obetenido: " + productos.size());
                productoAdapter.setProductos(productos); // Actualizar el adaptador con la lista de tiendas obtenida
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Producots", "Error al obtener las Productos: " + error.getMessage());
                // Manejar error de la base de datos, si es necesario
            }
        });
    }
}

