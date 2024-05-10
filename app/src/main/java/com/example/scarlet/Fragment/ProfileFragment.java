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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.scarlet.Data.Payment;
import com.example.scarlet.EditProfileActivity;
import com.example.scarlet.R;
import com.example.scarlet.SelectVoucherActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    private boolean isEditing=false;
    ImageView user_avatar;
    RelativeLayout back_btn, first_name_box,last_name_box,gender_box,birthday_box,email_box,phone_box,
            google_box,facebook_box;
    TextView general_information, mobile_number, email,linked_accounts;
    Button edit_btn;
    final Handler handler = new Handler();
    int delay=50;
    View view;
    private void BindView(View view){
        back_btn=view.findViewById(R.id.back_btn);
        edit_btn=view.findViewById(R.id.edit_button);
        first_name_box=view.findViewById(R.id.first_name_box);
        last_name_box=view.findViewById(R.id.last_name_box);
        gender_box=view.findViewById(R.id.gender_box);
        birthday_box=view.findViewById(R.id.birthday_box);
        email_box=view.findViewById(R.id.email_box);
        phone_box=view.findViewById(R.id.phone_box);
        google_box=view.findViewById(R.id.google_box);
        facebook_box=view.findViewById(R.id.facebook_box);
        general_information=view.findViewById(R.id.general_information);
        mobile_number=view.findViewById(R.id.mobile_number);
        email=view.findViewById(R.id.email);
        linked_accounts=view.findViewById(R.id.linked_accounts);
        user_avatar=view.findViewById(R.id.user_avatar);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.profile, container, false);

        getCustomerInfo();
        BindView(view);
        getAnimation();
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
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), EditProfileActivity.class);
//                startActivity(intent);
                catcherForResult.launch(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        return view;
    }
    ActivityResultLauncher<Intent> catcherForResult=
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if(result.getResultCode()== EditProfileActivity.RESULT_OK){
                                Intent catcher=result.getData();
                                if(catcher!=null){
                                    String reload=catcher.getStringExtra("reload");
                                    if(reload.equals("1")){
                                        getCustomerInfo();
                                    }
                                }
                            }
                        }
                    });
    private void getAnimation(){
        Animation fadeInAnimation= AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
        Animation slideInLeftAnimation= AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_in_left);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                general_information.startAnimation(slideInLeftAnimation);
            }
        },delay*0);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                edit_btn.startAnimation(slideInLeftAnimation);
            }
        },delay*1);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                first_name_box.startAnimation(slideInLeftAnimation);
            }
        },delay*1);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                last_name_box.startAnimation(slideInLeftAnimation);
            }
        },delay*1);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gender_box.startAnimation(slideInLeftAnimation);
            }
        },delay*2);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                birthday_box.startAnimation(slideInLeftAnimation);
            }
        },delay*3);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                phone_box.startAnimation(slideInLeftAnimation);
            }
        },delay*3);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                email_box.startAnimation(slideInLeftAnimation);
            }
        },delay*4);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mobile_number.startAnimation(slideInLeftAnimation);
            }
        },delay*1);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                email.startAnimation(slideInLeftAnimation);
            }
        },delay*2);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                linked_accounts.startAnimation(slideInLeftAnimation);
            }
        },delay*3);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                google_box.startAnimation(slideInLeftAnimation);
            }
        },delay*5);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                facebook_box.startAnimation(slideInLeftAnimation);
            }
        },delay*6);
    }
    private void getCustomerInfo(){
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
                        String gender=snapshot.child("gender").getValue(String.class);
                        String dateofbirth=snapshot.child("date_of_birth").getValue(String.class);
                        String phone=snapshot.child("phone_number").getValue(String.class);
                        String email=snapshot.child("email").getValue(String.class);
                        String avatar=snapshot.child("avatar_img").getValue(String.class);

                        EditText firstnameView=view.findViewById(R.id.first_name);
                        EditText lastnameView=view.findViewById(R.id.last_name);
                        EditText genderView=view.findViewById(R.id.gender);
                        EditText birthdayView=view.findViewById(R.id.birthday);
                        EditText phoneView=view.findViewById(R.id.phone);
                        EditText emailView=view.findViewById(R.id.email_input);

                        firstnameView.setText(firstname);
                        lastnameView.setText(lastname);
                        genderView.setText(gender);
                        birthdayView.setText(dateofbirth);
                        phoneView.setText(phone);
                        emailView.setText(email);
                        if(!avatar.isEmpty()){
                            ImageView customerAvatarView=view.findViewById(R.id.user_avatar);
                            Glide.with(getContext()).load(avatar).into(customerAvatarView);
                        }

                        int blackcolor= ContextCompat.getColor(getContext(),R.color.black);
                        firstnameView.setTextColor(blackcolor);
                        lastnameView.setTextColor(blackcolor);
                        genderView.setTextColor(blackcolor);
                        birthdayView.setTextColor(blackcolor);
                        phoneView.setTextColor(blackcolor);
                        emailView.setTextColor(blackcolor);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}