package com.example.blogandroid.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blogandroid.apiservice.APIService;
import com.example.blogandroid.databinding.ChatListItemBinding;
import com.example.blogandroid.models.ChatListModel;
import com.example.blogandroid.models.MessageModel;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListItemHolder> {
    private List<ChatListModel> chatList;
    ChatListItemBinding chatListItemBinding;
    OnCurrentChatListItemListener onCurrentChatListItemListener;
    private int userId;

    public ChatListAdapter(List<ChatListModel> list, OnCurrentChatListItemListener listener, int id) {
        this.chatList = list;
        this.onCurrentChatListItemListener = listener;
        this.userId = id;
    }

    @NonNull
    @Override
    public ChatListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        chatListItemBinding = ChatListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ChatListItemHolder(chatListItemBinding, onCurrentChatListItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListItemHolder holder, int position) {
        int userTwo;
        ChatListModel item = chatList.get(position);
        if (userId != item.getUser_one()) {
            holder.chatListItemBinding.receiverTextView.setText(item.getUser_one_name());
            userTwo = item.getUser_one();
        }
        else {
            holder.chatListItemBinding.receiverTextView.setText(item.getUser_two_name());
            userTwo = item.getUser_two();
        }
        HashMap<String, Integer> userTwoId = new HashMap<>();
        userTwoId.put("user_two", userTwo);
        APIService.apiService.getNewestMessage(userTwoId).enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                if (response.isSuccessful()) {
                    holder.chatListItemBinding.lastMessageTextView.setText(response.body().getContent());
                }
            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    class ChatListItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ChatListItemBinding chatListItemBinding;
        OnCurrentChatListItemListener onCurrentChatListItemListener;

        public ChatListItemHolder(@NonNull ChatListItemBinding binding, OnCurrentChatListItemListener listener) {
            super(binding.getRoot());

            this.chatListItemBinding = binding;
            this.onCurrentChatListItemListener = listener;
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onCurrentChatListItemListener.onCurrentChatListItemClick(getAdapterPosition());
        }
    }

    public interface OnCurrentChatListItemListener {
        void onCurrentChatListItemClick(int position);
    }
}
