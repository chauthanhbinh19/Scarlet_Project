package com.example.scarlet;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.scarlet.Adapter.DealAdapter;
import com.example.scarlet.Adapter.MembershipViewPageAdapter;
import com.example.scarlet.Adapter.VoucherViewPageAdapter;
import com.example.scarlet.Data.Membership;
import com.example.scarlet.Fragment.MembershipFragment;
import com.example.scarlet.Interface.FirebaseCallback;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MemberShipActivity extends AppCompatActivity {

    private List<Membership> membershipList;
    TextView voucher_point, voucher_text_1,voucher_text;
    private List<String> tabCount=new ArrayList<>();
    RelativeLayout back_btn;
    private void BindView(){
        back_btn=findViewById(R.id.back_btn);
        voucher_point=findViewById(R.id.voucher_point);
        voucher_text_1=findViewById(R.id.voucher_text_1);
        voucher_text=findViewById(R.id.voucher_text);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_ship);

        Window window = getWindow();
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.white));

        BindView();
        getPoint();
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        getMembershipTabCount(new FirebaseCallback() {
            @Override
            public void onCallBack(List<String> tabCount) {
                setupTabLayout(tabCount);
            }
        });
    }
    private void getMembershipTabCount(final FirebaseCallback callback){
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference myRef=firebaseDatabase.getReference("membership");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap:snapshot.getChildren()){
                    String membershipName=snap.child("name").getValue(String.class);
                    if(tabCount.isEmpty()){
                        tabCount.add(membershipName);
                    }
                    if (!tabCount.contains(membershipName)) {
                        tabCount.add(membershipName);
                    }
                }
                callback.onCallBack(tabCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void setupTabLayout(List<String> tabCount) {
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);
        MembershipViewPageAdapter adapter = new MembershipViewPageAdapter(getSupportFragmentManager(),tabCount);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String tabTitle = tab.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("tabTitle", tabTitle);

                MembershipFragment fragment = new MembershipFragment();
                fragment.setArguments(bundle);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

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