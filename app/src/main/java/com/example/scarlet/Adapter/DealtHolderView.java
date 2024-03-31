package com.example.scarlet.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Data.Deal;
import com.example.scarlet.Data.Product;
import com.example.scarlet.ProductDetailActivity;
import com.example.scarlet.R;

public class DealtHolderView extends RecyclerView.ViewHolder {
    ImageView icon;
    TextView discount,voucherName,expiryDate,deliveryMethod;

    public DealtHolderView(@NonNull View itemView) {
        super(itemView);
        icon=itemView.findViewById(R.id.delivery_method_icon);
        discount=itemView.findViewById(R.id.discount);
        voucherName=itemView.findViewById(R.id.voucher_name);
        expiryDate=itemView.findViewById(R.id.voucher_date);
        deliveryMethod=itemView.findViewById(R.id.delivery_method);

    }
    public void bindData(Deal deal){
        icon.setImageResource(deal.getDeliveryIcon());
        discount.setText(String.valueOf(deal.getDiscount())+"%");
        voucherName.setText(deal.getName());
        expiryDate.setText(deal.getExpiryDate().toString());
        deliveryMethod.setText(deal.getDeliveryMethod());
    }
}
