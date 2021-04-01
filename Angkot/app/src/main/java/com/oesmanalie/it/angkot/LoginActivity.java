package com.oesmanalie.it.angkot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.oesmanalie.it.angkot.generator.ServiceGenerator;
import com.oesmanalie.it.angkot.models.Supir;
import com.oesmanalie.it.angkot.networks.DataService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private DataService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void homepage(View view) {
        EditText userText = findViewById(R.id.editText);
        EditText passText = findViewById(R.id.editText2);
       // Toast.makeText(getApplicationContext(),   , Toast.LENGTH_SHORT).show();

        if( userText.getText().toString().isEmpty() || passText.getText().toString().isEmpty() ) {
            Toast.makeText(getApplicationContext(), "Username dan Password harus diisi dengan Benar" , Toast.LENGTH_SHORT).show();
        } else  requestLogin( userText.getText().toString(), passText.getText().toString() );

    }

    private void requestLogin( String username, String password ) {
        apiService = ServiceGenerator.createService(DataService.class);
        Call<Supir> login = apiService.loginRequest( username, password );
        login.enqueue(new Callback<Supir>() {
            @Override
            public void onResponse(Call<Supir> call, Response<Supir> response) {
                if( response.isSuccessful() ) {
                    Supir supir = response.body();
                    if( supir.getNama().contains("not found") ) {
                        Toast.makeText(getApplicationContext(), "Username atau Password salah" , Toast.LENGTH_SHORT).show();
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putString("nama", supir.getNama());
                        Intent home=new Intent(LoginActivity.this, AccountActivity.class);
                        home.putExtras(bundle);
                        startActivity(home);
                    }
                }
            }

            @Override
            public void onFailure(Call<Supir> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
