package com.example.scarlet.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Data.Offer;
import com.example.scarlet.Interface.GetPointCallback;
import com.example.scarlet.R;

import java.util.List;

public class OfferHistoryAdapter extends RecyclerView.Adapter<OfferHistoryHolderView> {
    public List<Offer> offerList;
    GetPointCallback getPointCallback;
    public OfferHistoryAdapter(List<Offer> offerList, GetPointCallback getPointCallback) {
        this.getPointCallback=getPointCallback;
        this.offerList = offerList;
    }

    @NonNull
    @Override
    public OfferHistoryHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_adapter_layout_2, parent, false);
        return new OfferHistoryHolderView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferHistoryHolderView holder, int position) {
//        Animation animation= AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);
        holder.bindData(offerList.get(position),getPointCallback);
//        holder.itemView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return offerList.size();
    }
}



