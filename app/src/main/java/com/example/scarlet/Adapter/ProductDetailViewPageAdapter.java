package com.example.scarlet.Adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.scarlet.Fragment.ProductDetailInfoFragment;
import com.example.scarlet.Fragment.ProductDetailRecommendFragment;
import com.example.scarlet.Fragment.ProductDetailReviewFragment;
import com.example.scarlet.Fragment.VoucherFragment;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailViewPageAdapter extends FragmentPagerAdapter {


    public ProductDetailViewPageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new ProductDetailInfoFragment();
            case 1:
                return new ProductDetailReviewFragment();
            case 2:
                return new ProductDetailRecommendFragment();
            default:
                return new ProductDetailInfoFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Information";
            case 1:
                return "Reviews";
            case 2:
                return "Recommend";
            default:
                return "Information";
        }
    }
}
