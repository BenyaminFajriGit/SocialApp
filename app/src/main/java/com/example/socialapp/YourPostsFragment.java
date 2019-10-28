package com.example.socialapp;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class YourPostsFragment extends Fragment {
    private RecyclerView rvPosts;
    private ArrayList<Post> list = new ArrayList<>();

    public YourPostsFragment() {
        // Required empty public constructor
    }
    public static YourPostsFragment newInstance() {
        YourPostsFragment fragment = new YourPostsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_your_posts, container, false);
        rvPosts = view.findViewById(R.id.rv_your_posts_list);
        rvPosts.setHasFixedSize(true);
        list.addAll(PostsData.getListData());
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        YourPostsAdapter ypostsAdapter = new YourPostsAdapter(list);
        rvPosts.setAdapter(ypostsAdapter);
        ypostsAdapter.setOnItemClickCallback(new OnItemClickCallback() {
            @Override
            public void onItemClicked(Post data) {
                showPost(data);
            }
        });
        return view;
    }

    private void showPost(Post post) {
        Intent postDetailIntent = new Intent(getContext(), PostActivity.class);
        postDetailIntent.putExtra("post_title", post.getTitle());
        postDetailIntent.putExtra("post_caption", post.getCaption());
        startActivity(postDetailIntent);
    }
}