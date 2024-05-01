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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.scarlet.EditProfileActivity;
import com.example.scarlet.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    private boolean isEditing=false;
    RelativeLayout back_btn, first_name_box,last_name_box,gender_box,birthday_box,email_box,phone_box,
            google_box,facebook_box;
    TextView general_information, mobile_number, email,linked_accounts;
    Button edit_btn;
    final Handler handler = new Handler();
    int delay=100;
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
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.profile, container, false);

        getCustomerInfo(view);
        BindView(view);
        getAnimation();
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
    private void getAnimation(){
        Animation generalInformationAnim= AnimationUtils.loadAnimation(general_information.getContext(), android.R.anim.fade_in);
        Animation mobileNumberAnim= AnimationUtils.loadAnimation(mobile_number.getContext(), android.R.anim.fade_in);
        Animation emailAnim= AnimationUtils.loadAnimation(email.getContext(), android.R.anim.fade_in);
        Animation linkedAccountAnim= AnimationUtils.loadAnimation(linked_accounts.getContext(), android.R.anim.fade_in);
        Animation first_name_boxAnim= AnimationUtils.loadAnimation(first_name_box.getContext(), android.R.anim.slide_in_left);
        Animation last_name_boxAnim= AnimationUtils.loadAnimation(last_name_box.getContext(), android.R.anim.slide_in_left);
        Animation gender_boxAnim= AnimationUtils.loadAnimation(gender_box.getContext(), android.R.anim.slide_in_left);
        Animation birthday_boxAnim= AnimationUtils.loadAnimation(birthday_box.getContext(), android.R.anim.slide_in_left);
        Animation email_boxAnim= AnimationUtils.loadAnimation(email_box.getContext(), android.R.anim.slide_in_left);
        Animation phone_boxAnim= AnimationUtils.loadAnimation(phone_box.getContext(), android.R.anim.slide_in_left);
        Animation google_boxAnim= AnimationUtils.loadAnimation(google_box.getContext(), android.R.anim.slide_in_left);
        Animation facebook_boxAnim= AnimationUtils.loadAnimation(facebook_box.getContext(), android.R.anim.slide_in_left);
        Animation editBtnAnim= AnimationUtils.loadAnimation(edit_btn.getContext(), android.R.anim.slide_in_left);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                general_information.startAnimation(generalInformationAnim);
            }
        },delay*0);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                edit_btn.startAnimation(editBtnAnim);
            }
        },delay*1);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                first_name_box.startAnimation(first_name_boxAnim);
            }
        },delay*1);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                last_name_box.startAnimation(last_name_boxAnim);
            }
        },delay*1);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gender_box.startAnimation(gender_boxAnim);
            }
        },delay*2);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                birthday_box.startAnimation(birthday_boxAnim);
            }
        },delay*3);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                phone_box.startAnimation(phone_boxAnim);
            }
        },delay*3);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                email.startAnimation(email_boxAnim);
            }
        },delay*4);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mobile_number.startAnimation(mobileNumberAnim);
            }
        },delay*1);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                email.startAnimation(emailAnim);
            }
        },delay*2);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                linked_accounts.startAnimation(linkedAccountAnim);
            }
        },delay*3);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                google_box.startAnimation(google_boxAnim);
            }
        },delay*5);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                facebook_box.startAnimation(facebook_boxAnim);
            }
        },delay*6);
    }
    private void enableEditing(View view){
        EditText firstnameView=view.findViewById(R.id.first_name);
        EditText lastnameView=view.findViewById(R.id.last_name);
        EditText genderView=view.findViewById(R.id.gender);
        EditText birthdayView=view.findViewById(R.id.birthday);
        EditText phoneView=view.findViewById(R.id.phone);
        EditText emailView=view.findViewById(R.id.email_input);

        firstnameView.setEnabled(true);
        lastnameView.setEnabled(true);
        genderView.setEnabled(true);
        birthdayView.setEnabled(true);
        phoneView.setEnabled(true);
        emailView.setEnabled(true);
    }
    private void disableEditing(View view){
        EditText firstnameView=view.findViewById(R.id.first_name);
        EditText lastnameView=view.findViewById(R.id.last_name);
        EditText genderView=view.findViewById(R.id.gender);
        EditText birthdayView=view.findViewById(R.id.birthday);
        EditText phoneView=view.findViewById(R.id.phone);
        EditText emailView=view.findViewById(R.id.email_input);

        firstnameView.setEnabled(false);
        lastnameView.setEnabled(false);
        genderView.setEnabled(false);
        birthdayView.setEnabled(false);
        phoneView.setEnabled(false);
        emailView.setEnabled(false);
    }
    private void getCustomerInfo(View view){
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