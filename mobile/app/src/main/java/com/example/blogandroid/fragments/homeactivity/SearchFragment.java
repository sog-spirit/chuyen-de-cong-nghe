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
import com.example.blogandroid.adapters.SearchPostAdapter;
import com.example.blogandroid.apiservice.APIService;
import com.example.blogandroid.databinding.FragmentSearchBinding;
import com.example.blogandroid.models.PostModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment implements SearchPostAdapter.OnSearchPostListener {
    private FragmentSearchBinding fragmentSearchBinding;
    SearchPostAdapter searchPostAdapter;
    private List<PostModel> postList;
    private List<PostModel> apiFetchedPostList;
    private String queryType = "TITLE";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentSearchBinding = FragmentSearchBinding.inflate(inflater, container, false);
        return fragmentSearchBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentSearchBinding.searchResultRecyclerView.setHasFixedSize(false);
        fragmentSearchBinding.searchResultRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        postList = new ArrayList<>();
        apiFetchedPostList = new ArrayList<>();
        searchPostAdapter = new SearchPostAdapter(postList, SearchFragment.this);
        fragmentSearchBinding.searchResultRecyclerView.setAdapter(searchPostAdapter);
        loadPosts();
        fragmentSearchBinding.postSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals(""))
                    loadPosts();
                queryPosts(newText);
                return false;
            }
        });
        fragmentSearchBinding.queryByTitleButton.setOnClickListener(view1 -> {
            queryType = "TITLE";
            postList.clear();
            String query = fragmentSearchBinding.postSearchView.getQuery().toString();
            for (PostModel post : apiFetchedPostList) {
                if (post.getTitle().toLowerCase().contains(query))
                    postList.add(post);
            }
            searchPostAdapter.notifyDataSetChanged();
        });
        fragmentSearchBinding.queryByUsernameButton.setOnClickListener(view1 -> {
            queryType = "USERNAME";
            postList.clear();
            String query = fragmentSearchBinding.postSearchView.getQuery().toString();
            for (PostModel post : apiFetchedPostList) {
                if (post.getName().toLowerCase().contains(query))
                    postList.add(post);
            }
            searchPostAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onSearchPostClick(int position) {
        PostModel post = postList.get(position);
        Intent intent = new Intent(getContext(), FragmentReplacerActivity.class);
        intent.putExtra("postId", post.getId());
        intent.putExtra("isComment", true);
        getContext().startActivity(intent);
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
                    apiFetchedPostList.clear();
                    apiFetchedPostList.addAll(response.body());
                    postList.clear();
                    postList.addAll(apiFetchedPostList);
                    searchPostAdapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(getActivity(), "Unable to search post", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<PostModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void queryPosts(String query) {
        postList.clear();
        if (queryType.equals("TITLE")) {
            for (PostModel post : apiFetchedPostList) {
                if (post.getTitle().toLowerCase().contains(query))
                    postList.add(post);
            }
        }
        else {
            for (PostModel post : apiFetchedPostList) {
                if (post.getName().toLowerCase().contains(query))
                    postList.add(post);
            }
        }
        searchPostAdapter.notifyDataSetChanged();
    }
}