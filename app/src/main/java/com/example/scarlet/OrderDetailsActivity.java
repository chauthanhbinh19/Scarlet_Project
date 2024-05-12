package com.example.scarlet;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
    TextView addressText, timeText, subtotal, delivery, tipText, totalText, status,deliveryMethodText;
    LottieAnimationView lottieAnimationView;
    Button cancelledBtn, orderAgainBtn;
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
        deliveryMethodText=findViewById(R.id.deliveryMethod);
        cancelledBtn=findViewById(R.id.cancelledBtn);
        orderAgainBtn=findViewById(R.id.orderAgainBtn);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_activities_detail);

        Window window = getWindow();
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.white));
        
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
        cancelledBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(OrderDetailsActivity.this);
                builder.setMessage("Do you want to cancel your order?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                                DatabaseReference myRef=firebaseDatabase.getReference("order").child(key);
                                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){
                                            myRef.child("orderStatus").setValue("cancelled");
                                            Toast.makeText(OrderDetailsActivity.this,"Cancelled successfully",Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
        orderAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(OrderDetailsActivity.this,OrderAgainActivity.class);
                intent1.putExtra("key",key);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
                    if(deliveryStatus.equals("delivery")){
                        deliveryMethodText.setText("Delivery");
                    }else if(deliveryStatus.equals("instore")){
                        deliveryMethodText.setText("In store");
                    }else if(deliveryStatus.equals("pickup")){
                        deliveryMethodText.setText("Pick up");
                    }
                    getProductData(tempList);
                    if(orderStatus.equals("pending")){
                        lottieAnimationView.setAnimation(R.raw.pending_animation);
                        lottieAnimationView.playAnimation();
                        lottieAnimationView.setRepeatCount(LottieDrawable.INFINITE);
                        status.setText("Pending");
                        status.setTextColor(getColor(R.color.yellow));
                        cancelledBtn.setVisibility(View.VISIBLE);
                        orderAgainBtn.setVisibility(View.GONE);
                    }else if(orderStatus.equals("done")){
                        lottieAnimationView.setAnimation(R.raw.done_animation);
                        lottieAnimationView.playAnimation();
                        lottieAnimationView.setRepeatCount(LottieDrawable.INFINITE);
                        status.setText("Done");
                        status.setTextColor(getColor(R.color.green1));
                        cancelledBtn.setVisibility(View.GONE);
                        orderAgainBtn.setVisibility(View.VISIBLE);
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
                for(Product pq:productKeyList){
                    for(DataSnapshot productSnap: snapshot.getChildren()){
                        String key=productSnap.getKey();

                        String productName=productSnap.child("name").getValue(String.class);
                        double productPrice=productSnap.child("price").getValue(double.class);
                        if(pq.getKey().equals(key)){
                            int productQuantity=pq.getQuantity();
                            String size=pq.getSize();
                            String productImg=productSnap.child("img").getValue(String.class);
                            double productTotal=productPrice*productQuantity;
                            tempTotal=tempTotal+productTotal;
                            Product product=new Product(productName,productTotal, productImg,productQuantity,key,size);
                            productList.add(product);
                        }
                    }
                }
                adapter=new ProductHorizontalAdapter(productList);
                productRecycleView.setAdapter(adapter);
                subtotal.setText(String.format("%.0f",tempTotal)+" đ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public int getQuantity(Product key, List<Product> keyList){
        for (Product item : keyList) {
            if (item.getKey().equals(key.getKey())) {
                return item.getQuantity();
            }
        }
        return 1;
    }
    public boolean checkKeyInList(Product key, List<Product> keyList) {
        for (Product item : keyList) {
            if (item.getKey().equals(key.getKey())) {
                return true;
            }
        }
        return false;
    }
}