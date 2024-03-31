package com.example.scarlet.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.scarlet.Fragment.VoucherFragment;

import java.util.ArrayList;
import java.util.List;

public class VoucherViewPageAdapter extends FragmentPagerAdapter {
//    private final List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();

    public VoucherViewPageAdapter(@NonNull FragmentManager fm, List<String> titleList) {
        super(fm);
        this.titleList = titleList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        String level = titleList.get(position);
        return VoucherFragment.newInstance(level);
    }

    @Override
    public int getCount() {
        return titleList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}
