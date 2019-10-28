package com.example.socialapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class PostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        TextView tvPostTitle = findViewById(R.id.tv_posttitle);

        TextView tvPostCaption = findViewById(R.id.tv_postcaption);

        String postTitle = getIntent().getStringExtra("post_title");
        String postCaption = getIntent().getStringExtra("post_caption");

        tvPostTitle.setText(postTitle);
        tvPostCaption.setText(postCaption);
    }
}
