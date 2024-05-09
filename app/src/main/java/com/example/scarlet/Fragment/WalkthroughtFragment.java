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
    private int step;
    ImageView imageView;
    TextView titleTextView,descriptionTextView;
    ImageView step1, step2, step3, step4;

    public WalkthroughtFragment(int imageResId, String title, String description, int step) {
        this.imageResId = imageResId;
        this.title = title;
        this.description = description;
        this.step=step;
    }
    private void BindView(View view){
        imageView = view.findViewById(R.id.imageView);
        titleTextView = view.findViewById(R.id.titleTextView);
        descriptionTextView = view.findViewById(R.id.descriptionTextView);
        step1=view.findViewById(R.id.step1);
        step2=view.findViewById(R.id.step2);
        step3=view.findViewById(R.id.step3);
        step4=view.findViewById(R.id.step4);
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
        if(step==1){
            step1.setBackgroundResource(R.drawable.rectangle_circle_white);
            step2.setBackgroundResource(R.drawable.rectangle_circle_gray);
            step3.setBackgroundResource(R.drawable.rectangle_circle_gray);
            step4.setBackgroundResource(R.drawable.rectangle_circle_gray);
        }else if(step==2){
            step1.setBackgroundResource(R.drawable.rectangle_circle_white);
            step2.setBackgroundResource(R.drawable.rectangle_circle_white);
            step3.setBackgroundResource(R.drawable.rectangle_circle_gray);
            step4.setBackgroundResource(R.drawable.rectangle_circle_gray);
        }else if(step==3){
            step1.setBackgroundResource(R.drawable.rectangle_circle_white);
            step2.setBackgroundResource(R.drawable.rectangle_circle_white);
            step3.setBackgroundResource(R.drawable.rectangle_circle_white);
            step4.setBackgroundResource(R.drawable.rectangle_circle_gray);
        }else if(step==4){
            step1.setBackgroundResource(R.drawable.rectangle_circle_white);
            step2.setBackgroundResource(R.drawable.rectangle_circle_white);
            step3.setBackgroundResource(R.drawable.rectangle_circle_white);
            step4.setBackgroundResource(R.drawable.rectangle_circle_white);
        }
        return view;
    }
}