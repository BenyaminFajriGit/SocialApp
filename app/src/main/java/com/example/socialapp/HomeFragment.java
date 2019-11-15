package com.example.socialapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import com.example.socialapp.model.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView rvPosts;
    private FloatingActionButton fabCreatePost;
    private PostsAdapter postsAdapter;
    private ArrayList<Post> list = new ArrayList<>();
    private SwipeRefreshLayout srlPosts;

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

        getPosts();

        rvPosts = view.findViewById(R.id.rv_posts_list);
        fabCreatePost = view.findViewById(R.id.fab_createpost);
        srlPosts = view.findViewById(R.id.srl_posts);

        rvPosts.setHasFixedSize(true);

        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        postsAdapter = new PostsAdapter(list);
        rvPosts.setAdapter(postsAdapter);

        postsAdapter.setOnItemClickCallback(new OnItemClickCallback() {
            @Override
            public void onItemClicked(Post data) {
                showPost(data);
            }
        });
        fabCreatePost.setOnClickListener(this);
        srlPosts.setColorSchemeResources(R.color.colorAccent);
        srlPosts.setOnRefreshListener(this);

        return view;
    }

    private void showPost(Post post) {
        Intent postDetailIntent = new Intent(getContext(), PostActivity.class);
        postDetailIntent.putExtra("post_name", post.getName());
        postDetailIntent.putExtra("post_caption", post.getPost());
        postDetailIntent.putExtra("post_id",post.getId_post());
        postDetailIntent.putExtra("post_time",post.getTime());
        startActivity(postDetailIntent);
    }

    @Override
    public void onClick(View view) {
        Intent createNewPostIntent = new Intent(getContext(), CreateNewPostActivity.class);
        startActivity(createNewPostIntent);
    }

    private void getPosts(){
        class GetPosts extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getContext(),"Fetching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showPosts(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();

                String s = rh.sendGetRequest("http://frozenbits.tech/socialAppWS/index.php/DataPost/getDataPostAll");

                return s;
            }
        }
        GetPosts gp = new GetPosts();
        gp.execute();

    }

    private void showPosts(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            boolean status= jsonObject.getBoolean("status");
            String message= jsonObject.getString("message");
            if(status){
                JSONArray result = jsonObject.getJSONArray("data");
                for (int i = 0; i < result.length(); i++) {
                    Post post = new Post();
                    JSONObject pst = result.getJSONObject(i);
                    post.setId_post(pst.getString("id_post"));
                    post.setId_user(pst.getString("id_user"));
                    post.setPost(pst.getString("post"));
                    post.setTime(pst.getString("waktu"));
                    post.setName(pst.getString("nama"));
                    list.add(post);
                }
                postsAdapter.notifyDataSetChanged();
            }else{
                Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
//                getActivity().finish();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        list.clear();
        getPosts();
        srlPosts.setRefreshing(false);
    }


}