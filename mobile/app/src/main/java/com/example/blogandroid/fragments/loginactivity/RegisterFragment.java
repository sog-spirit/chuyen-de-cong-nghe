package com.example.blogandroid.fragments.loginactivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.blogandroid.R;
import com.example.blogandroid.apiservice.APIService;
import com.example.blogandroid.databinding.FragmentRegisterBinding;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {
    private FragmentRegisterBinding fragmentRegisterBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentRegisterBinding = FragmentRegisterBinding.inflate(inflater, container, false);
        return fragmentRegisterBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeUIComponent();
    }

    private void initializeUIComponent() {
        fragmentRegisterBinding.backButton.setOnClickListener(view -> {
            getParentFragmentManager().popBackStack();
        });
        fragmentRegisterBinding.registerButton.setOnClickListener(view -> {
            String username = fragmentRegisterBinding.usernameTextInput.getText().toString();
            String password = fragmentRegisterBinding.passwordTextInput.getText().toString();
            String confirmationPassword = fragmentRegisterBinding.passwordConfirmTextInput.getText().toString();
            if (username.isEmpty() || password.isEmpty() || confirmationPassword.isEmpty()) {
                if (username.isEmpty())
                    fragmentRegisterBinding.usernameTextInputLayout.setError("Username is empty");
                if (password.isEmpty())
                    fragmentRegisterBinding.passwordTextInputLayout.setError("Password is empty");
                if (confirmationPassword.isEmpty())
                    fragmentRegisterBinding.passwordConfirmTextInputLayout.setError("Confirm password is empty");
                return;
            }
            if (!password.equals(confirmationPassword)) {
                fragmentRegisterBinding.passwordTextInputLayout.setError("Password and confirm password do not match");
                fragmentRegisterBinding.passwordConfirmTextInputLayout.setError("Password and confirm password do not match");
                return;
            }
            registerUser(username, password);
        });
        fragmentRegisterBinding.usernameTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (fragmentRegisterBinding.usernameTextInput.length() > 0)
                    fragmentRegisterBinding.usernameTextInputLayout.setError(null);
            }
        });
        fragmentRegisterBinding.passwordTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (fragmentRegisterBinding.passwordTextInput.length() > 0)
                    fragmentRegisterBinding.passwordTextInputLayout.setError(null);
            }
        });
        fragmentRegisterBinding.passwordConfirmTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (fragmentRegisterBinding.passwordConfirmTextInput.length() > 0)
                    fragmentRegisterBinding.passwordConfirmTextInputLayout.setError(null);
            }
        });
    }

    private void registerUser(String username, String password) {
        HashMap<String, String> userRegistrationData = new HashMap<>();
        userRegistrationData.put("username", username);
        userRegistrationData.put("password", password);
        APIService.apiService.registerUser(userRegistrationData).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    getParentFragmentManager().popBackStack();
                }
                else {
                    fragmentRegisterBinding.usernameTextInputLayout.setError("Username is existed!");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}