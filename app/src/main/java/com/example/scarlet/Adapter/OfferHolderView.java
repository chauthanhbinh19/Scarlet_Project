package com.example.scarlet.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Data.Offer;
import com.example.scarlet.R;

public class OfferHolderView extends RecyclerView.ViewHolder {
    TextView offerName,offerDescription,offerPoint;
    ImageView offerImage;

    public OfferHolderView(@NonNull View itemView) {
        super(itemView);
        offerName=itemView.findViewById(R.id.offer_name);
        offerDescription=itemView.findViewById(R.id.offer_description);
        offerPoint=itemView.findViewById(R.id.offer_point);
        offerImage=itemView.findViewById(R.id.offer_image);
    }
    public void bindData(Offer offer){
        offerName.setText(offer.getName());
        offerDescription.setText(offer.getDescription());
        offerPoint.setText(String.valueOf(offer.getPoint()));
        offerImage.setImageResource(offer.getImage());
    }
}
