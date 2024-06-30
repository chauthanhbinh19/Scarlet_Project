package com.example.scarlet.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Data.Product;
import com.example.scarlet.Data.User;
import com.example.scarlet.R;

import java.util.ArrayList;
import java.util.List;

public class AdminCustomerAdapter extends RecyclerView.Adapter<AdminCustomerHolderView> {
    public List<User> userList;
    public List<User> filteredData;
    public AdminCustomerAdapter(List<User> userList){
        this.userList=userList;
        this.filteredData=new ArrayList<>(userList);
    }


    @NonNull
    @Override
    public AdminCustomerHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_customer_adapter_layout, parent, false);
        return new AdminCustomerHolderView(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminCustomerHolderView holder, int position) {
        Animation animation= AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);
        holder.bindData(filteredData.get(position));
//        holder.itemView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return filteredData.size();
    }
    public void filterBySearch(String query){
        filteredData.clear();
        if(query.isEmpty() || query==null){
            filteredData.addAll(userList);
        }else{
            query=query.toLowerCase();
            for(User user:userList){
                if(user.getFirst_name().toLowerCase().contains(query)){
                    filteredData.add(user);
                }else if(user.getLast_name().toLowerCase().contains(query)){
                    filteredData.add(user);
                }else if(user.getEmail().toLowerCase().contains(query)){
                    filteredData.add(user);
                }else if(user.getPhone_number().toLowerCase().contains(query)){
                    filteredData.add(user);
                }
            }
        }
        notifyDataSetChanged();
    }
}
