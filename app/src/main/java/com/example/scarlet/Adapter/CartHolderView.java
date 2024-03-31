package com.example.scarlet.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Data.Product;
import com.example.scarlet.ProductDetailActivity;
import com.example.scarlet.R;
import com.example.scarlet.SearchProductActivity;

public class CartHolderView extends RecyclerView.ViewHolder {
    ImageView imageView,imageView2, delete_btn;
    TextView textView1,textView2;
    Context context;
    String productKey;

    public CartHolderView(@NonNull View itemView) {
        super(itemView);
        imageView=itemView.findViewById(R.id.product_image);
        textView1=itemView.findViewById(R.id.product_name);
        textView2=itemView.findViewById(R.id.product_price);
        imageView2=itemView.findViewById(R.id.product_category_icon);
        context=itemView.getContext();
        delete_btn=itemView.findViewById(R.id.delete_btn);
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
                Intent intent= new Intent(context, SearchProductActivity.class);
                context.startActivity(intent);
            }
        });
    }
    public void bindData(Product product){
        imageView.setImageResource(product.getImg());
        textView1.setText(product.getName());
        textView2.setText(String.valueOf(product.getPrice()));
        imageView2.setImageResource(product.getIcon());
        productKey=product.getKey();
    }
}
