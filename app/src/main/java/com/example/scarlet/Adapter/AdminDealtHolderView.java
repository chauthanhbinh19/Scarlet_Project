package com.example.scarlet.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.AdminEditProductActivity;
import com.example.scarlet.AdminEditVoucherActivity;
import com.example.scarlet.Data.Deal;
import com.example.scarlet.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;

public class AdminDealtHolderView extends RecyclerView.ViewHolder {
    ImageView icon, btnEdit, btnDelete;
    TextView discount,voucherName,expiryDate,deliveryMethod;
    Context context;
    String vouchername,voucherdiscount, deliverymethod, voucherdescription, expirydate, key;
    public AdminDealAdapter adapter;
    public AdminDealtHolderView linkAdapter(AdminDealAdapter adapter){
        this.adapter=adapter;
        return this;
    }
    public AdminDealtHolderView(@NonNull View itemView) {
        super(itemView);
        icon=itemView.findViewById(R.id.delivery_method_icon);
        discount=itemView.findViewById(R.id.discount);
        voucherName=itemView.findViewById(R.id.voucher_name);
        expiryDate=itemView.findViewById(R.id.voucher_date);
        deliveryMethod=itemView.findViewById(R.id.delivery_method);
        context=itemView.getContext();
        btnEdit=itemView.findViewById(R.id.btnEdit);
        btnDelete=itemView.findViewById(R.id.btnDelete);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, AdminEditVoucherActivity.class);
                intent.putExtra("voucherName",vouchername);
                intent.putExtra("voucherDiscount",String.valueOf(voucherdiscount));
                intent.putExtra("deliveryMethod",deliverymethod);
                intent.putExtra("voucherDiscription",voucherdescription);
                intent.putExtra("expiryDate",expirydate);
                intent.putExtra("voucherKey",key);
                context.startActivity(intent);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setMessage("Do you want to delete it?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                                DatabaseReference myRef=firebaseDatabase.getReference("deal").child(key);
                                myRef.removeValue();
                                adapter.dealList.remove(getAdapterPosition());
                                adapter.notifyDataSetChanged();
                                Toast.makeText(context,"Delete successfully",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel",null)
                        .show();
            }
        });
    }
    public void bindData(Deal deal){
        icon.setImageResource(deal.getDeliveryIcon());
        discount.setText(String.valueOf(deal.getDiscount())+"%");
        voucherName.setText(deal.getName());
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String formattedExpiryDate = format.format(deal.getExpiryDate());
        expiryDate.setText(formattedExpiryDate);
        deliveryMethod.setText(deal.getDeliveryMethod());
        vouchername= deal.getName();
        voucherdiscount=String.valueOf(deal.getDiscount());
        deliverymethod=deal.getDeliveryMethod();
        voucherdescription=deal.getDescription();
        expirydate=formattedExpiryDate;
        key=deal.getKey();
    }
}
