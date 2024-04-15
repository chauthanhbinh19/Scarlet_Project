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
import java.util.Random;

public class TrendHolderView extends RecyclerView.ViewHolder {
    ImageView imageView, imageView1;
    TextView textView1;
    Context context;
    String productKey;
    int[] backgroundImage={R.drawable.rectangle_gradient_blue,R.drawable.rectangle_gradient_cyan,R.drawable.rectangle_gradient_green,R.drawable.rectangle_gradient_purple,R.drawable.rectangle_gradient_red,R.drawable.rectangle_gradient_yellow, R.drawable.rectangle_gradient_curiosity_blue, R.drawable.rectangle_gradient_piglet,R.drawable.rectangle_gradient_vine};

    public TrendHolderView(@NonNull View itemView) {
        super(itemView);
        imageView=itemView.findViewById(R.id.productImage);
        imageView1=itemView.findViewById(R.id.productBackground);
        textView1=itemView.findViewById(R.id.productName);
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
    }
    public void bindData(Product product){
        Random random=new Random();
        int randomArray=backgroundImage[random.nextInt(backgroundImage.length)];
        imageView1.setImageResource(randomArray);
        Glide.with(context).load(product.getImg()).into(imageView);
        textView1.setText(product.getName());
        productKey=product.getKey();
    }
}
