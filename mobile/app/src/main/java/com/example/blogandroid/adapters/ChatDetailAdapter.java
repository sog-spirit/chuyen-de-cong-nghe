package com.example.blogandroid.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blogandroid.databinding.ReceivedMessageItemBinding;
import com.example.blogandroid.databinding.SentMessageItemBinding;
import com.example.blogandroid.models.MessageModel;

import java.util.List;

public class ChatDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<MessageModel> messageList;
    private int userId;
    public static int VIEW_TYPE_SENT = 1;
    public static int VIEW_TYPE_RECEIVED = 2;

    public ChatDetailAdapter(List<MessageModel> list, int id) {
        this.messageList = list;
        this.userId = id;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT) {
            return new SentMessageHolder(
                    SentMessageItemBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        }
        return new ReceivedMessageHolder(
                ReceivedMessageItemBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_SENT) {
            ((SentMessageHolder) holder).sentMessageItemBinding.messageContentTextView.setText(messageList.get(position).getContent());
        }
        else {
            ((ReceivedMessageHolder) holder).receivedMessageItemBinding.messageContentTextView.setText(messageList.get(position).getContent());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (messageList.get(position).getUser() == userId)
            return VIEW_TYPE_SENT;
        return VIEW_TYPE_RECEIVED;
    }

    class SentMessageHolder extends RecyclerView.ViewHolder {
        SentMessageItemBinding sentMessageItemBinding;

        public SentMessageHolder(@NonNull SentMessageItemBinding binding) {
            super(binding.getRoot());
            this.sentMessageItemBinding = binding;
        }
    }

    class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        ReceivedMessageItemBinding receivedMessageItemBinding;

        public ReceivedMessageHolder(@NonNull ReceivedMessageItemBinding binding) {
            super(binding.getRoot());
            this.receivedMessageItemBinding = binding;
        }
    }
}
