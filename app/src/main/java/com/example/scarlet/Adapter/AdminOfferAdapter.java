package com.example.scarlet.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Data.Deal;
import com.example.scarlet.Data.Offer;
import com.example.scarlet.Data.Product;
import com.example.scarlet.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdminOfferAdapter extends RecyclerView.Adapter<AdminOfferHolderView> {
    public List<Offer> offerList;
    public List<Offer> filteredData;
    public AdminOfferAdapter(List<Offer> offerList) {
        this.offerList = offerList;
        this.filteredData=new ArrayList<>(offerList);
        sortFilteredDataByStartLastedTime();
    }

    @NonNull
    @Override
    public AdminOfferHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_offer_adapter_layout, parent, false);
        return new AdminOfferHolderView(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminOfferHolderView holder, int position) {
        Animation animation= AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);
        holder.bindData(filteredData.get(position));
//        holder.itemView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return filteredData.size();
    }
    private void sortFilteredDataByStartLastedTime() {
        Collections.sort(filteredData, (o1, o2) -> o2.getStartDate().compareTo(o1.getStartDate()));
    }
    private void sortFilteredDataByStartLateTime() {
        Collections.sort(filteredData, (o1, o2) -> o1.getStartDate().compareTo(o2.getStartDate()));
    }
    private void sortFilteredDataByEndLastedTime() {
        Collections.sort(filteredData, (o1, o2) -> o2.getEndDate().compareTo(o1.getEndDate()));
    }
    private void sortFilteredDataByEndLateTime() {
        Collections.sort(filteredData, (o1, o2) -> o1.getEndDate().compareTo(o2.getEndDate()));
    }
    public void filterBySearch(String query){
        filteredData.clear();
        if(query.isEmpty() || query==null){
            filteredData.addAll(offerList);
        }else{
            query=query.toLowerCase();
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            for(Offer offer : offerList){
                if(offer.getName().toLowerCase().contains(query)){
                    filteredData.add(offer);
                }else if(String.valueOf(offer.getPoint()).toLowerCase().contains(query)){
                    filteredData.add(offer);
                }else if(format.format(offer.getStartDate()).toLowerCase().contains(query)){
                    filteredData.add(offer);
                }else if(format.format(offer.getEndDate()).toLowerCase().contains(query)){
                    filteredData.add(offer);
                }
            }
        }
        notifyDataSetChanged();
    }
}



