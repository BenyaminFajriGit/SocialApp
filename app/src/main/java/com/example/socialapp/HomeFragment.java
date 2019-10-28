package com.example.socialapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private RecyclerView rvPosts;
    private ArrayList<Post> list = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        rvPosts = view.findViewById(R.id.rv_posts_list);
        rvPosts.setHasFixedSize(true);
        list.addAll(PostsData.getListData());
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        PostsAdapter postsAdapter = new PostsAdapter(list);
        rvPosts.setAdapter(postsAdapter);
        postsAdapter.setOnItemClickCallback(new OnItemClickCallback() {
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