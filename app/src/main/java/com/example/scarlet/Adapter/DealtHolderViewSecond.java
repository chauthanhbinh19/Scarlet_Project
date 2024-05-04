package com.example.scarlet.Adapter;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Data.Deal;
import com.example.scarlet.Interface.GetKeyCallback;
import com.example.scarlet.Interface.GetThreeElementsCallback;
import com.example.scarlet.R;

import java.text.SimpleDateFormat;

public class DealtHolderViewSecond extends RecyclerView.ViewHolder {
    ImageView icon;
    TextView discount,voucherName,expiryDate,deliveryMethod;
    GetThreeElementsCallback getThreeElementsCallback;
    CheckBox checkbox;
    String key, name, code;

    public DealtHolderViewSecond(@NonNull View itemView) {
        super(itemView);
        icon=itemView.findViewById(R.id.delivery_method_icon);
        discount=itemView.findViewById(R.id.discount);
        voucherName=itemView.findViewById(R.id.voucher_name);
        expiryDate=itemView.findViewById(R.id.voucher_date);
        deliveryMethod=itemView.findViewById(R.id.delivery_method);
        checkbox=itemView.findViewById(R.id.checkbox);

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
}
