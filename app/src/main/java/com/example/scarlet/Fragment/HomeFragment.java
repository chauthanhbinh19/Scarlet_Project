package com.example.scarlet.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.scarlet.Adapter.CategoryAdapter;
import com.example.scarlet.Adapter.GridLayoutDecoration;
import com.example.scarlet.Adapter.ProductAdapter;
import com.example.scarlet.Data.Category;
import com.example.scarlet.Data.Product;
import com.example.scarlet.R;
import com.example.scarlet.SearchProductActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;
    private List<Category> categoryList;
    private List<Product> productList;
    RelativeLayout search;
    RecyclerView categoryRecyclerView,trendRecyclerView, ProductRecyclerView;
    TextView welcomeMessage;
    private void BindView(View view){
        search=view.findViewById(R.id.search_bar);
        welcomeMessage=view.findViewById(R.id.welcome_message);
        categoryRecyclerView= view.findViewById(R.id.category_recyclerView);
        trendRecyclerView=view.findViewById(R.id.trend_recyclerView);
        ProductRecyclerView=view.findViewById(R.id.product_recyclerView);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.home, container, false);
        BindView(view);
        validateUser(view);
        getCategoryData(view);
        getTrendData(view);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), SearchProductActivity.class);
                startActivity(intent);
            }
        });
//        getProductData(view);
        return view;
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
                    int categoryImage=snap.child("img").getValue(int.class);
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
        trendRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        trendRecyclerView.addItemDecoration(new GridLayoutDecoration(5,25));
        productList.add(new Product("Lychee Coconut Pudding","","7","Pudding",48000,100,R.drawable.banana_chocolate));
        productList.add(new Product("Lychee Coconut Pudding","","7","Pudding",148000,100,R.drawable.banana_peanut));

        productAdapter=new ProductAdapter(productList);
        trendRecyclerView.setAdapter(productAdapter);

    }
    private void getProductData(View view){
        productList=new ArrayList<>();
        ProductRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        ProductRecyclerView.addItemDecoration(new GridLayoutDecoration(5,25));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference productRef = database.getReference("product");
        DatabaseReference categoryRef = database.getReference("category");
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot productSnapshot) {
                for (DataSnapshot product : productSnapshot.getChildren()) {
                    String productKey=product.getKey();
                    String categoryId = product.child("categoryId").getValue(String.class);
                    String productName = product.child("name").getValue(String.class);
                    double productPrice = product.child("price").getValue(double.class);
                    int productImage = product.child("img").getValue(int.class);

                    categoryRef.child(categoryId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot categorySnapshot) {
                            if (categorySnapshot.exists()) {
                                int icon = categorySnapshot.child("img").getValue(int.class);
                                Product productWithIcon = new Product(productName, productPrice,productImage, icon,productKey);
                                productList.add(productWithIcon);

                            }
                            if(productList.size()>0){
                                productAdapter=new ProductAdapter(productList);
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