package com.example.scarlet.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Data.Product;
import com.example.scarlet.Data.User;
import com.example.scarlet.R;

import java.util.List;

public class AdminCustomerAdapter extends RecyclerView.Adapter<AdminCustomerHolderView> {
    public List<User> userList;
    public AdminCustomerAdapter(List<User> userList){

        this.userList=userList;
    }


    @NonNull
    @Override
    public AdminCustomerHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_customer_adapter_layout, parent, false);
        return new AdminCustomerHolderView(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminCustomerHolderView holder, int position) {
        holder.bindData(userList.get(position));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
