package com.example.socialapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.example.socialapp.model.Post;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.CardViewViewHolder>{
    private ArrayList<Post> listPost;
    private OnItemClickCallback onItemClickCallback;

    public PostsAdapter(ArrayList<Post> listPost) {
        this.listPost = listPost;
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.posts_list,viewGroup,false);
        return new CardViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardViewViewHolder cardViewViewHolder, int i) {
        Post post = listPost.get(i);

        cardViewViewHolder.tvName.setText(post.getName());
        cardViewViewHolder.tvCaption.setText(post.getPost());

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

    class CardViewViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvCaption;

        public CardViewViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_post_name);
            tvCaption = itemView.findViewById(R.id.tv_post_caption);
        }
    }

}
