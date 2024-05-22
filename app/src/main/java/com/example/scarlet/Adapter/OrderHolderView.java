package com.example.scarlet.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Data.Order;
import com.example.scarlet.OrderDetailsActivity;
import com.example.scarlet.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;

public class OrderHolderView extends RecyclerView.ViewHolder {
    TextView date, item, total, detail, cancel,textContent;
    ImageView icon;
    String key;
    Context context;
    public OrderAdapter adapter;
    public OrderHolderView linkAdapter(OrderAdapter adapter){
        this.adapter=adapter;
        return this;
    }
    public OrderHolderView(@NonNull View itemView) {
        super(itemView);
        date=itemView.findViewById(R.id.date);
        item=itemView.findViewById(R.id.item);
        total=itemView.findViewById(R.id.total);
        detail=itemView.findViewById(R.id.detail);
        cancel=itemView.findViewById(R.id.cancel);
        textContent=itemView.findViewById(R.id.textContent);
        icon=itemView.findViewById(R.id.icon);
        context=itemView.getContext();
        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, OrderDetailsActivity.class);
                intent.putExtra("orderKey",key);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(context, R.anim.slide_in_right, R.anim.slide_out_left);
                ActivityCompat.startActivity(context, intent, options.toBundle());
            }
        });
    }
    public void bindData(Order order){
        total.setText(String.format("%.0f", order.getTotal()));
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String formattedExpiryDate = format.format(order.getOrderDate());
        date.setText(formattedExpiryDate);
        item.setText(String.valueOf(order.getProductList().size())+" items");
        key=order.getKey();
        boolean isComfirmed=order.isConfirmed();
        if(order.getOrderStatus().equals("done") && !isComfirmed){
            textContent.setText("Your order has been taken by the Driver");
            icon.setImageResource(R.drawable.fast_delivery_3);
        }else if(order.getOrderStatus().equals("done") && isComfirmed){
            textContent.setText("Order was delivered successfully");
            icon.setImageResource(R.drawable.fast_delivery_3);
        }else if(order.getOrderStatus().equals("cancelled")){
            textContent.setText("Your order has been cancelled");
            icon.setImageResource(R.drawable.fast_delivery_3);
        }
    }
}
