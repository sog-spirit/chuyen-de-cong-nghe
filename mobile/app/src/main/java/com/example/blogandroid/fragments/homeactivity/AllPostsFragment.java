package com.example.blogandroid.fragments.homeactivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.blogandroid.R;
import com.example.blogandroid.adapters.AllPostsAdapter;
import com.example.blogandroid.apiservice.APIService;
import com.example.blogandroid.databinding.FragmentAllPostsBinding;
import com.example.blogandroid.models.PostModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllPostsFragment extends Fragment {
    private FragmentAllPostsBinding fragmentAllPostsBinding;
    AllPostsAdapter allPostsAdapter;
    private List<PostModel> postList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentAllPostsBinding = FragmentAllPostsBinding.inflate(inflater, container, false);
        return fragmentAllPostsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentAllPostsBinding.allPostsRecyclerView.setHasFixedSize(false);
        fragmentAllPostsBinding.allPostsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        postList = new ArrayList<>();
        allPostsAdapter = new AllPostsAdapter(postList);
        fragmentAllPostsBinding.allPostsRecyclerView.setAdapter(allPostsAdapter);
        loadPosts();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadPosts();
    }

    private void loadPosts() {
        APIService.apiService.getPosts().enqueue(new Callback<List<PostModel>>() {
            @Override
            public void onResponse(Call<List<PostModel>> call, Response<List<PostModel>> response) {
                if (response.isSuccessful()) {
                    postList.clear();
                    postList.addAll(response.body());
                    allPostsAdapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(getActivity(), "Unable to load posts from server!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<PostModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}