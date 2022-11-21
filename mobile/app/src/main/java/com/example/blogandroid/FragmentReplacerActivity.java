package com.example.blogandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.example.blogandroid.databinding.ActivityFragmentReplacerBinding;
import com.example.blogandroid.fragments.homeactivity.CommentDetailFragment;
import com.example.blogandroid.fragments.homeactivity.CommentFragment;
import com.example.blogandroid.fragments.homeactivity.CreatePostFragment;
import com.example.blogandroid.fragments.homeactivity.PostEditFragment;
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

        boolean isCreatePost = getIntent().getBooleanExtra("isCreatePost", false);
        boolean isEdit = getIntent().getBooleanExtra("isEdit", false);
        boolean isComment = getIntent().getBooleanExtra("isComment", false);
        boolean isCommentDetail = getIntent().getBooleanExtra("isCommentDetail", false);
        if (isCreatePost)
            setCurrentFragment(new CreatePostFragment());
        else if (isEdit)
            setCurrentFragment(new PostEditFragment());
        else if (isComment)
            setCurrentFragment(new CommentFragment());
        else if (isCommentDetail)
            setCurrentFragment(new CommentDetailFragment());
        else
            setCurrentFragment(new LoginFragment());
    }

    public void setCurrentFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        if (fragment instanceof RegisterFragment) {
            fragmentTransaction.addToBackStack(null);
        }
        if (fragment instanceof PostEditFragment) {
            int postId = getIntent().getIntExtra("postId", -1);
            Bundle bundle = new Bundle();
            bundle.putInt("postId", postId);
            fragment.setArguments(bundle);
        }
        if (fragment instanceof CommentFragment) {
            int postId = getIntent().getIntExtra("postId", -1);
            Bundle bundle = new Bundle();
            bundle.putInt("postId", postId);
            fragment.setArguments(bundle);
        }
        if (fragment instanceof CommentDetailFragment) {
            int commentId = getIntent().getIntExtra("commentId", -1);
            Bundle bundle = new Bundle();
            bundle.putInt("commentId", commentId);
            fragment.setArguments(bundle);
        }
        fragmentTransaction.replace(fragmentReplacerBinding.frameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }
}