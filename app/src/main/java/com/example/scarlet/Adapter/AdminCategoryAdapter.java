package com.example.scarlet.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Data.Category;
import com.example.scarlet.R;

import java.util.List;

public class AdminCategoryAdapter extends RecyclerView.Adapter<AdminCategoryHolderView> {
    public List<Category> categoryList;
    public AdminCategoryAdapter(List<Category> categoryList ){

        this.categoryList=categoryList;
    }

    @NonNull
    @Override
    public AdminCategoryHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_category_adapter_layout, parent, false);
        return new AdminCategoryHolderView(view).linkAdapter(this);
    }
    @Override
    public void onBindViewHolder(@NonNull AdminCategoryHolderView holder, int position) {
        holder.bindData(categoryList.get(position));
    }
    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}
