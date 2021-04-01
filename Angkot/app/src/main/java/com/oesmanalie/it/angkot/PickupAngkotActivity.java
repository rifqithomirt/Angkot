package com.oesmanalie.it.angkot;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.github.nkzawa.emitter.Emitter;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.oesmanalie.it.angkot.application.LatLngInterpolator;
import com.oesmanalie.it.angkot.application.MarkerAnimation;
import com.oesmanalie.it.angkot.fragment.BerandaFragment;
import com.oesmanalie.it.angkot.generator.ServiceGenerator;
import com.oesmanalie.it.angkot.models.Points;
import com.oesmanalie.it.angkot.networks.DataService;
import com.google.android.gms.location.FusedLocationProviderClient;


import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

public class PickupAngkotActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DataService apiService;
    private FusedLocationProviderClient fusedLocationClient;
    View mapView;
    private Integer idAngkot = 1;
    private String lynJenis = "A";
    private final JSONObject obj = new JSONObject();
    final LatLngInterpolator latLngInterpolator = new LatLngInterpolator.Spherical();

    final Marker[] markerSupir = new Marker[3];

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(Constant.SOCKET_SERVER_URL);
        } catch (URISyntaxException e) {}
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_angkot);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mSocket.on("supir", onNewMessage);
        mSocket.connect();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try {
            boolean success = googleMap.setMapStyle( MapStyleOptions.loadRawResourceStyle( getApplicationContext(), R.raw.style_json));
            if (!success) {
                Toast.makeText(getApplicationContext(),"Error Style parsing failed.", Toast.LENGTH_SHORT).show();
            }
        } catch (Resources.NotFoundException e) {
            Toast.makeText(getApplicationContext(),"Error Can't find style. Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        final Marker[] dataMarker = new Marker[3];
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

            if (mapView != null &&  mapView.findViewById(Integer.parseInt("1")) != null) {
                // Get the button view
                View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
                // and next place it, on bottom right (as Google Maps app)
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                        locationButton.getLayoutParams();
                // position on right bottom
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                layoutParams.setMargins(0, 0, 50, 200);

                fusedLocationClient.getLastLocation().addOnSuccessListener(PickupAngkotActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 18.0f));
                    }
                });
            }
        } else { }

        Bundle bundleExtra = getIntent().getExtras();
        String value = ""; // or other values
        if(bundleExtra != null) {
            value = bundleExtra.getString("rute");

            LatLng stopLatLng = new LatLng(bundleExtra.getDoubleArray("StopLatLng")[0], bundleExtra.getDoubleArray("StopLatLng")[1]);
            if (dataMarker[1] != null)
                dataMarker[1].remove();
            dataMarker[1] = mMap.addMarker(new MarkerOptions().position( stopLatLng ).title("Stop Point"));

            LatLng destLatLng = new LatLng(bundleExtra.getDoubleArray("DestLatLng")[0], bundleExtra.getDoubleArray("DestLatLng")[1]);
            if (dataMarker[2] != null)
                dataMarker[2].remove();
            dataMarker[2] = mMap.addMarker(new MarkerOptions().position( destLatLng ).title("Destination Point").icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder)));

            apiService = ServiceGenerator.createService(DataService.class);
            Call<List<Points>> pointsCall = apiService.getLatLng( bundleExtra.getString("rute") );
            pointsCall.enqueue(new Callback<List<Points>>() {
                @Override
                public void onResponse(Call<List<Points>> call, Response<List<Points>> response) {
                    final List<Points> pointList = response.body();

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
                }

                @Override
                public void onFailure(Call<List<Points>> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Error " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            PickupAngkotActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String lat;
                    String lng;
                    JSONObject data = (JSONObject) args[0];

                    try {
                        lat = data.getString("lat");
                        lng = data.getString("lng");
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        return;
                    }

                    LatLng MyLocation = new LatLng( Double.valueOf(lat) , Double.valueOf(lng));
                    if (markerSupir[0] == null)
                        markerSupir[0] = mMap.addMarker(new MarkerOptions().position( MyLocation ).icon( BitmapDescriptorFactory.fromResource(R.drawable.car) ));
                    else MarkerAnimation.animateMarkerToGB(markerSupir[0], MyLocation, latLngInterpolator);
                }
            });
        }
    };
}
