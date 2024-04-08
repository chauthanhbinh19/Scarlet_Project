package com.example.scarlet;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
    private List<String> tabCount=new ArrayList<>();
    RelativeLayout back_btn;
    private void BindView(){
        back_btn=findViewById(R.id.back_btn);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_ship);

        BindView();
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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


}