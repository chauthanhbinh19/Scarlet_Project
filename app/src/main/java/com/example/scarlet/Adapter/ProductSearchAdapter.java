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

public class ProductSearchAdapter extends RecyclerView.Adapter<ProductHolderView> {
    private List<Product> productList;
    private List<Product> filteredData;
    public ProductSearchAdapter(List<Product> productList){
        this.productList=productList;
        this.filteredData=new ArrayList<>(productList);
    }


    @NonNull
    @Override
    public ProductHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_adapter_layout_2, parent, false);
        return new ProductHolderView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolderView holder, int position) {
        Animation animation= AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);
        holder.bindData(filteredData.get(position));
        holder.itemView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return filteredData.size();
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
