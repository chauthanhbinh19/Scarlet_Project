package com.example.scarlet.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.scarlet.Data.Category;
import com.example.scarlet.R;

import java.util.List;

public class AdminCategoryDropdownAdapter extends ArrayAdapter<Category> {
    private Context mContext;
    private List<Category> mCategories;

    public AdminCategoryDropdownAdapter(Context context, int resource, List<Category> categories) {
        super(context, resource, categories);
        mContext = context;
        mCategories = categories;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    private View createView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.admin_category_dropdown_layout, parent, false);

        TextView nameTextView = view.findViewById(R.id.name_category);
        ImageView imgView = view.findViewById(R.id.icon);

        Category category = mCategories.get(position);
        nameTextView.setText(category.getName_category());
        Glide.with(mContext).load(category.getImg()).into(imgView);

        return view;
    }
}

