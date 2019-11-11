package com.example.socialapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class CreateNewCommentActivity extends AppCompatActivity implements View.OnClickListener, Validator.ValidationListener {

    private Button btnSubmitComment;
    @NotEmpty
    private EditText etCommentCaption;
    private Validator validator;
    private String postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_comment);

        btnSubmitComment = findViewById(R.id.btn_submit_comment);
        etCommentCaption = findViewById(R.id.et_comment);

        postId = getIntent().getStringExtra("id_post_comment");

        btnSubmitComment.setOnClickListener(this);
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @Override
    public void onClick(View view) {
        validator.validate();
    }


    public void addComment(){
        final String caption = etCommentCaption.getText().toString().trim();

        class AddComment extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(CreateNewCommentActivity.this,"Menambahkan...","Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                isSuccess(s);
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                Context appContext = LoginActivity.getAppContext();
                SharedPreferences loginData = appContext.getSharedPreferences("Login", MODE_PRIVATE);
                params.put("id_post",postId);
                params.put("id_user",loginData.getString("id_user","1"));
                params.put("comment",caption);
                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest("http://frozenbits.tech/socialAppWS/DataKomentar/addComment", params);
                return res;
            }
        }

        AddComment ac = new AddComment();
        ac.execute();
    }



    private void isSuccess(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            boolean status= jsonObject.getBoolean("status");
            String message= jsonObject.getString("message");
            if(status){
                Toast.makeText(this, "Comment Successfully added!", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(this, "Adding comment failed!", Toast.LENGTH_SHORT).show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onValidationSucceeded() {
        this.addComment();
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
