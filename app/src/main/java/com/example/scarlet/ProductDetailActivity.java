package com.example.scarlet;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.scarlet.Data.Cart;
import com.example.scarlet.Data.Favourite;
import com.example.scarlet.Data.ProductQuantity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ProductDetailActivity extends AppCompatActivity {
    public boolean isLoved=false;
    RelativeLayout back_button;
    ImageButton heart;
    TextView productNameView, productPriceView, categoryNameView, quantity;
    ImageView productImageView, categoryIconView, plus, minus;
    private void BindView(){
        back_button=findViewById(R.id.back_btn);
        heart=findViewById(R.id.heart);
        productNameView=findViewById(R.id.product_details_name);
        productPriceView=findViewById(R.id.product_details_price);
        categoryNameView=findViewById(R.id.product_details_category_name);
        productImageView=findViewById(R.id.product_details_image);
        categoryIconView=findViewById(R.id.product_details_category_icon);
        plus=findViewById(R.id.plus);
        minus=findViewById(R.id.minus);
        quantity=findViewById(R.id.quantity);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details);

        String productKey=getIntent().getStringExtra("productKey");
        getProductDetails(productKey);

        BindView();
        Button add=(Button) findViewById(R.id.add);
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
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart(productKey);
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qt=Integer.parseInt(quantity.getText().toString());
                qt=qt+1;
                quantity.setText(String.valueOf(qt));
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qt=Integer.parseInt(quantity.getText().toString());
                if(qt>1){
                    qt=qt-1;
                    quantity.setText(String.valueOf(qt));
                }
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
//                    Toast.makeText(ProductDetailActivity.this,"delete",Toast.LENGTH_SHORT).show();
                    heart.setImageResource(R.drawable.heart);
                }else{
                    insertToFavourite(productKey);
//                    Toast.makeText(ProductDetailActivity.this,"insert",Toast.LENGTH_SHORT).show();
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
                    String productImage=snapshot.child("img").getValue(String.class);
                    String categoryId=snapshot.child("categoryId").getValue(String.class);

                    myRef2.child(categoryId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot categorySnapshot) {
                            if(categorySnapshot.exists()){
                                String categoryIcon=categorySnapshot.child("img").getValue(String.class);
//                                categoryIconView.setImageResource(categoryIcon);
                                Glide.with(ProductDetailActivity.this).load(categoryIcon).into(categoryIconView);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    productNameView.setText(productName);
                    productPriceView.setText(String.valueOf(productPrice));
                    categoryNameView.setText(categoryName);
//                    productImageView.setImageResource(productImage);
                    Glide.with(ProductDetailActivity.this).load(productImage).into(productImageView);
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
    private void addToCart(String productKey){
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn=sharedPreferences.getBoolean("isLoggedIn",false);
        String userKey=sharedPreferences.getString("customerKey","");
        if(isLoggedIn && !userKey.isEmpty()){
            FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
            DatabaseReference myRef=firebaseDatabase.getReference("cart");
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<ProductQuantity> productIdList=new ArrayList<>();
                    if (snapshot.exists()) {
                        boolean found=false;
                        boolean founUser=false;
                        for(DataSnapshot snap: snapshot.getChildren()){
                            if(snap.child("customerId").getValue(String.class).equals(userKey)){
                                founUser=true;
                                if(snap.child("productQuantityList").exists()){
                                    DataSnapshot productIdObject=snap.child("productQuantityList");
                                    if(productIdObject.getValue() instanceof List){
                                        List<ProductQuantity> tempProductIdList = new ArrayList<>();
                                        int quantity=1;
                                        for(DataSnapshot productSnap: productIdObject.getChildren()){
                                            ProductQuantity productQuantity=productSnap.getValue(ProductQuantity.class);
                                            tempProductIdList.add(productQuantity);
                                        }
                                        productIdList=tempProductIdList;
                                        for(ProductQuantity pd:productIdList){
                                            if(pd.getProductId().equals(productKey)){
                                                quantity=pd.getQuantity()+1;
                                                pd.setQuantity(quantity);
                                                found=true;
                                                break;
                                            }
                                        }
                                        if(!found){
                                            productIdList.add(new ProductQuantity(productKey,quantity));
                                        }

                                        myRef.child(snap.getKey()).child("productQuantityList").setValue(productIdList);
                                        Toast.makeText(ProductDetailActivity.this,"Add to cart successfully", Toast.LENGTH_SHORT).show();
                                        break;
                                    }
                                } else{
                                    int qt=Integer.parseInt(quantity.getText().toString());
                                    productIdList.add(new ProductQuantity(productKey,qt));
                                    myRef.child(snap.getKey()).child("productQuantityList").setValue(productIdList);
                                    Toast.makeText(ProductDetailActivity.this,"Add to cart successfully", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }
                        }
                        if(!founUser){
                            int qt=Integer.parseInt(quantity.getText().toString());
                            productIdList.add(new ProductQuantity(productKey, qt));
                            Cart cart=new Cart(userKey,productIdList);
                            myRef.push().setValue(cart);
                            Toast.makeText(ProductDetailActivity.this,"Add to cart successfully", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        int qt=Integer.parseInt(quantity.getText().toString());
                        productIdList.add(new ProductQuantity(productKey, qt));
                        Cart cart=new Cart(userKey,productIdList);
                        myRef.push().setValue(cart);
                        Toast.makeText(ProductDetailActivity.this,"Add to cart successfully", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}