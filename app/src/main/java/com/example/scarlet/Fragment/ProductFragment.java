package com.example.scarlet.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.product, container, false);

        RelativeLayout search=view.findViewById(R.id.search_bar);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), SearchProductActivity.class);
                startActivity(intent);
            }
        });
        createCategory(view);
        getProductData(view);
        return view;
    }
    private void createCategory(View view){
        RecyclerView recyclerView= view.findViewById(R.id.category_recyclerView);
        categoryList=new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView.addItemDecoration(new GridLayoutDecoration(10,5));
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
                categoryAdapter=new CategoryAdapter(categoryList);
                recyclerView.setAdapter(categoryAdapter);
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

        RecyclerView recyclerView= view.findViewById(R.id.product_recyclerView_1);
        productList=new ArrayList<>();
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.addItemDecoration(new GridLayoutDecoration(5,30));

        Bundle args=getArguments();
        if(args !=null){
            String categoryKey= args.getString("categoryKey");
            if(categoryKey!=null){
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
                                        if(categoryKey.equals(categoryId)){
                                            productList.add(productWithIcon);
                                        }
                                    }
                                    productAdapter=new ProductAdapter(productList);
                                    recyclerView.setAdapter(productAdapter);
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