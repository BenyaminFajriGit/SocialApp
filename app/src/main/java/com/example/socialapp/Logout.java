package com.example.socialapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.socialapp.R;

import androidx.appcompat.app.AppCompatActivity;

public class Logout extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        logout();
    }

    public static void logout(){
        Context appContext = MainActivity.getAppContext();

        //Empty the SharedPreferences
        //Full link: https://stackoverflow.com/questions/4503807/how-to-empty-the-sharedpreferences-storage-in-android
        //begin
        SharedPreferences loginData = appContext.getSharedPreferences("Login", MODE_PRIVATE);
        SharedPreferences.Editor loginDataEdit = loginData.edit();

        //debug line for sharedpreferences check
        Toast.makeText(appContext, "Bye, "+loginData.getString("username", "but there's no username stored") , Toast.LENGTH_SHORT).show();

        loginDataEdit.clear();
        loginDataEdit.commit();
        //end
    }
}
