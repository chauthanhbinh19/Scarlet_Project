package com.example.scarlet.Fragment;

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
import java.util.List;

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
        recyclerView.addItemDecoration(new GridLayoutDecoration(0,10));
        getVoucherData(recyclerView, tabTitle);

        return view;
    }
    private void getVoucherData(RecyclerView recyclerView, String deliveryMethodClicked){
        dealList=new ArrayList<>();
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        Query Query= firebaseDatabase.getReference("deal");
        Query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot snap: snapshot.getChildren()){
                        String voucherName=snap.child("name").getValue(String.class);
                        int discount=snap.child("discount").getValue(int.class);
                        Date expiryDate=snap.child("expiryDate").getValue(Date.class);
                        String deliveryMethod=snap.child("deliveryMethod").getValue(String.class);
                        int deliveryMethodIcon=snap.child("deliveryIcon").getValue(int.class);
                        Deal deal=new Deal(voucherName,discount,expiryDate,deliveryMethod,deliveryMethodIcon);
                        if(deliveryMethod.equals(deliveryMethodClicked)){
                            dealList.add(deal);
                        }
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