package com.example.blogandroid.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blogandroid.databinding.NewChatItemBinding;
import com.example.blogandroid.models.NewChatListModel;

import java.util.List;

public class NewChatListAdapter extends RecyclerView.Adapter<NewChatListAdapter.NewChatListHolder> {
    final private List<NewChatListModel> newChatList;
    NewChatItemBinding newChatItemBinding;
    OnCurrentNewChatItemListener onCurrentNewChatItemListener;

    public NewChatListAdapter(List<NewChatListModel> list, OnCurrentNewChatItemListener listener) {
        this.newChatList = list;
        this.onCurrentNewChatItemListener = listener;
    }

    @NonNull
    @Override
    public NewChatListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        newChatItemBinding = NewChatItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new NewChatListHolder(newChatItemBinding, onCurrentNewChatItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NewChatListHolder holder, int position) {
        NewChatListModel item = newChatList.get(position);
        holder.newChatItemBinding.usernameTextView.setText(item.getUsername());
    }

    @Override
    public int getItemCount() {
        return newChatList.size();
    }

    class NewChatListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        NewChatItemBinding newChatItemBinding;
        OnCurrentNewChatItemListener onCurrentNewChatItemListener;

        public NewChatListHolder(@NonNull NewChatItemBinding binding, OnCurrentNewChatItemListener listener) {
            super(binding.getRoot());

            this.newChatItemBinding = binding;
            this.onCurrentNewChatItemListener = listener;
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onCurrentNewChatItemListener.onCurrentNewChatItemClick(getAdapterPosition());
        }
    }

    public interface OnCurrentNewChatItemListener {
        void onCurrentNewChatItemClick(int position);
    }
}
