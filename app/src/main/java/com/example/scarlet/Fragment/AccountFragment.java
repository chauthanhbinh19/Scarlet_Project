package com.example.scarlet.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.example.scarlet.FeedbackActivity;
import com.example.scarlet.LoyaltyActivity;
import com.example.scarlet.PoliciesActivity;
import com.example.scarlet.R;
import com.example.scarlet.ReviewActivity;
import com.example.scarlet.SignInActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountFragment extends Fragment {

    RelativeLayout profile_btn;
    RelativeLayout setting_btn;
    RelativeLayout policies_btn, feedback_btn;
    RelativeLayout app_version_btn, order_activities_btn, dashboard_button, loyalty_btn, review_button;
    Button create_account;
    Button sign_out_button;
    TextView accountText, orderText, generalText, helpText;
    final Handler handler = new Handler();
    int delay=60;
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
        feedback_btn=view.findViewById(R.id.feedback___support_button);
        accountText=view.findViewById(R.id.account_ek1);
        orderText=view.findViewById(R.id.order);
        generalText=view.findViewById(R.id.general_information);
        helpText=view.findViewById(R.id.help_center);
        review_button=view.findViewById(R.id.review_button);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.account, container, false);

        validateUser(view);

        BindView(view);
        checkSignInStautus();
        getAnimation(view);
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
        review_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(), ReviewActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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

        feedback_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFeedbackActivity();
            }
        });
        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(), SignInActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        dashboard_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(), AdminMainActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        sign_out_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences= getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putBoolean("isLoggedIn",false);
                editor.remove("customerKey");
                editor.remove("userType");
                editor.apply();

                TextView customerNameView=view.findViewById(R.id.user_name);
                customerNameView.setText("");
                create_account.setVisibility(View.VISIBLE);
                dashboard_button.setVisibility(View.GONE);
                FirebaseAuth mAuth=FirebaseAuth.getInstance();
                mAuth.signOut();
                Toast.makeText(getContext(),"Sign out successfully", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
    private void getAnimation(View view){
//        Animation viewAnim= AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_in_left);
//        view.startAnimation(viewAnim);
        Animation slideInLeftAnimation=AnimationUtils.loadAnimation(profile_btn.getContext(), android.R.anim.slide_in_left);
        accountText.startAnimation(slideInLeftAnimation);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                profile_btn.startAnimation(slideInLeftAnimation);
            }
        },delay);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setting_btn.startAnimation(slideInLeftAnimation);
            }
        },delay*2);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dashboard_button.startAnimation(slideInLeftAnimation);
            }
        },delay*2);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                orderText.startAnimation(slideInLeftAnimation);
            }
        },delay*2);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                order_activities_btn.startAnimation(slideInLeftAnimation);
            }
        },delay*2);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                review_button.startAnimation(slideInLeftAnimation);
            }
        },delay*2);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                generalText.startAnimation(slideInLeftAnimation);
            }
        },delay*2);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                policies_btn.startAnimation(slideInLeftAnimation);
            }
        },delay*2);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loyalty_btn.startAnimation(slideInLeftAnimation);
            }
        },delay*2);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                app_version_btn.startAnimation(slideInLeftAnimation);
            }
        },delay*2);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                helpText.startAnimation(slideInLeftAnimation);
            }
        },delay*2);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                feedback_btn.startAnimation(slideInLeftAnimation);
            }
        },delay*11);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sign_out_button.startAnimation(slideInLeftAnimation);
            }
        },delay*2);
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
    private void openFeedbackActivity(){
        Intent intent=new Intent(getContext(), FeedbackActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    private void openLoyaltyActivity(){
        Intent intent=new Intent(getContext(), LoyaltyActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    private void openProfileFragment(){
        ProfileFragment profileFragment=new ProfileFragment();
        FragmentManager fragmentManager=getParentFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.frame_layout,profileFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    private void openSettingFragment(){
        SettingFragment settingFragment=new SettingFragment();
        FragmentManager fragmentManager=getParentFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.frame_layout,settingFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    private void openPoliciesActivity(){
        Intent intent=new Intent(getContext(), PoliciesActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    private void openAppVersionFragment(){
        AppVersionFragment appVersionFragment=new AppVersionFragment();
        FragmentManager fragmentManager=getParentFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.frame_layout,appVersionFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    private void openOrderActivitiesFragment(){
        OrderActivitiesFragment orderActivitiesFragment=new OrderActivitiesFragment();
        FragmentManager fragmentManager=getParentFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
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