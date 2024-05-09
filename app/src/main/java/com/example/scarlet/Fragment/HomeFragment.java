package com.example.scarlet.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.scarlet.Adapter.CategoryAdapter;
import com.example.scarlet.Adapter.GridLayoutDecoration;
import com.example.scarlet.Adapter.ProductAdapter;
import com.example.scarlet.Adapter.TrendAdapter;
import com.example.scarlet.Data.Category;
import com.example.scarlet.Data.DealTransaction;
import com.example.scarlet.Data.Product;
import com.example.scarlet.R;
import com.example.scarlet.SearchProductActivity;
import com.example.scarlet.WalkthroughActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HomeFragment extends Fragment {
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;
    private TrendAdapter trendAdapter;
    private List<Category> categoryList;
    private List<Product> productList;
    RelativeLayout search, notification;
    RecyclerView categoryRecyclerView,trendRecyclerView, ProductRecyclerView;
    TextView welcomeMessage, menu, trends;
    final Handler handler = new Handler();
    int delay=150;
    private void BindView(View view){
        search=view.findViewById(R.id.search_bar);
        welcomeMessage=view.findViewById(R.id.welcome_message);
        categoryRecyclerView= view.findViewById(R.id.category_recyclerView);
        trendRecyclerView=view.findViewById(R.id.trend_recyclerView);
        ProductRecyclerView=view.findViewById(R.id.product_recyclerView);
        menu=view.findViewById(R.id.menu);
        trends=view.findViewById(R.id.trends);
        notification=view.findViewById(R.id.notification);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.home, container, false);
        BindView(view);
        validateUser(view);
        getCategoryData(view);
        getTrendData(view);
        getAnimation();
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), SearchProductActivity.class);
                startActivity(intent);
            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), WalkthroughActivity.class);
                startActivity(intent);
            }
        });
        getProductData(view);
        return view;
    }
    private void getAnimation(){
        Animation menuAnim= AnimationUtils.loadAnimation(menu.getContext(),android.R.anim.fade_in);
        Animation trendsAnim= AnimationUtils.loadAnimation(trends.getContext(),android.R.anim.fade_in);
        Animation categoryRecyclerViewAnim= AnimationUtils.loadAnimation(categoryRecyclerView.getContext(),android.R.anim.slide_in_left);
        Animation trendRecyclerViewAnim= AnimationUtils.loadAnimation(trendRecyclerView.getContext(),android.R.anim.slide_in_left);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                menu.startAnimation(menuAnim);
            }
        },delay*0);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                categoryRecyclerView.startAnimation(categoryRecyclerViewAnim);
            }
        },delay*1);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                trends.startAnimation(trendsAnim);
            }
        },delay*2);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                trendRecyclerView.startAnimation(trendRecyclerViewAnim);
            }
        },delay*3);
    }
    private void validateUser(View view){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn=sharedPreferences.getBoolean("isLoggedIn",false);
        String userKey=sharedPreferences.getString("customerKey","");
        if(isLoggedIn && !userKey.isEmpty()){
            FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
            DatabaseReference myRef=firebaseDatabase.getReference("user").child(userKey);
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        String firstname=snapshot.child("first_name").getValue(String.class);
                        String lastname=snapshot.child("last_name").getValue(String.class);
                        welcomeMessage.setText("Hi "+ lastname+" "+ firstname);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    private void getCategoryData(View view){
        categoryList=new ArrayList<>();
        categoryRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),5));
        categoryRecyclerView.addItemDecoration(new GridLayoutDecoration(15,0));
        Query query = FirebaseDatabase.getInstance().getReference("category");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryList.clear();
                for(DataSnapshot snap:snapshot.getChildren()){
                    String categoryName=snap.child("name_category").getValue(String.class);
                    String categoryImage=snap.child("img").getValue(String.class);
                    String categoryKey=snap.getKey();
                    Category category=new Category(categoryName,categoryImage,categoryKey);
                    categoryList.add(category);
                }
                if(categoryList.size()>0){
                    categoryAdapter=new CategoryAdapter(categoryList);
                    categoryRecyclerView.setAdapter(categoryAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getTrendData(View view){
        productList=new ArrayList<>();
        trendRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference= firebaseDatabase.getReference("product");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap:snapshot.getChildren()){
                    String productKey=snap.getKey();
                    String categoryId = snap.child("categoryId").getValue(String.class);
                    String productName = snap.child("name").getValue(String.class);
                    double productPrice = snap.child("price").getValue(double.class);
                    String productImage = snap.child("img").getValue(String.class);
                    Product product = new Product(productName, productPrice,productImage, "",productKey);
                    productList.add(product);
                }

                List<Product> randomProducts=new ArrayList<>();
                Random random = new Random();
                int totalProducts=productList.size();
                int numProductsToGet=10;
                for (int i = 0; i < numProductsToGet; i++) {
//                    int randomIndex = random.nextInt(totalProducts);
                    Product randomProduct = productList.get(i);
                    randomProducts.add(randomProduct);
                }
                trendAdapter=new TrendAdapter(randomProducts);
                trendRecyclerView.setAdapter(trendAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getProductData(View view){
        productList=new ArrayList<>();
        ProductRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        ProductRecyclerView.addItemDecoration(new GridLayoutDecoration(5,40));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query productRef = database.getReference("product");
        DatabaseReference categoryRef = database.getReference("category");
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot productSnapshot) {
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
                            if(productList.size()>0){
                                List<Product> tempList=productList.subList(0,Math.min(productList.size(),6));
                                productAdapter=new ProductAdapter(tempList);
                                ProductRecyclerView.setAdapter(productAdapter);
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
}