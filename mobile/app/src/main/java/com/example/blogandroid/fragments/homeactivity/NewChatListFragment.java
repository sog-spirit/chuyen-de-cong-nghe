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
import com.example.blogandroid.adapters.NewChatListAdapter;
import com.example.blogandroid.apiservice.APIService;
import com.example.blogandroid.databinding.FragmentNewChatListBinding;
import com.example.blogandroid.models.NewChatListModel;
import com.example.blogandroid.models.NewChatResultModel;
import com.example.blogandroid.models.UserModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewChatListFragment extends Fragment implements NewChatListAdapter.OnCurrentNewChatItemListener {
    private FragmentNewChatListBinding fragmentNewChatListBinding;
    NewChatListAdapter newChatListAdapter;
    private List<NewChatListModel> apiFetchedNewChatList;
    private List<NewChatListModel> newChatList;
    private int userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentNewChatListBinding = FragmentNewChatListBinding.inflate(inflater, container, false);
        return fragmentNewChatListBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeUI();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCurrentNewChatItemClick(int position) {
        NewChatListModel item = newChatList.get(position);
        createChat(item.getId());
    }

    private void initializeUI() {
        APIService.apiService.getUserId().enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful()) {
                    setUserId(response.body().getId());
                    apiFetchedNewChatList = new ArrayList<>();
                    newChatList = new ArrayList<>();
                    fragmentNewChatListBinding.newChatResultRecyclerView.setHasFixedSize(false);
                    fragmentNewChatListBinding.newChatResultRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    newChatListAdapter = new NewChatListAdapter(newChatList, NewChatListFragment.this);
                    fragmentNewChatListBinding.newChatResultRecyclerView.setAdapter(newChatListAdapter);
                    fetchNewChatList();
                    fragmentNewChatListBinding.newChatSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            if (newText.equals(""))
                                fetchNewChatList();
                            queryNewChat(newText);
                            return false;
                        }
                    });
                }
                else {
                    Toast.makeText(getActivity(), "Unable to get user id", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
    private void setUserId(int id) { this.userId = id; }

    private void fetchNewChatList() {
        APIService.apiService.getUsers().enqueue(new Callback<List<NewChatListModel>>() {
            @Override
            public void onResponse(Call<List<NewChatListModel>> call, Response<List<NewChatListModel>> response) {
                if (response.isSuccessful()) {
                    apiFetchedNewChatList.clear();
                    apiFetchedNewChatList.addAll(response.body());
                    newChatList.clear();
                    newChatList.addAll(apiFetchedNewChatList);
                    newChatListAdapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(getActivity(), "Unable to load user list", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<NewChatListModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void queryNewChat(String query) {
        newChatList.clear();
        for (NewChatListModel item : apiFetchedNewChatList) {
            if (item.getUsername().toLowerCase().contains(query))
                newChatList.add(item);
        }
        newChatListAdapter.notifyDataSetChanged();
    }

    private void createChat(int userTwoId) {
        HashMap<String, Integer> userTwo = new HashMap<>();
        userTwo.put("user_two", userTwoId);
        APIService.apiService.createChat(userTwo).enqueue(new Callback<NewChatResultModel>() {
            @Override
            public void onResponse(Call<NewChatResultModel> call, Response<NewChatResultModel> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(getContext(), FragmentReplacerActivity.class);
                    intent.putExtra("isMessage", true);
                    intent.putExtra("userId", userId);
                    intent.putExtra("chatId", response.body().getId());
                    intent.putExtra("secondUserId", userTwoId);
                    getContext().startActivity(intent);
                }
                else {
                    HashMap<String, Integer> data = new HashMap<>();
                    data.put("user_two", userTwoId);
                    APIService.apiService.getChatId(data).enqueue(new Callback<NewChatResultModel>() {
                        @Override
                        public void onResponse(Call<NewChatResultModel> call, Response<NewChatResultModel> response) {
                            if (response.isSuccessful()) {
                                Intent intent = new Intent(getContext(), FragmentReplacerActivity.class);
                                intent.putExtra("isMessage", true);
                                intent.putExtra("userId", userId);
                                intent.putExtra("chatId", response.body().getId());
                                intent.putExtra("secondUserId", userTwoId);
                                getContext().startActivity(intent);
                            }
                            else {
                                Toast.makeText(getActivity(), "Unable to load message list", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<NewChatResultModel> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<NewChatResultModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}