package com.example.scarlet.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.scarlet.Fragment.WalkthroughtFragment;

import java.util.List;

public class WalkthroughtAdapter extends FragmentPagerAdapter {
    private List<WalkthroughtFragment> fragmentList;

    public WalkthroughtAdapter(@NonNull FragmentManager fm, List<WalkthroughtFragment> fragmentList) {
        super(fm);
        this.fragmentList=fragmentList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
