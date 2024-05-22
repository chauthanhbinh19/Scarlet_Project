package com.example.scarlet.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Adapter.GridLayoutDecoration;
import com.example.scarlet.Adapter.OrderAdapter;
import com.example.scarlet.Data.Order;
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

public class OrderActivitiesFragment extends Fragment {

    RelativeLayout back_btn;
    Button btnOngoing, btnHistory;
    RecyclerView orderRecycleView;
    List<Order> orderList;
    OrderAdapter orderAdapter;
    int defaultStatus=1;
    TextView order_activities_text;
    private void BindView(View view){
        btnHistory=view.findViewById(R.id.btnHistory);
        btnOngoing=view.findViewById(R.id.btnOngoing);
        back_btn=view.findViewById(R.id.back_btn);
        orderRecycleView=view.findViewById(R.id.order_activities_recyclerView);
        order_activities_text=view.findViewById(R.id.order_activities_text);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.order_activities, container, false);

        BindView(view);
        getStatus();
        getAnimation();
        orderRecycleView.setLayoutManager(new GridLayoutManager(getContext(),1));
//        orderRecycleView.addItemDecoration(new GridLayoutDecoration(5,10));
        getOrderData("ongoing");
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getParentFragmentManager().popBackStack();
                AccountFragment accountFragment=new AccountFragment();
                FragmentManager fragmentManager=getParentFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                fragmentTransaction.replace(R.id.frame_layout,accountFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        btnOngoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultStatus=1;
                getStatus();
                getOrderData("ongoing");
            }
        });
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultStatus=2;
                getStatus();
                getOrderData("history");
            }
        });
        return view;
    }
    private void getAnimation(){
        Animation order_activities_textAnim= AnimationUtils.loadAnimation(order_activities_text.getContext(), android.R.anim.fade_in);
        order_activities_text.startAnimation(order_activities_textAnim);
    }
    private void getStatus(){
        switch (defaultStatus){
            case 1:
                btnOngoing.setBackground(getResources().getDrawable(R.drawable.rectangle_burgundy_radius));
                btnHistory.setBackground(getResources().getDrawable(R.drawable.rectangle_lightpink_radius));
                btnOngoing.setTextColor(getResources().getColor(R.color.white));
                btnHistory.setTextColor(getResources().getColor(R.color.black));
                break;
            case 2:
                btnOngoing.setBackground(getResources().getDrawable(R.drawable.rectangle_lightpink_radius));
                btnHistory.setBackground(getResources().getDrawable(R.drawable.rectangle_burgundy_radius));
                btnOngoing.setTextColor(getResources().getColor(R.color.black));
                btnHistory.setTextColor(getResources().getColor(R.color.white));
                break;
        }
    }
    private void getOrderData(String status){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn=sharedPreferences.getBoolean("isLoggedIn",false);
        final String userKey=sharedPreferences.getString("customerKey","");
        if(isLoggedIn && !userKey.isEmpty()){
            orderList=new ArrayList<>();
            FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
            DatabaseReference myRef= firebaseDatabase.getReference("order");
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot snap: snapshot.getChildren()){
                        String customerKey=snap.child("userId").getValue(String.class);
                        String orderStatus=snap.child("orderStatus").getValue(String.class);
                        boolean isConfirmed=snap.child("confirmed").getValue(boolean.class);
                        if(status.equals("ongoing")){
                            if(orderStatus.equals("pending")){
                                if(customerKey.equals(userKey)){
                                    String key=snap.getKey();
                                    Date date=snap.child("orderDate").getValue(Date.class);
                                    double total=snap.child("total").getValue(double.class);
                                    List<Product> productList=new ArrayList<>();
                                    for(DataSnapshot productSnap: snap.child("productList").getChildren()){
                                        Product product=productSnap.getValue(Product.class);
                                        productList.add(product);
                                    }
                                    Order order=new Order(orderStatus,date,total,productList,key,isConfirmed);
                                    orderList.add(order);
                                }
                            }else if(orderStatus.equals("done") && !isConfirmed){
                                if(customerKey.equals(userKey)){
                                    String key=snap.getKey();
                                    Date date=snap.child("orderDate").getValue(Date.class);
                                    double total=snap.child("total").getValue(double.class);
                                    List<Product> productList=new ArrayList<>();
                                    for(DataSnapshot productSnap: snap.child("productList").getChildren()){
                                        Product product=productSnap.getValue(Product.class);
                                        productList.add(product);
                                    }
                                    Order order=new Order(orderStatus,date,total,productList,key,isConfirmed);
                                    orderList.add(order);
                                }
                            }
                        }else{
                            if((orderStatus.equals("done") && isConfirmed) || orderStatus.equals("cancelled")){
                                if(customerKey.equals(userKey)){
                                    String key=snap.getKey();
                                    Date date=snap.child("orderDate").getValue(Date.class);
                                    double total=snap.child("total").getValue(double.class);
                                    List<Product> productList=new ArrayList<>();
                                    for(DataSnapshot productSnap: snap.child("productList").getChildren()){
                                        Product product=productSnap.getValue(Product.class);
                                        productList.add(product);
                                    }
                                    Order order=new Order(orderStatus,date,total,productList,key,isConfirmed);
                                    orderList.add(order);
                                }
                            }
                        }
                    }
                    orderAdapter =new OrderAdapter(orderList);
                    orderRecycleView.setAdapter(orderAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}