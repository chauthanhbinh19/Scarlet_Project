package com.example.scarlet.Adapter;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Data.Deal;
import com.example.scarlet.Interface.GetKeyCallback;
import com.example.scarlet.Interface.GetThreeElementsCallback;
import com.example.scarlet.R;

import java.util.List;

public class DealSecondAdapter extends RecyclerView.Adapter<DealtHolderViewSecond> {
    private List<Deal> dealList;
    public GetThreeElementsCallback getThreeElementsCallback;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    public DealSecondAdapter(List<Deal> dealList, GetThreeElementsCallback getThreeElementsCallback) {
        this.dealList = dealList;
        this.getThreeElementsCallback=getThreeElementsCallback;
    }

    @NonNull
    @Override
    public DealtHolderViewSecond onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.voucher_adapter_layout_2, parent, false);
        return new DealtHolderViewSecond(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DealtHolderViewSecond holder,int position) {
//        Animation animation= AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);
        holder.bindData(dealList.get(position),getThreeElementsCallback);
//        holder.itemView.startAnimation(animation);
        holder.checkbox.setChecked(selectedItems.get(position, false));

        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = holder.getAdapterPosition();

                if (selectedItems.get(clickedPosition, false)) {
                    selectedItems.delete(clickedPosition);
                } else {
                    selectedItems.clear(); // Reset all checkboxes
                    selectedItems.put(clickedPosition, true);
                }

                notifyDataSetChanged(); // Update RecyclerView
            }
        });
    }

    @Override
    public int getItemCount() {
        return dealList.size();
    }
}



