package com.example.scarlet.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.scarlet.R;

public class AppVersionFragment extends Fragment {

    RelativeLayout back_btn;
    final Handler handler = new Handler();
    int delay=150;
    TextView logo, app_version_1_0, des,team_10;
    private void BindView(View view){
        back_btn=view.findViewById(R.id.back_btn);
        logo=view.findViewById(R.id.logo);
        app_version_1_0=view.findViewById(R.id.app_version_1_0);
        team_10=view.findViewById(R.id.team_10);
        des=view.findViewById(R.id.des);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.app_version, container, false);

        BindView(view);
//        getAnimation();
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getParentFragmentManager().popBackStack();
                AccountFragment accountFragment=new AccountFragment();
                FragmentManager fragmentManager=getParentFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                fragmentTransaction.replace(R.id.frame_layout,accountFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return view;
    }
//    private void getAnimation(){
//        Animation logoAnim= AnimationUtils.loadAnimation(logo.getContext(), android.R.anim.fade_in);
//        Animation app_versionAnim= AnimationUtils.loadAnimation(app_version_1_0.getContext(), android.R.anim.fade_in);
//        Animation teamAnim= AnimationUtils.loadAnimation(team_10.getContext(), android.R.anim.fade_in);
//        Animation desAnim= AnimationUtils.loadAnimation(des.getContext(), android.R.anim.fade_in);
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                logo.startAnimation(logoAnim);
//            }
//        },delay*1);
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                app_version_1_0.startAnimation(app_versionAnim);
//            }
//        },delay*2);
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                team_10.startAnimation(teamAnim);
//            }
//        },delay*3);
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                des.startAnimation(desAnim);
//            }
//        },delay*4);
//    }
}