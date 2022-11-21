package com.example.blogandroid.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.blogandroid.fragments.homeactivity.AllPostsFragment;
import com.example.blogandroid.fragments.homeactivity.CurrentUserPostsFragment;
import com.example.blogandroid.fragments.homeactivity.SearchFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    int numberOfTabs;

    public ViewPagerAdapter(@NonNull FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new AllPostsFragment();
            case 1:
                return new CurrentUserPostsFragment();
            case 2:
                return new SearchFragment();
        }
        return new AllPostsFragment();
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
