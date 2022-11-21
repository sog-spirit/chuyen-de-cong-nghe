package com.example.blogandroid.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.blogandroid.FragmentReplacerActivity;
import com.example.blogandroid.HomeActivity;
import com.example.blogandroid.R;
import com.example.blogandroid.apiservice.APIService;
import com.example.blogandroid.databinding.FragmentLoginBinding;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding fragmentLoginBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentLoginBinding = FragmentLoginBinding.inflate(inflater, container, false);
        return fragmentLoginBinding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeUIComponent();
    }

    private void initializeUIComponent() {
        fragmentLoginBinding.loginButton.setOnClickListener(view -> {
            String username = fragmentLoginBinding.usernameTextInput.getText().toString();
            String password = fragmentLoginBinding.passwordTextInput.getText().toString();

            if (username.isEmpty() || password.isEmpty()) {
                if (username.isEmpty())
                    fragmentLoginBinding.usernameTextInputLayout.setError("Username is empty");
                if (password.isEmpty())
                    fragmentLoginBinding.passwordTextInputLayout.setError("Password is empty");
                return;
            }
            loginUser(username, password);
        });
        fragmentLoginBinding.registerButton.setOnClickListener(view -> {});
        fragmentLoginBinding.usernameTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (fragmentLoginBinding.usernameTextInput.length() > 0)
                    fragmentLoginBinding.usernameTextInputLayout.setError(null);
            }
        });
        fragmentLoginBinding.passwordTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (fragmentLoginBinding.passwordTextInput.length() > 0)
                    fragmentLoginBinding.passwordTextInputLayout.setError(null);
            }
        });
    }

    private void loginUser(String username, String password) {
        HashMap<String, String> userLoginData = new HashMap<>();
        userLoginData.put("username", username);
        userLoginData.put("password", password);
        APIService.apiService.loginUser(userLoginData).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    if (getActivity() == null)
                        return;
                    Intent intent = new Intent(getActivity().getApplicationContext(), HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    getActivity().finish();
                }
                else {
                    fragmentLoginBinding.usernameTextInputLayout.setError("Username or password is incorrect!");
                    fragmentLoginBinding.passwordTextInputLayout.setError("Username or password is incorrect!");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}