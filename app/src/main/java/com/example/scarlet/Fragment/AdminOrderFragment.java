package com.example.scarlet.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Adapter.AdminOrderAdapter;
import com.example.scarlet.Adapter.GridLayoutDecoration;
import com.example.scarlet.Data.Address;
import com.example.scarlet.Data.Order;
import com.example.scarlet.Data.Payment;
import com.example.scarlet.Data.Product;
import com.example.scarlet.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AdminOrderFragment extends Fragment {

    int defaultStatus=1;
    RelativeLayout pending, done, cancel;
    TextView textpending, textdone, textcancel;
    AdminOrderAdapter adminOrderAdapter;
    List<Order> orderList;
    RecyclerView orderRecycleview;
    EditText search_bar;
    final Handler handler = new Handler();
    int delay=150;
    private void BindView(View view){
        pending=view.findViewById(R.id.pending);
        done=view.findViewById(R.id.done);
        cancel=view.findViewById(R.id.cancel);
        textpending=view.findViewById(R.id.textpending);
        textdone=view.findViewById(R.id.textdone);
        textcancel=view.findViewById(R.id.textcancel);
        orderRecycleview=view.findViewById(R.id.order_recyclerView);
        search_bar=view.findViewById(R.id.search_bar);
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.admin_fragment_order, container, false);

        BindView(view);
        checkStatus();
        orderList=new ArrayList<>();
        orderRecycleview.setLayoutManager(new GridLayoutManager(getContext(),1));
        orderRecycleview.addItemDecoration(new GridLayoutDecoration(0,5));
        getOrderData("pending");
        getAnimation();
        pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultStatus=1;
                getOrderData("pending");
                checkStatus();
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultStatus=2;
                getOrderData("done");
                checkStatus();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultStatus=3;
                getOrderData("cancelled");
                checkStatus();
            }
        });
        return view;
    }
    private void getAnimation(){
        Animation searchAnim= AnimationUtils.loadAnimation(search_bar.getContext(), android.R.anim.slide_in_left);
        Animation pendingAnim= AnimationUtils.loadAnimation(pending.getContext(), android.R.anim.slide_in_left);
        Animation doneAnim= AnimationUtils.loadAnimation(done.getContext(), android.R.anim.slide_in_left);
        Animation cancelAnim= AnimationUtils.loadAnimation(cancel.getContext(), android.R.anim.slide_in_left);
        Animation recycleViewAnim= AnimationUtils.loadAnimation(orderRecycleview.getContext(), android.R.anim.fade_in);
        search_bar.startAnimation(searchAnim);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pending.startAnimation(pendingAnim);
            }
        },delay*0);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                done.startAnimation(doneAnim);
            }
        },delay*0);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                cancel.startAnimation(cancelAnim);
            }
        },delay*0);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                orderRecycleview.startAnimation(recycleViewAnim);
            }
        },delay*3);
    }
    private void checkStatus(){
        switch (defaultStatus){
            case 1:
                pending.setBackground(getResources().getDrawable(R.drawable.rectangle_burgundy_radius));
                done.setBackground(getResources().getDrawable(R.drawable.rectangle_lightpink_radius));
                cancel.setBackground(getResources().getDrawable(R.drawable.rectangle_lightpink_radius));
                textpending.setTextColor(getResources().getColor(R.color.white));
                textdone.setTextColor(getResources().getColor(R.color.burgundy));
                textcancel.setTextColor(getResources().getColor(R.color.burgundy));
                break;
            case 2:
                pending.setBackground(getResources().getDrawable(R.drawable.rectangle_lightpink_radius));
                done.setBackground(getResources().getDrawable(R.drawable.rectangle_burgundy_radius));
                cancel.setBackground(getResources().getDrawable(R.drawable.rectangle_lightpink_radius));
                textpending.setTextColor(getResources().getColor(R.color.burgundy));
                textdone.setTextColor(getResources().getColor(R.color.white));
                textcancel.setTextColor(getResources().getColor(R.color.burgundy));
                break;
            case 3:
                pending.setBackground(getResources().getDrawable(R.drawable.rectangle_lightpink_radius));
                done.setBackground(getResources().getDrawable(R.drawable.rectangle_lightpink_radius));
                cancel.setBackground(getResources().getDrawable(R.drawable.rectangle_burgundy_radius));
                textpending.setTextColor(getResources().getColor(R.color.burgundy));
                textdone.setTextColor(getResources().getColor(R.color.burgundy));
                textcancel.setTextColor(getResources().getColor(R.color.white));
                break;
        }
    }
    private void getOrderData(String orderStatus){
        orderList.clear();
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference myRef= firebaseDatabase.getReference("order");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap:snapshot.getChildren()){
                    String status=snap.child("orderStatus").getValue(String.class);
                    if(status.equals(orderStatus)){
                        String key=snap.getKey();
                        double deliveryFee=snap.child("deliveryFee").getValue(double.class);
                        String deliveryStatus=snap.child("deliveryStatus").getValue(String.class);
                        Date orderDate=snap.child("orderDate").getValue(Date.class);
                        String orderStatus=snap.child("orderStatus").getValue(String.class);
                        Address address=snap.child("shippingAddress").getValue(Address.class);
                        double tip=snap.child("tip").getValue(double.class);
                        double total=snap.child("total").getValue(double.class);
                        DataSnapshot productListSnapshot =snap.child("productList");

                        List<Product> tempList=new ArrayList<>();
                        for(DataSnapshot snapshot1:productListSnapshot.getChildren()){
                            Product product=snapshot1.getValue(Product.class);
                            tempList.add(product);
                        }
                        Order order=new Order("",orderStatus,new Payment(),address,orderDate,total,tip,deliveryStatus,deliveryFee,tempList,key);
                        orderList.add(order);
                    }
                }
                adminOrderAdapter=new AdminOrderAdapter(orderList);
                orderRecycleview.setAdapter(adminOrderAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}