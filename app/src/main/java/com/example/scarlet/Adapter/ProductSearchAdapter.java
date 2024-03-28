package com.example.scarlet.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Data.Product;
import com.example.scarlet.R;

import java.util.List;

public class ProductSearchAdapter extends RecyclerView.Adapter<ProductHolderView> {
    private List<Product> productList;
    public ProductSearchAdapter(List<Product> productList){

        this.productList=productList;
    }


    @NonNull
    @Override
    public ProductHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_adapter_layout_2, parent, false);
        return new ProductHolderView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolderView holder, int position) {
        holder.bindData(productList.get(position));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
