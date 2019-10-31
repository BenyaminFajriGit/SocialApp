package com.example.socialapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.Min;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class EditProfil extends AppCompatActivity implements View.OnClickListener, Validator.ValidationListener {

    private Button btnDaftar;

    @NotEmpty
    @Length(max = 12, min = 6, message = "Input must be between 6 and 12 characters")
    private EditText editTextUsername;

    @NotEmpty
    @Password(min=8,scheme=Password.Scheme.ALPHA_NUMERIC,message = "Password at least 8 and character must containt alphabet and number ")
    private EditText editTextPassword;

    @NotEmpty
    @Length(max = 255, min = 1, message = "Input must be between 1 and 255 characters")
    private EditText editTextNama;

    @NotEmpty
    @Length(max = 20, min = 4, message = "Input must be between 6 and 20 characters")
    @Pattern(regex =  "[0-9]+",message = "only number are accepted")
    private EditText editTextno_hp;

    private Validator validator;

    int id;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profil);
        btnDaftar= (Button) findViewById(R.id.btDaftar);
        editTextUsername=(EditText) findViewById(R.id.username);
        editTextPassword = (EditText) findViewById(R.id.password);
        editTextNama = (EditText) findViewById(R.id.nama);
        editTextno_hp = (EditText) findViewById(R.id.noHP);
        btnDaftar.setOnClickListener(this);
        getUser();
        id=7;//ganti jadi yang diambil dari get extra atau variabel global login
        validator=new Validator(this);
        validator.setValidationListener(this);


    }

    @Override
    public void onClick(View view)
    {
        validator.validate();

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
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("id_user",Integer.toString(id));
                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest("http://frozenbits.tech/socialAppWS/DataUser/getDataUser",hashMap);

                return s;
            }
        }
        GetUser gu = new GetUser();
        gu.execute();

    }

    private void showUser(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            boolean status= jsonObject.getBoolean("status");
            String message= jsonObject.getString("message");
            if(status){
                JSONArray result = jsonObject.getJSONArray("data");
                JSONObject c = result.getJSONObject(0);
                String username=c.getString("username");
                String password = c.getString("password");
                String nama = c.getString("nama");
                String no_hp = c.getString("no_hp");
                editTextUsername.setText(username);
                editTextPassword.setText(password);
                editTextNama.setText(nama);
                editTextno_hp.setText(no_hp);
            }else{
                Toast.makeText(EditProfil.this,message,Toast.LENGTH_LONG).show();
                finish();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateUser(){
        final String username= editTextUsername.getText().toString();
        final String password = editTextPassword.getText().toString().trim();
        final String nama = editTextNama.getText().toString().trim();
        final String no_hp= editTextno_hp.getText().toString().trim();

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
                checkStatusUpdate(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("id_user",Integer.toString(id));
                hashMap.put("username",username);
                hashMap.put("password",password);
                hashMap.put("nama",nama);
                hashMap.put("no_hp",no_hp);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest("http://frozenbits.tech/socialAppWS/DataUser/updateData",hashMap);
                System.out.println(s);
                return s;
            }
        }

        UpdateUser uu = new UpdateUser();
        uu.execute();
    }

    @Override
    public void onValidationSucceeded() {
        updateUser();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            // Display error messages
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void checkStatusUpdate(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            boolean status= jsonObject.getBoolean("status");
            String message= jsonObject.getString("message");
            if(status){
                Toast.makeText(this, "Profil Updated Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(this, "Username is already used!", Toast.LENGTH_SHORT).show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

