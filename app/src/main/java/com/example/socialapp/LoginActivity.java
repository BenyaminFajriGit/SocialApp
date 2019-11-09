package com.example.socialapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    private Button btnSignUp, btnSignIn;
    private EditText etUsername, etPassword;
    private SharedPreferences loginData;

    //MAIN APP CONTEXT FOR THE WHOLE APP
    private static Context appContext;
    public static Context getAppContext(){
        return appContext;
    }
    //====================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        appContext = getApplicationContext();
        btnSignUp = findViewById(R.id.btn_signup);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(getApplicationContext(),Register.class);
                startActivity(registerIntent);
            }
        });
        btnSignIn = findViewById(R.id.btn_signin);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        //Function to login by pressing enter/return without clicking the button
        TextView.OnEditorActionListener enterListener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView etPassword, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_NULL
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    login();
                }
                return true;
            }
        };
        etPassword.setOnEditorActionListener(enterListener);

        //=================START AUTOMATIC LOGIN=============
        //Check if login data exists. Choose from any value (username, user_id, etc).
        loginData = appContext.getSharedPreferences("Login", MODE_PRIVATE);
        String uname = loginData.getString("username",null);
        if(uname!=null){
            startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
        }
        //=================END AUTOMATIC LOGIN==============
    }

    private String id_user, nama, username;
    private JSONObject userData;

    public void login(){
        final String usr, pass;
        usr = etUsername.getText().toString();
        pass = etPassword.getText().toString();

        class Login extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(LoginActivity.this,"Logging in...","",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.d("onpostexec",s);
                loading.dismiss();
                isSuccess(s);
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put("username",usr);
                params.put("password",pass);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest("http://frozenbits.tech/socialAppWS/DataUser/validateLogin", params);
                //Log.d("res",res); //Get fetch result
                String dataPull = "";
                try{
                    JSONObject result = new JSONObject(res);
                    if(result.getBoolean("status")){ //kondisi login berhasil, langsung login di sini
                        JSONArray rawUserData = result.getJSONArray("data");
                        String rud = result.getString("data");
                        //Log.d("rawuserdata",rud); //get "data" key from json object
                        userData = rawUserData.getJSONObject(0); //create inner json object
                        id_user = userData.getString("id_user");
                        //Log.d("iduser",id_user); //get "id_user" key from inner json object

                        rh = new RequestHandler();
                        params = new HashMap<>();
                        params.put("id_user", id_user);

                        dataPull = rh.sendPostRequest("http://frozenbits.tech/socialAppWS/DataUser/getDataUser",params);
                    } else { //kondisi login gagal, aksinya diurus di isSuccess()
                        return res;
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
                //Log.d("datapull",dataPull); //get dataPull, basically the same as the "res" variable
                    //isi: {"status":true,"message":"Data Ditemukan",
                    //      "data":[{"id_user":"10","nama":"asd","username":"fbitsfbits",
                    //      "password":"8068c76c7376bc08e2836ab26359d4a4","no_hp":"123"}]}
                return dataPull;
            }
        }
        Login login = new Login();
        login.execute();

    }

    private void isSuccess(String json){
        //Log.d("Line0","Line0");
        //Log.d("SomethingOuter", json);
        //Log.d("SomethingEmpty", String.valueOf(json.isEmpty()));
        try {
            //Log.d("SomethingTry", json); //data check
            JSONObject jsonObject = new JSONObject(json);
            //Log.d("Line1", "Line1");
            //commented for future access reference
            boolean status= jsonObject.getBoolean("status");
            //Log.d("Line2", "Line2");
            String message= jsonObject.getString("message");
            //Log.d("Line3", "Line3");

            if(status){
                //create new sharedpreference to store login details
                //assign data values
                nama = userData.getString("nama");
                username = userData.getString("username");

                //Full link: https://stackoverflow.com/questions/9771061/how-to-save-login-information-locally-in-android
                //begin
                loginData = appContext.getSharedPreferences("Login", MODE_PRIVATE);
                SharedPreferences.Editor loginDataEdit = loginData.edit();
                loginDataEdit.putString("username",username );
                loginDataEdit.putString("nama",nama);
                loginDataEdit.putString("id_user",id_user);

                loginDataEdit.commit();
                //end

                Toast.makeText(this, "Logged in!" , Toast.LENGTH_SHORT).show();
                //JANGAN pernah tambah finish() di LoginActivity.java
                //alasan: biar bisa pake flag FLAG_ACTIVITY_SINGLE_TOP agar setelah logout jadi gak bisa tekan tombol back.
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                //empty the input fields
                etUsername.setText("");
                etPassword.setText("");
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            //Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }
}
