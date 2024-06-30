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

import java.util.List;

public class TrendAdapter extends RecyclerView.Adapter<TrendHolderView> {
    private List<Product> productList;
    public TrendAdapter(List<Product> productList){

        this.productList=productList;
    }


    @NonNull
    @Override
    public TrendHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trend_adapter_layout, parent, false);
        return new TrendHolderView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrendHolderView holder, int position) {
        Animation animation= AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.fade_in);
        holder.bindData(productList.get(position));
//        holder.itemView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
