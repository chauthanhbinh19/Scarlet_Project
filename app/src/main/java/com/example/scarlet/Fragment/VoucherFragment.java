package com.example.scarlet.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Adapter.DealAdapter;
import com.example.scarlet.Adapter.DealSecondAdapter;
import com.example.scarlet.Adapter.GridLayoutDecoration;
import com.example.scarlet.Data.Deal;
import com.example.scarlet.R;
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

public class VoucherFragment extends Fragment {
    private String tabTitle;
    private DealAdapter dealAdapter;
    private List<Deal> dealList;
    RecyclerView recyclerView;
    public static VoucherFragment newInstance(String tabTitle) {
        VoucherFragment fragment = new VoucherFragment();
        Bundle args = new Bundle();
        args.putString("tabTitle", tabTitle);
        fragment.setArguments(args);
        return fragment;
    }
    public VoucherFragment(){

    }
    private void BindView(View view){
        recyclerView=view.findViewById(R.id.voucher_recyclerView);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.voucher_tabpage, container, false);
        Bundle bundle = getArguments();
        String tabTitle = bundle.getString("tabTitle");

        BindView(view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        recyclerView.addItemDecoration(new GridLayoutDecoration(0,5));
        getVoucherData(recyclerView, tabTitle);

        return view;
    }
    private void getVoucherData(RecyclerView recyclerView, String deliveryMethodClicked){
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
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

}