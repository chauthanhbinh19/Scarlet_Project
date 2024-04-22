package com.example.scarlet.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.scarlet.AdminMainActivity;
import com.example.scarlet.LoyaltyActivity;
import com.example.scarlet.PoliciesActivity;
import com.example.scarlet.R;
import com.example.scarlet.SignInActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountFragment extends Fragment {

    RelativeLayout profile_btn;
    RelativeLayout setting_btn;
    RelativeLayout policies_btn;
    RelativeLayout app_version_btn, order_activities_btn, dashboard_button, loyalty_btn;
    Button create_account;
    Button sign_out_button;
    private void BindView(View view){
        profile_btn=view.findViewById(R.id.profile_button);
        setting_btn=view.findViewById(R.id.settings_button);
        policies_btn=view.findViewById(R.id.policies_button);
        app_version_btn=view.findViewById(R.id.app_version_button);
        create_account=view.findViewById(R.id.create_account_btn);
        loyalty_btn=view.findViewById(R.id.loyalty_program_button);
        order_activities_btn=view.findViewById(R.id.order_activities_button);
        sign_out_button=view.findViewById(R.id.sign_out_btn);
        dashboard_button=view.findViewById(R.id.dashboard_button);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.account, container, false);

        validateUser(view);

        BindView(view);
        checkSignInStautus();
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

        order_activities_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOrderActivitiesFragment();
            }
        });
        policies_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPoliciesActivity();
            }
        });

        app_version_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAppVersionFragment();
            }
        });

        loyalty_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoyaltyActivity();
            }
        });
        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(), SignInActivity.class);
                startActivity(intent);

            }
        });
        dashboard_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(), AdminMainActivity.class);
                startActivity(intent);
            }
        });
        sign_out_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences= getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putBoolean("isLoggedIn",false);
                editor.remove("customerKey");
                editor.apply();

                create_account.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(),"Sign out successfully", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
    private void checkSignInStautus(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn=sharedPreferences.getBoolean("isLoggedIn",false);
        String userKey=sharedPreferences.getString("customerKey","");
        String userType=sharedPreferences.getString("userType","");
        if(!isLoggedIn && userKey.isEmpty()){
            create_account.setVisibility(View.VISIBLE);
        }else{
            create_account.setVisibility(View.GONE);
        }
        if(userType.equals("employee")){
            dashboard_button.setVisibility(View.VISIBLE);
        }else if(userType.equals("customer")){
            dashboard_button.setVisibility(View.GONE);
        }
    }
    private void openLoyaltyActivity(){
        Intent intent=new Intent(getContext(), LoyaltyActivity.class);
        startActivity(intent);
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
    private void openPoliciesActivity(){
        Intent intent=new Intent(getContext(), PoliciesActivity.class);
        startActivity(intent);
    }
    private void openAppVersionFragment(){
        AppVersionFragment appVersionFragment=new AppVersionFragment();
        FragmentManager fragmentManager=getParentFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,appVersionFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    private void openOrderActivitiesFragment(){
        OrderActivitiesFragment orderActivitiesFragment=new OrderActivitiesFragment();
        FragmentManager fragmentManager=getParentFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,orderActivitiesFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    private void validateUser(View view){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn=sharedPreferences.getBoolean("isLoggedIn",false);
        String userKey=sharedPreferences.getString("customerKey","");
        if(isLoggedIn && !userKey.isEmpty()){
            FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
            DatabaseReference myRef=firebaseDatabase.getReference("user").child(userKey);
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        String firstname=snapshot.child("first_name").getValue(String.class);
                        String lastname=snapshot.child("last_name").getValue(String.class);
                        String customerAvatar=snapshot.child("avatar_img").getValue(String.class);

                        TextView customerNameView=view.findViewById(R.id.user_name);

                        if(!customerAvatar.isEmpty()){
                            ImageView customerAvatarView=view.findViewById(R.id.user_avatar);
                            Glide.with(getContext()).load(customerAvatar).into(customerAvatarView);
                        }
                        customerNameView.setText(lastname+ " "+ firstname);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}