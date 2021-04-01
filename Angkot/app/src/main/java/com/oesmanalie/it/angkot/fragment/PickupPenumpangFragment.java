package com.oesmanalie.it.angkot.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.oesmanalie.it.angkot.Constant;
import com.oesmanalie.it.angkot.PickupAngkotActivity;
import com.oesmanalie.it.angkot.R;
import com.oesmanalie.it.angkot.application.Functions;
import com.oesmanalie.it.angkot.application.LatLngInterpolator;
import com.oesmanalie.it.angkot.application.MarkerAnimation;
import com.oesmanalie.it.angkot.generator.ServiceGenerator;
import com.oesmanalie.it.angkot.models.Points;
import com.oesmanalie.it.angkot.models.PostResponse;
import com.oesmanalie.it.angkot.networks.DataService;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PickupPenumpangFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DataService apiService;
    private FusedLocationProviderClient fusedLocationClient;
    View mapView;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    final LatLngInterpolator latLngInterpolator = new LatLngInterpolator.Spherical();
    private BottomSheetBehavior sheetBehavior;
    private ConstraintLayout bottom_sheet;
    private LatLng myLocation;
    private TextView textStatusPenumpang;

    final Marker[] markerSupir = new Marker[4];

    final Marker[] penumpangMarker = new Marker[3];

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(Constant.SOCKET_SERVER_URL);
        } catch (URISyntaxException e) {}
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mapView = inflater.inflate(R.layout.fragment_pickuppenumpang, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        bottom_sheet = mapView.findViewById(R.id.bottom_sheet_penumpang);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);

        sheetBehavior.setPeekHeight(90);
        sheetBehavior.setHideable(false);
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);



        mSocket.on("supir", onNewMessage);
        mSocket.connect();
        return mapView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        textStatusPenumpang = mapView.findViewById(R.id.text_status_penumpang);

        try {
            boolean success = googleMap.setMapStyle( MapStyleOptions.loadRawResourceStyle( getContext(), R.raw.style_json));
            if (!success) {
                Toast.makeText(getContext(),"Error Style parsing failed.", Toast.LENGTH_SHORT).show();
            }
        } catch (Resources.NotFoundException e) {
            Toast.makeText(getContext(),"Error Can't find style. Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        final Marker[] dataMarker = new Marker[3];
        fusedLocationClient = LocationServices.getFusedLocationProviderClient( PickupPenumpangFragment.super.getContext() );

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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
                layoutParams.setMargins(0, 0, 30, 100);

                fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 18.0f));
                        myLocation = new LatLng( location.getLatitude(), location.getLongitude() );
                    }
                });
            }
        } else { }


        final Bundle bundleExtra = getArguments();
        if(bundleExtra != null) {

            LatLng stopLatLng = new LatLng(bundleExtra.getDoubleArray("StopLatLng")[0], bundleExtra.getDoubleArray("StopLatLng")[1]);
            if (dataMarker[1] != null)
                dataMarker[1].remove();
            dataMarker[1] = mMap.addMarker(new MarkerOptions().position( stopLatLng ).title("Stop Point").icon(BitmapDescriptorFactory.fromResource(R.drawable.berhenti)));



            LatLng destLatLng = new LatLng(bundleExtra.getDoubleArray("DestLatLng")[0], bundleExtra.getDoubleArray("DestLatLng")[1]);
            if (dataMarker[2] != null)
                dataMarker[2].remove();
            dataMarker[2] = mMap.addMarker(new MarkerOptions().position( destLatLng ).title("Destination Point").icon(BitmapDescriptorFactory.fromResource(R.drawable.tujuan)));

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
                    Toast.makeText(getContext(), "Error " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(Constant.TIME_UPDATE_LOKASI_PENUMPANG);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                myLocation = new LatLng(location.getLatitude(), location.getLongitude());
            }
        };
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);

        Button btnPenumpang = mapView.findViewById(R.id.button_daftar_sebagai_penumpang);
        btnPenumpang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                apiService = ServiceGenerator.createService(DataService.class);
                Call<List<Points>> pointsAll = apiService.getLatLng( bundleExtra.getString("rute") );
                pointsAll.enqueue(new Callback<List<Points>>() {
                    @Override
                    public void onResponse(Call<List<Points>> call, Response<List<Points>> response) {
                        final List<Points> pointList = response.body();
                        List<Points> listClosestPoint2 = Functions.getListClosestPoint2(pointList, myLocation);
                        if( listClosestPoint2.size() > 0 ) {
                            if (penumpangMarker[1] != null)
                                penumpangMarker[1].remove();
                            penumpangMarker[1] = mMap.addMarker(new MarkerOptions().position( listClosestPoint2.get(0).getLatLng() ).title("Titik Berangkat").icon(BitmapDescriptorFactory.fromResource(R.drawable.berangkat)));
                            //penumpangMarker[1].showInfoWindow();
                            String startPoint = listClosestPoint2.get(0).getLat() + "," + listClosestPoint2.get(0).getLon();
                            String stopPoint = bundleExtra.getDoubleArray("StopLatLng")[0] + "," + bundleExtra.getDoubleArray("StopLatLng")[1];
                            String destinationPoint = bundleExtra.getDoubleArray("DestLatLng")[0] + "," + bundleExtra.getDoubleArray("DestLatLng")[1];
                            String namaRute = bundleExtra.getString("rute");
                            String penumpangId = bundleExtra.getString("penumpangId");
                            PostPenumpang( startPoint,  destinationPoint, stopPoint, namaRute, penumpangId);

                        } else {
                            textStatusPenumpang.setText("Lokasi anda terlalu jauh");
                            //Toast.makeText(getContext(), "Lokasi anda terlalu jauh" , Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<List<Points>> call, Throwable t) {
                        Toast.makeText(getContext(), "Error " + t.getMessage() , Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    private void PostPenumpang(String startPoint, String stopPoint, String destinationPoint, String namaRute, String penumpangId  ) {
        apiService = ServiceGenerator.createService(DataService.class);
        Call<PostResponse> responseCall = apiService.postPenumpang( startPoint,destinationPoint, stopPoint, namaRute , penumpangId );
        responseCall.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                PostResponse result = response.body();
                textStatusPenumpang.setText("Angkot sedang menuju ke lokasi berangkat anda");
                //Toast.makeText(getContext(), result.getMsg(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Toast.makeText( getContext(), "Error:" + t.getMessage(), Toast.LENGTH_SHORT ).show();
            }
        });
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String lat;
                    String lng;
                    JSONObject data = (JSONObject) args[0];

                    try {
                        lat = data.getString("lat");
                        lng = data.getString("lng");
                    } catch (JSONException e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
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