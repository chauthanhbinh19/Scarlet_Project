package com.example.scarlet.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Data.Review;
import com.example.scarlet.R;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewHolderView> {
    List<Review> reviewList;

    public ReviewAdapter(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_adapter_layout, parent, false);
        return new ReviewHolderView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewHolderView holder, int position) {
        holder.bindData(reviewList.get(position));
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }
}
