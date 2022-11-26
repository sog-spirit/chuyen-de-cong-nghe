package com.example.blogandroid.fragments.homeactivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.blogandroid.FragmentReplacerActivity;
import com.example.blogandroid.R;
import com.example.blogandroid.adapters.ChatListAdapter;
import com.example.blogandroid.apiservice.APIService;
import com.example.blogandroid.databinding.ChatListItemBinding;
import com.example.blogandroid.databinding.FragmentChatListBinding;
import com.example.blogandroid.models.ChatListModel;
import com.example.blogandroid.models.UserModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatListFragment extends Fragment implements ChatListAdapter.OnCurrentChatListItemListener {
    private FragmentChatListBinding chatListBinding;
    ChatListAdapter chatListAdapter;
    private List<ChatListModel> apiFetchedChatList;
    private List<ChatListModel> chatList;
    private int userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        chatListBinding = FragmentChatListBinding.inflate(inflater, container, false);
        return chatListBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeUI();
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchChatList();
    }

    @Override
    public void onCurrentChatListItemClick(int position) {
        ChatListModel item = chatList.get(position);
        Intent intent = new Intent(getContext(), FragmentReplacerActivity.class);
        intent.putExtra("isMessage", true);
        intent.putExtra("userId", userId);
        intent.putExtra("chatId", item.getId());
        if (item.getUser_one() != userId)
            intent.putExtra("secondUserId", item.getUser_one());
        else
            intent.putExtra("secondUserId", item.getUser_two());
        getContext().startActivity(intent);
    }

    private void initializeUI() {
        APIService.apiService.getUserId().enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful()) {
                    setUserId(response.body().getId());
                    chatListBinding.chatListRecyclerView.setHasFixedSize(false);
                    chatListBinding.chatListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    chatList = new ArrayList<>();
                    apiFetchedChatList = new ArrayList<>();
                    chatListAdapter = new ChatListAdapter(chatList, ChatListFragment.this, userId);
                    chatListBinding.chatListRecyclerView.setAdapter(chatListAdapter);
                    fetchChatList();
                    chatListBinding.chatSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            if (newText.equals(""))
                                fetchChatList();
                            queryChat(newText);
                            return false;
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
    private void fetchChatList() {
        APIService.apiService.getChats().enqueue(new Callback<List<ChatListModel>>() {
            @Override
            public void onResponse(Call<List<ChatListModel>> call, Response<List<ChatListModel>> response) {
                if (response.isSuccessful()) {
                    apiFetchedChatList.clear();
                    apiFetchedChatList.addAll(response.body());
                    chatList.clear();
                    chatList.addAll(apiFetchedChatList);
                    chatListAdapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(getActivity(), "Unable to fetch chat list", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<ChatListModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void setUserId(int id) { this.userId = id; }

    private void queryChat(String query) {
        chatList.clear();
        for (ChatListModel item : apiFetchedChatList) {
            if (item.getUser_one_name().toLowerCase().contains(query) ||
                    item.getUser_two_name().toLowerCase().contains(query)) {
                chatList.add(item);
            }
        }
        chatListAdapter.notifyDataSetChanged();
    }
}