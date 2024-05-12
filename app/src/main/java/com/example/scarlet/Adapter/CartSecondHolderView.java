package com.example.scarlet.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.scarlet.Data.Product;
import com.example.scarlet.Interface.GetProductCallback;
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

public class CartSecondHolderView extends RecyclerView.ViewHolder {
    ImageView imageView,imageView2, delete_btn;
    TextView textView1,textView2,textView3, sizeText;
    ImageView plus,minus;
    Context context;
    String productKey, size;
    int quantity;
    RelativeLayout sizeBox;
    private GetProductCallback getProductCallback;
    private CartSecondAdapter adapter;
    public CartSecondHolderView linkAdapter(CartSecondAdapter adapter){
        this.adapter=adapter;
        return this;
    }

    public CartSecondHolderView(@NonNull View itemView) {
        super(itemView);
        imageView=itemView.findViewById(R.id.product_image);
        textView1=itemView.findViewById(R.id.product_name);
        textView2=itemView.findViewById(R.id.product_price);
        imageView2=itemView.findViewById(R.id.product_category_icon);
        textView3=itemView.findViewById(R.id.product_quantity_text);
        sizeBox=itemView.findViewById(R.id.sizeBox);
        sizeText=itemView.findViewById(R.id.size);
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
                double total=adapter.productList.get(getAdapterPosition()).getTotal();
                getProductCallback.itemClick(productKey,size,total,1);
                adapter.productList.remove(getAdapterPosition());
                adapter.notifyDataSetChanged();
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity=quantity+1;
                adapter.productList.get(getAdapterPosition()).setQuantity(quantity);
                double price=adapter.productList.get(getAdapterPosition()).getPrice();
                double total=adapter.productList.get(getAdapterPosition()).getTotal();
                double newTotal=total+price;
                adapter.productList.get(getAdapterPosition()).setTotal(newTotal);
                getProductCallback.itemClick(productKey,size,price,0);
                adapter.notifyDataSetChanged();
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity=quantity-1;
                adapter.productList.get(getAdapterPosition()).setQuantity(quantity);
                double price=adapter.productList.get(getAdapterPosition()).getPrice();
                double total=adapter.productList.get(getAdapterPosition()).getTotal();
                double newTotal=total-price;
                adapter.productList.get(getAdapterPosition()).setTotal(newTotal);
                getProductCallback.itemClick(productKey,size,price,1);
                adapter.notifyDataSetChanged();
            }
        });
    }
    public void bindData(Product product, GetProductCallback getProductCallback){
//        imageView.setImageResource(product.getImg());
        textView1.setText(product.getName());
        textView2.setText(String.format("%.0f", product.getTotal())+" đ");
        textView3.setText(String.valueOf(product.getQuantity()));
        Glide.with(context).load(product.getIcon()).into(imageView2);
        Glide.with(context).load(product.getImg()).into(imageView);
        productKey=product.getKey();
        this.getProductCallback=getProductCallback;
        quantity=product.getQuantity();
        size=product.getSize();
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
