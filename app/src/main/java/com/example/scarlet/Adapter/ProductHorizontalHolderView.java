package com.example.scarlet.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.scarlet.Data.Cart;
import com.example.scarlet.Data.Product;
import com.example.scarlet.Data.ProductQuantity;
import com.example.scarlet.ProductDetailActivity;
import com.example.scarlet.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProductHorizontalHolderView extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView textView1,textView2, quantityText, sizeText;
    RelativeLayout quantityBox, sizeBox;
    Context context;
    String productKey, size;
    int quantity;

    public ProductHorizontalHolderView(@NonNull View itemView) {
        super(itemView);
        imageView=itemView.findViewById(R.id.product_image);
        textView1=itemView.findViewById(R.id.product_name);
        textView2=itemView.findViewById(R.id.product_price);
        quantityBox=itemView.findViewById(R.id.quantityBox);
        quantityText=itemView.findViewById(R.id.quantity);
        sizeBox=itemView.findViewById(R.id.sizeBox);
        sizeText=itemView.findViewById(R.id.size);
        context=itemView.getContext();
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
    }
    public void bindData(Product product){
        Glide.with(context).load(product.getImg()).into(imageView);
        textView1.setText(product.getName());
        textView2.setText(String.format("%.0f", product.getPrice())+" đ");
        productKey=product.getKey();
        quantity=product.getQuantity();
        size=product.getSize();
        if(quantity>1){
            quantityBox.setVisibility(View.VISIBLE);
            quantityText.setText(String.valueOf(quantity));
        }else{
            quantityBox.setVisibility(View.GONE);
        }
        if(size.equals("small")){
            sizeBox.setVisibility(View.VISIBLE);
            sizeText.setText("S");
        }else if(size.equals("medium")){
            sizeBox.setVisibility(View.VISIBLE);
            sizeText.setText("M");
        }else if(size.equals("large")){
            sizeBox.setVisibility(View.VISIBLE);
            sizeText.setText("L");
        }else{
            sizeBox.setVisibility(View.GONE);
        }
    }
}
