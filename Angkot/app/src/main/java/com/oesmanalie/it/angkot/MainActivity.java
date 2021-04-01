package com.oesmanalie.it.angkot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private int waktu_loading=5000;
    private ImageView logo1, logo2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logo1 = findViewById(R.id.imageView);
        logo2 = findViewById(R.id.imageView2);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //setelah loading maka akan langsung berpindah ke home activity
                Intent home=new Intent(MainActivity.this, CategoryActivity.class);
                startActivity(home);
                finish();

            }
        },waktu_loading);
        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.animation);
        logo1.startAnimation(myanim);
        logo2.startAnimation(myanim);
    }
}
