package com.example.scarlet;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class YourRightsActivity extends AppCompatActivity {

    RelativeLayout back_btn;
    TextView voucher_point, voucher_text_1,voucher_text;
    private void BindView(){
        back_btn=findViewById(R.id.back_btn);
        voucher_point=findViewById(R.id.voucher_point);
        voucher_text_1=findViewById(R.id.voucher_text_1);
        voucher_text=findViewById(R.id.voucher_text);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.your_rights);

        Window window = getWindow();
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.burgundy));
        
        BindView();
        getPoint();
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }
    private void getPoint(){
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn=sharedPreferences.getBoolean("isLoggedIn",false);
        String userKey=sharedPreferences.getString("customerKey","");

        if(isLoggedIn && !userKey.isEmpty()){
            FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
            DatabaseReference myRef=firebaseDatabase.getReference("user");
            myRef.child(userKey).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        int oldRankPoint=snapshot.child("rankPoint").getValue(int.class);
                        int oldPoint=snapshot.child("point").getValue(int.class);
                        voucher_point.setText(String.valueOf(oldPoint)+" point");
                        if(oldRankPoint<10000){
                            voucher_text_1.setText(String.valueOf(10000-oldRankPoint)+" points left and you achieve bronze");
                            voucher_text.setText("New");
                        }else if(oldRankPoint>=10000 && oldRankPoint<30000){
                            voucher_text_1.setText(String.valueOf(30000-oldRankPoint)+" points left and you achieve silver");
                            voucher_text.setText("Bronze");
                        }else if(oldRankPoint>=30000 && oldRankPoint<50000){
                            voucher_text_1.setText(String.valueOf(50000-oldRankPoint)+" points left and you achieve gold");
                            voucher_text.setText("Silver");
                        }else if(oldRankPoint>=50000 && oldRankPoint<70000){
                            voucher_text_1.setText(String.valueOf(70000-oldRankPoint)+" points left and you achieve diamond");
                            voucher_text.setText("Gold");
                        }else{
                            voucher_text.setText("Diamond");
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

}