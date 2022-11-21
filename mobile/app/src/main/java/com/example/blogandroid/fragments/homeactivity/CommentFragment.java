package com.example.blogandroid.fragments.homeactivity;

import android.content.Intent;
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

import com.example.blogandroid.FragmentReplacerActivity;
import com.example.blogandroid.R;
import com.example.blogandroid.adapters.CommentAdapter;
import com.example.blogandroid.apiservice.APIService;
import com.example.blogandroid.databinding.FragmentCommentBinding;
import com.example.blogandroid.models.CommentModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentFragment extends Fragment implements CommentAdapter.OnCommentListener {

    private FragmentCommentBinding fragmentCommentBinding;
    CommentAdapter commentAdapter;
    private List<CommentModel> commentList;
    int postId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentCommentBinding = FragmentCommentBinding.inflate(inflater, container, false);
        return fragmentCommentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null)
            postId = getArguments().getInt("postId");
        fragmentCommentBinding.commentRecyclerView.setHasFixedSize(false);
        fragmentCommentBinding.commentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(commentList, CommentFragment.this);
        fragmentCommentBinding.commentRecyclerView.setAdapter(commentAdapter);
        fragmentCommentBinding.sendCommentButton.setOnClickListener(view1 -> {
            String commentContent = fragmentCommentBinding.commentEditText.getText().toString();
            if (commentContent.isEmpty()) {
                Toast.makeText(getContext(), "Comment cannot be empty", Toast.LENGTH_LONG).show();
                return;
            }
            HashMap<String, Object> newCommentData = new HashMap<>();
            newCommentData.put("post", postId);
            newCommentData.put("content", commentContent);
            APIService.apiService.createCommentFromPost(newCommentData).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        fragmentCommentBinding.commentEditText.setText("");
                        Toast.makeText(getContext(), "Comment created successfully", Toast.LENGTH_LONG).show();
                        loadThisPostComments();
                    }
                    else {
                        Toast.makeText(getContext(), "Error creating comment", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        });
        fragmentCommentBinding.sendCommentButton.setClickable(false);
        fragmentCommentBinding.sendCommentButton.setFocusable(false);
        fragmentCommentBinding.backButton.setOnClickListener(view1 -> getActivity().finish());
        fragmentCommentBinding.commentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (fragmentCommentBinding.commentEditText.getText().length() == 0) {
                    fragmentCommentBinding.sendCommentButton.setClickable(false);
                    fragmentCommentBinding.sendCommentButton.setFocusable(false);
                }
                else {
                    fragmentCommentBinding.sendCommentButton.setClickable(true);
                    fragmentCommentBinding.sendCommentButton.setFocusable(true);
                }
            }
        });
        loadThisPostComments();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadThisPostComments();
    }

    private void loadThisPostComments() {
        HashMap<String, Integer> postIdData = new HashMap<>();
        postIdData.put("post_id", postId);
        APIService.apiService.getCommentsFromPost(postIdData).enqueue(new Callback<List<CommentModel>>() {
            @Override
            public void onResponse(Call<List<CommentModel>> call, Response<List<CommentModel>> response) {
                if (response.isSuccessful()) {
                    commentList.clear();
                    commentList.addAll(response.body());
                    commentAdapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(getContext(), "Unable to load post from server!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<CommentModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onCommentClick(int position) {
        CommentModel comment = commentList.get(position);
        Intent intent = new Intent(getContext(), FragmentReplacerActivity.class);
        intent.putExtra("commentId", comment.getId());
        intent.putExtra("isCommentDetail", true);
        getContext().startActivity(intent);
    }
}