package com.example.scarlet.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.scarlet.Data.Review;
import com.example.scarlet.R;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

public class ReviewHolderView extends RecyclerView.ViewHolder {
    ImageView avatar;
    TextView name, comment, date;
    ImageView star1, star2, star3, star4, star5;
    Context context;
    public ReviewHolderView(@NonNull View itemView) {
        super(itemView);
        context=itemView.getContext();
        avatar=itemView.findViewById(R.id.user_avatar);
        name=itemView.findViewById(R.id.userName);
        comment=itemView.findViewById(R.id.reviewComment);
        date=itemView.findViewById(R.id.reviewDate);
        star1=itemView.findViewById(R.id.star1);
        star2=itemView.findViewById(R.id.star2);
        star3=itemView.findViewById(R.id.star3);
        star4=itemView.findViewById(R.id.star4);
        star5=itemView.findViewById(R.id.star5);
    }
    public void bindData(Review review){
        Glide.with(context).load(review.getCustomerImage()).into(avatar);
        name.setText(review.getCustomerName());
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String formattedExpiryDate = format.format(review.getDate());
        date.setText(formattedExpiryDate);
        comment.setText(review.getComment());
        int rating=review.getRating();
        if(rating==1){
            star1.setBackgroundResource(R.drawable.star_yellow_active);
            star2.setBackgroundResource(R.drawable.star_yellow_unactive);
            star3.setBackgroundResource(R.drawable.star_yellow_unactive);
            star4.setBackgroundResource(R.drawable.star_yellow_unactive);
            star5.setBackgroundResource(R.drawable.star_yellow_unactive);
        }else if(rating==2){
            star1.setBackgroundResource(R.drawable.star_yellow_active);
            star2.setBackgroundResource(R.drawable.star_yellow_active);
            star3.setBackgroundResource(R.drawable.star_yellow_unactive);
            star4.setBackgroundResource(R.drawable.star_yellow_unactive);
            star5.setBackgroundResource(R.drawable.star_yellow_unactive);
        }else if(rating==3){
            star1.setBackgroundResource(R.drawable.star_yellow_active);
            star2.setBackgroundResource(R.drawable.star_yellow_active);
            star3.setBackgroundResource(R.drawable.star_yellow_active);
            star4.setBackgroundResource(R.drawable.star_yellow_unactive);
            star5.setBackgroundResource(R.drawable.star_yellow_unactive);
        }else if(rating==4){
            star1.setBackgroundResource(R.drawable.star_yellow_active);
            star2.setBackgroundResource(R.drawable.star_yellow_active);
            star3.setBackgroundResource(R.drawable.star_yellow_active);
            star4.setBackgroundResource(R.drawable.star_yellow_active);
            star5.setBackgroundResource(R.drawable.star_yellow_unactive);
        }else if(rating==5){
            star1.setBackgroundResource(R.drawable.star_yellow_active);
            star2.setBackgroundResource(R.drawable.star_yellow_active);
            star3.setBackgroundResource(R.drawable.star_yellow_active);
            star4.setBackgroundResource(R.drawable.star_yellow_active);
            star5.setBackgroundResource(R.drawable.star_yellow_active);
        }
    }
}
