package com.example.scarlet.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Data.Deal;
import com.example.scarlet.Interface.GetKeyCallback;
import com.example.scarlet.R;

import java.util.List;

public class DealSecondAdapter extends RecyclerView.Adapter<DealtHolderViewSecond> {
    private List<Deal> dealList;
    public GetKeyCallback getKeyCallback;
    public DealSecondAdapter(List<Deal> dealList, GetKeyCallback getKeyCallback) {
        this.dealList = dealList;
        this.getKeyCallback=getKeyCallback;
    }

    @NonNull
    @Override
    public DealtHolderViewSecond onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.voucher_adapter_layout_2, parent, false);
        return new DealtHolderViewSecond(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DealtHolderViewSecond holder, int position) {
        Animation animation= AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);
        holder.bindData(dealList.get(position),getKeyCallback);
        holder.itemView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return dealList.size();
    }
}



