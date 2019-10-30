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
    @Password(min=8,scheme=Password.Scheme.ALPHA_NUMERIC,message = "Password at least 8 and character must containt alphabet and number ")
    private EditText editTextPassword;

    @NotEmpty
    private EditText editTextNama;

    @NotEmpty
    @Pattern(regex =  "[0-9]+",message = "Phone only number are accepted")
    private EditText editTextno_hp;

    private Validator validator;

    String username;//ini diisi extra intent
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profil);
        btnDaftar= (Button) findViewById(R.id.btDaftar);
        editTextPassword = (EditText) findViewById(R.id.password);
        editTextNama = (EditText) findViewById(R.id.nama);
        editTextno_hp = (EditText) findViewById(R.id.noHP);
        btnDaftar.setOnClickListener(this);
        username="test" ;//ini diisi extra intent
        getUser();
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
                hashMap.put("id_user","1");//ini ganti username
                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest("http://frozenbits.tech/socialAppWS/DataUser/getDataUser",hashMap);
                System.out.println(s);
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
                String password = c.getString("password");
                String nama = c.getString("nama");
                String no_hp = c.getString("no_hp");

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
                Toast.makeText(EditProfil.this,s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("username",username);
                hashMap.put("password",password);
                hashMap.put("nama",nama);
                hashMap.put("no_hp",no_hp);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest("http://192.168.1.45/socialAppWS/index.php/User/update",hashMap);

                return s;
            }
        }

        UpdateUser uu = new UpdateUser();
        uu.execute();
    }

    @Override
    public void onValidationSucceeded() {
        updateUser();
        Toast.makeText(this, "pProfil Updated Successfully", Toast.LENGTH_SHORT).show();
        finish();
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
}

