package com.example.scarlet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.scarlet.Data.Customer;
import com.example.scarlet.Data.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class ProductDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details);

        String productKey=getIntent().getStringExtra("productKey");
        getProductDetails(productKey);

        RelativeLayout back_button=findViewById(R.id.back_btn);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    private void getProductDetails(String productKey){
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference myRef=firebaseDatabase.getReference("product").child(productKey);
        DatabaseReference myRef2=firebaseDatabase.getReference("category");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String productName=snapshot.child("name").getValue(String.class);
                    Double productPrice=snapshot.child("price").getValue(Double.class);
                    String categoryName=snapshot.child("categoryName").getValue(String.class);
                    int productImage=snapshot.child("img").getValue(int.class);
                    String categoryId=snapshot.child("categoryId").getValue(String.class);

                    TextView productNameView=findViewById(R.id.product_details_name);
                    TextView productPriceView=findViewById(R.id.product_details_price);
                    TextView categoryNameView=findViewById(R.id.product_details_category_name);
                    ImageView productImageView=findViewById(R.id.product_details_image);

                    myRef2.child(categoryId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot categorySnapshot) {
                            if(categorySnapshot.exists()){
                                int categoryIcon=categorySnapshot.child("img").getValue(int.class);
                                ImageView categoryIconView=findViewById(R.id.product_details_category_icon);
                                categoryIconView.setImageResource(categoryIcon);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    productNameView.setText(productName);
                    productPriceView.setText(String.valueOf(productPrice));
                    categoryNameView.setText(categoryName);
                    productImageView.setImageResource(productImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}