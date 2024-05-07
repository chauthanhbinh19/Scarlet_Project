package com.example.scarlet.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Data.Product;
import com.example.scarlet.R;

import java.util.ArrayList;
import java.util.List;

public class AdminProductAdapter extends RecyclerView.Adapter<AdminProductHolderView> {
    public List<Product> productList;
    public List<Product> filteredData;
    public AdminProductAdapter(List<Product> productList){
        this.productList=productList;
        this.filteredData=new ArrayList<>(productList);
    }
    @NonNull
    @Override
    public AdminProductHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_product_adapter_layout, parent, false);
        return new AdminProductHolderView(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminProductHolderView holder, int position) {
        Animation animation= AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);
        holder.bindData(filteredData.get(position));
        holder.itemView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return filteredData.size();
    }
    public void filterByAlphabet(String query){
        filteredData.clear();
        query=query.toLowerCase();
        for(Product product:productList){
            if(product.getName().toLowerCase().contains(query)){
                filteredData.add(product);
            }
        }
        notifyDataSetChanged();
    }
    public void filterBySearch(String query){
        filteredData.clear();
        if(query.isEmpty() || query==null){
            filteredData.addAll(productList);
        }else{
            query=query.toLowerCase();
            for(Product product:productList){
                if(product.getName().toLowerCase().contains(query)){
                    filteredData.add(product);
                }
            }
        }
        notifyDataSetChanged();
    }
}
