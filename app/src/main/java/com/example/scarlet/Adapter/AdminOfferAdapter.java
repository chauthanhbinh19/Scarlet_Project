package com.example.scarlet.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Data.Deal;
import com.example.scarlet.Data.Offer;
import com.example.scarlet.R;

import java.util.List;

public class AdminOfferAdapter extends RecyclerView.Adapter<AdminOfferHolderView> {
    public List<Offer> offerList;

    public AdminOfferAdapter(List<Offer> offerList) {

        this.offerList = offerList;
    }

    @NonNull
    @Override
    public AdminOfferHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_offer_adapter_layout, parent, false);
        return new AdminOfferHolderView(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminOfferHolderView holder, int position) {
        holder.bindData(offerList.get(position));
    }

    @Override
    public int getItemCount() {
        return offerList.size();
    }
}



