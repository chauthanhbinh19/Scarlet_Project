package com.example.scarlet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Adapter.GridLayoutDecoration;
import com.example.scarlet.Adapter.ProductReviewAdapter;
import com.example.scarlet.Adapter.ProductSearchAdapter;
import com.example.scarlet.Data.Payment;
import com.example.scarlet.Data.Product;
import com.example.scarlet.Interface.GetKeyCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class SelectProductActivity extends AppCompatActivity {

    RelativeLayout back_btn;
    private ProductReviewAdapter productAdapter;
    private List<Product> productList;
    RecyclerView recyclerView;
    GetKeyCallback getKeyCallback;
    String keyString;
    EditText search;
    Button chooseBtn;
    private void BindView(){
        back_btn=findViewById(R.id.back_btn);
        recyclerView=findViewById(R.id.product_recyclerView);
        chooseBtn=findViewById(R.id.chooseBtn);
        search=findViewById(R.id.search);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_product);

        Window window = getWindow();
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.white));
        
        BindView();
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        recyclerView.addItemDecoration(new GridLayoutDecoration(5,15));
        getKeyCallback=new GetKeyCallback() {
            @Override
            public void itemClick(String key, int type) {
                keyString=key;
            }
        };
        getProductData(recyclerView, getKeyCallback);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
                Intent intent=new Intent();
                intent.putExtra("key","0");
                setResult(SelectVoucherActivity.RESULT_OK,intent);
                SelectProductActivity.super.onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        chooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("key",keyString);
                setResult(SelectVoucherActivity.RESULT_OK,intent);
                SelectProductActivity.super.onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String keyword=s.toString().trim();
                productAdapter.filterBySearch(keyword);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void getProductData(RecyclerView recyclerView, GetKeyCallback getKeyCallback){
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
                                productAdapter=new ProductReviewAdapter(productList, getKeyCallback);
                                recyclerView.setAdapter(productAdapter);
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