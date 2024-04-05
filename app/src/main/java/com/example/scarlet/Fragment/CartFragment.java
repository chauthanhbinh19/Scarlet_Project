package com.example.scarlet.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Adapter.CartAdapter;
import com.example.scarlet.Adapter.CartHolderView;
import com.example.scarlet.Adapter.GridLayoutDecoration;
import com.example.scarlet.Data.Product;
import com.example.scarlet.Data.ProductQuantity;
import com.example.scarlet.Interface.GetStringCallback;
import com.example.scarlet.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {

    FirebaseDatabase firebaseDatabase;
    Query query;
    private List<Product> productList;
    private CartAdapter productAdapter;
    private RecyclerView recyclerView;
    private View view;
    TextView totalView;
    private double total=0;
    GetStringCallback getStringCallback;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.cart, container, false);

        productList=new ArrayList<>();
        recyclerView=view.findViewById(R.id.product_recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        recyclerView.addItemDecoration(new GridLayoutDecoration(5,15));


        totalView=view.findViewById(R.id.total);

        getStringCallback=new GetStringCallback() {
            @Override
            public void itemClick(double price, int type) {
                if(type==0){
                    double oldTotal=Double.parseDouble(totalView.getText().toString());
                    double newTotal=oldTotal+price;
                    totalView.setText(String.valueOf(newTotal));
                }
                if(type==1){
                    double oldTotal=Double.parseDouble(totalView.getText().toString());
                    double newTotal=oldTotal-price;
                    totalView.setText(String.valueOf(newTotal));
                }
            }
        };
        validateUser(recyclerView,getStringCallback);
        return view;
    }
    private void validateUser(final RecyclerView recyclerView, GetStringCallback getStringCallback1){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn=sharedPreferences.getBoolean("isLoggedIn",false);
        final String userKey=sharedPreferences.getString("customerKey","");
        if(isLoggedIn && !userKey.isEmpty()){
            firebaseDatabase=FirebaseDatabase.getInstance();
            DatabaseReference myRef=firebaseDatabase.getReference("user").child(userKey);
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        getCartData(userKey,recyclerView,getStringCallback1);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    private void getCartData(String userKey,  RecyclerView recyclerView, GetStringCallback getStringCallback1){
        firebaseDatabase=FirebaseDatabase.getInstance();
        query=firebaseDatabase.getReference("cart").orderByChild("customerId").equalTo(userKey);
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
                            List<ProductQuantity> productKeyList =tempProductIdList;
                            getProductData(productKeyList, recyclerView,getStringCallback1);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getProductData(List<ProductQuantity> productKeyList,RecyclerView recyclerView, GetStringCallback getStringCallback1){

        firebaseDatabase = FirebaseDatabase.getInstance();
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
                    int productImage = product.child("img").getValue(int.class);

                    categoryRef.child(categoryId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot categorySnapshot) {
                            if (categorySnapshot.exists()) {
                                int icon = categorySnapshot.child("img").getValue(int.class);
                                if(checkKeyInList(productKey,productKeyList)){
                                    int quantity=getQuantity(productKey,productKeyList);
                                    total=total+productPrice*quantity;
                                    Product productWithIcon = new Product(productName, productPrice,productImage, icon,productKey.getProductId(),quantity,productPrice*quantity);
                                    productList.add(productWithIcon);
                                    totalView=view.findViewById(R.id.total);
                                    totalView.setText(String.valueOf(total));
                                }
                            }
                            productAdapter=new CartAdapter(productList,getStringCallback1);
                            recyclerView.setAdapter(productAdapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Xử lý lỗi nếu có
                        }
                    });
                }
                List<Product> cc=productList;
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

}