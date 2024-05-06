package com.example.scarlet.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.scarlet.Data.Cart;
import com.example.scarlet.Data.Product;
import com.example.scarlet.Data.ProductQuantity;
import com.example.scarlet.MemberShipActivity;
import com.example.scarlet.ProductDetailActivity;
import com.example.scarlet.R;
import com.example.scarlet.SearchProductActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductHolderView extends RecyclerView.ViewHolder {
    ImageView imageView,imageView2, add_btn;
    TextView textView1,textView2;
    Context context;
    String productKey;

    public ProductHolderView(@NonNull View itemView) {
        super(itemView);
        imageView=itemView.findViewById(R.id.product_image);
        textView1=itemView.findViewById(R.id.product_name);
        textView2=itemView.findViewById(R.id.product_price);
        imageView2=itemView.findViewById(R.id.product_category_icon);
        context=itemView.getContext();
        add_btn=itemView.findViewById(R.id.add_btn);
        DatabaseReference c=FirebaseDatabase.getInstance().getReference("customers");
        imageView.setOnClickListener(new View.OnClickListener() {
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
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, ProductDetailActivity.class);
                intent.putExtra("productKey",productKey);
                context.startActivity(intent);
            }
        });
        add_btn.setOnClickListener(new View.OnClickListener() {
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
                            List<ProductQuantity> productIdList=new ArrayList<>();
                            if (snapshot.exists()) {
                                boolean found=false;
                                boolean founUser=false;
                                for(DataSnapshot snap: snapshot.getChildren()){
                                    if(snap.child("customerId").getValue(String.class).equals(userKey)){
                                        founUser=true;
                                        if(snap.child("productQuantityList").exists()){
                                            DataSnapshot productIdObject=snap.child("productQuantityList");
                                            if(productIdObject.getValue() instanceof List){
                                                List<ProductQuantity> tempProductIdList = new ArrayList<>();
                                                int quantity=1;
                                                for(DataSnapshot productSnap: productIdObject.getChildren()){
                                                    ProductQuantity productQuantity=productSnap.getValue(ProductQuantity.class);
                                                    tempProductIdList.add(productQuantity);
                                                }
                                                productIdList=tempProductIdList;
                                                for(ProductQuantity pd:productIdList){
                                                    if(pd.getProductId().equals(productKey)){
                                                        quantity=pd.getQuantity()+1;
                                                        pd.setQuantity(quantity);
                                                        found=true;
                                                        break;
                                                    }
                                                }
                                                if(!found){
                                                    productIdList.add(new ProductQuantity(productKey,quantity,0));
                                                }

                                                myRef.child(snap.getKey()).child("productQuantityList").setValue(productIdList);
                                                Toast.makeText(context,"Add to cart successfully", Toast.LENGTH_SHORT).show();
                                                break;
                                            }
                                        } else{
                                            productIdList.add(new ProductQuantity(productKey,1,0));
                                            myRef.child(snap.getKey()).child("productQuantityList").setValue(productIdList);
                                            Toast.makeText(context,"Add to cart successfully", Toast.LENGTH_SHORT).show();
                                            break;
                                        }
                                    }
                                }
                                if(!founUser){
                                    productIdList.add(new ProductQuantity(productKey, 1,0));
                                    Cart cart=new Cart(userKey,productIdList);
                                    myRef.push().setValue(cart);
                                    Toast.makeText(context,"Add to cart successfully", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                productIdList.add(new ProductQuantity(productKey, 1,0));
                                Cart cart=new Cart(userKey,productIdList);
                                myRef.push().setValue(cart);
                                Toast.makeText(context,"Add to cart successfully", Toast.LENGTH_SHORT).show();
                                return;
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
    public void bindData(Product product){
//        imageView.setImageResource(product.getImg());
        Glide.with(context).load(product.getImg()).into(imageView);
        textView1.setText(product.getName());
        textView2.setText(String.format("%.0f", product.getPrice())+" đ");
        Glide.with(context).load(product.getIcon()).into(imageView2);
        productKey=product.getKey();
    }
}
