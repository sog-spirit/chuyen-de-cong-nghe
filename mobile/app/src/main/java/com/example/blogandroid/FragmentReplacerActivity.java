package com.example.blogandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.example.blogandroid.databinding.ActivityFragmentReplacerBinding;
import com.example.blogandroid.fragments.loginactivity.LoginFragment;
import com.example.blogandroid.fragments.loginactivity.RegisterFragment;

public class FragmentReplacerActivity extends AppCompatActivity {
    private ActivityFragmentReplacerBinding fragmentReplacerBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentReplacerBinding = ActivityFragmentReplacerBinding.inflate(getLayoutInflater());
        View viewRoot = fragmentReplacerBinding.getRoot();
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        setContentView(viewRoot);

        setCurrentFragment(new LoginFragment());
    }

    public void setCurrentFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        if (fragment instanceof RegisterFragment) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.replace(fragmentReplacerBinding.frameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }
}