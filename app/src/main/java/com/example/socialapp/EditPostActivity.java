package com.example.socialapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class EditPostActivity extends AppCompatActivity {

    EditText etPostCaption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        etPostCaption = findViewById(R.id.et_postcaption_edit);

        String postContent = getIntent().getStringExtra("post_content");
        etPostCaption.setText(postContent);
    }
}
