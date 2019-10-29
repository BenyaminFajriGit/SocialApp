package com.example.socialapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CommentsActivity extends AppCompatActivity {
    private RecyclerView rvComments;
    private FloatingActionButton fabCreateComment;
    private ArrayList<Comment> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        fabCreateComment = findViewById(R.id.fab_createcomment);
        rvComments = findViewById(R.id.rv_comments_list);
        rvComments.setHasFixedSize(true);
        list.addAll(CommentsData.getListData());

        showRecyclerList();
        fabCreateComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createCommentIntent = new Intent(getApplicationContext(),CreateNewCommentActivity.class);
                startActivity(createCommentIntent);
            }
        });
    }

    private void showRecyclerList() {
        rvComments.setLayoutManager(new LinearLayoutManager(this));
        CommentsAdapter commentsAdapter = new CommentsAdapter(list);
        rvComments.setAdapter(commentsAdapter);
    }

}