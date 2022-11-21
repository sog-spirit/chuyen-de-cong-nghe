package com.example.blogandroid.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blogandroid.databinding.SearchPostItemBinding;
import com.example.blogandroid.models.PostModel;

import java.util.List;

public class SearchPostAdapter extends RecyclerView.Adapter<SearchPostAdapter.SearchPostHolder> {
    private List<PostModel> searchResultList;
    SearchPostItemBinding searchPostItemBinding;
    OnSearchPostListener onSearchPostListener;

    public SearchPostAdapter(List<PostModel> list, OnSearchPostListener listener) {
        this.searchResultList = list;
        this.onSearchPostListener = listener;
    }

    @NonNull
    @Override
    public SearchPostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        searchPostItemBinding = SearchPostItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SearchPostHolder(searchPostItemBinding, onSearchPostListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchPostHolder holder, int position) {
        PostModel post = searchResultList.get(position);
        holder.searchPostItemBinding.authorTextView.setText(post.getName());
        holder.searchPostItemBinding.titleTextView.setText(post.getTitle());
    }

    @Override
    public int getItemCount() {
        return searchResultList.size();
    }

    class SearchPostHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        SearchPostItemBinding searchPostItemBinding;
        OnSearchPostListener onSearchPostListener;

        public SearchPostHolder(@NonNull SearchPostItemBinding binding, OnSearchPostListener listener) {
            super(binding.getRoot());

            this.searchPostItemBinding = binding;
            this.onSearchPostListener = listener;
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onSearchPostListener.onSearchPostClick(getAdapterPosition());
        }
    }

    public interface OnSearchPostListener {
        void onSearchPostClick(int position);
    }
}
