package com.example.scarlet.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Data.Deal;
import com.example.scarlet.Interface.GetKeyCallback;
import com.example.scarlet.Interface.GetThreeElementsCallback;
import com.example.scarlet.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DealtHolderViewSecond extends RecyclerView.ViewHolder {
    ImageView icon;
    TextView discount,voucherName,expiryDate,deliveryMethod;
    GetThreeElementsCallback getThreeElementsCallback;
    CheckBox checkbox;
    String key, name, code;
    Context context;
    String productTitle="";

    public DealtHolderViewSecond(@NonNull View itemView) {
        super(itemView);
        icon=itemView.findViewById(R.id.delivery_method_icon);
        discount=itemView.findViewById(R.id.discount);
        voucherName=itemView.findViewById(R.id.voucher_name);
        expiryDate=itemView.findViewById(R.id.voucher_date);
        deliveryMethod=itemView.findViewById(R.id.delivery_method);
        checkbox=itemView.findViewById(R.id.checkbox);
        context= itemView.getContext();

        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    getThreeElementsCallback.itemClick(key,name,code,1);
                }else{
                    getThreeElementsCallback.itemClick(key,name,code,0);
                }
            }
        });
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(key);
            }
        });

    }
    public void bindData(Deal deal,GetThreeElementsCallback getThreeElementsCallback1){
        icon.setImageResource(deal.getDeliveryIcon());
        discount.setText(String.valueOf(deal.getDiscount())+"%");
        voucherName.setText(deal.getName());
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String formattedExpiryDate = format.format(deal.getExpiryDate());
        expiryDate.setText(formattedExpiryDate);
        deliveryMethod.setText(deal.getDeliveryMethod());
        getThreeElementsCallback=getThreeElementsCallback1;
        key=deal.getKey();
        name=deal.getName();
        code=deal.getCode();
    }
    public void showDialog(String key){
        final Dialog dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.discount_information);
        ImageButton close=dialog.findViewById(R.id.btnClose);
        TextView title=dialog.findViewById(R.id.title);
        TextView productText=dialog.findViewById(R.id.productText);
        TextView remainDate=dialog.findViewById(R.id.remainDate);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference= firebaseDatabase.getReference("deal");
        DatabaseReference productRef= firebaseDatabase.getReference("product");

        databaseReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String titleText=snapshot.child("name").getValue(String.class);
                    title.setText(titleText);
                    Date expirationDate=snapshot.child("expiryDate").getValue(Date.class);
                    Date currentDate = new Date();
                    long differenceInMillis = expirationDate.getTime() - currentDate.getTime();
                    long differenceInDays = differenceInMillis / (1000 * 60 * 60 * 24);
                    if(differenceInDays>1){
                        remainDate.setText("In "+differenceInDays+" days");
                    }else if(differenceInDays==1){
                        remainDate.setText("In "+differenceInDays+" day");
                    }else{
                        remainDate.setText("Expired");
                    }
                    ArrayList<String> productIdArray = snapshot.child("productId").getValue(new GenericTypeIndicator<ArrayList<String>>() {});
                    int count=0;
                    ArrayList<String> productIdArray1=new ArrayList<>();
                    if(productIdArray != null && !productIdArray.isEmpty()) {
                        for (String productId : productIdArray) {
                            productIdArray1.add(productId);
                            count++;
                        }
                    }
                    if(count>0){
                        for(String id: productIdArray1){
                            productRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                    if(snapshot1.exists()){
                                        String productName=snapshot1.child("name").getValue(String.class);
                                        productTitle=productTitle+productName+" \n ";
                                        productText.setText(productTitle);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }else{
                        productText.setText("Apply for all products");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}
