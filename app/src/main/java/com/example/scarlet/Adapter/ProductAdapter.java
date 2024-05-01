package com.example.scarlet.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Data.Category;
import com.example.scarlet.Data.Product;
import com.example.scarlet.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductHolderView> {
    private List<Product> productList;
    public ProductAdapter(List<Product> productList){

        this.productList=productList;
    }


    @NonNull
    @Override
    public ProductHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_adapter_layout_1, parent, false);
        return new ProductHolderView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolderView holder, int position) {
        Animation animation= AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);
        holder.bindData(productList.get(position));
        holder.itemView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
