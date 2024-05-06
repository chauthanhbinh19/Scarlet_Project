package com.example.scarlet.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.scarlet.Data.Category;
import com.example.scarlet.Fragment.ProductFragment;
import com.example.scarlet.R;


public class CategoryHolderView extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView textView;
    String categoryKey, categoryName;
    Context context;

    public CategoryHolderView(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.icon);
        textView = itemView.findViewById(R.id.name_category);
        context=itemView.getContext();
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(categoryKey !=null){
                    ProductFragment productFragment=new ProductFragment();
                    Bundle args=new Bundle();
                    args.putString("categoryKey",categoryKey);
                    args.putString("categoryName",categoryName);
                    productFragment.setArguments(args);

                    FragmentManager fragmentManager = ((AppCompatActivity)itemView.getContext()).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right);
                    fragmentTransaction.replace(R.id.frame_layout, productFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });
    }
    public void bindData(Category category){
//        imageView.setImageResource(category.getImg());
        Glide.with(context).load(category.getImg()).into(imageView);
        textView.setText(category.getName_category());
        categoryKey=category.getKey();
        categoryName=category.getName_category();
    }
}
