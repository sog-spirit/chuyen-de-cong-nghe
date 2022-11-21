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
import com.example.blogandroid.databinding.FragmentCreatePostBinding;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePostFragment extends Fragment {
    private FragmentCreatePostBinding fragmentCreatePostBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentCreatePostBinding = FragmentCreatePostBinding.inflate(inflater, container, false);
        return fragmentCreatePostBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getActivity().getBaseContext(), R.array.post_status, R.layout.spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fragmentCreatePostBinding.postStatusSpinner.setAdapter(spinnerAdapter);

        fragmentCreatePostBinding.createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = fragmentCreatePostBinding.titleTextInput.getText().toString();
                String content = fragmentCreatePostBinding.contentTextInput.getText().toString();
                if (title.isEmpty() || content.isEmpty()) {
                    if (title.isEmpty())
                        fragmentCreatePostBinding.titleTextInputLayout.setError("Title is empty");
                    if (content.isEmpty())
                        fragmentCreatePostBinding.contentTextInputLayout.setError("Content is empty");
                    return;
                }
                String status = fragmentCreatePostBinding.postStatusSpinner.getSelectedItem().toString().toUpperCase();
                createNewPost(title, content, status);
            }
        });
        fragmentCreatePostBinding.titleTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (fragmentCreatePostBinding.titleTextInput.getText().length() > 0)
                    fragmentCreatePostBinding.titleTextInputLayout.setError(null);
            }
        });
        fragmentCreatePostBinding.contentTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (fragmentCreatePostBinding.contentTextInput.getText().length() > 0)
                    fragmentCreatePostBinding.contentTextInputLayout.setError(null);
            }
        });
        fragmentCreatePostBinding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
    }

    private void createNewPost(String title, String content, String status) {
        HashMap<String, String> newPostData = new HashMap<>();
        newPostData.put("title", title);
        newPostData.put("content", content);
        newPostData.put("status", status);
        APIService.apiService.createPost(newPostData).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Post uploaded successfully", Toast.LENGTH_LONG).show();
                    getActivity().finish();
                }
                else {
                    Toast.makeText(getContext(), "An error has occurred while creating new post", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}