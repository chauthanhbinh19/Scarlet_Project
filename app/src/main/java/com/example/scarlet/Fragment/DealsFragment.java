package com.example.scarlet.Fragment;

import android.adservices.measurement.MeasurementManager;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Adapter.DealAdapter;
import com.example.scarlet.Adapter.DealSecondAdapter;
import com.example.scarlet.Adapter.GridLayoutDecoration;
import com.example.scarlet.Data.Deal;
import com.example.scarlet.Data.Payment;
import com.example.scarlet.ExchangePointActivity;
import com.example.scarlet.MemberShipActivity;
import com.example.scarlet.PointHistoryActivity;
import com.example.scarlet.R;
import com.example.scarlet.SelectVoucherActivity;
import com.example.scarlet.VoucherActivity;
import com.example.scarlet.YourRightsActivity;
import com.google.firebase.Firebase;
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

public class DealsFragment extends Fragment {
    private DealAdapter dealAdapter;
    private List<Deal> dealList;
    RecyclerView recyclerView;
    TextView voucher_text_1;
    RelativeLayout memberShip,exchangePoint,pointHistory,yourRights, voucher_box;
    TextView more,voucher_point;
    TextView deal_text;
    final Handler handler = new Handler();
    int delay=100;
    private void BindView(View view){
        recyclerView=view.findViewById(R.id.voucher_recyclerView);
        memberShip=view.findViewById(R.id.voucher_feature_1);
        exchangePoint=view.findViewById(R.id.voucher_feature_2);
        pointHistory=view.findViewById(R.id.voucher_feature_3);
        yourRights=view.findViewById(R.id.voucher_feature_4);
        more=view.findViewById(R.id.voucher_more);
        deal_text=view.findViewById(R.id.deal_text);
        voucher_box=view.findViewById(R.id.voucher_box);
        voucher_point=view.findViewById(R.id.voucher_point);
        voucher_text_1=view.findViewById(R.id.voucher_text_1);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.deals, container, false);

        BindView(view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        recyclerView.addItemDecoration(new GridLayoutDecoration(0,10));
        getDealsData(recyclerView);
        getPoint();
        getAnimation(view);

        memberShip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), MemberShipActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        exchangePoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), ExchangePointActivity.class);
//                startActivity(intent);
                catcherForResult.launch(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        pointHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), PointHistoryActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        yourRights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), YourRightsActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), VoucherActivity.class);
                startActivity(intent);
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
                            if(result.getResultCode()== ExchangePointActivity.RESULT_OK){
                                Intent catcher=result.getData();
                                if(catcher!=null){
                                    String status=catcher.getStringExtra("status");
                                    if(status.equals("1")){
                                        getPoint();
                                    }
                                }
                            }
                        }
                    });
    private void getAnimation(View view){
        Animation dealTextAnim= AnimationUtils.loadAnimation(deal_text.getContext(), android.R.anim.fade_in);
        Animation voucherBoxAnim=AnimationUtils.loadAnimation(voucher_box.getContext(), android.R.anim.slide_in_left);
        Animation memberShipAnim=AnimationUtils.loadAnimation(memberShip.getContext(), android.R.anim.slide_in_left);
        Animation exchangePointAnim=AnimationUtils.loadAnimation(exchangePoint.getContext(), android.R.anim.slide_in_left);
        Animation pointHistoryAnim=AnimationUtils.loadAnimation(pointHistory.getContext(), android.R.anim.slide_in_left);
        Animation yourRightsAnim=AnimationUtils.loadAnimation(yourRights.getContext(), android.R.anim.slide_in_left);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                voucher_box.startAnimation(voucherBoxAnim);
            }
        },delay*0);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                memberShip.startAnimation(memberShipAnim);
            }
        },delay);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                exchangePoint.startAnimation(exchangePointAnim);
            }
        },delay*2);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pointHistory.startAnimation(pointHistoryAnim);
            }
        },delay*3);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                yourRights.startAnimation(yourRightsAnim);
            }
        },delay*4);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                deal_text.startAnimation(dealTextAnim);
            }
        },delay*2);
    }
    private void getDealsData(RecyclerView recyclerView){
        dealList=new ArrayList<>();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
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
                                            dealAdapter=new DealAdapter(dealList);
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

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    private void getPoint(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
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
                        voucher_point.setText(String.valueOf(oldPoint));
                        if(oldRankPoint<10000){
                            voucher_text_1.setText(String.valueOf(10000-oldRankPoint)+" points left and you achieve bronze");
                        }else if(oldRankPoint>=10000 && oldRankPoint<30000){
                            voucher_text_1.setText(String.valueOf(30000-oldRankPoint)+" points left and you achieve silver");
                        }else if(oldRankPoint>=30000 && oldRankPoint<50000){
                            voucher_text_1.setText(String.valueOf(50000-oldRankPoint)+" points left and you achieve gold");
                        }else if(oldRankPoint>=50000 && oldRankPoint<70000){
                            voucher_text_1.setText(String.valueOf(70000-oldRankPoint)+" points left and you achieve diamond");
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