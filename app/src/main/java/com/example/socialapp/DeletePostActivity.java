package com.example.socialapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class DeletePostActivity extends AppCompatActivity  {

    Button btnDeletePost;
    Button btnCancelDeletePost;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_post);

        btnDeletePost = findViewById(R.id.btn_submit_delete_post);
        btnCancelDeletePost = findViewById(R.id.btn_cancel_delete_post);

        id = getIntent().getStringExtra("id_post");

        btnDeletePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePost();
            }
        });

        btnCancelDeletePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void deletePost() {
        class DeletePost extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(DeletePostActivity.this,"Deleting...","Wait...",false,false);
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
                hashMap.put("id_post",id);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest("http://frozenbits.tech/socialAppWS/DataPost/deletePost",hashMap);
                System.out.println(s);
                System.out.println(id);
                return s;
            }
        }
        DeletePost del = new DeletePost();
        del.execute();
    }


    private void checkStatusUpdate(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            boolean status= jsonObject.getBoolean("status");
            if(status){
                Toast.makeText(this, "Post Deleted Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(this, "Post Delete Failed", Toast.LENGTH_SHORT).show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
