package com.example.socialapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class EditProfil extends AppCompatActivity implements View.OnClickListener  {
    private Button btnDaftar;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextNama;
    private EditText editTextNoHP;
    String username;//ini diisi extra intent
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profil);
        btnDaftar= (Button) findViewById(R.id.btDaftar);
        editTextPassword = (EditText) findViewById(R.id.password);
        editTextNama = (EditText) findViewById(R.id.nama);
        editTextNoHP = (EditText) findViewById(R.id.noHP);
        btnDaftar.setOnClickListener(this);
        username="test" ;//ini diisi extra intent
        getUser();


    }

    @Override
    public void onClick(View view) {
        updateUser();
    }

    private void getUser(){
        class GetUser extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(EditProfil.this,"Fetching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showUser(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam("http://192.168.1.45/socialAppWS/index.php/User/get/",username);
                System.out.println(s);
                return s;
            }
        }
        GetUser gu = new GetUser();
        gu.execute();

    }

    private void showUser(String json){
        try {
            JSONArray result = new JSONArray(json);
            JSONObject c = result.getJSONObject(0);
            String password = c.getString("password");
            String nama = c.getString("nama");
            String noHP = c.getString("noHP");

            editTextPassword.setText(password);
            editTextNama.setText(nama);
            editTextNoHP.setText(noHP);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateUser(){
        final String password = editTextPassword.getText().toString().trim();
        final String nama = editTextNama.getText().toString().trim();
        final String noHP= editTextNoHP.getText().toString().trim();

        class UpdateUser extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(EditProfil.this,"Updating...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(EditProfil.this,s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("username",username);
                hashMap.put("password",password);
                hashMap.put("nama",nama);
                hashMap.put("noHP",noHP);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest("http://192.168.1.45/socialAppWS/index.php/User/update",hashMap);

                return s;
            }
        }

        UpdateUser uu = new UpdateUser();
        uu.execute();
    }
}

