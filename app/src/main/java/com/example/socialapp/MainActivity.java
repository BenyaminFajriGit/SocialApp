package com.example.socialapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnDaftar;
    Button btnDashboard;
    Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnDaftar = findViewById(R.id.buttonDaftar);
        btnDashboard = findViewById(R.id.btn_dashboard);
        btnDaftar.setOnClickListener(this);
        btnDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
            }
        });
        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

    }

    @Override
    public void onClick(View view) {
        if(view==findViewById(R.id.edit)){
            startActivity(new Intent(this, EditProfil.class));
        }
        else{
            startActivity(new Intent(this, Register.class));
        }

    }
}
