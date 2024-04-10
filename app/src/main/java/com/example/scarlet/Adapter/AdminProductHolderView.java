package com.example.scarlet.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

public class AdminProductHolderView extends RecyclerView.ViewHolder {
    ImageView imageView,imageView2, add_btn;
    TextView textView1,textView2,textView3;
    Context context;
    String productKey;

    public AdminProductHolderView(@NonNull View itemView) {
        super(itemView);
        imageView=itemView.findViewById(R.id.icon);
        textView1=itemView.findViewById(R.id.name_product);
        textView2=itemView.findViewById(R.id.price_product);
        textView3=itemView.findViewById(R.id.point_product);
        context=itemView.getContext();
    }
    public void bindData(Product product){
//        imageView.setImageResource(product.getImg());
        Glide.with(context).load(product.getImg()).into(imageView);
        textView1.setText(product.getName());
        textView2.setText(String.valueOf(product.getPrice()));
        textView3.setText(String.valueOf(product.getPoint()));
        productKey=product.getKey();
    }
}
