package com.example.scarlet.Adapter;

import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Data.Order;
import com.example.scarlet.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;

public class AdminOrderHolderView extends RecyclerView.ViewHolder {
    TextView orderKey, orderDate, orderStatus, orderTotal;
    ImageView btnMore;
    Context context;
    String key;
    AdminOrderAdapter adapter;
    public AdminOrderHolderView linkAdapter(AdminOrderAdapter adapter){
        this.adapter=adapter;
        return this;
    }
    public AdminOrderHolderView(@NonNull View itemView) {
        super(itemView);
        orderKey=itemView.findViewById(R.id.orderName);
        orderDate=itemView.findViewById(R.id.orderDate);
        orderStatus=itemView.findViewById(R.id.orderStatus);
        orderTotal=itemView.findViewById(R.id.orderTotal);
        btnMore=itemView.findViewById(R.id.btnMore);
        context=itemView.getContext();
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu();
            }
        });
    }
    public void bindData(Order order){
        orderKey.setText("#-"+order.getKey());
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
        orderTotal.setText("Total: "+String.valueOf(order.getTotal()));
        if(!order.getOrderStatus().equals("pending")){
            btnMore.setVisibility(View.GONE);
        }
        key=order.getKey();
    }
    private void showPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(context, itemView, Gravity.RIGHT);
        popupMenu.inflate(R.menu.order_menu);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            popupMenu.setForceShowIcon(true);
//        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.order_done){
                    changeOrderStatus("done");
                    adapter.orderList.remove(getAdapterPosition());
                    adapter.notifyDataSetChanged();
                }else if(item.getItemId()==R.id.order_cancelled){
                    changeOrderStatus("cancelled");
                    adapter.orderList.remove(getAdapterPosition());
                    adapter.notifyDataSetChanged();
                }
                return true;
            }
        });

        popupMenu.show();
    }
    private void changeOrderStatus(String orderstatus){
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference myRef= firebaseDatabase.getReference("order").child(key);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    myRef.child("orderStatus").setValue(orderstatus);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
