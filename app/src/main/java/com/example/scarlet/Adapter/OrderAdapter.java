package com.example.scarlet.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Data.Order;
import com.example.scarlet.R;

import java.util.Collections;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderHolderView> {
    public List<Order> orderList;
    public OrderAdapter(List<Order> orderList){
        this.orderList=orderList;
        sortFilteredDataByLastedTime();
    }
    @NonNull
    @Override
    public OrderHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_activities_adapter_layout, parent, false);
        return new OrderHolderView(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHolderView holder, int position) {
        Animation animation= AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);
        holder.bindData(orderList.get(position));
        holder.itemView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
    private void sortFilteredDataByLastedTime() {
        Collections.sort(orderList, (o1, o2) -> o2.getOrderDate().compareTo(o1.getOrderDate()));
    }
    private void sortFilteredDataByLateTime() {
        Collections.sort(orderList, (o1, o2) -> o1.getOrderDate().compareTo(o2.getOrderDate()));
    }
}
