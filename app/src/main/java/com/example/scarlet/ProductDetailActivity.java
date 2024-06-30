package com.example.scarlet;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.scarlet.Adapter.CartAdapter;
import com.example.scarlet.Adapter.GridLayoutDecoration;
import com.example.scarlet.Adapter.ProductSearchAdapter;
import com.example.scarlet.Adapter.ReviewAdapter;
import com.example.scarlet.Data.Cart;
import com.example.scarlet.Data.Favourite;
import com.example.scarlet.Data.Product;
import com.example.scarlet.Data.ProductQuantity;
import com.example.scarlet.Data.Review;
import com.example.scarlet.Fragment.CartFragment;
import com.example.scarlet.Interface.GetStringCallback;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class ProductDetailActivity extends AppCompatActivity {
    public boolean isLoved=false;
    RelativeLayout back_button, reviewTabBox, recommendTabBox,informationTabBox,
            recommendBtn, reviewBtn, informationBtn;
    ImageButton heart;
    ReviewAdapter adapter;
    ProductSearchAdapter productAdapter;
    List<Review> reviewList;
    List<Product> productList;
    TextView  productPriceView, text1, text2, text3;
    ImageView productImageView, line1, line2, line3;
    String productKey;
    RecyclerView reviewRecycleView, productRecycleView;
    ImageView categoryIconView, plus, minus;
    TextView  categoryNameView, quantity, productNameText, averageRating, itemText, totalView;
    int defaultStatus=1;
    int count=0;
    double average=0;
    int total=0;
    double subtotal=0;
    int statusSize=1;
    String size="small";
    Button purchase;
    View smallBg,mediumBg,largeBg;
    ImageView smallIcon, mediumIcon, largeIcon;
    TextView smallText, mediumText, largeText;
    RelativeLayout checkoutBtn, small, medium,large, box_size;
    ImageView star1, star2, star3, star4, star5, package_box, cartBtn;
    final Handler handler = new Handler();

    private void BindView(){
        back_button=findViewById(R.id.back_btn);
        heart=findViewById(R.id.heart);
//        productNameView=findViewById(R.id.product_details_name);
        productPriceView=findViewById(R.id.product_details_price);
        productImageView=findViewById(R.id.product_details_image);
        categoryIconView=findViewById(R.id.product_details_category_icon);
        plus=findViewById(R.id.plus);
        minus=findViewById(R.id.minus);
        categoryNameView=findViewById(R.id.product_details_category_name);
        quantity=findViewById(R.id.quantity);
        reviewRecycleView=findViewById(R.id.review_recyclerView);
        productRecycleView=findViewById(R.id.product_recyclerView);
        reviewTabBox=findViewById(R.id.reviewTabBox);
        recommendTabBox=findViewById(R.id.recommendTabBox);
        informationTabBox=findViewById(R.id.informationTabBox);
        recommendBtn=findViewById(R.id.recommendBtn);
        reviewBtn=findViewById(R.id.reviewBtn);
        informationBtn=findViewById(R.id.informationBtn);
        text1=findViewById(R.id.text1);
        text2=findViewById(R.id.text2);
        text3=findViewById(R.id.text3);
        line1=findViewById(R.id.line1);
        line2=findViewById(R.id.line2);
        line3=findViewById(R.id.line3);
        productNameText=findViewById(R.id.productNameText);
        averageRating=findViewById(R.id.averageRating);
        star1=findViewById(R.id.star1);
        star2=findViewById(R.id.star2);
        star3=findViewById(R.id.star3);
        star4=findViewById(R.id.star4);
        star5=findViewById(R.id.star5);
        purchase=findViewById(R.id.purchase);
        checkoutBtn=findViewById(R.id.checkoutBtn);
        itemText=findViewById(R.id.itemText);
        package_box=findViewById(R.id.package_box);

        box_size=findViewById(R.id.box_size);
        small=findViewById(R.id.small);
        medium=findViewById(R.id.medium);
        large=findViewById(R.id.large);
        smallBg=findViewById(R.id.smallBg);
        mediumBg=findViewById(R.id.mediumBg);
        largeBg=findViewById(R.id.largeBg);
        smallIcon=findViewById(R.id.smallIcon);
        mediumIcon=findViewById(R.id.mediumIcon);
        largeIcon=findViewById(R.id.largeIcon);
        smallText=findViewById(R.id.smallText);
        mediumText=findViewById(R.id.mediumText);
        largeText=findViewById(R.id.largeText);

        cartBtn=findViewById(R.id.cartBtn);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details2);

        productKey=getIntent().getStringExtra("productKey");
        getProductDetails(productKey);

        Window window = getWindow();
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.white));

        BindView();
        ImageButton add=(ImageButton) findViewById(R.id.add);
        productRecycleView.setLayoutManager(new GridLayoutManager(this,1));
        productRecycleView.addItemDecoration(new GridLayoutDecoration(5,10));
        reviewRecycleView.setLayoutManager(new GridLayoutManager(this,1));
        reviewRecycleView.addItemDecoration(new GridLayoutDecoration(5,10));
        checkTabStatus();
        validateFavouriteProduct(productKey,heart);
        getProductData();
        getReview();
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                Toast.makeText(ProductDetailActivity.this,"Add successfully!",Toast.LENGTH_SHORT).show();
//                getNumberOfItems();
//                package_box.setVisibility(View.VISIBLE);
//                checkoutBtn.setVisibility(View.VISIBLE);
//                getTotal();
//                Animation slideUpAnimation = AnimationUtils.loadAnimation(ProductDetailActivity.this, R.anim.slide_up);
//                checkoutBtn.setAnimation(slideUpAnimation);
//                package_box.setAnimation(slideUpAnimation);
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        package_box.setBackgroundResource(R.drawable.paper_bag_packed);
//                    }
//                },700);
            }
        });
        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String totalString=totalView.getText().toString();
                Intent intent=new Intent(ProductDetailActivity.this, DeliveryActivity.class);
                intent.putExtra("total",totalString);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
        recommendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultStatus=1;
                checkTabStatus();
            }
        });
        reviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultStatus=2;
                checkTabStatus();
            }
        });
        informationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultStatus=3;
                checkTabStatus();
            }
        });
        small.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusSize=1;
                size="small";
                changeSizeStatus();
            }
        });
        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusSize=2;
                size="medium";
                changeSizeStatus();
            }
        });
        large.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusSize=3;
                size="large";
                changeSizeStatus();
            }
        });
        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProductDetailActivity.this, MainActivity.class);
                intent.putExtra("fragmentToLoad","CartFragment");
                startActivity(intent);
            }
        });
    }

    private void changeSizeStatus(){
        switch (statusSize){
            case 1:
                smallBg.setBackgroundResource(R.drawable.rectangle_11_shape);
                mediumBg.setBackgroundResource(R.drawable._bg__white);
                largeBg.setBackgroundResource(R.drawable._bg__white);
                smallText.setTextColor(getColor(R.color.white));
                mediumText.setTextColor(getColor(R.color.black));
                largeText.setTextColor(getColor(R.color.black));
                smallIcon.setBackground(getResources().getDrawable(R.drawable.cup));
                mediumIcon.setBackground(getResources().getDrawable(R.drawable.cup_2));
                largeIcon.setBackground(getResources().getDrawable(R.drawable.cup_2));
                break;
            case 2:
                smallBg.setBackgroundResource(R.drawable._bg__white);
                mediumBg.setBackgroundResource(R.drawable.rectangle_11_shape);
                largeBg.setBackgroundResource(R.drawable._bg__white);
                smallText.setTextColor(getColor(R.color.black));
                mediumText.setTextColor(getColor(R.color.white));
                largeText.setTextColor(getColor(R.color.black));
                smallIcon.setBackground(getResources().getDrawable(R.drawable.cup_2));
                mediumIcon.setBackground(getResources().getDrawable(R.drawable.cup));
                largeIcon.setBackground(getResources().getDrawable(R.drawable.cup_2));
                break;
            case 3:
                smallBg.setBackgroundResource(R.drawable._bg__white);
                mediumBg.setBackgroundResource(R.drawable._bg__white);
                largeBg.setBackgroundResource(R.drawable.rectangle_11_shape);
                smallText.setTextColor(getColor(R.color.black));
                mediumText.setTextColor(getColor(R.color.black));
                largeText.setTextColor(getColor(R.color.white));
                smallIcon.setBackground(getResources().getDrawable(R.drawable.cup_2));
                mediumIcon.setBackground(getResources().getDrawable(R.drawable.cup_2));
                largeIcon.setBackground(getResources().getDrawable(R.drawable.cup));
                break;
        }
    }
    private void checkTabStatus(){
        switch (defaultStatus){
            case 1:
                text1.setTextColor(getColor(R.color.black));
                text2.setTextColor(getColor(R.color.gray1));
                text3.setTextColor(getColor(R.color.gray1));
                line1.setBackgroundColor(getColor(R.color.burgundy));
                line2.setBackgroundColor(getColor(R.color.white));
                line3.setBackgroundColor(getColor(R.color.white));
                recommendTabBox.setVisibility(View.VISIBLE);
                reviewTabBox.setVisibility(View.GONE);
                informationTabBox.setVisibility(View.GONE);
                break;
            case 2:
                text1.setTextColor(getColor(R.color.gray1));
                text2.setTextColor(getColor(R.color.black));
                text3.setTextColor(getColor(R.color.gray1));
                line1.setBackgroundColor(getColor(R.color.white));
                line2.setBackgroundColor(getColor(R.color.burgundy));
                line3.setBackgroundColor(getColor(R.color.white));
                recommendTabBox.setVisibility(View.GONE);
                reviewTabBox.setVisibility(View.VISIBLE);
                informationTabBox.setVisibility(View.GONE);
                break;
            case 3:
                text1.setTextColor(getColor(R.color.gray1));
                text2.setTextColor(getColor(R.color.gray1));
                text3.setTextColor(getColor(R.color.black));
                line1.setBackgroundColor(getColor(R.color.white));
                line2.setBackgroundColor(getColor(R.color.white));
                line3.setBackgroundColor(getColor(R.color.burgundy));
                recommendTabBox.setVisibility(View.GONE);
                reviewTabBox.setVisibility(View.GONE);
                informationTabBox.setVisibility(View.VISIBLE);
                break;
        }
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
                            heart.setBackgroundResource(R.drawable.heart_3);
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
                    heart.setBackgroundResource(R.drawable.heart);
                }else{
                    insertToFavourite(productKey);
//                    Toast.makeText(ProductDetailActivity.this,"insert",Toast.LENGTH_SHORT).show();
                    heart.setBackgroundResource(R.drawable.heart_3);
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
                    String productImage=snapshot.child("img").getValue(String.class);
                    String categoryId=snapshot.child("categoryId").getValue(String.class);
                    String categoryName=snapshot.child("categoryName").getValue(String.class);
                    if(categoryName.equals("Drinks")){
                        box_size.setVisibility(View.VISIBLE);
                    }

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
                    productNameText.setText(productName);
//                    productNameView.setText(productName);
                    productPriceView.setText(String.format("%.0f",productPrice));
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
        SharedPreferences sharedPreferences =getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
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
                        String categoryName=categoryNameView.getText().toString();
                        if(!categoryName.equals("Drinks")){
                            size="";
                        }
                        for(DataSnapshot snap: snapshot.getChildren()){
                            if(snap.child("customerId").getValue(String.class).equals(userKey)){
                                founUser=true;
                                if(snap.child("productQuantityList").exists()){
                                    DataSnapshot productIdObject=snap.child("productQuantityList");
                                    if(productIdObject.getValue() instanceof List){
                                        List<ProductQuantity> tempProductIdList = new ArrayList<>();
                                        int qt=Integer.parseInt(quantity.getText().toString());
                                        for(DataSnapshot productSnap: productIdObject.getChildren()){
                                            ProductQuantity productQuantity=productSnap.getValue(ProductQuantity.class);
                                            tempProductIdList.add(productQuantity);
                                        }
                                        productIdList=tempProductIdList;
                                        for(ProductQuantity pd:productIdList){
                                            if(pd.getProductId().equals(productKey) && pd.getSize().equals(size)){
                                                found=true;
                                                qt=pd.getQuantity()+qt;
                                                pd.setQuantity(qt);
                                                break;
                                            }
                                        }
                                        if(!found){
                                            productIdList.add(new ProductQuantity(productKey,qt,0,size));
                                        }

                                        myRef.child(snap.getKey()).child("productQuantityList").setValue(productIdList);
//                                        Toast.makeText(ProductDetailActivity.this,"Add to cart successfully", Toast.LENGTH_SHORT).show();
//                                        showStatusDialog();
                                        break;
                                    }
                                } else{
                                    int qt=Integer.parseInt(quantity.getText().toString());
                                    productIdList.add(new ProductQuantity(productKey,qt,0,size));
                                    myRef.child(snap.getKey()).child("productQuantityList").setValue(productIdList);
//                                    Toast.makeText(ProductDetailActivity.this,"Add to cart successfully", Toast.LENGTH_SHORT).show();
//                                    showStatusDialog();
                                    break;
                                }
                            }
                        }
                        if(!founUser){
                            int qt=Integer.parseInt(quantity.getText().toString());
                            productIdList.add(new ProductQuantity(productKey, qt,0,size));
                            Cart cart=new Cart(userKey,productIdList);
                            myRef.push().setValue(cart);
//                            Toast.makeText(ProductDetailActivity.this,"Add to cart successfully", Toast.LENGTH_SHORT).show();
//                            showStatusDialog();
                        }
                    }else{
                        int qt=Integer.parseInt(quantity.getText().toString());
                        productIdList.add(new ProductQuantity(productKey, qt,0,size));
                        Cart cart=new Cart(userKey,productIdList);
                        myRef.push().setValue(cart);
//                        Toast.makeText(ProductDetailActivity.this,"Add to cart successfully", Toast.LENGTH_SHORT).show();
//                        showStatusDialog();
                        return;
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    private void getReview(){
        reviewList=new ArrayList<>();
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        Query myRef=firebaseDatabase.getReference("review");
        DatabaseReference userRef=firebaseDatabase.getReference("user");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap:snapshot.getChildren()){
                    String productId=snap.child("productId").getValue(String.class);
                    if(productId.equals(productKey)){
                        String customerId=snap.child("customerId").getValue(String.class);
                        int rating=snap.child("rating").getValue(int.class);
                        String comment=snap.child("comment").getValue(String.class);
                        Date date=snap.child("date").getValue(Date.class);
                        userRef.child(customerId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                if(snapshot1.exists()){
                                    String first_name=snapshot1.child("first_name").getValue(String.class);
                                    String last_name=snapshot1.child("last_name").getValue(String.class);
                                    String image=snapshot1.child("avatar_img").getValue(String.class);
                                    Review review=new Review(customerId,productKey,rating,comment,date,last_name+" "+first_name,image);
                                    reviewList.add(review);

                                    count=count+1;
                                    total=total+rating;
                                    average=(double)total/(double)count;
                                    averageRating.setText(" ("+String.format("%.1f",average)+") - " + String.valueOf(count)+" Reviews");
                                    changeStar(average);

                                    if(reviewList.size()>0){
                                        List<Review> limitedReviewList = reviewList.subList(0, Math.min(reviewList.size(), 5));
                                        adapter=new ReviewAdapter(limitedReviewList);
                                        reviewRecycleView.setAdapter(adapter);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getProductData(){
        productList=new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference productRef = database.getReference("product");
        DatabaseReference categoryRef = database.getReference("category");
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot productSnapshot) {
                productList.clear();
                for (DataSnapshot product : productSnapshot.getChildren()) {
                    String productKey=product.getKey();
                    String categoryId = product.child("categoryId").getValue(String.class);
                    String productName = product.child("name").getValue(String.class);
                    double productPrice = product.child("price").getValue(double.class);
                    String productImage = product.child("img").getValue(String.class);

                    categoryRef.child(categoryId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot categorySnapshot) {
                            if (categorySnapshot.exists()) {
                                String icon = categorySnapshot.child("img").getValue(String.class);
                                Product productWithIcon = new Product(productName, productPrice,productImage, icon,productKey);
                                productList.add(productWithIcon);

                            }
                            Collections.shuffle(productList);
                            if(productList.size()>0){
                                List<Product> limitedProductList = productList.subList(0, Math.min(productList.size(), 5));
                                productAdapter=new ProductSearchAdapter(limitedProductList);
                                productRecycleView.setAdapter(productAdapter);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Xử lý lỗi nếu có
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });
    }
    private void changeStar(double rating){
        if(rating<1){
            star1.setBackgroundResource(R.drawable.star_yellow_active);
            star2.setBackgroundResource(R.drawable.star_yellow_unactive);
            star3.setBackgroundResource(R.drawable.star_yellow_unactive);
            star4.setBackgroundResource(R.drawable.star_yellow_unactive);
            star5.setBackgroundResource(R.drawable.star_yellow_unactive);
        }else if(rating>= 1 && rating<2){
            star1.setBackgroundResource(R.drawable.star_yellow_active);
            star2.setBackgroundResource(R.drawable.star_yellow_active);
            star3.setBackgroundResource(R.drawable.star_yellow_unactive);
            star4.setBackgroundResource(R.drawable.star_yellow_unactive);
            star5.setBackgroundResource(R.drawable.star_yellow_unactive);
        }else if(rating>=2 && rating<3){
            star1.setBackgroundResource(R.drawable.star_yellow_active);
            star2.setBackgroundResource(R.drawable.star_yellow_active);
            star3.setBackgroundResource(R.drawable.star_yellow_active);
            star4.setBackgroundResource(R.drawable.star_yellow_unactive);
            star5.setBackgroundResource(R.drawable.star_yellow_unactive);
        }else if(rating>=3 && rating<4){
            star1.setBackgroundResource(R.drawable.star_yellow_active);
            star2.setBackgroundResource(R.drawable.star_yellow_active);
            star3.setBackgroundResource(R.drawable.star_yellow_active);
            star4.setBackgroundResource(R.drawable.star_yellow_active);
            star5.setBackgroundResource(R.drawable.star_yellow_unactive);
        }else if(rating>=4){
            star1.setBackgroundResource(R.drawable.star_yellow_active);
            star2.setBackgroundResource(R.drawable.star_yellow_active);
            star3.setBackgroundResource(R.drawable.star_yellow_active);
            star4.setBackgroundResource(R.drawable.star_yellow_active);
            star5.setBackgroundResource(R.drawable.star_yellow_active);
        }
    }
    private void getNumberOfItems(){
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn=sharedPreferences.getBoolean("isLoggedIn",false);
        final String userKey=sharedPreferences.getString("customerKey","");
        if(isLoggedIn && !userKey.isEmpty()){
            FirebaseDatabase firebaseDatabase1=FirebaseDatabase.getInstance();
            Query query=firebaseDatabase1.getReference("cart").orderByChild("customerId").equalTo(userKey);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            DataSnapshot productIdObject=childSnapshot.child("productQuantityList");
                            if(productIdObject.getValue() instanceof List){
                                List<ProductQuantity> tempProductIdList = new ArrayList<>();
                                for(DataSnapshot productSnap: productIdObject.getChildren()){
                                    ProductQuantity productQuantity=productSnap.getValue(ProductQuantity.class);
                                    tempProductIdList.add(productQuantity);
                                }
                                List<ProductQuantity> productKeyList=new ArrayList<>(tempProductIdList);
                                itemText.setText(String.valueOf(productKeyList.size())+" Items");
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
    private void getTotal(){
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn=sharedPreferences.getBoolean("isLoggedIn",false);
        final String userKey=sharedPreferences.getString("customerKey","");
        if(isLoggedIn && !userKey.isEmpty()){
            FirebaseDatabase firebaseDatabase1=FirebaseDatabase.getInstance();
            Query query=firebaseDatabase1.getReference("cart").orderByChild("customerId").equalTo(userKey);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        List<ProductQuantity> tempProductIdList = new ArrayList<>();
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            DataSnapshot productIdObject=childSnapshot.child("productQuantityList");
                            if(productIdObject.getValue() instanceof List){
                                for(DataSnapshot productSnap: productIdObject.getChildren()){
                                    ProductQuantity productQuantity=productSnap.getValue(ProductQuantity.class);
                                    tempProductIdList.add(productQuantity);
                                }
                            }
                        }
                        getTotalFromProductData(tempProductIdList);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    private void getTotalFromProductData(List<ProductQuantity> productKeyList){

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference productRef = firebaseDatabase.getReference("product");
        DatabaseReference categoryRef = firebaseDatabase.getReference("category");
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot productSnapshot) {
                for (DataSnapshot product : productSnapshot.getChildren()) {
//                    String productKey=product.getKey();
                    ProductQuantity productKey=new ProductQuantity(product.getKey(),1);
                    String categoryId = product.child("categoryId").getValue(String.class);
                    String productName = product.child("name").getValue(String.class);
                    double productPrice = product.child("price").getValue(double.class);
                    String productImage = product.child("img").getValue(String.class);

                    categoryRef.child(categoryId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot categorySnapshot) {
                            if (categorySnapshot.exists()) {
                                String icon = categorySnapshot.child("img").getValue(String.class);
                                if(checkKeyInList(productKey,productKeyList)){
                                    int quantity=getQuantity(productKey,productKeyList);
                                    subtotal=subtotal+productPrice*quantity;
                                    Product productWithIcon = new Product(productName, productPrice,productImage, icon,productKey.getProductId(),quantity,productPrice*quantity);
                                    productList.add(productWithIcon);
                                    totalView=findViewById(R.id.totalView);
                                    totalView.setText(String.format("%.0f", subtotal));
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Xử lý lỗi nếu có
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu có
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
    public int getQuantity(ProductQuantity key, List<ProductQuantity> keyList){
        for (ProductQuantity item : keyList) {
            if (item.getProductId().equals(key.getProductId())) {
                return item.getQuantity();
            }
        }
        return 1;
    }
    public void showStatusDialog(){
        final Dialog dialog=new Dialog(ProductDetailActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.success_dialog);

        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_circle_white_30));
        dialog.show();
    }
}