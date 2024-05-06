package com.example.scarlet.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.scarlet.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class WalkthroughtFragment extends Fragment {

    private int imageResId;
    private String title;
    private String description;
    ImageView imageView;
    TextView titleTextView,descriptionTextView;

    public WalkthroughtFragment(int imageResId, String title, String description) {
        this.imageResId = imageResId;
        this.title = title;
        this.description = description;
    }
    private void BindView(View view){
        imageView = view.findViewById(R.id.imageView);
        titleTextView = view.findViewById(R.id.titleTextView);
        descriptionTextView = view.findViewById(R.id.descriptionTextView);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.slide_walkthrought, container, false);

        BindView(view);
        imageView.setBackgroundResource(imageResId);
        titleTextView.setText(title);
        descriptionTextView.setText(description);
        return view;
    }
}