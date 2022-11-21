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
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.blogandroid.R;
import com.example.blogandroid.apiservice.APIService;
import com.example.blogandroid.databinding.FragmentPostEditBinding;
import com.example.blogandroid.models.PostModel;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostEditFragment extends Fragment {
    private FragmentPostEditBinding fragmentPostEditBinding;
    ArrayAdapter<CharSequence> spinnerAdapter;
    int postId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentPostEditBinding = FragmentPostEditBinding.inflate(inflater, container, false);
        return fragmentPostEditBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spinnerAdapter = ArrayAdapter.createFromResource(getActivity().getBaseContext(), R.array.post_status, R.layout.spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fragmentPostEditBinding.postStatusSpinner.setAdapter(spinnerAdapter);

        if (getArguments() != null)
            postId = getArguments().getInt("postId", 0);
        getPostDetail();
        fragmentPostEditBinding.savePostButton.setOnClickListener(view1 -> {
            String title = fragmentPostEditBinding.titleTextInput.getText().toString();
            String content = fragmentPostEditBinding.contentTextInput.getText().toString();
            if (title.isEmpty() || content.isEmpty()) {
                if (title.isEmpty())
                    fragmentPostEditBinding.titleTextInputLayout.setError("Title is empty");
                if (content.isEmpty())
                    fragmentPostEditBinding.contentTextInputLayout.setError("Content is empty");
                return;
            }
            String status = fragmentPostEditBinding.postStatusSpinner.getSelectedItem().toString().toUpperCase();
            updatePost(title, content, status);
            getActivity().finish();
        });
        fragmentPostEditBinding.deletePostButton.setOnClickListener(view1 -> {
            deletePost();
            getActivity().finish();
        });
        fragmentPostEditBinding.backButton.setOnClickListener(view1 -> getActivity().finish());
        fragmentPostEditBinding.titleTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (fragmentPostEditBinding.titleTextInput.getText().length() > 0)
                    fragmentPostEditBinding.titleTextInputLayout.setError(null);
            }
        });
        fragmentPostEditBinding.contentTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (fragmentPostEditBinding.contentTextInput.getText().length() > 0)
                    fragmentPostEditBinding.contentTextInputLayout.setError(null);
            }
        });
    }

    private void getPostDetail() {
        HashMap<String, Integer> postIdData = new HashMap<>();
        postIdData.put("post_id", postId);
        APIService.apiService.getPostById(postIdData).enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                if (response.isSuccessful()) {
                    PostModel post = response.body();
                    fragmentPostEditBinding.titleTextInput.setText(post.getTitle());
                    fragmentPostEditBinding.contentTextInput.setText(post.getContent());
                    String status = post.getStatus();

                    int spinnerPosition = spinnerAdapter.getPosition(status.substring(0, 1).toUpperCase() + status.substring(1).toLowerCase());
                    fragmentPostEditBinding.postStatusSpinner.setSelection(spinnerPosition);
                }
                else {
                    Toast.makeText(getContext(), "An error has occurred while getting post detail", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<PostModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void updatePost(String title, String content, String status) {
        HashMap<String, Object> editPostData = new HashMap<>();
        editPostData.put("post_id", postId);
        editPostData.put("title", title);
        editPostData.put("content", content);
        editPostData.put("status", status);
        APIService.apiService.savePost(editPostData).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Update post successfully", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getContext(), "An error has occurred while updating post", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void deletePost() {
        
    }
}