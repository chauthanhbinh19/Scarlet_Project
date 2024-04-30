package com.example.scarlet.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Data.Deal;
import com.example.scarlet.R;

import java.util.List;

public class AdminDealAdapter extends RecyclerView.Adapter<AdminDealtHolderView> {
    public List<Deal> dealList;

    public AdminDealAdapter(List<Deal> dealList) {

        this.dealList = dealList;
    }

    @NonNull
    @Override
    public AdminDealtHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_voucher_adapter_layout, parent, false);
        return new AdminDealtHolderView(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminDealtHolderView holder, int position) {
        Animation animation= AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);
        holder.bindData(dealList.get(position));
        holder.itemView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return dealList.size();
    }
}



