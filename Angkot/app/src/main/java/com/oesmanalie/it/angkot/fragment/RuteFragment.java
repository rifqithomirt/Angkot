package com.oesmanalie.it.angkot.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.oesmanalie.it.angkot.R;

public class RuteFragment extends Fragment  {



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rute, container, false);
        ImageView im1 = view.findViewById(R.id.imageView12);
        ImageView im2 = view.findViewById(R.id.imageView13);
        ImageView im3 = view.findViewById(R.id.imageView14);
        ImageView im4 = view.findViewById(R.id.imageView15);
        ImageView im5 = view.findViewById(R.id.imageView16);
        ImageView im6 = view.findViewById(R.id.imageView17);
        onClickImage(im1);
        onClickImage(im2);
        onClickImage(im3);
        onClickImage(im4);
        onClickImage(im5);
        onClickImage(im6);
        return view;
    }
    private void onClickImage(ImageView imageView){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                if( view.getId() == R.id.imageView12 ) {
                    bundle.putString("kodelyn", "Rute Lyn A");
                } else if( view.getId() == R.id.imageView13 ) {
                    bundle.putString("kodelyn", "Rute Lyn B");
                } else if( view.getId() == R.id.imageView14 ) {
                    bundle.putString("kodelyn", "Rute Lyn C");
                } else if( view.getId() == R.id.imageView15 ) {
                    bundle.putString("kodelyn", "Rute Lyn D");
                } else if( view.getId() == R.id.imageView16 ) {
                    bundle.putString("kodelyn", "Rute Lyn E");
                } else if( view.getId() == R.id.imageView17 ) {
                    bundle.putString("kodelyn", "Rute Lyn FG");
                }
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                LihatRuteFragment lihatRuteFragment = new LihatRuteFragment();
                lihatRuteFragment.setArguments(bundle);
                fragmentTransaction.add(R.id.rute_cons, lihatRuteFragment).addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.rute_cons, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}