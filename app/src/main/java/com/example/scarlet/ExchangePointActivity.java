package com.example.scarlet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Adapter.GridLayoutDecoration;
import com.example.scarlet.Adapter.MembershipAdapter;
import com.example.scarlet.Adapter.OfferAdapter;
import com.example.scarlet.Data.Offer;
import com.example.scarlet.Interface.GetPointCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ExchangePointActivity extends AppCompatActivity {

    private OfferAdapter offerAdapter;
    private List<Offer> offerList;
    RelativeLayout back_btn;
    GetPointCallback getPointCallback;
    RecyclerView recyclerView;
    TextView voucher_point;
    private void BindView(){
        back_btn=findViewById(R.id.back_btn);
        recyclerView=findViewById(R.id.exchange_point_recyclerView);
        voucher_point=findViewById(R.id.voucher_point);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exchange_point);

        Window window = getWindow();
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.white));

        BindView();
        recyclerView.setLayoutManager(new GridLayoutManager(ExchangePointActivity.this,1));
        recyclerView.addItemDecoration(new GridLayoutDecoration(0,10));
        getPointCallback=new GetPointCallback() {
            @Override
            public void itemClick(int point, int type) {
                voucher_point.setText(String.valueOf(point)+" point");
            }
        };
        getOfferData(recyclerView,getPointCallback);
        getPoint();
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
                Intent intent=new Intent();
                intent.putExtra("status","1");
                setResult(ExchangePointActivity.RESULT_OK,intent);
                ExchangePointActivity.super.onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }
    private void getOfferData(RecyclerView recyclerView, GetPointCallback getPointCallback){
        offerList=new ArrayList<>();
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        Query Query= firebaseDatabase.getReference("offer");
        Query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot snap: snapshot.getChildren()){
                        String offerName=snap.child("name").getValue(String.class);
                        String offerDescription=snap.child("description").getValue(String.class);
                        int offerPoint=snap.child("point").getValue(int.class);
                        int offerImage=snap.child("image").getValue(int.class);
                        String code=snap.child("code").getValue(String.class);
                        String key=snap.getKey();
                        Offer offer=new Offer(offerName,code,offerDescription,offerPoint,R.drawable.scartet_1,key);
                        offerList.add(offer);
                    }
                    if(offerList.size()>0){
                        offerAdapter=new OfferAdapter(offerList,getPointCallback);
                        recyclerView.setAdapter(offerAdapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                        int oldPoint=snapshot.child("point").getValue(int.class);
                        voucher_point.setText(String.valueOf(oldPoint)+" point");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}