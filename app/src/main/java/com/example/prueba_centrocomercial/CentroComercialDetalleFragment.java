package com.example.prueba_centrocomercial;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class CentroComercialDetalleFragment extends Fragment {

    private TextView tvMallName;
    private TextView tvLocation;
    private ImageView ivLogo;

    private LinearLayout detalleLayout;
    private RecyclerView recyclerView;


    public CentroComercialDetalleFragment() {
        // Constructor vacío requerido
    }

    // Método para crear una nueva instancia del fragmento y pasar datos si es necesario
    public static CentroComercialDetalleFragment newInstance(CentroComercial centroComercial) {
        CentroComercialDetalleFragment fragment = new CentroComercialDetalleFragment();
        Bundle args = new Bundle();
        args.putString("mallName", centroComercial.getNombre());
        args.putString("location", centroComercial.getUbicacion());
        args.putString("logoUrl", centroComercial.getLogoUrl());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_centro_comercial_detalle, container, false);
        tvMallName = view.findViewById(R.id.tvMallNameDetalle);
        tvLocation = view.findViewById(R.id.tvLocationDetalle);
        ivLogo = view.findViewById(R.id.ivLogoDetalle);

        // Obtener datos del argumento
        Bundle args = getArguments();
        if (args != null) {
            String mallName = args.getString("mallName", "");
            String location = args.getString("location", "");
            String logoUrl = args.getString("logoUrl", "");

            // Mostrar los datos en las vistas
            tvMallName.setText(mallName);
            tvLocation.setText(location);
            Picasso.get().load(logoUrl).into(ivLogo);
        }

        return view;
    }
}