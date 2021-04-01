package com.oesmanalie.it.angkot.fragment;
import android.os.Bundle;

import com.oesmanalie.it.angkot.adapter.ListViewAdapter;
import com.oesmanalie.it.angkot.R;
import com.oesmanalie.it.angkot.generator.ServiceGenerator;
import com.oesmanalie.it.angkot.models.Locations;
import com.oesmanalie.it.angkot.models.Points;
import com.oesmanalie.it.angkot.networks.DataService;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchingFragment extends Fragment {

    private String[] locations;
    private ArrayList<String> locationNames = new ArrayList<String>();
    private ListView list;
    private ListViewAdapter adapter;
    private android.widget.SearchView editsearch;
    private DataService apiService;
    public static ArrayList<Locations> locationsArrayList = new ArrayList<Locations>();
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_searching, container, false);

        //Toast.makeText(getContext(), String.valueOf(locationNames.size()), Toast.LENGTH_LONG).show();

        Log.d("DEBUUUUUG", "lewaat");

        apiService = ServiceGenerator.createService(DataService.class);
        Call<List<Points>> pointsCall = apiService.getLatLng( "all");

        pointsCall.enqueue(new Callback<List<Points>>() {
            @Override
            public void onResponse(Call<List<Points>> call, Response<List<Points>> response) {
                final List<Points> pointList = response.body();
                for( Points point : pointList ) {
                    if( point.getName() != null ) {
                        locationNames.add(point.getName());
                    }
                    //else Log.d("name", String.valueOf(point.getLat()));
                }

                Toast.makeText(getContext(), String.valueOf(locationNames.size()), Toast.LENGTH_LONG).show();
                list = view.findViewById(R.id.listview_fragment);
                locationsArrayList = new ArrayList<>();

                for( String name : locationNames ) {
                    Locations location = new Locations(name);
                    locationsArrayList.add(location);
                }

                adapter = new ListViewAdapter(getContext());
                list.setAdapter(adapter);

                editsearch = view.findViewById(R.id.search_peta_fragment);
                editsearch.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        String text = newText;
                        adapter.filter(text);
                        return false;
                    }
                });
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Bundle bundle = new Bundle();
                        bundle.putInt( "arraysearch" ,position);
                        bundle.putString("fragment", "action_beranda");

                        FragmentManager fragmentManager = getParentFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        BerandaFragment berandaFragment = new BerandaFragment();
                        berandaFragment.setArguments( bundle );
                        fragmentTransaction.setCustomAnimations(R.anim.fragment_slide_left_enter,
                                R.anim.fragment_slide_left_exit,
                                R.anim.fragment_slide_right_enter,
                                R.anim.fragment_slide_right_exit);
                        fragmentTransaction.replace(R.id.beranda_fragment_layout, berandaFragment);
                        fragmentTransaction.commit();

                        //Intent home = new Intent(com.oesmanalie.it.angkot.SearchingActivity.this, HomePageActivity.class);
                        //home.putExtras(bundle);
                        //startActivity(home);
                        //finish();
                        //loadFragment(new BerandaFragment(), bundle);
                        //Toast.makeText(MainActivity.this, movieNamesArrayList.get(position).getAnimalName(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onFailure(Call<List<Points>> call, Throwable t) {

            }
        });
        return view;
    }
}
