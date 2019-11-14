package com.example.socialapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    Button btnEditProfile, btnLogout,btnEditPassword;
    TextView textUsername;
    int id;
    String username;
    private SharedPreferences loginData;
    Context appContext ;
    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        btnEditProfile = view.findViewById(R.id.btn_edit_profile);
        btnEditProfile.setOnClickListener(this);
        textUsername= (TextView) view.findViewById(R.id.tv_profile_username);
        appContext = LoginActivity.getAppContext();
        loginData = appContext.getSharedPreferences("Login", Context.MODE_PRIVATE);
        username=loginData.getString("username","unknown");
        textUsername.setText(username);
        btnLogout = view.findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Logout.logout();
                Intent goBack = new Intent(LoginActivity.getAppContext(), LoginActivity.class);
                goBack.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP); //kill all activities after logging out
                startActivity(goBack);
            }
        });
        btnEditPassword= view.findViewById(R.id.btn_edit_password);
        btnEditPassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent editPassword = new Intent(getContext(), EditPassword.class);
                startActivity(editPassword);
            }
        });
        return view;
    }

    @Override
    public void onClick(View view) {
        Intent editProfil = new Intent(getContext(), EditProfil.class);
        startActivity(editProfil);
    }
}
