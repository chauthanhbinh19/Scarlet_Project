package com.example.scarlet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.scarlet.Data.Customer;
import com.example.scarlet.Data.Favourite;
import com.example.scarlet.Data.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class ProductDetailActivity extends AppCompatActivity {
    public boolean isLoved=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details);

        String productKey=getIntent().getStringExtra("productKey");
        getProductDetails(productKey);

        RelativeLayout back_button=findViewById(R.id.back_btn);
        ImageButton heart=findViewById(R.id.heart);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        validateFavouriteProduct(productKey,heart);
        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFavouriteProduct2(productKey,heart);
            }
        });
    }
    private void validateFavouriteProduct(String producKey,ImageView heart){
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn=sharedPreferences.getBoolean("isLoggedIn",false);
        String userKey=sharedPreferences.getString("customerKey","");

        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        Query query=firebaseDatabase.getReference("favourite").orderByChild("customerId").equalTo(userKey);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    boolean found=false;
                    for(DataSnapshot snap: snapshot.getChildren()){
                        if(snap.child("productId").getValue(String.class).equals(producKey)){
                            isLoved=true;
                            found=true;
                            heart.setImageResource(R.drawable.heart__1__1);
                            break;
                        }
                    }
                    if(!found){
                        isLoved=false;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void validateFavouriteProduct2(String productKey, ImageButton heart){
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn=sharedPreferences.getBoolean("isLoggedIn",false);
        String userKey=sharedPreferences.getString("customerKey","");

        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        Query query=firebaseDatabase.getReference("favourite").orderByChild("customerId").equalTo(userKey);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isFavourite = false;
                if(snapshot.exists()){
                    for(DataSnapshot snap: snapshot.getChildren()){
                        if(snap.child("productId").getValue(String.class).equals(productKey)){
                            isFavourite=true;
                        }
                    }
                }
                if(isFavourite){
                    deleteFavourite(productKey);
                    Toast.makeText(ProductDetailActivity.this,"delete",Toast.LENGTH_SHORT).show();
                    heart.setImageResource(R.drawable.heart);
                }else{
                    insertToFavourite(productKey);
                    Toast.makeText(ProductDetailActivity.this,"insert",Toast.LENGTH_SHORT).show();
                    heart.setImageResource(R.drawable.heart__1__1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
    private void insertToFavourite(String producKey){
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn=sharedPreferences.getBoolean("isLoggedIn",false);
        String userKey=sharedPreferences.getString("customerKey","");
        if(isLoggedIn && !userKey.isEmpty()){
            FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
            DatabaseReference myRef=firebaseDatabase.getReference("favourite");

            Favourite favourite=new Favourite(userKey,producKey);
            myRef.push().setValue(favourite, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    if (error == null) {
//                        Toast.makeText(ProductDetailActivity.this,"Favourite added successfully",Toast.LENGTH_SHORT).show();
                    } else {
//                        Toast.makeText(ProductDetailActivity.this,"Firebase, Error adding favourite",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    private void deleteFavourite(String producKey){
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn=sharedPreferences.getBoolean("isLoggedIn",false);
        String userKey=sharedPreferences.getString("customerKey","");

        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        Query query=firebaseDatabase.getReference("favourite").orderByChild("customerId").equalTo(userKey);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot snap: snapshot.getChildren()){
                        if(snap.child("productId").getValue(String.class).equals(producKey)){
                            snap.getRef().removeValue();
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