package com.example.socialapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class CommentsActivity extends AppCompatActivity {
    private RecyclerView rvComments;
    private ArrayList<Comment> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        rvComments = findViewById(R.id.rv_comments_list);
        rvComments.setHasFixedSize(true);
        list.addAll(CommentsData.getListData());

        showRecyclerList();
    }

    private void showRecyclerList() {
        rvComments.setLayoutManager(new LinearLayoutManager(this));
        CommentsAdapter commentsAdapter = new CommentsAdapter(list);
        rvComments.setAdapter(commentsAdapter);
    }

}