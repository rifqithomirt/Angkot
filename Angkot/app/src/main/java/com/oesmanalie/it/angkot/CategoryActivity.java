package com.oesmanalie.it.angkot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import static butterknife.internal.Utils.arrayOf;

public class CategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted. Request for permission


            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission is not granted", Toast.LENGTH_LONG).show();
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 1);
                }else{
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION), 1);
                }
            } else {
                Toast.makeText(this, "Permission is granted", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void login(View view) {
        Intent home=new Intent(CategoryActivity.this, LoginActivity.class);
        startActivity(home);
    }
    public void homepage2(View view) {
        Intent home=new Intent(CategoryActivity.this, HomePageActivity.class);
        startActivity(home);
    }
}
