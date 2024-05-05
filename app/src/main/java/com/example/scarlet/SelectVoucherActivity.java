package com.example.scarlet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Adapter.DealAdapter;
import com.example.scarlet.Adapter.DealSecondAdapter;
import com.example.scarlet.Adapter.GridLayoutDecoration;
import com.example.scarlet.Data.Deal;
import com.example.scarlet.Data.DealTransaction;
import com.example.scarlet.Interface.GetKeyCallback;
import com.example.scarlet.Interface.GetThreeElementsCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class SelectVoucherActivity extends AppCompatActivity {

    RelativeLayout back_btn;
    private DealSecondAdapter dealAdapter;
    private List<Deal> dealList;
    private List<DealTransaction> dealTransactionList;
    RecyclerView recyclerView;
    String deliveryStatus;
    String voucherKey="0",voucherName, voucherCode;
    GetThreeElementsCallback getThreeElementsCallback;
    Button chooseBtn;
    private void BindView(){
        back_btn=findViewById(R.id.back_btn);
        chooseBtn=findViewById(R.id.chooseBtn);
        recyclerView=findViewById(R.id.voucher_recyclerView);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discount);

        Window window = getWindow();
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.burgundy));

        Intent intent=getIntent();
        if(intent!= null){
            deliveryStatus=intent.getStringExtra("deliveryStatus");
        }
        BindView();
        recyclerView.setLayoutManager(new GridLayoutManager(SelectVoucherActivity.this,1));
        recyclerView.addItemDecoration(new GridLayoutDecoration(0,5));
        getThreeElementsCallback=new GetThreeElementsCallback() {
            @Override
            public void itemClick(String key, String name, String code, int type) {
                if(type==1){
                    voucherKey=key;
                    voucherName=name;
                    voucherCode=code;
                }else{
                    voucherKey="0";
                    voucherName=name;
                    voucherCode=code;
                }
            }
        };
        getVoucherData(recyclerView, deliveryStatus);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent();
                intent1.putExtra("voucherKey","0");
                intent1.putExtra("voucherName",voucherName);
                intent1.putExtra("voucherCode",voucherCode);
                setResult(SelectVoucherActivity.RESULT_OK,intent1);
                SelectVoucherActivity.super.onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        chooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent();
                intent1.putExtra("voucherKey",voucherKey);
                intent1.putExtra("voucherName",voucherName);
                intent1.putExtra("voucherCode",voucherCode);
                setResult(SelectVoucherActivity.RESULT_OK,intent1);
//                finish();
                SelectVoucherActivity.super.onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }
    private void getVoucherData(RecyclerView recyclerView, String deliveryMethodClicked){
        dealList=new ArrayList<>();
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn=sharedPreferences.getBoolean("isLoggedIn",false);
        String userKey=sharedPreferences.getString("customerKey","");
        if(isLoggedIn && !userKey.isEmpty()){
            FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
            Query myRef1=firebaseDatabase.getReference("deal");
            Query myRef2=firebaseDatabase.getReference("deal_transaction");
            Set<String> dealKeySet = new HashSet<>();
            Set<String> dealKeyHistory = new HashSet<>();
            myRef1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    dealList.clear();
                    if(snapshot.exists()){
                        for (DataSnapshot snap: snapshot.getChildren()){
                            String voucherName=snap.child("name").getValue(String.class);
                            int discount=snap.child("discount").getValue(int.class);
                            Date expiryDate=snap.child("expiryDate").getValue(Date.class);
                            String deliveryMethod=snap.child("deliveryMethod").getValue(String.class);
                            String codeSelect=snap.child("code").getValue(String.class);
                            int deliveryMethodIcon=1;
                            if(deliveryMethod.equals("delivery")){
                                deliveryMethodIcon=R.drawable.delivery_bike;
                            }else if(deliveryMethod.equals("instore")){
                                deliveryMethodIcon=R.drawable.store;
                            }else if(deliveryMethod.equals("pickup")){
                                deliveryMethodIcon=R.drawable.food_delivery;
                            }
                            String description=snap.child("description").getValue(String.class);
                            String key=snap.getKey();
                            Deal deal=new Deal(voucherName,discount,expiryDate,deliveryMethod,deliveryMethodIcon,description,key,codeSelect);
                            if(deliveryMethod.equals(deliveryMethodClicked)){
                                myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                        if(snapshot1.exists()){
                                            boolean customerFound=false;
                                            for(DataSnapshot snap1: snapshot1.getChildren()){
                                                String customerId=snap1.child("customerId").getValue(String.class);
                                                if(customerId.equals(userKey)){
                                                    customerFound=true;
                                                    String code=snap1.child("code").getValue(String.class);
                                                    dealKeyHistory.add(code);
                                                }
                                            }
                                            for(DataSnapshot snap1: snapshot1.getChildren()){
                                                String customerId=snap1.child("customerId").getValue(String.class);
                                                if(customerId.equals(userKey)){
                                                    customerFound=true;
                                                    String code=snap1.child("code").getValue(String.class);
                                                    if(!dealKeySet.contains(codeSelect)){
                                                        if(!codeSelect.equals(code)){
                                                            if(!dealKeyHistory.contains(code) || !dealKeyHistory.contains(codeSelect)){
                                                                dealList.add(deal);
                                                                dealKeySet.add(codeSelect);
                                                            }
                                                        }else if(codeSelect.equals(code)){
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                            if(!customerFound){
                                                dealList.add(deal);
                                                dealKeySet.add(codeSelect);
                                            }
                                            if(dealList.size()>0){
                                                dealAdapter=new DealSecondAdapter(dealList,getThreeElementsCallback);
                                                recyclerView.setAdapter(dealAdapter);
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
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }
}