package com.example.scarlet.Fragment;

import android.adservices.measurement.MeasurementManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Adapter.DealAdapter;
import com.example.scarlet.Adapter.GridLayoutDecoration;
import com.example.scarlet.Data.Deal;
import com.example.scarlet.ExchangePointActivity;
import com.example.scarlet.MemberShipActivity;
import com.example.scarlet.PointHistoryActivity;
import com.example.scarlet.R;
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
import java.util.List;

public class DealsFragment extends Fragment {
    private DealAdapter dealAdapter;
    private List<Deal> dealList;
    RecyclerView recyclerView;
    RelativeLayout memberShip,exchangePoint,pointHistory,yourRights;
    TextView more;
    private void BindView(View view){
        recyclerView=view.findViewById(R.id.voucher_recyclerView);
        memberShip=view.findViewById(R.id.voucher_feature_1);
        exchangePoint=view.findViewById(R.id.voucher_feature_2);
        pointHistory=view.findViewById(R.id.voucher_feature_3);
        yourRights=view.findViewById(R.id.voucher_feature_4);
        more=view.findViewById(R.id.voucher_more);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.deals, container, false);

        BindView(view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        recyclerView.addItemDecoration(new GridLayoutDecoration(0,10));
        getDealsData(recyclerView);

        memberShip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), MemberShipActivity.class);
                startActivity(intent);
            }
        });
        exchangePoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), ExchangePointActivity.class);
                startActivity(intent);
            }
        });
        pointHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), PointHistoryActivity.class);
                startActivity(intent);
            }
        });
        yourRights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), YourRightsActivity.class);
                startActivity(intent);
            }
        });
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), VoucherActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
    private void getDealsData(RecyclerView recyclerView){
        dealList=new ArrayList<>();
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        Query query=firebaseDatabase.getReference("deal");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot snap:snapshot.getChildren()){
                        String voucherName=snap.child("name").getValue(String.class);
                        String key=snap.getKey();
                        int discount=snap.child("discount").getValue(int.class);
                        Date expiryDate=snap.child("expiryDate").getValue(Date.class);
                        String deliveryMethod=snap.child("deliveryMethod").getValue(String.class);
                        int deliveryMethodIcon=1;
                        if(deliveryMethod.equals("delivery")){
                            deliveryMethodIcon=R.drawable.delivery_bike;
                        }else if(deliveryMethod.equals("instore")){
                            deliveryMethodIcon=R.drawable.store;
                        }else if(deliveryMethod.equals("pickup")){
                            deliveryMethodIcon=R.drawable.food_delivery;
                        }
                        String description=snap.child("description").getValue(String.class);
                        Deal deal=new Deal(voucherName,discount,expiryDate,deliveryMethod,deliveryMethodIcon,description,key);
                        if(dealList.size() >4){
                            break;
                        }
                        dealList.add(deal);
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