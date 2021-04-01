package com.oesmanalie.it.angkot.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.oesmanalie.it.angkot.Constant;
import com.oesmanalie.it.angkot.R;
import com.oesmanalie.it.angkot.application.LatLngInterpolator;
import com.oesmanalie.it.angkot.application.MarkerAnimation;

import java.net.URISyntaxException;

public class LihatHalteFragment extends Fragment implements OnMapReadyCallback {

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private GoogleMap mMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lihat_halte, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try {
            boolean success = googleMap.setMapStyle( MapStyleOptions.loadRawResourceStyle( getContext(), R.raw.style_json));
            if (!success) {
                Toast.makeText(getContext(),"Error Style parsing failed.", Toast.LENGTH_SHORT).show();
            }
        } catch (Resources.NotFoundException e) {
            Toast.makeText(getContext(),"Error Can't find style. Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        final Marker[] dataMarker = new Marker[3];
        Bundle bundle = getArguments();
        Double lat = bundle.getDouble("lat");
        Double lng = bundle.getDouble("lng");
        Toast.makeText(getContext(), lat + " " +lng, Toast.LENGTH_LONG).show();

        LatLng myLocation = new LatLng( lat, lng );
        if (dataMarker[0] == null) {
            dataMarker[0] = mMap.addMarker(new MarkerOptions().position(myLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.busstop)).title(bundle.getString("nama")));
            dataMarker[0].showInfoWindow();
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation , 18.0f));


/*
        if (ContextCompat.checkSelfPermission(LihatHalteFragment.super.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted. Request for permission
            Toast.makeText(getContext(), "Permission is not granted", Toast.LENGTH_LONG).show();

            if (ContextCompat.checkSelfPermission(LihatHalteFragment.super.getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.shouldShowRequestPermissionRationale(LihatHalteFragment.super.getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION)){
                    ActivityCompat.requestPermissions(LihatHalteFragment.super.getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }else{
                    ActivityCompat.requestPermissions(LihatHalteFragment.super.getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
            }
        } else {
            final Marker[] dataMarker = new Marker[3];
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(LihatHalteFragment.super.getContext());
            final LatLngInterpolator latLngInterpolator = new LatLngInterpolator.Spherical();


        }

 */
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults){
        switch (requestCode){
            case 1: {
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(LihatHalteFragment.super.getContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(LihatHalteFragment.super.getContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                        setSupirLocation();
                    }
                }else{
                    Toast.makeText(LihatHalteFragment.super.getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void setSupirLocation () {
        final Marker[] dataMarker = new Marker[3];
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(LihatHalteFragment.super.getContext());
        final LatLngInterpolator latLngInterpolator = new LatLngInterpolator.Spherical();

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                LatLng MyLocation = new LatLng(location.getLatitude(), location.getLongitude());
                if (dataMarker[0] == null)
                    dataMarker[0] = mMap.addMarker(new MarkerOptions().position( new LatLng(location.getLatitude(), location.getLongitude()) ).icon( BitmapDescriptorFactory.fromResource(R.drawable.car) ));
                else MarkerAnimation.animateMarkerToGB(dataMarker[0], MyLocation, latLngInterpolator);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MyLocation, 18.0f));
            }
        };
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }
}
