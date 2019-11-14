package com.example.socialapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class EditPassword extends AppCompatActivity implements View.OnClickListener, Validator.ValidationListener {

    private Button btnDaftar;
    private SharedPreferences loginData;
    Context appContext ;
    @NotEmpty
    @Password(min=8,scheme=Password.Scheme.ALPHA_NUMERIC,message = "Password at least 8 and character must containt alphabet and number ")
    private EditText editTextOldPassword;

    @NotEmpty
    @Password(min=8,scheme=Password.Scheme.ALPHA_NUMERIC,message = "Password at least 8 and character must containt alphabet and number ")
    private EditText editTextNewPassword;


    private Validator validator;

    int id;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        appContext = LoginActivity.getAppContext();
        loginData = appContext.getSharedPreferences("Login", MODE_PRIVATE);
        btnDaftar= (Button) findViewById(R.id.btn_update_pass);
        editTextOldPassword = (EditText) findViewById(R.id.oldPassword);
        editTextNewPassword = (EditText) findViewById(R.id.newPassword);
        btnDaftar.setOnClickListener(this);
        id=Integer.parseInt(loginData.getString("id_user", "0")) ;//ganti jadi yang diambil dari get extra atau variabel global login
        validator=new Validator(this);
        validator.setValidationListener(this);


    }

    @Override
    public void onClick(View view)
    {
        validator.validate();

    }





    private void updatePassword(){
        final String oldPassword = editTextOldPassword.getText().toString().trim();
        final String newPassword= editTextNewPassword.getText().toString().trim();

        class UpdateUser extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(EditPassword.this,"Updating...","Wait...",false,false);
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
                hashMap.put("oldPassword",oldPassword);
                hashMap.put("password",newPassword);

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
        updatePassword();
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
                Toast.makeText(this, "Password Changed Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(this, "Old Password is wrong!", Toast.LENGTH_SHORT).show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

