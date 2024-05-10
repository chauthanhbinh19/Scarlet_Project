package com.example.scarlet.Adapter;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Data.Product;
import com.example.scarlet.Interface.GetKeyCallback;
import com.example.scarlet.R;

import java.util.ArrayList;
import java.util.List;

public class ProductReviewAdapter extends RecyclerView.Adapter<ProductReviewHolderView> {
    private List<Product> productList;
    private List<Product> filteredData;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    GetKeyCallback getKeyCallback;
    public ProductReviewAdapter(List<Product> productList, GetKeyCallback getKeyCallback){
        this.productList=productList;
        this.getKeyCallback=getKeyCallback;
        filteredData=new ArrayList<>(productList);
    }

    @NonNull
    @Override
    public ProductReviewHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_adapter_layout_5, parent, false);
        return new ProductReviewHolderView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductReviewHolderView holder, int position) {
        holder.bindData(filteredData.get(position),getKeyCallback);
        holder.checkbox.setChecked(selectedItems.get(position, false));
        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = holder.getAdapterPosition();

                if (selectedItems.get(clickedPosition, false)) {
                    selectedItems.delete(clickedPosition);
                } else {
                    selectedItems.clear(); // Reset all checkboxes
                    selectedItems.put(clickedPosition, true);
                }

                notifyDataSetChanged(); // Update RecyclerView
            }
        });
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
