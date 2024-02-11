package com.example.prueba_centrocomercial;


import android.content.Context;
import android.content.Intent;
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

import com.example.prueba_centrocomercial.Entidades.CentroComercial;
import com.example.prueba_centrocomercial.Entidades.Tienda;
import com.google.android.gms.maps.MapView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CentroComercialDetalleFragment extends Fragment {

    private TextView tvMallName;
    private TextView tvLocation;
    private ImageView ivLogo;
    private RecyclerView recyclerView;
    private TiendaAdapter tiendaAdapter;
    private DatabaseReference TiendasRef; // Referencia a la base de datos de las tiendas
    private MapView mapView;
    private double latitud;
    private double longitud;
    private Context context;
    private  boolean islogin;

    private String idUsuario;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
    public CentroComercialDetalleFragment(boolean islogin,String idUsuario) {
        this.islogin=islogin;
        this.idUsuario=idUsuario;
    }
    public CentroComercialDetalleFragment() {
        // Constructor vacío requerido
    }

    public static CentroComercialDetalleFragment newInstance(CentroComercial centroComercial,boolean islogin,String idUsuario) {
        CentroComercialDetalleFragment fragment = new CentroComercialDetalleFragment();
        Bundle args = new Bundle();
        args.putString("mallName", centroComercial.getNombre());
        args.putString("location", centroComercial.getUbicacion());
        args.putString("logoUrl", centroComercial.getLogoUrl());
        args.putString("idCentroCOmercial",centroComercial.getId());
        args.putDouble("latitud",centroComercial.getLatitud());
        args.putDouble("longitud",centroComercial.getLongitud());
        args.putBoolean("isLogin", islogin);
        args.putString("idUsuario", idUsuario);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("CentroComercialDetalleFragment", "Fragmento en el layout: fragment_centro_comercial_detalle");
        View view = inflater.inflate(R.layout.fragment_centro_comercial_detalle, container, false);
        tvMallName = view.findViewById(R.id.tvMallNameDetalle);
        tvLocation = view.findViewById(R.id.tvLocationDetalle);
        ivLogo = view.findViewById(R.id.ivLogoDetalle);
        recyclerView = view.findViewById(R.id.recyclerView);
        String centroComercialId="";

        // Inicializar la referencia a la base de datos de las tiendas
        TiendasRef = FirebaseDatabase.getInstance().getReference().child("tiendas");

        Bundle args = getArguments();
        if (args != null) {
            String mallName = args.getString("mallName", "");
            String location = args.getString("location", "");
            String logoUrl = args.getString("logoUrl", "");
            centroComercialId = args.getString("idCentroCOmercial", "");
            latitud= args.getDouble("latitud",0);
            longitud= args.getDouble("longitud",0);
            this.islogin=args.getBoolean("isLogin",false);
            this.idUsuario=args.getString("idUsuario","");

            tvMallName.setText(mallName);
            tvLocation.setText(location);
            Picasso.get().load(logoUrl).into(ivLogo);
        }

        // Configurar el RecyclerView y el adaptador
        System.out.println("Esto antes de la instancia"+this.islogin);
        if (this.islogin){
            tiendaAdapter = new TiendaAdapter(new ArrayList<>(), getContext(),this.islogin,this.idUsuario);
        }else{
            tiendaAdapter= new TiendaAdapter(new ArrayList<>(), getContext());
        }
         // Inicializar el adaptador con una lista vacía
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(tiendaAdapter);

        // Obtener la lista de tiendas del centro comercial
        obtenerListaDeTiendas(centroComercialId); // Llamar al método para obtener los datos de la base de datos
        tiendaAdapter.setOnItemClickListener(new TiendaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Tienda tienda) {
                Log.d("Click", "Di click: ");
                mostrarDetalleTienda(tienda);

            }
        });
        tiendaAdapter.setOnAgregarProductoClickListener(new TiendaAdapter.OnAgregarProductoClickListener() {
            @Override
            public void onAgregarProductoClick(String tiendaId, String usuarioId) {
                agregarProducot(tiendaId,usuarioId);
            }
        });


        return view;
    }

    // Método para obtener la lista de tiendas de la base de datos
    private void obtenerListaDeTiendas(String idCentroComercial) {
        // Hacer una consulta a la base de datos para obtener los datos de las tiendas del centro comercial específico
        TiendasRef.orderByChild("idCentroComercial").equalTo(idCentroComercial).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Tienda> tiendas = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Tienda tienda = dataSnapshot.getValue(Tienda.class);
                    tiendas.add(tienda);
                }
                Log.d("Tiendas", "Cantidad de tiendas obtenidas: " + tiendas.size());
                tiendaAdapter.setTiendas(tiendas); // Actualizar el adaptador con la lista de tiendas obtenida
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Tiendas", "Error al obtener las tiendas: " + error.getMessage());
                // Manejar error de la base de datos, si es necesario
            }
        });
    }
    private void mostrarDetalleTienda(Tienda tienda) {
        // Oculta la lista (RecyclerView)
        recyclerView.setVisibility(View.GONE);

        // Crea una instancia del Fragment de detalle y pasa la información del centro comercial
        TiendaDetalleFragment detalleFragment = TiendaDetalleFragment.newInstance(tienda);

        // Reemplaza el contenido del contenedor principal con el Fragment de detalle
        getParentFragmentManager().beginTransaction()
                .replace(R.id.contenedor_principal, detalleFragment)
                .addToBackStack(null)
                .commit();
    }
    private void agregarProducot(String idTienda, String idUsuario) {
        Intent intent = new Intent(context, AgregarProducto.class);
        intent.putExtra("idTienda", idTienda);
        intent.putExtra("idUsuario", idUsuario);// Puedes pasar cualquier otro dato necesario
        context.startActivity(intent);

    }



}
