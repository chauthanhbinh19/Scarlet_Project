package com.example.scarlet.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Data.Product;
import com.example.scarlet.Fragment.CartFragment;
import com.example.scarlet.Interface.GetStringCallback;
import com.example.scarlet.ProductDetailActivity;
import com.example.scarlet.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartHolderView extends RecyclerView.ViewHolder {
    ImageView imageView,imageView2, delete_btn;
    TextView textView1,textView2,textView3;
    ImageView plus,minus;
    Context context;
    String productKey;
    private GetStringCallback getStringCallback;
    private CartAdapter adapter;
    public CartHolderView linkAdapter(CartAdapter adapter){
        this.adapter=adapter;
        return this;
    }

    public CartHolderView(@NonNull View itemView) {
        super(itemView);
        imageView=itemView.findViewById(R.id.product_image);
        textView1=itemView.findViewById(R.id.product_name);
        textView2=itemView.findViewById(R.id.product_price);
        imageView2=itemView.findViewById(R.id.product_category_icon);
        textView3=itemView.findViewById(R.id.product_quantity_text);
        context=itemView.getContext();
        delete_btn=itemView.findViewById(R.id.delete_btn);
        plus=itemView.findViewById(R.id.plus);
        minus=itemView.findViewById(R.id.minus);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, ProductDetailActivity.class);
                intent.putExtra("productKey",productKey);
                context.startActivity(intent);
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, ProductDetailActivity.class);
                intent.putExtra("productKey",productKey);
                context.startActivity(intent);
            }
        });
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, ProductDetailActivity.class);
                intent.putExtra("productKey",productKey);
                context.startActivity(intent);
            }
        });
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                boolean isLoggedIn=sharedPreferences.getBoolean("isLoggedIn",false);
                String userKey=sharedPreferences.getString("customerKey","");
                if(isLoggedIn && !userKey.isEmpty()){
                    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                    DatabaseReference myRef=firebaseDatabase.getReference("cart");
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            List<String> productIdList=new ArrayList<>();
                            for(DataSnapshot snap: snapshot.getChildren()){
                                if(snap.child("customerId").getValue(String.class).equals(userKey)){
                                    DataSnapshot productQuantityListSnapshot =snap.child("productQuantityList");

                                    for(DataSnapshot snap1:productQuantityListSnapshot.getChildren()){
                                        if(snap1.child("productId").getValue(String.class).equals(productKey)){
                                            adapter.productList.remove(getAdapterPosition());
                                            adapter.notifyDataSetChanged();
                                            snap1.getRef().removeValue();
                                            break;
                                        }
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
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                boolean isLoggedIn=sharedPreferences.getBoolean("isLoggedIn",false);
                String userKey=sharedPreferences.getString("customerKey","");

                if(isLoggedIn && !userKey.isEmpty()){
                    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                    DatabaseReference myRef=firebaseDatabase.getReference("cart");

                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot snapshot1:snapshot.getChildren()){
                                if(snapshot1.child("customerId").getValue(String.class).equals(userKey)){
                                    DataSnapshot productQuantityListSnapshot =snapshot1.child("productQuantityList");

                                    for(DataSnapshot snap:productQuantityListSnapshot.getChildren()){
                                        if(snap.child("productId").getValue(String.class).equals(productKey)){
                                            int quantity=snap.child("quantity").getValue(int.class);
                                            quantity=quantity+1;
                                            adapter.productList.get(getAdapterPosition()).setQuantity(quantity);
                                            double price=adapter.productList.get(getAdapterPosition()).getPrice();
                                            double total=adapter.productList.get(getAdapterPosition()).getTotal();
                                            double newTotal=total+price;
                                            adapter.productList.get(getAdapterPosition()).setTotal(newTotal);
                                            getStringCallback.itemClick(price,0);
                                            adapter.notifyDataSetChanged();
                                            snap.getRef().child("quantity").setValue(quantity);
                                        }
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
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                boolean isLoggedIn=sharedPreferences.getBoolean("isLoggedIn",false);
                String userKey=sharedPreferences.getString("customerKey","");

                if(isLoggedIn && !userKey.isEmpty()){
                    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                    DatabaseReference myRef=firebaseDatabase.getReference("cart");

                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot snapshot1:snapshot.getChildren()){
                                if(snapshot1.child("customerId").getValue(String.class).equals(userKey)){
                                    DataSnapshot productQuantityListSnapshot =snapshot1.child("productQuantityList");

                                    for(DataSnapshot snap:productQuantityListSnapshot.getChildren()){
                                        if(snap.child("productId").getValue(String.class).equals(productKey)){
                                            int quantity=snap.child("quantity").getValue(int.class);
                                            quantity=quantity-1;
                                            adapter.productList.get(getAdapterPosition()).setQuantity(quantity);
                                            double price=adapter.productList.get(getAdapterPosition()).getPrice();
                                            double total=adapter.productList.get(getAdapterPosition()).getTotal();
                                            double newTotal=total-price;
                                            adapter.productList.get(getAdapterPosition()).setTotal(newTotal);
                                            getStringCallback.itemClick(price,1);
                                            adapter.notifyDataSetChanged();
                                            snap.getRef().child("quantity").setValue(quantity);
                                        }
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
        });
    }
    public void bindData(Product product, GetStringCallback getStringCallback1){
        imageView.setImageResource(product.getImg());
        textView1.setText(product.getName());
        textView2.setText(String.valueOf(product.getTotal()));
        textView3.setText(String.valueOf(product.getQuantity()));
        imageView2.setImageResource(product.getIcon());
        productKey=product.getKey();
        this.getStringCallback=getStringCallback1;
    }
}
