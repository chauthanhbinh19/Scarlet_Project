package com.example.scarlet.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Data.Product;
import com.example.scarlet.Interface.GetStringCallback;
import com.example.scarlet.R;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartHolderView> {
    public List<Product> productList;
    public GetStringCallback getStringCallback;
    public CartAdapter(List<Product> productList, GetStringCallback getStringCallback1){

        this.productList=productList;
        this.getStringCallback=getStringCallback1;
    }
    @NonNull
    @Override
    public CartHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_adapter_layout_3, parent, false);
        return new CartHolderView(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull CartHolderView holder, int position) {
        Animation animation= AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);
        holder.bindData(productList.get(position),getStringCallback);
//        holder.itemView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}

