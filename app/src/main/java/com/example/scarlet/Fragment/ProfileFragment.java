package com.example.scarlet.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.profile, container, false);

        getCustomerInfo(view);
        RelativeLayout back_btn=view.findViewById(R.id.back_btn);
        Button edit_btn=view.findViewById(R.id.edit_button);
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

    private void enableEditing(View view){
        EditText firstnameView=view.findViewById(R.id.first_name);
        EditText lastnameView=view.findViewById(R.id.last_name);
        EditText genderView=view.findViewById(R.id.gender);
        EditText birthdayView=view.findViewById(R.id.birthday);
        EditText addressView=view.findViewById(R.id.address);
        EditText phoneView=view.findViewById(R.id.phone);
        EditText emailView=view.findViewById(R.id.email_input);

        firstnameView.setEnabled(true);
        lastnameView.setEnabled(true);
        genderView.setEnabled(true);
        birthdayView.setEnabled(true);
        addressView.setEnabled(true);
        phoneView.setEnabled(true);
        emailView.setEnabled(true);
    }
    private void disableEditing(View view){
        EditText firstnameView=view.findViewById(R.id.first_name);
        EditText lastnameView=view.findViewById(R.id.last_name);
        EditText genderView=view.findViewById(R.id.gender);
        EditText birthdayView=view.findViewById(R.id.birthday);
        EditText addressView=view.findViewById(R.id.address);
        EditText phoneView=view.findViewById(R.id.phone);
        EditText emailView=view.findViewById(R.id.email_input);

        firstnameView.setEnabled(false);
        lastnameView.setEnabled(false);
        genderView.setEnabled(false);
        birthdayView.setEnabled(false);
        addressView.setEnabled(false);
        phoneView.setEnabled(false);
        emailView.setEnabled(false);
    }
    private void getCustomerInfo(View view){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn=sharedPreferences.getBoolean("isLoggedIn",false);
        String userKey=sharedPreferences.getString("customerKey","");
        if(isLoggedIn && !userKey.isEmpty()){
            FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
            DatabaseReference myRef=firebaseDatabase.getReference("customers").child(userKey);
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
                        String address=snapshot.child("address").getValue(String.class);

                        EditText firstnameView=view.findViewById(R.id.first_name);
                        EditText lastnameView=view.findViewById(R.id.last_name);
                        EditText genderView=view.findViewById(R.id.gender);
                        EditText birthdayView=view.findViewById(R.id.birthday);
                        EditText addressView=view.findViewById(R.id.address);
                        EditText phoneView=view.findViewById(R.id.phone);
                        EditText emailView=view.findViewById(R.id.email_input);

                        firstnameView.setText(firstname);
                        lastnameView.setText(lastname);
                        genderView.setText(gender);
                        birthdayView.setText(dateofbirth);
                        addressView.setText(address);
                        phoneView.setText(phone);
                        emailView.setText(email);

                        int blackcolor= ContextCompat.getColor(getContext(),R.color.black);
                        firstnameView.setTextColor(blackcolor);
                        lastnameView.setTextColor(blackcolor);
                        genderView.setTextColor(blackcolor);
                        birthdayView.setTextColor(blackcolor);
                        addressView.setTextColor(blackcolor);
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