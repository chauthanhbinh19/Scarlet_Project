package com.example.scarlet.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Data.Product;
import com.example.scarlet.R;

import java.util.List;

public class AdminProductAdapter extends RecyclerView.Adapter<AdminProductHolderView> {
    public List<Product> productList;
    public AdminProductAdapter(List<Product> productList){

        this.productList=productList;
    }
    @NonNull
    @Override
    public AdminProductHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_product_adapter_layout, parent, false);
        return new AdminProductHolderView(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminProductHolderView holder, int position) {
        holder.bindData(productList.get(position));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
