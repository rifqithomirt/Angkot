package com.oesmanalie.it.angkot.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


import com.google.android.gms.location.FusedLocationProviderClient;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.oesmanalie.it.angkot.models.AngkotModel;
import com.oesmanalie.it.angkot.models.AngkotSheet;
import com.oesmanalie.it.angkot.Constant;
import com.oesmanalie.it.angkot.R;
import com.oesmanalie.it.angkot.application.Functions;
import com.oesmanalie.it.angkot.generator.ServiceGenerator;
import com.oesmanalie.it.angkot.models.Points;
import com.oesmanalie.it.angkot.networks.DataService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BerandaFragment extends Fragment implements OnMapReadyCallback, AngkotSheet.OnAngkotClickListener {
    private DataService apiService;
    private GoogleMap mMap;
    private BottomNavigationView bottomNavigationView;
    private View view;
    private FusedLocationProviderClient fusedLocationClient;
    public List<AngkotModel> listAngkot = new ArrayList<>();
    public List<Points> listPoints1 = new ArrayList<>();
    public LatLng latLngPoint;
    private BottomSheetBehavior sheetBehavior;
    private ConstraintLayout bottom_sheet;
    public AngkotSheet angkotSheet;
    public RecyclerView rv;
    public RecyclerView.LayoutManager layoutManager;
    Bundle bundleSearch;

    View mapView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_beranda, container, false);

        bundleSearch = getArguments();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapView = mapFragment.getView();
        mapFragment.getMapAsync(this);

        bottom_sheet = view.findViewById(R.id.bottom_sheet_angkot);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);

        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        return view;
    }

    public void showBottomSheet( List<Points> listPoint, LatLng destPoint) {
        latLngPoint = destPoint;
        listAngkot.clear();
        listPoints1.clear();
        for( Points angkotPoint : listPoint ) {
            listAngkot.add( new AngkotModel(angkotPoint.getLyn(), "") );
            listPoints1.add(angkotPoint);
        }
        rv = view.findViewById(R.id.recycler_view);
        angkotSheet = new AngkotSheet(listAngkot);
        angkotSheet.setListener(this);
        layoutManager = new LinearLayoutManager(getContext());
        rv.setAdapter(angkotSheet);
        rv.setLayoutManager(layoutManager);
        sheetBehavior.setPeekHeight(120);
        sheetBehavior.setHideable(false);
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onClick(View view, int position) {

        Bundle fgBundle = new Bundle();
        fgBundle.putString("rute", listAngkot.get(position).getNamaAngkot() );
        fgBundle.putString("penumpangId", bundleSearch.getString("penumpangId"));
        double[] arr1 = new double[2];
        arr1[0] = listPoints1.get(position).getLat();
        arr1[1] = listPoints1.get(position).getLon();
        fgBundle.putDoubleArray( "StopLatLng",  arr1 );
        double[] arr2 = new double[2];
        arr2[0] = latLngPoint.latitude;
        arr2[1] = latLngPoint.longitude;
        fgBundle.putDoubleArray( "DestLatLng",  arr2 );

        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        PickupPenumpangFragment pickupPenumpangFragment = new PickupPenumpangFragment();
        pickupPenumpangFragment.setArguments( fgBundle );
        fragmentTransaction.setCustomAnimations(R.anim.fragment_slide_left_enter,
                R.anim.fragment_slide_left_exit,
                R.anim.fragment_slide_right_enter,
                R.anim.fragment_slide_right_exit);
        fragmentTransaction.add(R.id.beranda_fragment_layout, pickupPenumpangFragment).addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void funLocation () {

        try {
            boolean success = mMap.setMapStyle( MapStyleOptions.loadRawResourceStyle( getContext(), R.raw.style_json));
            if (!success) {
                Toast.makeText(getContext(),"Error Style parsing failed.", Toast.LENGTH_SHORT).show();
            }
        } catch (Resources.NotFoundException e) {
            Toast.makeText(getContext(),"Error Can't find style. Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(BerandaFragment.super.getContext());


        if (ContextCompat.checkSelfPermission(BerandaFragment.super.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

            if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
                // Get the button view
                View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
                // and next place it, on bottom right (as Google Maps app)
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
                // position on right bottom
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                layoutParams.setMargins(0, 0, 50, 200);

                fusedLocationClient.getLastLocation().addOnSuccessListener(BerandaFragment.super.getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if( location != null ) mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 18.0f));
                    }
                });
            }

        } else {
            // Show rationale and request permission.
        }

        final LinearLayout destButton =  view.findViewById(R.id.button_destination);
        final LinearLayout destLayoutButton = view.findViewById(R.id.layout_setdestination);
        final LinearLayout destLayoutLabel = view.findViewById(R.id.layout_destinationLabel);
        final LinearLayout pinLayout = view.findViewById(R.id.layout_pintujuan);
        final SearchView searchViewDestination = view.findViewById(R.id.search_destination);

        final Marker[] dataMarker = new Marker[2];
        final Points[] closestStopPoint = new Points[3];
        final LatLng[] destPoint = new LatLng[1];
        List<Points> listClosestPoint = new ArrayList<>();

        destLayoutButton.setVisibility(LinearLayout.GONE);


        apiService = ServiceGenerator.createService(DataService.class);
        Call<List<Points>> pointsCall = apiService.getLatLng( "all");

        pointsCall.enqueue(new Callback<List<Points>>() {
            @Override
            public void onResponse(Call<List<Points>> call, Response<List<Points>> response) {
                final List<Points> pointList = response.body();

                if( bundleSearch.containsKey("arraysearch")  ) {

                    Integer PositionSearch = bundleSearch.getInt("arraysearch");
                    //Toast.makeText(getContext(), String.valueOf( PositionSearch), Toast.LENGTH_LONG).show();
                    Points newDest = pointList.get(PositionSearch);

                    LatLng newPointDest = newDest.getLatLng();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newPointDest, 16.0f));
                    funGetDest( newPointDest, pointList );
                } else {
                    LatLng firstPoint = new LatLng(Constant.CENTER_LAT, Constant.CENTER_LNG);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstPoint, 16.0f));
                }

                destButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        destPoint[0] = new LatLng(mMap.getCameraPosition().target.latitude, mMap.getCameraPosition().target.longitude);
                        closestStopPoint[0] = Functions.getClosestPoint(pointList, destPoint[0]);

                        List<Points> listClosestPoint2 = Functions.getListClosestPoint2(pointList, destPoint[0]);



                        if( listClosestPoint2.size() != 0 ) {
                            //Toast.makeText(getApplicationContext(), "Success, You can use Angkot " + closestStopPoint[0].getLyn(), Toast.LENGTH_LONG).show();
                            if (dataMarker[0] != null)
                                dataMarker[0].remove();
                            dataMarker[0] = mMap.addMarker(new MarkerOptions().position(destPoint[0]).title("Destination Point").icon(BitmapDescriptorFactory.fromResource(R.drawable.tujuan)));
                            //destLabel.setText(mMap.getCameraPosition().target.latitude + " " + mMap.getCameraPosition().target.longitude);
                            destLayoutButton.setVisibility(LinearLayout.GONE);

                            if (dataMarker[1] != null)
                                dataMarker[1].remove();
                            dataMarker[1] = mMap.addMarker(new MarkerOptions().position( closestStopPoint[0].getLatLng() ).title("Stop Point").icon(BitmapDescriptorFactory.fromResource(R.drawable.berhenti)));

                            showBottomSheet( listClosestPoint2 , destPoint[0 ]);

                        } else {
                            Toast.makeText(BerandaFragment.super.getContext(), "Diluar Jangkauan", Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }

            @Override
            public void onFailure(Call<List<Points>> call, Throwable t) {
                Toast.makeText(BerandaFragment.super.getContext(), "Error " + t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });

        destLayoutLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destLayoutButton.setVisibility(LinearLayout.VISIBLE);
            }
        });

        pinLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchViewDestination.clearFocus();
                if( destLayoutButton.getVisibility() == View.VISIBLE )
                    destLayoutButton.setVisibility(View.GONE);
                else destLayoutButton.setVisibility(View.VISIBLE);
            }
        });

        searchViewDestination.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                searchViewDestination.clearFocus();
                if( destLayoutButton.getVisibility() == View.VISIBLE )
                    destLayoutButton.setVisibility(View.GONE);
                if( hasFocus  ) {

                    FragmentManager fragmentManager = getParentFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    SearchingFragment searchingFragment = new SearchingFragment();
                    fragmentTransaction.setCustomAnimations(R.anim.fragment_slide_left_enter,
                            R.anim.fragment_slide_left_exit,
                            R.anim.fragment_slide_right_enter,
                            R.anim.fragment_slide_right_exit);
                    fragmentTransaction.add(R.id.beranda_fragment_layout, searchingFragment).addToBackStack(null);
                    fragmentTransaction.commit();

                    //Intent searchActivity = new Intent( BerandaFragment.super.getContext(), SearchingActivity.class);
                    //startActivity(searchActivity);
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(BerandaFragment.super.getContext());

        if (ContextCompat.checkSelfPermission(BerandaFragment.super.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted. Request for permission
            Toast.makeText(BerandaFragment.super.getContext(), "Permission is not granted", Toast.LENGTH_LONG).show();
            if (ContextCompat.checkSelfPermission(BerandaFragment.super.getContext(), Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.shouldShowRequestPermissionRationale(BerandaFragment.super.getActivity(), Manifest.permission.INTERNET)){
                    ActivityCompat.requestPermissions(BerandaFragment.super.getActivity(), new String[]{Manifest.permission.INTERNET}, 1);
                    //funLocation();
                }else{
                    ActivityCompat.requestPermissions(BerandaFragment.super.getActivity(), new String[]{Manifest.permission.INTERNET}, 1);
                }
            }
        } else {
            funLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1: {
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(BerandaFragment.super.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                        funLocation();
                    }
                }else{
                    Toast.makeText(BerandaFragment.super.getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    public void funGetDest ( LatLng destPoint1, List<Points> pointsList ) {
        final Marker[] dataMarker = new Marker[2];
        final Points[] closestStopPoint = new Points[3];
        final LatLng[] destPoint = new LatLng[1];
        destPoint[0] = destPoint1;
        closestStopPoint[0] = Functions.getClosestPoint(pointsList, destPoint[0]);

        List<Points> listClosestPoint2 = Functions.getListClosestPoint2(pointsList, destPoint[0]);

        if( listClosestPoint2.size() != 0 ) {
            //Toast.makeText(getApplicationContext(), "Success, You can use Angkot " + closestStopPoint[0].getLyn(), Toast.LENGTH_LONG).show();
            if (dataMarker[0] != null)
                dataMarker[0].remove();
            dataMarker[0] = mMap.addMarker(new MarkerOptions().position(destPoint[0]).title("Destination Point").icon(BitmapDescriptorFactory.fromResource(R.drawable.tujuan) ));
            //destLabel.setText(mMap.getCameraPosition().target.latitude + " " + mMap.getCameraPosition().target.longitude);
            //destLayoutButton.setVisibility(LinearLayout.GONE);

            if (dataMarker[1] != null)
                dataMarker[1].remove();
            dataMarker[1] = mMap.addMarker(new MarkerOptions().position( closestStopPoint[0].getLatLng() ).title("Stop Point").icon(BitmapDescriptorFactory.fromResource(R.drawable.berhenti)));

            showBottomSheet( listClosestPoint2 , destPoint[0 ]);

        } else {
            Toast.makeText(BerandaFragment.super.getContext(), "Diluar Jangkauan", Toast.LENGTH_LONG).show();
        }
    }
}
