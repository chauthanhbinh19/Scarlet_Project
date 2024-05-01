package com.example.scarlet.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class ProductFragment extends Fragment {
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;
    private List<Category> categoryList;
    private List<Product> productList;
    RelativeLayout back_btn;
    RelativeLayout search;
    TextView category_text;
    RecyclerView categoryRecyclerView, productRecyclerView;
    private void BindView(View view){
        search=view.findViewById(R.id.search_bar);
        categoryRecyclerView= view.findViewById(R.id.category_recyclerView);
        productRecyclerView= view.findViewById(R.id.product_recyclerView_1);
        category_text=view.findViewById(R.id.category_text);
        back_btn=view.findViewById(R.id.back_btn);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.product, container, false);

        BindView(view);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), SearchProductActivity.class);
                startActivity(intent);
            }
        });
        getCategoryData(view);
        getProductData(view);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHomeFragment();
            }
        });
        return view;
    }
    private void openHomeFragment(){
        HomeFragment homeFragment=new HomeFragment();
        FragmentManager fragmentManager=requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,homeFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    private void getCategoryData(View view){
        categoryList=new ArrayList<>();
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        categoryRecyclerView.addItemDecoration(new GridLayoutDecoration(10,5));
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
    private void getProductData(View view){
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference productRef = firebaseDatabase.getReference("product");
        DatabaseReference categoryRef = firebaseDatabase.getReference("category");

        productList=new ArrayList<>();
        productRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        productRecyclerView.addItemDecoration(new GridLayoutDecoration(5,30));

        Bundle args=getArguments();
        if(args !=null){
            String categoryKey= args.getString("categoryKey");
            if(categoryKey!=null){
                productRef.addValueEventListener(new ValueEventListener() {
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
                                        String categoryText=categorySnapshot.child("name_category").getValue(String.class);
                                        category_text.setText(categoryText);
                                        Product productWithIcon = new Product(productName, productPrice,productImage, icon,productKey);
                                        if(categoryKey.equals(categoryId)){
                                            productList.add(productWithIcon);
                                        }
                                    }
                                    if(productList.size()>0){
                                        productAdapter=new ProductAdapter(productList);
                                        productRecyclerView.setAdapter(productAdapter);
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

    }
}