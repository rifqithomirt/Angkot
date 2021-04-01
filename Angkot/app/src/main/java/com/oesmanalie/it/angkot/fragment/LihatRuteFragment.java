package com.oesmanalie.it.angkot.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.oesmanalie.it.angkot.CategoryActivity;
import com.oesmanalie.it.angkot.Constant;
import com.oesmanalie.it.angkot.HomePageActivity;
import com.oesmanalie.it.angkot.R;
import com.oesmanalie.it.angkot.application.LatLngInterpolator;
import com.oesmanalie.it.angkot.application.MarkerAnimation;
import com.oesmanalie.it.angkot.fragment.RuteFragment;
import com.oesmanalie.it.angkot.generator.ServiceGenerator;
import com.oesmanalie.it.angkot.models.Points;
import com.oesmanalie.it.angkot.models.Routes;
import com.oesmanalie.it.angkot.networks.DataService;
import com.oesmanalie.it.angkot.networks.OnBackPressed;

import java.net.URISyntaxException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LihatRuteFragment extends Fragment implements OnMapReadyCallback {
    private DataService apiService;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private GoogleMap mMap;
    private View view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_lihat_rute, container, false);
        final SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        /*
        OnBackPressedCallback callback = new OnBackPressedCallback(true ) {
            @Override
            public void handleOnBackPressed() {

                Toast.makeText(getContext(), "Back", Toast.LENGTH_SHORT).show();
            }
        };


        Integer cnt =  getActivity().getSupportFragmentManager().getBackStackEntryCount();
        Toast.makeText(getContext(),  "Count " + String.valueOf(cnt), Toast.LENGTH_SHORT).show();

         */

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Bundle bundleExtra = this.getArguments();

        TextView tvLyn = view.findViewById(R.id.tvLyn);
        tvLyn.setText(bundleExtra.getString("kodelyn"));
        mMap = googleMap;

        try {
            boolean success = googleMap.setMapStyle( MapStyleOptions.loadRawResourceStyle( getContext(), R.raw.style_json));
            if (!success) {
                Toast.makeText(getContext(),"Error Style parsing failed.", Toast.LENGTH_SHORT).show();
            }
        } catch (Resources.NotFoundException e) {
            Toast.makeText(getContext(),"Error Can't find style. Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        apiService = ServiceGenerator.createService(DataService.class);

        final TextView keteranganRute = view.findViewById(R.id.tvRut);

        Call<Routes> ruteCall = apiService.getDataRute( bundleExtra.getString("kodelyn") );
        ruteCall.enqueue(new Callback<Routes>() {
            @Override
            public void onResponse(Call<Routes> call, Response<Routes> response) {
                Routes rute = response.body();
                keteranganRute.setText(rute.getKeterangan());
            }

            @Override
            public void onFailure(Call<Routes> call, Throwable t) {

            }
        });

        Call<List<Points>> pointsCall = apiService.getLatLng( bundleExtra.getString("kodelyn") );
        pointsCall.enqueue(new Callback<List<Points>>() {
            @Override
            public void onResponse(Call<List<Points>> call, Response<List<Points>> response) {
                final List<Points> pointList = response.body();

                //Toast.makeText(getContext(), String.valueOf(pointList.size()), Toast.LENGTH_SHORT).show();

                PolylineOptions polylineOptions = new PolylineOptions();
                LatLngBounds.Builder builder = new LatLngBounds.Builder();

                for (Points checkPoint : pointList) {
                    polylineOptions.add(checkPoint.getLatLng());
                    builder.include(checkPoint.getLatLng());
                }

                mMap.addPolyline(polylineOptions.color(Color.argb(120, 255,0,0)));
                // Center camera on past run
                LatLngBounds bounds = builder.build();
                int padding = 20;
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                mMap.animateCamera(cameraUpdate);

                int index = 0;
                for( Double[] lattitude: Constant.HALTE_LATITUDE ) {
                    LatLng latlng = new LatLng( (funGetDecimal(lattitude) * -1), funGetDecimal( Constant.HALTE_LONGITUDE[ index ] )  );
                    mMap.addMarker(new MarkerOptions().position( latlng ).title("Halte").icon( BitmapDescriptorFactory.fromResource(R.drawable.busstop) ));
                    index++;
                }

            }

            @Override
            public void onFailure(Call<List<Points>> call, Throwable t) {
                Toast.makeText(LihatRuteFragment.super.getContext() , "Error " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private double funGetDecimal( Double[] dms ){
        double decimal;
        decimal = ((dms[1] * 60)+ dms[2]) / (60*60);
        return dms[0] + decimal;
    }



}
