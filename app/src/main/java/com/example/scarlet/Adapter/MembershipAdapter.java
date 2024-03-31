package com.example.scarlet.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Data.Deal;
import com.example.scarlet.R;

import java.util.List;

public class MembershipAdapter extends RecyclerView.Adapter<MembershipHolderView> {
    private List<String> privileges;

    public MembershipAdapter(List<String> privileges) {

        this.privileges = privileges;
    }

    @NonNull
    @Override
    public MembershipHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.membership_adapter_layout, parent, false);
        return new MembershipHolderView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MembershipHolderView holder, int position) {
        holder.bindData(privileges.get(position));
    }

    @Override
    public int getItemCount() {
        return privileges.size();
    }
}



