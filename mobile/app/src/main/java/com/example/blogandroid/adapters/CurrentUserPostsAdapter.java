package com.example.blogandroid.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blogandroid.databinding.CurrentUserPostsItemBinding;
import com.example.blogandroid.models.PostModel;

import java.util.List;

public class CurrentUserPostsAdapter extends RecyclerView.Adapter<CurrentUserPostsAdapter.CurrentUserPostsHolder> {
    private List<PostModel> currentUserPostsList;
    CurrentUserPostsItemBinding currentUserPostsItemBinding;
    OnCurrentUserPostListener onCurrentUserPostListener;

    public CurrentUserPostsAdapter(List<PostModel> list, OnCurrentUserPostListener listener) {
        this.currentUserPostsList = list;
        this.onCurrentUserPostListener = listener;
    }

    @NonNull
    @Override
    public CurrentUserPostsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        currentUserPostsItemBinding = CurrentUserPostsItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CurrentUserPostsHolder(currentUserPostsItemBinding, onCurrentUserPostListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrentUserPostsHolder holder, int position) {
        PostModel post = currentUserPostsList.get(position);
        holder.currentUserPostsItemBinding.titleTextView.setText(post.getTitle());
        holder.currentUserPostsItemBinding.contentTextView.setText(post.getContent());
        holder.currentUserPostsItemBinding.timeTextView.setText(post.getCreated_on().toString());
        holder.currentUserPostsItemBinding.statusTextView.setText(post.getStatus());
        holder.currentUserPostsItemBinding.commentButton.setOnClickListener((View.OnClickListener) view -> {

        });
    }

    @Override
    public int getItemCount() {
        return currentUserPostsList.size();
    }

    class CurrentUserPostsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CurrentUserPostsItemBinding currentUserPostsItemBinding;
        OnCurrentUserPostListener onCurrentUserPostListener;

        public CurrentUserPostsHolder(@NonNull CurrentUserPostsItemBinding binding, OnCurrentUserPostListener listener) {
            super(binding.getRoot());

            this.currentUserPostsItemBinding = binding;
            this.onCurrentUserPostListener = listener;
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onCurrentUserPostListener.onCurrentUserPostClick(getAdapterPosition());
        }
    }

    public interface OnCurrentUserPostListener {
        void onCurrentUserPostClick(int position);
    }
}
