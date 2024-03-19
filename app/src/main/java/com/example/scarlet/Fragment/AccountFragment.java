package com.example.scarlet.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.scarlet.R;

public class AccountFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.account, container, false);

        RelativeLayout profile_btn=view.findViewById(R.id.profile_button);
        RelativeLayout setting_btn=view.findViewById(R.id.settings_button);
        RelativeLayout policies_btn=view.findViewById(R.id.policies_button);
        RelativeLayout app_version_btn=view.findViewById(R.id.app_version_button);

        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfileFragment();
            }
        });

        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettingFragment();
            }
        });

        policies_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPoliciesFragment();
            }
        });

        app_version_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAppVersionFragment();
            }
        });

        return view;
    }
    private void openProfileFragment(){
        ProfileFragment profileFragment=new ProfileFragment();
        FragmentManager fragmentManager=getParentFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,profileFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    private void openSettingFragment(){
        SettingFragment settingFragment=new SettingFragment();
        FragmentManager fragmentManager=getParentFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,settingFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    private void openPoliciesFragment(){
        PoliciesFragment policiesFragment=new PoliciesFragment();
        FragmentManager fragmentManager=getParentFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,policiesFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    private void openAppVersionFragment(){
        AppVersionFragment appVersionFragment=new AppVersionFragment();
        FragmentManager fragmentManager=getParentFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,appVersionFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}