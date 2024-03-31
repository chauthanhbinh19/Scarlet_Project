package com.example.scarlet.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Data.Offer;
import com.example.scarlet.R;

import java.util.List;

public class OfferAdapter extends RecyclerView.Adapter<OfferHolderView> {
    private List<Offer> offerList;

    public OfferAdapter(List<Offer> offerList) {

        this.offerList = offerList;
    }

    @NonNull
    @Override
    public OfferHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_adapter_layout, parent, false);
        return new OfferHolderView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferHolderView holder, int position) {
        holder.bindData(offerList.get(position));
    }

    @Override
    public int getItemCount() {
        return offerList.size();
    }
}



