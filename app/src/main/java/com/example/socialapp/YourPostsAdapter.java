package com.example.socialapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.example.socialapp.model.Post;

public class YourPostsAdapter extends RecyclerView.Adapter<YourPostsAdapter.CardViewViewHolder>  {
    private ArrayList<Post> listPost;
    private Context ctx;
    private OnItemClickCallback onItemClickCallback;

    public YourPostsAdapter(ArrayList<Post> listPost) {
        this.listPost = listPost;
    }

    public YourPostsAdapter(ArrayList<Post> listPost, Context ctx) {
        this.listPost = listPost;
        this.ctx = ctx;
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.your_posts_list,viewGroup,false);
        return new CardViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardViewViewHolder cardViewViewHolder, int i) {
        Post post = listPost.get(i);

        cardViewViewHolder.tvName.setText(post.getName());
        cardViewViewHolder.tvCaption.setText(post.getPost());
        cardViewViewHolder.postContent = post.getPost();
        cardViewViewHolder.postId = post.getId_post();

        cardViewViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickCallback.onItemClicked(listPost.get(cardViewViewHolder.getAdapterPosition()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return listPost.size();
    }

    class CardViewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvName, tvCaption;
        ImageButton ibEditPost, ibDeletePost;
        String postContent, postId;

        public CardViewViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_ypost_name);
            tvCaption = itemView.findViewById(R.id.tv_ypost_caption);
            ibEditPost = itemView.findViewById(R.id.ib_editpost);
            ibDeletePost = itemView.findViewById(R.id.ib_deletepost);

            ibEditPost.setOnClickListener(this);
            ibDeletePost.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view == ibEditPost) {
                Intent editPostIntent = new Intent(ctx,EditPostActivity.class);
                editPostIntent.putExtra("post_content",postContent);
                editPostIntent.putExtra("id_post", postId);
                ctx.startActivity(editPostIntent);
            } else if(view == ibDeletePost) {
                Intent deletePostIntent = new Intent(ctx,DeletePostActivity.class);
                deletePostIntent.putExtra("post_content",postContent);
                deletePostIntent.putExtra("id_post", postId);
                ctx.startActivity(deletePostIntent);
            }
        }
    }


}
