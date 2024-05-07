package com.example.scarlet.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Data.Order;
import com.example.scarlet.Data.Product;
import com.example.scarlet.R;

import java.util.ArrayList;
import java.util.List;

public class AdminOrderAdapter extends RecyclerView.Adapter<AdminOrderHolderView> {
    public List<Order> orderList;
    public List<Order> filteredData;
    public AdminOrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
        this.filteredData=new ArrayList<>(orderList);
    }

    @NonNull
    @Override
    public AdminOrderHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_order_adapter_layout, parent, false);
        return new AdminOrderHolderView(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminOrderHolderView holder, int position) {
        Animation animation= AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);
        holder.bindData(filteredData.get(position));
        holder.itemView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return filteredData.size();
    }
//    public void filterBySearch(String query){
//        filteredData.clear();
//        if(query.isEmpty() || query==null){
//            filteredData.addAll(productList);
//        }else{
//            query=query.toLowerCase();
//            for(Product product:productList){
//                if(product.getName().toLowerCase().contains(query)){
//                    filteredData.add(product);
//                }
//            }
//        }
//        notifyDataSetChanged();
//    }
}
