package com.example.scarlet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Adapter.CartAdapter;
import com.example.scarlet.Adapter.CartSecondAdapter;
import com.example.scarlet.Adapter.GridLayoutDecoration;
import com.example.scarlet.Data.Cart;
import com.example.scarlet.Data.Product;
import com.example.scarlet.Data.ProductQuantity;
import com.example.scarlet.Interface.GetProductCallback;
import com.example.scarlet.Interface.GetStringCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class OrderAgainActivity extends AppCompatActivity {

    RelativeLayout back_btn;
    RecyclerView product_recyclerView;
    String key;
    List<Product> productList, productKeyList;
    GetProductCallback getProductCallback;
    RecyclerView recyclerView;
    TextView totalView;
    CartSecondAdapter productAdapter;
    Button checkoutBtn;
    private double total=0;
    private void BindView(){
        back_btn=findViewById(R.id.back_btn);
        recyclerView=findViewById(R.id.product_recyclerView);
        totalView=findViewById(R.id.total);
        checkoutBtn=findViewById(R.id.checkoutBtn);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_detail_again);

        Window window = getWindow();
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.white));
        
        BindView();
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        recyclerView.addItemDecoration(new GridLayoutDecoration(5,15));
        Intent intent=new Intent();
        if(intent!=null){
            key=getIntent().getStringExtra("key");
            String a=key;
        }
        getProductCallback=new GetProductCallback() {
            @Override
            public void itemClick(String key, String size, double price, int type) {
                for(Product product:productList){
                    if(product.getKey().equals(key) && product.getSize().equals(size)){
                        if(type==0){
                            double oldTotal=Double.parseDouble(totalView.getText().toString());
                            double newTotal=oldTotal+price;
                            totalView.setText(String.format("%.0f", newTotal));
                        }
                        if(type==1){
                            double oldTotal=Double.parseDouble(totalView.getText().toString());
                            double newTotal=oldTotal-price;
                            totalView.setText(String.format("%.0f", newTotal));
                        }
                    }
                }
                List<Product> a=productList;
            }
        };
        getOrderData(getProductCallback,recyclerView);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences =getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                boolean isLoggedIn=sharedPreferences.getBoolean("isLoggedIn",false);
                String userKey=sharedPreferences.getString("customerKey","");
                if(isLoggedIn && !userKey.isEmpty()){
                    List<ProductQuantity> a=new ArrayList<>();
                    for(Product pq:productList){
                        ProductQuantity product=new ProductQuantity(pq.getKey(),pq.getQuantity(),0,pq.getSize());
                        a.add(product);
                    }
                    addToCart(userKey,a);
                }
            }
        });
    }
    private void getOrderData(GetProductCallback getProductCallback, RecyclerView recyclerView){
        productKeyList=new ArrayList<>();
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference myRef=firebaseDatabase.getReference("order").child(key);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    DataSnapshot productQuantity=snapshot.child("productList");

                    for(DataSnapshot snap:productQuantity.getChildren()){
                        String name=snap.child("name").getValue(String.class);
                        int quantity=snap.child("quantity").getValue(int.class);
                        double price=snap.child("price").getValue(double.class);
                        String size=snap.child("size").getValue(String.class);
                        String key=snap.child("key").getValue(String.class);
                        Product product=new Product(name,quantity,price,size,key);
                        productKeyList.add(product);
                    }
                    getProductData(productKeyList,recyclerView,getProductCallback);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getProductData(List<Product> productKeyList, RecyclerView recyclerView, GetProductCallback getProductCallback){
        productList=new ArrayList<>();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference productRef = firebaseDatabase.getReference("product");
        DatabaseReference categoryRef = firebaseDatabase.getReference("category");
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot productSnapshot) {
                productList.clear();
                for(Product pq:productKeyList){
                    for (DataSnapshot product : productSnapshot.getChildren()) {
                        String key=product.getKey();
                        String categoryId = product.child("categoryId").getValue(String.class);
                        String productName = product.child("name").getValue(String.class);
                        double productPrice = product.child("price").getValue(double.class);
                        String productImage = product.child("img").getValue(String.class);

                        categoryRef.child(categoryId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot categorySnapshot) {
                                if (categorySnapshot.exists()) {
                                    String icon = categorySnapshot.child("img").getValue(String.class);
                                    if(key.equals(pq.getKey())){
                                        int quantity=pq.getQuantity();
                                        String size=pq.getSize();
                                        total=total+productPrice*quantity;
                                        Product productWithIcon = new Product(productName, productPrice,productImage, icon,pq.getKey(),quantity,productPrice*quantity,size);
                                        productList.add(productWithIcon);
                                        totalView=findViewById(R.id.total);
                                        totalView.setText(String.format("%.0f", total));
                                    }
                                }
                                productAdapter=new CartSecondAdapter(productList,getProductCallback);
                                recyclerView.setAdapter(productAdapter);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                // Xử lý lỗi nếu có
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });
    }
    private void addToCart(String userKey, List<ProductQuantity> productQuantities){
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference myRef=firebaseDatabase.getReference("cart");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for(DataSnapshot snap: snapshot.getChildren()){
                        if(snap.child("customerId").getValue(String.class).equals(userKey)){
                            myRef.child(snap.getKey()).child("productQuantityList").setValue(productQuantities);
                            Toast.makeText(OrderAgainActivity.this,"Add to cart successfully", Toast.LENGTH_SHORT).show();

                            String total=totalView.getText().toString();
                            Intent intent1=new Intent(OrderAgainActivity.this, DeliveryActivity.class);
                            intent1.putExtra("total",total);
                            startActivity(intent1);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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