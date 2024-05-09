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

public class OfferHolderView extends RecyclerView.ViewHolder {
    TextView offerName,offerDescription,offerPoint;
    ImageView offerImage;
    String key, code;
    int point;
    GetPointCallback getPointCallback;

    public OfferHolderView(@NonNull View itemView) {
        super(itemView);
        offerName=itemView.findViewById(R.id.offer_name);
        offerDescription=itemView.findViewById(R.id.offer_description);
        offerPoint=itemView.findViewById(R.id.offer_point);
        offerImage=itemView.findViewById(R.id.offer_image);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(itemView.getContext());
                builder.setMessage("You want to exchange points?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences sharedPreferences = itemView.getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                                boolean isLoggedIn=sharedPreferences.getBoolean("isLoggedIn",false);
                                String userKey=sharedPreferences.getString("customerKey","");
                                if(isLoggedIn && !userKey.isEmpty()){
                                    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                                    DatabaseReference offerRef= firebaseDatabase.getReference("offer_transaction");
                                    Date date=new Date();
                                    DatabaseReference userRef=firebaseDatabase.getReference("user");
                                    userRef.child(userKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(snapshot.exists()){
                                                int oldPoint=snapshot.child("point").getValue(int.class);
                                                int newPoint=oldPoint-point;
                                                if(newPoint>0){
                                                    userRef.child(userKey).child("point").setValue(newPoint);
                                                    OfferTransaction offerTransaction=new OfferTransaction(userKey,key, code, date,point);
                                                    offerRef.push().setValue(offerTransaction);
                                                    getPointCallback.itemClick(newPoint,1);
                                                    Toast.makeText(itemView.getContext(), "Exchange successfully",Toast.LENGTH_SHORT).show();
                                                }else{
                                                    Toast.makeText(itemView.getContext(), "Your point is not enough",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }
                        })
                        .setNegativeButton("Cancel",null)
                        .show();
            }
        });
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
