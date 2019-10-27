package com.example.socialapp;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class YourPostsFragment extends Fragment {

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
        return inflater.inflate(R.layout.fragment_your_posts, container, false);
    }

}