package com.example.scarlet.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Data.Category;
import com.example.scarlet.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryHolderView> {
    private List<Category> categoryList;
    public CategoryAdapter(List<Category> categoryList){

        this.categoryList=categoryList;
    }

    @NonNull
    @Override
    public CategoryHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_adapter_layout, parent, false);
        return new CategoryHolderView(view);
    }
    @Override
    public void onBindViewHolder(@NonNull CategoryHolderView holder, int position) {
        holder.bindData(categoryList.get(position));
    }
    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}
