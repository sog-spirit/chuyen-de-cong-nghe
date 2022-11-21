package com.example.blogandroid.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blogandroid.databinding.CommentItemBinding;
import com.example.blogandroid.models.CommentModel;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentItemHolder> {
    private List<CommentModel> commentList;
    CommentItemBinding commentItemBinding;
    OnCommentListener onCommentListener;

    public CommentAdapter(List<CommentModel> list, OnCommentListener listener) {
        this.commentList = list;
        this.onCommentListener = listener;
    }

    @NonNull
    @Override
    public CommentItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        commentItemBinding = CommentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CommentItemHolder(commentItemBinding, onCommentListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentItemHolder holder, int position) {
        CommentModel comment = commentList.get(position);
        holder.commentItemBinding.commentUsername.setText(comment.getName());
        holder.commentItemBinding.commentContent.setText(comment.getContent());
        String createTime = "Created at: " + comment.getCreated_on().toString();
        holder.commentItemBinding.commentCreatedTime.setText(createTime);
        String updateTime = "Updated at: " + comment.getUpdated_on().toString();
        holder.commentItemBinding.commentUpdatedTime.setText(updateTime);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    class CommentItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CommentItemBinding commentItemBinding;
        OnCommentListener onCommentListener;

        public CommentItemHolder(@NonNull CommentItemBinding binding, OnCommentListener listener) {
            super(binding.getRoot());

            this.commentItemBinding = binding;
            this.onCommentListener = listener;
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onCommentListener.onCommentClick(getAdapterPosition());
        }
    }

    public interface OnCommentListener {
        void onCommentClick(int position);
    }
}
