package com.example.scarlet.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Data.Order;
import com.example.scarlet.R;

import java.text.SimpleDateFormat;

public class AdminOrderHolderView extends RecyclerView.ViewHolder {
    TextView orderKey, orderDate, orderStatus;
    Context context;
    public AdminOrderHolderView(@NonNull View itemView) {
        super(itemView);
        orderKey=itemView.findViewById(R.id.orderName);
        orderDate=itemView.findViewById(R.id.orderDate);
        orderStatus=itemView.findViewById(R.id.orderStatus);
        context=itemView.getContext();
    }
    public void bindData(Order order){
        orderKey.setText("Order-"+order.getKey());
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String formattedOrderDate = format.format(order.getOrderDate());
        orderDate.setText("Order date: "+formattedOrderDate);
        orderStatus.setText(order.getOrderStatus());
        if(order.getOrderStatus().equals("pending")){
            orderStatus.setBackground(context.getDrawable(R.drawable.rectangle_yellow_radius));
        }else if(order.getOrderStatus().equals("done")){
            orderStatus.setBackground(context.getDrawable(R.drawable.rectangle_green_radius));
        }else if(order.getOrderStatus().equals("cancelled")){
            orderStatus.setBackground(context.getDrawable(R.drawable.rectangle_gray_radius));
        }
    }
}
