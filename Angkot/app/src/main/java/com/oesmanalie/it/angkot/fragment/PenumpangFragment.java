package com.oesmanalie.it.angkot.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.oesmanalie.it.angkot.Constant;
import com.oesmanalie.it.angkot.R;
import com.oesmanalie.it.angkot.application.LatLngInterpolator;
import com.oesmanalie.it.angkot.application.MarkerAnimation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class PenumpangFragment extends Fragment implements OnMapReadyCallback {

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private GoogleMap mMap;
    private Integer idAngkot = 1;
    private String lynJenis = "A";
    private final JSONObject obj = new JSONObject();

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(Constant.SOCKET_SERVER_URL);
        } catch (URISyntaxException e) {}
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_penumpang, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mSocket.on("penumpang", onNewMessage);
        mSocket.connect();

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(PenumpangFragment.super.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted. Request for permission
            Toast.makeText(getContext(), "Permission is not granted", Toast.LENGTH_LONG).show();

            if (ContextCompat.checkSelfPermission(PenumpangFragment.super.getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.shouldShowRequestPermissionRationale(PenumpangFragment.super.getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION)){
                    ActivityCompat.requestPermissions(PenumpangFragment.super.getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }else{
                    ActivityCompat.requestPermissions(PenumpangFragment.super.getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
            }
        } else {
            final Marker[] dataMarker = new Marker[3];
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(PenumpangFragment.super.getContext());
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
                    try {
                        obj.put("idAngkot", idAngkot);
                        obj.put("lynJenis", lynJenis);
                        obj.put("lat", location.getLatitude());
                        obj.put("lng", location.getLongitude());
                    } catch (Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    mSocket.emit("supir", obj);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MyLocation, 18.0f));
                }
            };
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode){
            case 1: {
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(PenumpangFragment.super.getContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(PenumpangFragment.super.getContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                        setSupirLocation();
                    }
                }else{
                    Toast.makeText(PenumpangFragment.super.getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void setSupirLocation () {
        final Marker[] dataMarker = new Marker[3];
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(PenumpangFragment.super.getContext());
        final LatLngInterpolator latLngInterpolator = new LatLngInterpolator.Spherical();

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(Constant.TIME_UPDATE_LOKASI_SUPIR);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                LatLng MyLocation = new LatLng(location.getLatitude(), location.getLongitude());
                if (dataMarker[0] == null)
                    dataMarker[0] = mMap.addMarker(new MarkerOptions().position( new LatLng(location.getLatitude(), location.getLongitude()) ).icon( BitmapDescriptorFactory.fromResource(R.drawable.car) ));
                else MarkerAnimation.animateMarkerToGB(dataMarker[0], MyLocation, latLngInterpolator);
                try {
                    obj.put("idAngkot", idAngkot);
                    obj.put("lynJenis", lynJenis);
                    obj.put("lat", location.getLatitude());
                    obj.put("lng", location.getLongitude());
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
                mSocket.emit("supir", obj);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MyLocation, 18.0f));
            }
        };
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //String lat;
                    //String lng;
                    //JSONObject data = (JSONObject) args[0];

                    //try {
                    //    lat = data.getString("lat");
                    //    lng = data.getString("lng");
                    //} catch (JSONException e) {
                    //    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    //    return;
                    //}
                    //Toast.makeText(getContext(), lat, Toast.LENGTH_LONG).show();
                }
            });
        }
    };
}
