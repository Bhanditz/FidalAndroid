package com.gianlu.fidal.Adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class FragmentPagerAdapter extends androidx.fragment.app.FragmentPagerAdapter {
    private final Fragment[] fragments;

    public FragmentPagerAdapter(@NonNull FragmentManager fm, Fragment... fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        Fragment fragment = getItem(position);
        if (fragment == null) return null;

        Bundle args = fragment.getArguments();
        if (args == null) return null;
        else return args.getString("title");
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}
