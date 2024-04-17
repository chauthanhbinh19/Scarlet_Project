package com.example.scarlet.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Data.Order;
import com.example.scarlet.R;

import java.util.List;

public class AdminOrderAdapter extends RecyclerView.Adapter<AdminOrderHolderView> {
    public List<Order> orderList;

    public AdminOrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public AdminOrderHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_order_adapter_layout, parent, false);
        return new AdminOrderHolderView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminOrderHolderView holder, int position) {
        holder.bindData(orderList.get(position));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
