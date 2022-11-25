package com.example.blogandroid.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blogandroid.FragmentReplacerActivity;
import com.example.blogandroid.databinding.AllPostsItemBinding;
import com.example.blogandroid.models.PostModel;

import java.util.List;

public class AllPostsAdapter extends RecyclerView.Adapter<AllPostsAdapter.AllPostsHolder> {
    private List<PostModel> postList;
    AllPostsItemBinding allPostsItemBinding;

    public AllPostsAdapter(List<PostModel> postLists) {
        this.postList = postLists;
    }

    @NonNull
    @Override
    public AllPostsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        allPostsItemBinding = AllPostsItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AllPostsHolder(allPostsItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AllPostsHolder holder, int position) {
        PostModel post = postList.get(position);
        holder.allPostsItemBinding.authorTextView.setText(post.getName());
        holder.allPostsItemBinding.titleTextView.setText(post.getTitle());
        holder.allPostsItemBinding.timeTextView.setText(post.getCreated_on().toString());
        holder.allPostsItemBinding.contentTextView.setText(post.getContent());
        holder.allPostsItemBinding.commentButton.setOnClickListener(view -> {
            Intent intent = new Intent(holder.allPostsItemBinding.authorTextView.getContext(), FragmentReplacerActivity.class);
            intent.putExtra("postId", post.getId());
            intent.putExtra("isComment", true);
            holder.allPostsItemBinding.authorTextView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    static class AllPostsHolder extends RecyclerView.ViewHolder {
        AllPostsItemBinding allPostsItemBinding;

        public AllPostsHolder(@NonNull AllPostsItemBinding binding) {
            super(binding.getRoot());
            this.allPostsItemBinding = binding;
        }
    }
}
