package com.example.socialapp;



import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class Register extends AppCompatActivity implements View.OnClickListener{
    private Button btnDaftar;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextNama;
    private EditText editTextNoHP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btnDaftar= (Button) findViewById(R.id.btDaftar);
        editTextUsername = (EditText) findViewById(R.id.username);
        editTextPassword = (EditText) findViewById(R.id.password);
        editTextNama = (EditText) findViewById(R.id.nama);
        editTextNoHP = (EditText) findViewById(R.id.noHP);
        btnDaftar.setOnClickListener(this);


    }
    @Override
    public void onClick(View view) {
        this.register();
    }
    public void register(){
        final String username= editTextUsername.getText().toString().trim();
        final String password= editTextPassword.getText().toString().trim();
        final String nama= editTextNama.getText().toString().trim();
        final String noHP= editTextNoHP.getText().toString().trim();

        class AddUser extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Register.this,"Menambahkan...","Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(Register.this,s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put("username",username);
                params.put("password",password);
                params.put("nama",nama);
                params.put("noHP",noHP);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest("http://192.168.1.45/socialAppWS/index.php/User/add", params);
                return res;
            }
        }

        AddUser au = new AddUser();
        System.out.println(au.execute());
    }


}
