package com.example.scarlet.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Data.Offer;
import com.example.scarlet.Data.OfferTransaction;
import com.example.scarlet.Interface.GetPointCallback;
import com.example.scarlet.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class OfferHistoryHolderView extends RecyclerView.ViewHolder {
    TextView offerName,offerDescription,offerPoint;
    ImageView offerImage;
    String key, code;
    int point;
    GetPointCallback getPointCallback;

    public OfferHistoryHolderView(@NonNull View itemView) {
        super(itemView);
        offerName=itemView.findViewById(R.id.offer_name);
        offerDescription=itemView.findViewById(R.id.offer_description);
        offerPoint=itemView.findViewById(R.id.offer_point);
        offerImage=itemView.findViewById(R.id.offer_image);

    }
    public void bindData(Offer offer, GetPointCallback getPointCallback1){
        offerName.setText(offer.getName());
        offerDescription.setText(offer.getDescription());
        offerPoint.setText(String.valueOf(offer.getPoint()));
        offerImage.setImageResource(offer.getImage());
        key=offer.getKey();
        code=offer.getCode();
        point=offer.getPoint();
        getPointCallback=getPointCallback1;
    }
}
