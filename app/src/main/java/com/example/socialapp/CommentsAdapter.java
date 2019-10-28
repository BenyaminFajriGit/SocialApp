package com.example.socialapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CardViewViewHolder>{
    private ArrayList<Comment> listComment;
    private OnItemClickCallback onItemClickCallback;

    public CommentsAdapter(ArrayList<Comment> listComment) {
        this.listComment = listComment;
    }

    @NonNull
    @Override
    public CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comments_list,viewGroup,false);
        return new CardViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardViewViewHolder cardViewViewHolder, int i) {
        Comment comment = listComment.get(i);

        cardViewViewHolder.tvUsername.setText(comment.getUsername());
        cardViewViewHolder.tvCaption.setText(comment.getCaption());

    }

    @Override
    public int getItemCount() {
        return listComment.size();
    }

    class CardViewViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvCaption;

        public CardViewViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tv_comment_username);
            tvCaption = itemView.findViewById(R.id.tv_comment_caption);
        }
    }

}
