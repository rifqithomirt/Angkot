package com.oesmanalie.it.angkot;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.oesmanalie.it.angkot.fragment.AccountFragment;
import com.oesmanalie.it.angkot.fragment.FaqFragment;
import com.oesmanalie.it.angkot.fragment.PenumpangFragment;
import com.oesmanalie.it.angkot.fragment.SupirFragment;

public class AccountActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    Bundle bundleLogin = new Bundle();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        bundleLogin = getIntent().getExtras();
        Fragment fragmentSupir = new SupirFragment();
        fragmentSupir.setArguments(bundleLogin);
        loadFragment(fragmentSupir);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bn_main);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fragment_slide_left_enter,
                    R.anim.fragment_slide_left_exit,
                    R.anim.fragment_slide_right_enter,
                    R.anim.fragment_slide_right_exit).replace(R.id.fl_container, fragment).commit();
            return true;
        }
        return false;
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()){
            case R.id.action_pass:
                fragment = new SupirFragment();
                fragment.setArguments(bundleLogin);
                break;
            case R.id.action_account:
                fragment = new AccountFragment();
                fragment.setArguments(bundleLogin);
                break;
            case R.id.action_faq:
                fragment = new FaqFragment();
                break;

        }
        return loadFragment(fragment);
    }
}
