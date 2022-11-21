package com.example.blogandroid.fragments.homeactivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.blogandroid.R;
import com.example.blogandroid.apiservice.APIService;
import com.example.blogandroid.databinding.FragmentCommentDetailBinding;
import com.example.blogandroid.models.CommentModel;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentDetailFragment extends Fragment {
    private FragmentCommentDetailBinding fragmentCommentDetailBinding;
    int commentId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentCommentDetailBinding = FragmentCommentDetailBinding.inflate(inflater, container, false);
        return fragmentCommentDetailBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            commentId = getArguments().getInt("commentId");
        }
        getCommentDetail();
        fragmentCommentDetailBinding.backButton.setOnClickListener(view1 -> getActivity().finish());
        fragmentCommentDetailBinding.saveCommentButton.setOnClickListener(view1 -> {
            editComment();
            getActivity().finish();
        });
        fragmentCommentDetailBinding.deleteCommentButton.setOnClickListener(view1 -> {
            deleteComment();
            getActivity().finish();
        });
        fragmentCommentDetailBinding.contentTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void getCommentDetail() {
        APIService.apiService.getCommentById(commentId).enqueue(new Callback<CommentModel>() {
            @Override
            public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                if (response.isSuccessful()) {
                    CommentModel comment = response.body();
                    fragmentCommentDetailBinding.contentTextInput.setText(comment.getContent());
                }
                else {
                    Toast.makeText(getContext(), "Unable to load comment", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CommentModel> call, Throwable t) {

            }
        });
    }

    private void deleteComment() {
        HashMap<String, Integer> commentIdData = new HashMap<>();
        commentIdData.put("comment_id", commentId);
        APIService.apiService.deleteComment(commentIdData).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Comment deleted successfully", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getContext(), "Unable to delete comment", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void editComment() {
        HashMap<String, Object> commentEditData = new HashMap<>();
        commentEditData.put("comment_id", commentId);
        commentEditData.put("content", fragmentCommentDetailBinding.contentTextInput.getText().toString());
        APIService.apiService.editComment(commentEditData).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Edit comment successfully", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getContext(), "Unable to edit comment", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}