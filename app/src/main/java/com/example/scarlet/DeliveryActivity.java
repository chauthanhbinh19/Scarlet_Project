package com.example.scarlet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Adapter.ProductHorizontalAdapter;
import com.example.scarlet.Data.Product;
import com.example.scarlet.Data.ProductQuantity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class DeliveryActivity extends AppCompatActivity {

    TextView address, additionalInfo, deliveryPrice;
    TextView totalView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference cartRef, productRef;
    RelativeLayout back_btn;
    Button continue_btn;
    String total,deliveryStatus;
    RecyclerView productRecycleView;
    ProductHorizontalAdapter adapter;
    private void BindView(){
        back_btn=findViewById(R.id.back_btn);
        address=findViewById(R.id.delivery_address_content);
        additionalInfo=findViewById(R.id.delivery_additional_content);
        totalView=findViewById(R.id.total);
        continue_btn=findViewById(R.id.continue_btn);
        deliveryPrice=findViewById(R.id.deliveryPrice);
        productRecycleView=findViewById(R.id.totalProductRecycleView);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivery);

        Window window = getWindow();
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.burgundy));

        BindView();
        Intent intent=getIntent();
        if(intent!= null){
            deliveryStatus=intent.getStringExtra("deliveryStatus");
            String street=intent.getStringExtra("street");
            String ward=intent.getStringExtra("ward");
            String district=intent.getStringExtra("district");
            String province=intent.getStringExtra("province");
            String postalCode=intent.getStringExtra("postalCode");
            String additionalInfo1=intent.getStringExtra("additionalInfo");
            total=intent.getStringExtra("total");

            String text=street+", "+ward+", "+district+", "+province;
            address.setText(text);
            additionalInfo.setText(additionalInfo1);
            totalView.setText(total);
        }
        productRecycleView.setLayoutManager(new LinearLayoutManager(DeliveryActivity.this,LinearLayoutManager.HORIZONTAL,false));
        getCartData();
        if(deliveryStatus.equals("delivery")){
            deliveryPrice.setText("10000");
        }else if(deliveryStatus.equals("pickup")){
            deliveryPrice.setText("0");
        }
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(DeliveryActivity.this, PaymentActivity.class);
                intent1.putExtra("total",total);
                intent1.putExtra("deliveryStatus",deliveryStatus);
                startActivity(intent1);
            }
        });
    }
    private void getCartData(){
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn=sharedPreferences.getBoolean("isLoggedIn",false);
        String userKey=sharedPreferences.getString("customerKey","");

        if(isLoggedIn && !userKey.isEmpty()){
            firebaseDatabase= FirebaseDatabase.getInstance();
            cartRef=firebaseDatabase.getReference("cart");

            cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<ProductQuantity> productQuantityList=new ArrayList<>();
                    if(snapshot.exists()){
                        for(DataSnapshot cartSnap: snapshot.getChildren()){
                            String customerId=cartSnap.child("customerId").getValue(String.class);
                            if(customerId.equals(userKey)){
                                DataSnapshot productQuantityListSnapshot =cartSnap.child("productQuantityList");

                                for(DataSnapshot snapshot1:productQuantityListSnapshot.getChildren()){
                                    ProductQuantity productQuantity=snapshot1.getValue(ProductQuantity.class);
                                    productQuantityList.add(productQuantity);
                                }
                            }
                        }
                        getProductData(productQuantityList, userKey);
                    }else{

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    private void getProductData(List<ProductQuantity> productKeyList, String userKey){

        firebaseDatabase = FirebaseDatabase.getInstance();
        productRef = firebaseDatabase.getReference("product");
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Product> productList=new ArrayList<>();
                for(DataSnapshot productSnap: snapshot.getChildren()){
                    ProductQuantity productKey=new ProductQuantity(productSnap.getKey(),1);
                    if(checkKeyInList(productKey,productKeyList)){
                        String productName=productSnap.child("name").getValue(String.class);
                        double productPrice=productSnap.child("price").getValue(double.class);
//                        int productQuantity=getQuantity(productKey,productKeyList);
                        String productImg=productSnap.child("img").getValue(String.class);
//                        double productTotal=productPrice*productQuantity;
                        Product product=new Product(productName,productPrice, productImg);
                        productList.add(product);
                    }
                }
                adapter=new ProductHorizontalAdapter(productList);
                productRecycleView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public boolean checkKeyInList(ProductQuantity key, List<ProductQuantity> keyList) {
        for (ProductQuantity item : keyList) {
            if (item.getProductId().equals(key.getProductId())) {
                return true;
            }
        }
        return false;
    }
}