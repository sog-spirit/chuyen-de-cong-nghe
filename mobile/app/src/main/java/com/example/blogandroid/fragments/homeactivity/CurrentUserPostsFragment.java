package com.example.blogandroid.fragments.homeactivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.blogandroid.R;
import com.example.blogandroid.adapters.CurrentUserPostsAdapter;
import com.example.blogandroid.apiservice.APIService;
import com.example.blogandroid.databinding.FragmentCurrentUserPostsBinding;
import com.example.blogandroid.models.PostModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurrentUserPostsFragment extends Fragment implements CurrentUserPostsAdapter.OnCurrentUserPostListener {
    private FragmentCurrentUserPostsBinding fragmentCurrentUserPostsBinding;
    CurrentUserPostsAdapter currentUserPostsAdapter;
    private List<PostModel> currentUserPostsList;
    private List<PostModel> apiFetchedList;
    private String sortOrder = "NEWEST";
    private String filterType = "ALL";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentCurrentUserPostsBinding = FragmentCurrentUserPostsBinding.inflate(inflater, container, false);
        return fragmentCurrentUserPostsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentCurrentUserPostsBinding.allPostsRecyclerView.setHasFixedSize(false);
        fragmentCurrentUserPostsBinding.allPostsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        currentUserPostsList = new ArrayList<>();
        apiFetchedList = new ArrayList<>();
        currentUserPostsAdapter = new CurrentUserPostsAdapter(currentUserPostsList, CurrentUserPostsFragment.this);
        fragmentCurrentUserPostsBinding.allPostsRecyclerView.setAdapter(currentUserPostsAdapter);
        fragmentCurrentUserPostsBinding.currentUserPostSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.equals(""))
                    loadCurrentUserPosts();
                queryPosts(s);
                return false;
            }
        });
        loadCurrentUserPosts();

        fragmentCurrentUserPostsBinding.sortAscendingButton.setOnClickListener(view1 -> {
            sortOrder = "ASCENDING";
            String query = fragmentCurrentUserPostsBinding.currentUserPostSearchView.getQuery().toString();
            queryPosts(query);
        });
        fragmentCurrentUserPostsBinding.sortDescendingButton.setOnClickListener(view1 -> {
            sortOrder = "DESCENDING";
            String query = fragmentCurrentUserPostsBinding.currentUserPostSearchView.getQuery().toString();
            queryPosts(query);
        });
        fragmentCurrentUserPostsBinding.allStatusFilterButton.setOnClickListener(view1 -> {
            filterType = "ALL";
            String query = fragmentCurrentUserPostsBinding.currentUserPostSearchView.getQuery().toString();
            queryPosts(query);
        });
        fragmentCurrentUserPostsBinding.publishStatusFilterButton.setOnClickListener(view1 -> {
            filterType = "PUBLISH";
            String query = fragmentCurrentUserPostsBinding.currentUserPostSearchView.getQuery().toString();
            queryPosts(query);
        });
        fragmentCurrentUserPostsBinding.draftStatusFilterButton.setOnClickListener(view1 -> {
            filterType = "DRAFT";
            String query = fragmentCurrentUserPostsBinding.currentUserPostSearchView.getQuery().toString();
            queryPosts(query);
        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void loadCurrentUserPosts() {
        APIService.apiService.getCurrentUserPosts().enqueue(new Callback<List<PostModel>>() {
            @Override
            public void onResponse(Call<List<PostModel>> call, Response<List<PostModel>> response) {
                if (response.isSuccessful()) {
                    apiFetchedList.clear();
                    apiFetchedList.addAll(response.body());
                    queryPosts("");
                }
                else {
                    Toast.makeText(getActivity(), "Unable to load current user posts!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<PostModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onCurrentUserPostClick(int position) {

    }

    private void queryPosts(String query) {
        currentUserPostsList.clear();
        if (!filterType.equals("ALL")) {
            if (filterType.equals("PUBLISH")) {
                for (PostModel post : apiFetchedList) {
                    if (post.getTitle().toLowerCase().contains(query) && post.getStatus().equals("PUBLISH"))
                        currentUserPostsList.add(post);
                }
            }
            else {
                for (PostModel post : apiFetchedList) {
                    if (post.getTitle().toLowerCase().contains(query) && post.getStatus().equals("DRAFT"))
                        currentUserPostsList.add(post);
                }
            }
        }
        else {
            for (PostModel post : apiFetchedList) {
                if (post.getTitle().toLowerCase().contains(query))
                    currentUserPostsList.add(post);
            }
        }
        Collections.sort(currentUserPostsList, (a, b) -> {
            if (sortOrder.equals("ASCENDING"))
                return a.getId() - b.getId();
            return b.getId() - a.getId();
        });
        currentUserPostsAdapter.notifyDataSetChanged();
    }
}