package com.example.socialapp;



import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import java.util.HashMap;
import java.util.List;

import javax.xml.validation.ValidatorHandler;

public class Register extends AppCompatActivity implements View.OnClickListener, Validator.ValidationListener {


    private Button btnDaftar;

    @NotEmpty
    private EditText editTextUsername;

    @NotEmpty
    @Password(min=8,scheme=Password.Scheme.ALPHA_NUMERIC,message = "Password at least 8 and character must containt alphabet and number ")
    private EditText editTextPassword;

    @NotEmpty
    private EditText editTextNama;

    @NotEmpty
    @Pattern(regex =  "[0-9]+",message = "Phone only number are accepted")
    private EditText editTextNoHP;

    private Validator validator;

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
        validator=new Validator(this);
        validator.setValidationListener(this);



    }
    @Override
    public void onClick(View view) {
        validator.validate();

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
                params.put("no_hp",noHP);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest("http://frozenbits.tech/socialAppWS/DataUser/addUser", params);
                return res;
            }
        }

        AddUser au = new AddUser();
        System.out.println(au.execute());
    }


    @Override
    public void onValidationSucceeded() {
        this.register();
        Toast.makeText(this, "Successfully register", Toast.LENGTH_SHORT).show();
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
