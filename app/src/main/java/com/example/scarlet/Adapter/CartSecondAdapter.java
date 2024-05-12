package com.example.scarlet.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Data.Product;
import com.example.scarlet.Interface.GetProductCallback;
import com.example.scarlet.Interface.GetStringCallback;
import com.example.scarlet.R;

import java.util.List;

public class CartSecondAdapter extends RecyclerView.Adapter<CartSecondHolderView> {
    public List<Product> productList;
    public GetProductCallback getProductCallback;
    public CartSecondAdapter(List<Product> productList, GetProductCallback getProductCallback){

        this.productList=productList;
        this.getProductCallback=getProductCallback;
    }
    @NonNull
    @Override
    public CartSecondHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_adapter_layout_3, parent, false);
        return new CartSecondHolderView(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull CartSecondHolderView holder, int position) {
        Animation animation= AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);
        holder.bindData(productList.get(position),getProductCallback);
//        holder.itemView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}

