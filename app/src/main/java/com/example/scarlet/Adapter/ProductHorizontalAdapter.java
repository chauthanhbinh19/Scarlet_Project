package com.example.scarlet.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Data.Product;
import com.example.scarlet.R;

import java.util.List;

public class ProductHorizontalAdapter extends RecyclerView.Adapter<ProductHorizontalHolderView> {
    private List<Product> productList;
    public ProductHorizontalAdapter(List<Product> productList){

        this.productList=productList;
    }


    @NonNull
    @Override
    public ProductHorizontalHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_adapter_layout_4, parent, false);
        return new ProductHorizontalHolderView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHorizontalHolderView holder, int position) {
        holder.bindData(productList.get(position));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
