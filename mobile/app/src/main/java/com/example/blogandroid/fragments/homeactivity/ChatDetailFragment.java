package com.example.blogandroid.fragments.homeactivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.blogandroid.R;
import com.example.blogandroid.adapters.ChatDetailAdapter;
import com.example.blogandroid.apiservice.APIService;
import com.example.blogandroid.databinding.FragmentChatDetailBinding;
import com.example.blogandroid.models.MessageModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatDetailFragment extends Fragment {
    private FragmentChatDetailBinding fragmentChatDetailBinding;
    ChatDetailAdapter chatDetailAdapter;
    private List<MessageModel> messageList;
    private int userId;
    private int secondUserid;
    private int chatId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentChatDetailBinding = FragmentChatDetailBinding.inflate(inflater, container, false);
        return fragmentChatDetailBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getInt("userId", -1);
            secondUserid = getArguments().getInt("secondUserId", -1);
            chatId = getArguments().getInt("chatId", -1);
        }
        fragmentChatDetailBinding.messageRecyclerView.setHasFixedSize(false);
        fragmentChatDetailBinding.messageRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        messageList = new ArrayList<>();
        chatDetailAdapter = new ChatDetailAdapter(messageList, userId);
        fragmentChatDetailBinding.messageRecyclerView.setAdapter(chatDetailAdapter);
        fetchMessageList();
        fragmentChatDetailBinding.backButton.setOnClickListener(view1 -> {
            getActivity().finish();
        });
        fragmentChatDetailBinding.messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (fragmentChatDetailBinding.messageEditText.getText().length() == 0) {
                    fragmentChatDetailBinding.sendMessageButton.setClickable(false);
                    fragmentChatDetailBinding.sendMessageButton.setFocusable(false);
                }
                else {
                    fragmentChatDetailBinding.sendMessageButton.setClickable(true);
                    fragmentChatDetailBinding.sendMessageButton.setFocusable(true);
                }
            }
        });
        fragmentChatDetailBinding.sendMessageButton.setOnClickListener(view1 -> {
            createMessage();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchMessageList();
    }

    private void fetchMessageList() {
        HashMap<String, Integer> userTwoId = new HashMap<>();
        userTwoId.put("user_two", secondUserid);
        APIService.apiService.getMessages(userTwoId).enqueue(new Callback<List<MessageModel>>() {
            @Override
            public void onResponse(Call<List<MessageModel>> call, Response<List<MessageModel>> response) {
                if (response.isSuccessful()) {
                    messageList.clear();
                    messageList.addAll(response.body());
                    chatDetailAdapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(getActivity(), "Unable to load messages", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<MessageModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void createMessage() {
        String message = fragmentChatDetailBinding.messageEditText.getText().toString();
        HashMap<String, Object> messageData = new HashMap<>();
        messageData.put("content", message);
        messageData.put("chat", chatId);
        messageData.put("user", userId);
        APIService.apiService.createMessage(messageData).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    fetchMessageList();
                    fragmentChatDetailBinding.messageEditText.setText("");
                }
                else {
                    Toast.makeText(getActivity(), "Unable to send message", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}