package com.example.socialapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class EditPostActivity extends AppCompatActivity {

    EditText etPostCaption;
    Button btnEditPost;
    String id;
    String editCaption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        etPostCaption = findViewById(R.id.et_postcaption_edit);
        btnEditPost = findViewById(R.id.btn_submit_edit_post);

        String postContent = getIntent().getStringExtra("post_content");
        id = getIntent().getStringExtra("id_post");
        etPostCaption.setText(postContent);

        btnEditPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPost();
            }
        });
    }

    public void editPost() {
        editCaption= etPostCaption.getText().toString().trim();

        class UpdatePost extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(EditPostActivity.this,"Updating...","Wait...",false,false);
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
                hashMap.put("post",editCaption);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest("http://frozenbits.tech/socialAppWS/DataPost/updatePost",hashMap);
                System.out.println(s);
                System.out.println(editCaption);
                System.out.println(id);
                return s;
            }
        }

        UpdatePost up = new UpdatePost();
        up.execute();
    }

    private void checkStatusUpdate(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            boolean status= jsonObject.getBoolean("status");
            if(status){
                Toast.makeText(this, "Post Updated Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(this, "Post update failed", Toast.LENGTH_SHORT).show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
