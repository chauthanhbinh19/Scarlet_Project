package com.example.scarlet.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Data.Product;
import com.example.scarlet.R;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartHolderView> {
    private List<Product> productList;
    public CartAdapter(List<Product> productList){

        this.productList=productList;
    }


    @NonNull
    @Override
    public CartHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_adapter_layout_3, parent, false);
        return new CartHolderView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartHolderView holder, int position) {
        holder.bindData(productList.get(position));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
