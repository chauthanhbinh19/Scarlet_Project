package com.example.scarlet.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.scarlet.Adapter.GridLayoutDecoration;
import com.example.scarlet.Adapter.ProductAdapter;
import com.example.scarlet.Adapter.ProductSearchAdapter;
import com.example.scarlet.Data.Product;
import com.example.scarlet.MainActivity;
import com.example.scarlet.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavouriteFragment extends Fragment {
    private EditText editText;
    private MeowBottomNavigation bottomNavigation;
    private ProductSearchAdapter productAdapter;
    private List<Product> productList;
    RecyclerView recyclerView;
    RelativeLayout signinNotification,recycleBox;
    private void BindView(View view){
        recyclerView=view.findViewById(R.id.product_recyclerView);
        signinNotification=view.findViewById(R.id.signInNotification);
        recycleBox=view.findViewById(R.id.recycleBox);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.favourite, container, false);

        BindView(view);
        checkSignInStatus();
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        recyclerView.addItemDecoration(new GridLayoutDecoration(5,15));

        validateUser(view,recyclerView);
        return view;
    }
    private void checkSignInStatus(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn=sharedPreferences.getBoolean("isLoggedIn",false);
        String userKey=sharedPreferences.getString("customerKey","");
        if(!isLoggedIn && userKey.isEmpty()){
            signinNotification.setVisibility(View.VISIBLE);
            recycleBox.setVisibility(View.GONE);
        }else{
            signinNotification.setVisibility(View.GONE);
            recycleBox.setVisibility(View.VISIBLE);
        }
    }
    private void validateUser(View view, RecyclerView recyclerView){
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
                        getFavouriteProductData(userKey,view,recyclerView);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    private void getFavouriteProductData(String userKey, View view, RecyclerView recyclerView){
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        Query query=firebaseDatabase.getReference("favourite").orderByChild("customerId").equalTo(userKey);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    List<String> productKeyList=new ArrayList<>();
                    for(DataSnapshot snap: snapshot.getChildren()){
                        String productKey=snap.child("productId").getValue(String.class);
                        productKeyList.add(productKey);
                    }
                    getProductData(productKeyList,recyclerView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getProductData(List<String> productKeyList,RecyclerView recyclerView){
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
                                if(checkKeyInList(productKey,productKeyList)){
                                    productList.add(productWithIcon);
                                }
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
    public boolean checkKeyInList(String key, List<String> keyList) {
        for (String item : keyList) {
            if (item.equals(key)) {
                return true;
            }
        }
        return false;
    }
}