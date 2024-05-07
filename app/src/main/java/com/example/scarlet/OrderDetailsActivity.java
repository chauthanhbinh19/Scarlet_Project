package com.example.scarlet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.example.scarlet.Adapter.ProductHorizontalAdapter;
import com.example.scarlet.Data.Address;
import com.example.scarlet.Data.Product;
import com.example.scarlet.Data.ProductQuantity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class OrderDetailsActivity extends AppCompatActivity {

    RelativeLayout back_btn;
    String key;
    List<Product> productList;
    ProductHorizontalAdapter adapter;
    RecyclerView productRecycleView;
    TextView addressText, timeText, subtotal, delivery, tipText, totalText, status;
    LottieAnimationView lottieAnimationView;
    private void BindView(){
        back_btn=findViewById(R.id.back_btn);
        addressText=findViewById(R.id.address);
        timeText=findViewById(R.id.time);
        delivery=findViewById(R.id.delivery);
        subtotal=findViewById(R.id.subtotal);
        tipText=findViewById(R.id.tip);
        totalText=findViewById(R.id.total);
        productRecycleView=findViewById(R.id.totalProductRecycleView);
        lottieAnimationView=findViewById(R.id.status_animation);
        status=findViewById(R.id.status);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_activities_detail);

        Window window = getWindow();
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.burgundy));
        
        BindView();
        Intent intent=new Intent();
        if(intent!=null){
            key=getIntent().getStringExtra("orderKey");
            String a=key;
        }
        productRecycleView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        getOrderDetail();
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }
    private void getOrderDetail(){
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference myRef=firebaseDatabase.getReference("order");
        myRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    double deliveryFee=snapshot.child("deliveryFee").getValue(double.class);
                    String deliveryStatus=snapshot.child("deliveryStatus").getValue(String.class);
                    Date orderDate=snapshot.child("orderDate").getValue(Date.class);
                    String orderStatus=snapshot.child("orderStatus").getValue(String.class);
                    Address address=snapshot.child("shippingAddress").getValue(Address.class);
                    double tip=snapshot.child("tip").getValue(double.class);
                    double total=snapshot.child("total").getValue(double.class);
                    DataSnapshot productListSnapshot =snapshot.child("productList");

                    List<Product> tempList=new ArrayList<>();
                    for(DataSnapshot snapshot1:productListSnapshot.getChildren()){
                        Product product=snapshot1.getValue(Product.class);
                        tempList.add(product);
                    }

                    addressText.setText(address.getStreet()+" "+ address.getWard() +" "+address.getDistrict()+ " "+address.getProvince());
                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                    String formattedExpiryDate = format.format(orderDate);
                    timeText.setText(formattedExpiryDate);
                    delivery.setText(String.format("%.0f",deliveryFee)+" đ");
                    tipText.setText(String.format("%.0f",tip)+" đ");
                    totalText.setText(String.format("%.0f",total)+" đ");
                    getProductData(tempList);
                    if(orderStatus.equals("pending")){
                        lottieAnimationView.setAnimation(R.raw.pending_animation);
                        lottieAnimationView.playAnimation();
                        lottieAnimationView.setRepeatCount(LottieDrawable.INFINITE);
                        status.setText("Pending");
                        status.setTextColor(getColor(R.color.yellow));
                    }else if(orderStatus.equals("done")){
                        lottieAnimationView.setAnimation(R.raw.done_animation);
                        lottieAnimationView.playAnimation();
                        lottieAnimationView.setRepeatCount(LottieDrawable.INFINITE);
                        status.setText("Done");
                        status.setTextColor(getColor(R.color.green1));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getProductData(List<Product> productKeyList){

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference productRef = firebaseDatabase.getReference("product");
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Product> productList=new ArrayList<>();
                double tempTotal=0;
                for(DataSnapshot productSnap: snapshot.getChildren()){
                    String productName=productSnap.child("name").getValue(String.class);
                    double productPrice=productSnap.child("price").getValue(double.class);
                    Product productKey=new Product(productSnap.getKey(),productName,1);
                    if(checkKeyInList(productKey,productKeyList)){
                        int productQuantity=getQuantity(productKey,productKeyList);
                        String productImg=productSnap.child("img").getValue(String.class);
                        double productTotal=productPrice*productQuantity;
                        tempTotal=tempTotal+productTotal;
                        Product product=new Product(productName,productTotal, productImg,productQuantity);
                        productList.add(product);
                    }
                }
                adapter=new ProductHorizontalAdapter(productList);
                productRecycleView.setAdapter(adapter);
                subtotal.setText(String.format("%.0f",tempTotal));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public int getQuantity(Product key, List<Product> keyList){
        for (Product item : keyList) {
            if (item.getName().equals(key.getName())) {
                return item.getQuantity();
            }
        }
        return 1;
    }
    public boolean checkKeyInList(Product key, List<Product> keyList) {
        for (Product item : keyList) {
            if (item.getName().equals(key.getName())) {
                return true;
            }
        }
        return false;
    }
}