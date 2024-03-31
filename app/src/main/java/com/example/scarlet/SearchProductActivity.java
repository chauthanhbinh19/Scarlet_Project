package com.example.scarlet;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Adapter.GridLayoutDecoration;
import com.example.scarlet.Adapter.ProductAdapter;
import com.example.scarlet.Adapter.ProductSearchAdapter;
import com.example.scarlet.Data.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class SearchProductActivity extends AppCompatActivity {
    private ProductSearchAdapter productAdapter;
    private List<Product> productList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        RecyclerView recyclerView=findViewById(R.id.product_recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        recyclerView.addItemDecoration(new GridLayoutDecoration(5,15));

        getProductData(recyclerView);
        Button back_btn=(Button) findViewById(R.id.cancled_btn);
        EditText search=(EditText) findViewById(R.id.search);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String keyword=s.toString().trim();
                if(keyword==null || keyword.isEmpty()){
                    getProductData(recyclerView);
                }else{
                    searchProducts(keyword, recyclerView);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void getProductData(RecyclerView recyclerView){
        productList=new ArrayList<>();

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
//                            if(productList.size()>0){
//                                productAdapter=new ProductSearchAdapter(productList);
//                                recyclerView.setAdapter(productAdapter);
//                            }
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
    private void searchProducts(String keyword, RecyclerView recyclerView){
        productList=new ArrayList<>();

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
                                productList=filterProduct(productList,keyword);
                            }
                            productAdapter=new ProductSearchAdapter(productList);
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
    private List<Product> filterProduct(List<Product> productList,String keyword){
        List<Product> result=new ArrayList<>();
        for(Product product : productList){
            if(product.getName().toLowerCase().contains(keyword.toLowerCase())){
                result.add(product);
            }
        }
        return result;
    }

}