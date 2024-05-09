package com.example.scarlet.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieDrawable;
import com.example.scarlet.Data.Address;
import com.example.scarlet.Data.Order;
import com.example.scarlet.Data.Product;
import com.example.scarlet.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdminOrderHolderView extends RecyclerView.ViewHolder {
    TextView orderKey, orderDate, orderStatus, orderTotal, subtotalText;
    ImageView btnMore, icon;
    Context context;
    String key;
    AdminOrderAdapter adapter;
    private Activity activity;
    double deliveryFee, tip, total;
    ProductHorizontalAdapter productAdapter;
    List<Product> productList;
    Address address;
    String deliveryStatus, orderStatusString;
    Date orderDateString;
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
        activity=(Activity) itemView.getContext();
        icon=itemView.findViewById(R.id.icon);
        orderKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup();
            }
        });
        orderDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup();
            }
        });
        orderStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup();
            }
        });
        orderTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup();
            }
        });
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup();
            }
        });
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu();
            }
        });
    }
    public void bindData(Order order){
        orderKey.setText("ID-"+order.getKey());
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
        orderStatusString=order.getOrderStatus();
        deliveryStatus=order.getDeliveryStatus();
        address=order.getShippingAddress();
        orderDateString=order.getOrderDate();
        deliveryFee=order.getDeliveryFee();
        productList=new ArrayList<>(order.getProductList());
        tip=order.getTip();
        total=order.getTotal();
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
    public void showPopup(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.admin_order_activities_detail, null);

        TextView statusText=dialogView.findViewById(R.id.status);
        TextView addressText=dialogView.findViewById(R.id.address);
        TextView timeText=dialogView.findViewById(R.id.time);
        TextView deliveryMethodText=dialogView.findViewById(R.id.deliveryMethod);
        RecyclerView productRecycleView=dialogView.findViewById(R.id.totalProductRecycleView);
        subtotalText=dialogView.findViewById(R.id.subtotal);
        TextView deliveryText=dialogView.findViewById(R.id.delivery);
        TextView tipText=dialogView.findViewById(R.id.tip);
        TextView totalText=dialogView.findViewById(R.id.total);
        ImageButton btnClose=dialogView.findViewById(R.id.btnClose);
        builder.setView(dialogView);

        android.app.AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.rectangle_circle_white_30));

        statusText.setText(orderStatusString);
        addressText.setText(address.getStreet()+" "+ address.getWard() +" "+address.getDistrict()+ " "+address.getProvince());
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String formattedExpiryDate = format.format(orderDateString);
        timeText.setText(formattedExpiryDate);
        deliveryText.setText(String.format("%.0f",deliveryFee)+" đ");
        tipText.setText(String.format("%.0f",tip)+" đ");
        totalText.setText(String.format("%.0f",total)+" đ");
        if(deliveryStatus.equals("delivery")){
            deliveryMethodText.setText("Delivery");
        }else if(deliveryStatus.equals("instore")){
            deliveryMethodText.setText("In store");
        }else if(deliveryStatus.equals("pickup")){
            deliveryMethodText.setText("Pick up");
        }

        if(orderStatusString.equals("pending")){
            statusText.setText("Pending");
            statusText.setTextColor(context.getColor(R.color.yellow));
        }else if(orderStatusString.equals("done")){
            statusText.setText("Done");
            statusText.setTextColor(context.getColor(R.color.green1));
        }else if(orderStatusString.equals("cancelled")){
            statusText.setText("Cancelled");
            statusText.setTextColor(context.getColor(R.color.black));
        }

        productRecycleView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));

        getProductData(productList,productRecycleView);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    private void getProductData(List<Product> productKeyList, RecyclerView recyclerView){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference productRef = firebaseDatabase.getReference("product");
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Product> productList1=new ArrayList<>();
                double tempTotal=0;
                for(DataSnapshot productSnap: snapshot.getChildren()){
                    String productName=productSnap.child("name").getValue(String.class);
                    double productPrice=productSnap.child("price").getValue(double.class);
                    Product productKey=new Product(productSnap.getKey(),productName,1);
                    if(checkKeyInList(productKey,productKeyList)){
                        String key=productSnap.getKey();
                        int productQuantity=getQuantity(productKey,productKeyList);
                        String productImg=productSnap.child("img").getValue(String.class);
                        double productTotal=productPrice*productQuantity;
                        tempTotal=tempTotal+productTotal;
                        Product product=new Product(productName,productTotal, productImg,productQuantity, key);
                        productList1.add(product);
                    }
                }
                productAdapter=new ProductHorizontalAdapter(productList1);
                recyclerView.setAdapter(productAdapter);
                subtotalText.setText(String.format("%.0f",tempTotal));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public int getQuantity(Product key, List<Product> keyList){
        for (Product item : keyList) {
            if (item.getKey().equals(key.getKey())) {
                return item.getQuantity();
            }
        }
        return 1;
    }
    public boolean checkKeyInList(Product key, List<Product> keyList) {
        for (Product item : keyList) {
            if (item.getKey().equals(key.getKey())) {
                return true;
            }
        }
        return false;
    }
}
