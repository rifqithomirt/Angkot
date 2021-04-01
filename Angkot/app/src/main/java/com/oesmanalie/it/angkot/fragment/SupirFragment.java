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
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
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
import com.oesmanalie.it.angkot.Constant;
import com.oesmanalie.it.angkot.R;
import com.oesmanalie.it.angkot.application.Functions;
import com.oesmanalie.it.angkot.application.LatLngInterpolator;
import com.oesmanalie.it.angkot.application.MarkerAnimation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.util.Function;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.oesmanalie.it.angkot.generator.ServiceGenerator;
import com.oesmanalie.it.angkot.models.PenumpangPosition;
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

import static com.oesmanalie.it.angkot.application.Functions.funGetDecimal;

public class SupirFragment extends Fragment implements OnMapReadyCallback {

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private GoogleMap mMap;
    private Integer idAngkot = 1;
    private String lynJenis = "A";
    private final JSONObject obj = new JSONObject();
    private DataService apiService;
    private View view;

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(Constant.SOCKET_SERVER_URL);
        } catch (URISyntaxException e) {}
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_supir, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mSocket.on("penumpang", onNewMessage);
        mSocket.connect();

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

        if (ContextCompat.checkSelfPermission(SupirFragment.super.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted. Request for permission
            Toast.makeText(getContext(), "Permission is not granted", Toast.LENGTH_LONG).show();

            if (ContextCompat.checkSelfPermission(SupirFragment.super.getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.shouldShowRequestPermissionRationale(SupirFragment.super.getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION)){
                    ActivityCompat.requestPermissions(SupirFragment.super.getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }else{
                    ActivityCompat.requestPermissions(SupirFragment.super.getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
            }
        } else {
            /*
            final Marker[] dataMarker = new Marker[3];
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(SupirFragment.super.getContext());
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
            */
            setSupirLocation();


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode){
            case 1: {
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(SupirFragment.super.getContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(SupirFragment.super.getContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                        setSupirLocation();
                    }
                }else{
                    Toast.makeText(SupirFragment.super.getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void setSupirLocation () {
        final Marker[] dataMarker = new Marker[3];
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(SupirFragment.super.getContext());
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
                Bundle bundle = getArguments();
                try {
                    obj.put("idAngkot", idAngkot);
                    obj.put("lynJenis", lynJenis);
                    obj.put("lat", location.getLatitude());
                    obj.put("lng", location.getLongitude());
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
                mSocket.emit("supir", obj);
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MyLocation, 18.0f));
            }
        };
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        getJumlahPenumpang();
    }

    private void getJumlahPenumpang(){
        final TextView textJumlahPenumpang = view.findViewById(R.id.textJumlahPenumpang);
        Bundle bundle = getArguments();
        apiService = ServiceGenerator.createService(DataService.class);
        Call<List<PenumpangPosition>> responseCall = apiService.getPenumpang( bundle.getString("nama"));
        responseCall.enqueue(new Callback<List<PenumpangPosition>>() {
            @Override
            public void onResponse(Call<List<PenumpangPosition>> call, Response<List<PenumpangPosition>> response) {
                List<PenumpangPosition> penumpang = response.body();
                textJumlahPenumpang.setText( String.valueOf( penumpang.size() ) );
                if( penumpang.size() > 0 ) {
                    String ruteId = penumpang.get(0).getRuteID();
                    setPolyLine( ruteId );
                    Marker[] markerPenumpang = new Marker[penumpang.size()];

                    for( PenumpangPosition position : penumpang ) {
                        String[] arrLatLng = position.getStartPoint().split(",");
                        LatLng penumpangLatLng = new LatLng( Double.valueOf(arrLatLng[0]) , Double.valueOf(arrLatLng[1]) );
                        mMap.addMarker(new MarkerOptions().position( penumpangLatLng ).icon(BitmapDescriptorFactory.fromResource(R.drawable.penumpangmap)));
                    }
                }
            }
            @Override
            public void onFailure(Call<List<PenumpangPosition>> call, Throwable t) {
                Toast.makeText(getContext(), "Error:" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setPolyLine( String ruteid ) {
        Call<List<Points>> pointsCall = apiService.getLatLngByRuteId( ruteid );
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

                int index = 0;
                for( Double[] lattitude: Constant.HALTE_LATITUDE ) {
                    LatLng latlng = new LatLng( (funGetDecimal(lattitude) * -1), funGetDecimal( Constant.HALTE_LONGITUDE[ index ] )  );
                    mMap.addMarker(new MarkerOptions().position( latlng ).title("Halte").icon( BitmapDescriptorFactory.fromResource(R.drawable.busstop) ));
                    index++;
                }
            }
            @Override
            public void onFailure(Call<List<Points>> call, Throwable t) {
                Toast.makeText(getContext() , "Error:" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
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
