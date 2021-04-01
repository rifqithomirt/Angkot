package com.oesmanalie.it.angkot;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.oesmanalie.it.angkot.fragment.AccountFragment;
import com.oesmanalie.it.angkot.fragment.BerandaFragment;
import com.oesmanalie.it.angkot.fragment.Faq2Fragment;
import com.oesmanalie.it.angkot.fragment.FaqFragment;
import com.oesmanalie.it.angkot.fragment.HalteFragment;
import com.oesmanalie.it.angkot.fragment.PenumpangFragment;
import com.oesmanalie.it.angkot.fragment.PickupPenumpangFragment;
import com.oesmanalie.it.angkot.fragment.RuteFragment;
import com.oesmanalie.it.angkot.fragment.SupirFragment;

import java.util.UUID;

import static butterknife.internal.Utils.arrayOf;

public class HomePageActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private String penumpangID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);


        penumpangID = UUID.randomUUID().toString();

        /*
        Bundle bundleExtra = getIntent().getExtras();
        if(bundleExtra != null) {
            String item = bundleExtra.getString("fragment");
            Integer position = bundleExtra.getInt("arraysearch");
            Fragment fragment = null;
            switch (item){
                case "action_rute":
                    fragment = new RuteFragment();
                    break;
                case "action_halte":
                    fragment = new HalteFragment();
                    break;
                case "action_faq2":
                    fragment = new Faq2Fragment();
                    break;
                case "action_beranda":
                    fragment = new BerandaFragment();
                    break;
                case "action_pickup_penumpang":
                    fragment = new PickupPenumpangFragment();
            }
            fragment.setArguments(bundleExtra);
            loadFragment(fragment);
            BottomNavigationView bottomNavigationView = findViewById(R.id.bn_main2);
            bottomNavigationView.setOnNavigationItemSelectedListener(this);
        } else {


            loadFragment(new RuteFragment());
            BottomNavigationView bottomNavigationView = findViewById(R.id.bn_main2);
            bottomNavigationView.setOnNavigationItemSelectedListener(this);
        }
        */

        loadFragment(new RuteFragment());
        BottomNavigationView bottomNavigationView = findViewById(R.id.bn_main2);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fragment_slide_left_enter,
                    R.anim.fragment_slide_left_exit,
                    R.anim.fragment_slide_right_enter,
                    R.anim.fragment_slide_right_exit).replace(R.id.fl_container2, fragment).commit();
            return true;
        }
        return false;
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentManager fm = this.getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
        Fragment fragment = null;
        switch (item.getItemId()){
            case R.id.action_home:
                fragment = new RuteFragment();
                break;
            case R.id.action_halte:
                fragment = new HalteFragment();
                break;
            case R.id.action_faq2:
                fragment = new Faq2Fragment();
                break;
            case R.id.action_route:
                fragment = new BerandaFragment();
                Bundle penumpangBundle = new Bundle();
                penumpangBundle.putString("penumpangId", penumpangID);
                fragment.setArguments( penumpangBundle );
                break;

        }
        return loadFragment(fragment);
    }
}
