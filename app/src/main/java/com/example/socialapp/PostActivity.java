package com.example.socialapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        TextView tvPostName = findViewById(R.id.tv_postname);

        TextView tvPostCaption = findViewById(R.id.tv_postcaption);

        FloatingActionButton fabViewComments = findViewById(R.id.fab_commentpost);
        fabViewComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent commentsIntent = new Intent(getApplicationContext(),CommentsActivity.class);
                startActivity(commentsIntent);
            }
        });

        String postName = getIntent().getStringExtra("post_name");
        String postCaption = getIntent().getStringExtra("post_caption");

        tvPostName.setText(postName);
        tvPostCaption.setText(postCaption);
    }
}
