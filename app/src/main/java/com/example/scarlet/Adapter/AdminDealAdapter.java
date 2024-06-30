package com.example.scarlet.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Data.Deal;
import com.example.scarlet.Data.Product;
import com.example.scarlet.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdminDealAdapter extends RecyclerView.Adapter<AdminDealtHolderView> {
    public List<Deal> dealList;
    public List<Deal> filteredData;
    public AdminDealAdapter(List<Deal> dealList) {
        this.dealList = dealList;
        this.filteredData=new ArrayList<>(dealList);
        sortFilteredDataByLastedTime();
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
        holder.bindData(filteredData.get(position));
//        holder.itemView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return filteredData.size();
    }
    private void sortFilteredDataByLastedTime() {
        Collections.sort(filteredData, (o1, o2) -> o2.getExpiryDate().compareTo(o1.getExpiryDate()));
    }
    private void sortFilteredDataByLateTime() {
        Collections.sort(filteredData, (o1, o2) -> o1.getExpiryDate().compareTo(o2.getExpiryDate()));
    }
    public void filterByDelivery(){
        filteredData.clear();
        for(Deal deal:dealList){
            if(deal.getDeliveryMethod().equals("delivery")){
                filteredData.add(deal);
            }
        }
        notifyDataSetChanged();
    }
    public void filterByInstore(){
        filteredData.clear();
        for(Deal deal:dealList){
            if(deal.getDeliveryMethod().equals("instore")){
                filteredData.add(deal);
            }
        }
        notifyDataSetChanged();
    }
    public void filterByPickup(){
        filteredData.clear();
        for(Deal deal:dealList){
            if(deal.getDeliveryMethod().equals("pickup")){
                filteredData.add(deal);
            }
        }
        notifyDataSetChanged();
    }
    public void filterBySearch(String query, String deliveryMethod){
        filteredData.clear();
        if(query.isEmpty() || query==null){
            filteredData.addAll(dealList);
        }else{
            if(deliveryMethod.equals("all")){
                query=query.toLowerCase();
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                for(Deal deal : dealList){
                    if(deal.getName().toLowerCase().contains(query)){
                        filteredData.add(deal);
                    }else if(String.valueOf(deal.getDiscount()).toLowerCase().contains(query)){
                        filteredData.add(deal);
                    }else if(format.format(deal.getExpiryDate()).toLowerCase().contains(query)){
                        filteredData.add(deal);
                    }
                }
            }else{
                query=query.toLowerCase();
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                for(Deal deal : dealList){
                    if(deal.getDeliveryMethod().equals(deliveryMethod)){
                        if(deal.getName().toLowerCase().contains(query)){
                            filteredData.add(deal);
                        }else if(String.valueOf(deal.getDiscount()).toLowerCase().contains(query)){
                            filteredData.add(deal);
                        }else if(format.format(deal.getExpiryDate()).toLowerCase().contains(query)){
                            filteredData.add(deal);
                        }
                    }
                }
            }
        }
        notifyDataSetChanged();
    }
}



