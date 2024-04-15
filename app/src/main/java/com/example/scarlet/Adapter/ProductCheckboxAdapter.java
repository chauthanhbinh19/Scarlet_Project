package com.example.scarlet.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Data.Product;
import com.example.scarlet.Interface.GetKeyCallback;
import com.example.scarlet.Interface.GetStringCallback;
import com.example.scarlet.R;

import java.util.List;

public class ProductCheckboxAdapter extends RecyclerView.Adapter<ProductCheckboxHolderView> {
    List<Product> productList;
    public GetKeyCallback getKeyCallback;
    public ProductCheckboxAdapter(List<Product> productList, GetKeyCallback getKeyCallback){

        this.productList=productList;
        this.getKeyCallback=getKeyCallback;
    }
    @NonNull
    @Override
    public ProductCheckboxHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_checkbox_adapter_layout, parent, false);
        return new ProductCheckboxHolderView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductCheckboxHolderView holder, int position) {
        holder.bindData(productList.get(position),getKeyCallback);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
