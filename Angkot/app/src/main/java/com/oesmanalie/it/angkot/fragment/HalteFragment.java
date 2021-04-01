package com.oesmanalie.it.angkot.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oesmanalie.it.angkot.adapter.AngkotAdapter;
import com.oesmanalie.it.angkot.models.AngkotModel;
import com.oesmanalie.it.angkot.models.AngkotSheet;
import com.oesmanalie.it.angkot.adapter.ListViewAdapter;
import com.oesmanalie.it.angkot.R;

import java.util.ArrayList;
import java.util.List;

public class HalteFragment extends Fragment implements AngkotSheet.OnAngkotClickListener{
    public RecyclerView rv;
    public AngkotSheet angkotSheet;
    public RecyclerView.LayoutManager layoutManager;
    public AngkotAdapter angkotAdapter;
    private ListViewAdapter adapter;
    private android.widget.SearchView editsearch;
    public List<AngkotModel> listAngkot = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_halte, container, false);
        rv = view.findViewById(R.id.recycler_view_halte);
        listAngkot.add(new AngkotModel("HALTE GREEN GARDEN", ""));
        listAngkot.add(new AngkotModel("HALTE ICON MALL", ""));
        listAngkot.add(new AngkotModel("HALTE ABR", ""));
        listAngkot.add(new AngkotModel("HALTE GKA", ""));
        listAngkot.add(new AngkotModel("HALTE GKB BUNDARAN", ""));
        listAngkot.add(new AngkotModel("HALTE RANDU AGUNG", ""));
        listAngkot.add(new AngkotModel("HALTE BNI '46", ""));
        listAngkot.add(new AngkotModel("HALTE SMAN 1 GRESIK", ""));
        listAngkot.add(new AngkotModel("HALTE SMPN 1 MANYAR", ""));
        listAngkot.add(new AngkotModel("HALTE MIE SEDAP", ""));
        listAngkot.add(new AngkotModel("HALTE SMPN 2 KEBOMAS", ""));
        listAngkot.add(new AngkotModel("HALTE SDN BANJARSARI", ""));
        listAngkot.add(new AngkotModel("HALTE MAKAM CAGAK AGUNG", ""));
        listAngkot.add(new AngkotModel("HALTE SMPN BALONGPANGGANG", ""));
        listAngkot.add(new AngkotModel("HALTE SMPN BENJENG", ""));
        listAngkot.add(new AngkotModel("HALTE AMBENG - AMBENG", ""));

        angkotSheet = new AngkotSheet(listAngkot);
        angkotSheet.setListener(this);
        layoutManager = new LinearLayoutManager(getContext());
        rv.setAdapter(angkotSheet);
        rv.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void onClick(View view, int position) {
        Bundle bundle = new Bundle();
        listAngkot.get(position);

        Double[][] lat = {
                {7.,10.,3.38},
                {7.,10.,2.20},
                {7., 9.,59.94},
                {7., 9.,41.95},
                {7., 9.,34.31},
                {7., 9.,40.61},
                {7.,10.,0.51},
                {7.,10.,4.00},
                {7., 8.,24.67},
                {7., 7.,53.12},
                {7., 9.,35.81},
                {7.,10.,32.32},
                {7.,13.,24.04},
                {7.,16.,1.85},
                {7.,15.,36.45},
                {7.,10.,0.38}};

        Double[][] lng = {
                {112.,35.,59.28},
                {112.,36.,8.91},
                {112.,36.,25.65},
                {112.,36.,57.47},
                {112.,37.,5.00},
                {112.,37.,25.30},
                {112.,38.,29.15},
                {112.,39.,10.17},
                {112.,36.,58.60},
                {112.,36.,45.06},
                {112.,37.,4.18},
                {112.,34.,56.98},
                {112.,33.,39.92},
                {112.,27.,12.19},
                {112.,30.,24.95},
                {112.,33.,45.35}
        };

        bundle.putDouble("lat", funGetDecimal( lat[position] ) * -1 );
        bundle.putDouble("lng", funGetDecimal( lng[position] ) );
        bundle.putString("nama", listAngkot.get(position).getNamaAngkot());
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        LihatHalteFragment lihatHalteFragment = new LihatHalteFragment();
        lihatHalteFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.peta_cons, lihatHalteFragment).addToBackStack(null);
        fragmentTransaction.commit();
    }

    private double funGetDecimal( Double[] dms ){
        double decimal;
        decimal = ((dms[1] * 60)+ dms[2]) / (60*60);
        return dms[0] + decimal;
    }
}