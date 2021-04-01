package com.oesmanalie.it.angkot.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.oesmanalie.it.angkot.R;
import com.oesmanalie.it.angkot.generator.ServiceGenerator;
import com.oesmanalie.it.angkot.models.Supir;
import com.oesmanalie.it.angkot.networks.DataService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountFragment extends Fragment {

    private DataService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        final TextView accountName = view.findViewById(R.id.account_nama);
        final TextView accountKtp = view.findViewById(R.id.account_ktp);
        final TextView accountJenisAngkot = view.findViewById(R.id.account_jenisangkot);
        final TextView accountPlatNomor = view.findViewById(R.id.account_platnomor);
        Bundle bundle = getArguments();

        String supirName = bundle.getString("nama");
        Toast.makeText(getContext(), "nama supir " + supirName, Toast.LENGTH_SHORT).show();
        apiService = ServiceGenerator.createService(DataService.class);
        Call<Supir> supirData = apiService.getSupirData( supirName );
        supirData.enqueue(new Callback<Supir>() {
            @Override
            public void onResponse(Call<Supir> call, Response<Supir> response) {
                Supir objSupir = response.body();
                accountName.setText(objSupir.getNama().toUpperCase());
                accountKtp.setText(objSupir.getKtp());
                accountJenisAngkot.setText(objSupir.getNamaAngkot());
                accountPlatNomor.setText(objSupir.getSimA().toUpperCase());
            }

            @Override
            public void onFailure(Call<Supir> call, Throwable t) {

            }
        });
        return view;
    }
}
