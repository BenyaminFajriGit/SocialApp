package com.example.socialapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    private Button btnSignUp;
    private EditText etUsername, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnSignUp = findViewById(R.id.btn_signup);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(getApplicationContext(),Register.class);
                startActivity(registerIntent);
            }
        });

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
    }

    public void login(View v){
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
                    JSONArray rawUserData = result.getJSONArray("data");
                    String rud = result.getString("data");
                    //Log.d("rawuserdata",rud); //get "data" key from json object
                    JSONObject userData = rawUserData.getJSONObject(0); //create inner json object
                    String id_user = userData.getString("id_user");
                    //Log.d("iduser",id_user); //get "id_user" key from inner json object

                    if (result.getBoolean("status")){
                        rh = new RequestHandler();
                        params = new HashMap<>();
                        params.put("id_user", id_user);

                        dataPull = rh.sendPostRequest("http://frozenbits.tech/socialAppWS/DataUser/getDataUser",params);
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
                Log.d("datapull",dataPull);
                return dataPull;
                //OK hasil query udah bener, tinggal benerin ini kenapa error di bagian isSuccess()
                //cek lagi nanti, gue pengen tidur.
            }
        }
        Login login = new Login();
        login.execute();

    }

    private void isSuccess(String json){
        try {
            Log.d("something", json);
            JSONObject jsonObject = new JSONObject(json);
            boolean status= jsonObject.getBoolean("status");
            String message= jsonObject.getString("message");
            JSONArray jA = jsonObject.getJSONArray("data");

            if(status){
                //Toast.makeText(this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, jA.getString(0) , Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
