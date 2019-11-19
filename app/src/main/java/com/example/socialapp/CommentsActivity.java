package com.example.socialapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.socialapp.model.Comment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

public class CommentsActivity extends AppCompatActivity {
    private RecyclerView rvComments;
    private ArrayList<Comment> list = new ArrayList<>();
    private CommentsAdapter ycommentsAdapter;
    //final String postId = getIntent().getStringExtra("id_post");
    private FloatingActionButton fabCreateComment;





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        getYourComments();
        fabCreateComment = findViewById(R.id.fab_createcomment);
        rvComments = findViewById(R.id.rv_comments_list);
        rvComments.setHasFixedSize(true);
        ycommentsAdapter = new CommentsAdapter(list,LoginActivity.getAppContext());
        rvComments.setAdapter(ycommentsAdapter);



        //list.addAll(CommentsData.getListData());

        //showRecyclerList();

        final String postId = getIntent().getStringExtra("id_post");

        fabCreateComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createCommentIntent = new Intent(LoginActivity.getAppContext(),CreateNewCommentActivity.class);
                createCommentIntent.putExtra("id_post_comment",postId);
                startActivity(createCommentIntent);
            }
        });
    }



    private void getYourComments(){
        class GetYourComments extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //loading = ProgressDialog.show(LoginActivity.getAppContext(),"Fetching...","Wait...",false,false);
            }



            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //loading.dismiss();
                showComments(s);
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();

                //Context appContext = LoginActivity.getAppContext();
                //SharedPreferences loginData = appContext.getSharedPreferences("Login", MODE_PRIVATE);

                params.put("id_post","1");
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest("http://frozenbits.tech/socialAppWS/index.php/DataKomentar/getPostComment", params);

                return s;
            }
        }
        GetYourComments gyc = new GetYourComments();
        gyc.execute();

    }

    private void showComments(String json){
        Log.d("showComments", "TEST");
        try {
            Log.d("inTry", "INTRY");
            JSONObject jsonObject = new JSONObject(json);
            boolean status= jsonObject.getBoolean("status");
            String message= jsonObject.getString("message");
            Log.d("afterJSON", "TEST");
            Log.d("JSONvalue", json);

            if(status){
                JSONArray result = jsonObject.getJSONArray("data");
                Log.d("afterJSONArray", "TEST");
                for (int i = 0; i < result.length(); i++) {
                    Comment comment = new Comment();
                    JSONObject pst = result.getJSONObject(i);
                    comment.setId_post(pst.getString("id_post"));
                    comment.setId_user(pst.getString("id_user"));
                    comment.setComment(pst.getString("comment"));
                    Log.d("Comments",pst.getString("comment"));
                    comment.setTime(pst.getString("waktu"));
                    comment.setName(pst.getString("nama"));
                    list.add(comment);
                }
                ycommentsAdapter.notifyDataSetChanged();
                Log.d("afterCommentsAdapter", "TEST");
            }else{
                Toast.makeText(LoginActivity.getAppContext(),message,Toast.LENGTH_LONG).show();
                Log.d("inElse", "TEST");
//                getActivity().finish();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



}