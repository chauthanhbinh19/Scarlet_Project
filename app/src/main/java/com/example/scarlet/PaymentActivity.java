package com.example.scarlet;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scarlet.Data.Address;
import com.example.scarlet.Data.CreateOrder;
import com.example.scarlet.Data.Order;
import com.example.scarlet.Data.Payment;
import com.example.scarlet.Data.Product;
import com.example.scarlet.Data.ProductQuantity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class PaymentActivity extends AppCompatActivity {

    EditText txtAmount;
    RelativeLayout back_btn;
    Button pay, c1Btn, c2Btn, c3Btn, c4Btn;
    TextView totalView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef, cartRef,productRef, orderRef, addressRef;
    double total;
    Address address;
    String deliveryStatus;
    RadioButton radioZaloPay, radioCash;
    int defaultStatus=4;
    int tip=0;
    private void BindView() {
        totalView=findViewById(R.id.total);
        back_btn=findViewById(R.id.back_btn);
        pay=findViewById(R.id.btnPay);
        radioZaloPay=findViewById(R.id.radio_zaloPay);
        radioCash=findViewById(R.id.radio_cash);
        c1Btn=findViewById(R.id.c1_btn);
        c2Btn=findViewById(R.id.c2_btn);
        c3Btn=findViewById(R.id.c3_btn);
        c4Btn=findViewById(R.id.c4_btn);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ZaloPaySDK.init(2553, Environment.SANDBOX);

        BindView();
        Intent intent=getIntent();
        if(intent!= null){
            String totalT=intent.getStringExtra("total");
            deliveryStatus=intent.getStringExtra("deliveryStatus");
            totalView.setText(totalT);
        }
        int price=Integer.parseInt(totalView.getText().toString());
        int tip1=15*price/100;
        c1Btn.setText("15% \n " + String.valueOf(tip1) +"đ");
        int tip2=18*price/100;
        c2Btn.setText("18% \n " + String.valueOf(tip2) +"đ");
        int tip3=20*price/100;
        c3Btn.setText("20% \n " + String.valueOf(tip3) +"đ");
        getStatus();
        radioCash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    radioZaloPay.setChecked(false);
                }
            }
        });
        radioZaloPay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    radioCash.setChecked(false);
                }
            }
        });
        c1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultStatus=1;
                int price=Integer.parseInt(totalView.getText().toString());
                tip=15*price/100;
                getStatus();
            }
        });
        c2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultStatus=2;
                int price=Integer.parseInt(totalView.getText().toString());
                tip=18*price/100;
                getStatus();
            }
        });
        c3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultStatus=3;
                int price=Integer.parseInt(totalView.getText().toString());
                tip=20*price/100;
                getStatus();
            }
        });
        c4Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultStatus=4;
                getStatus();
            }
        });
        pay.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if(radioCash.isChecked()){
                    Payment payment=new Payment();
                    payment.setType("Cash");
                    getCartData(payment);
                }else if(radioZaloPay.isChecked()){
                    requestZaloPay();
                }
            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void getStatus(){
        switch (defaultStatus){
            case 1:
                c1Btn.setBackground(getResources().getDrawable(R.drawable.left_white_stroke_burgundy));
                c2Btn.setBackground(getResources().getDrawable(R.drawable.center_white_stroke_lightbrown));
                c3Btn.setBackground(getResources().getDrawable(R.drawable.center_white_stroke_lightbrown));
                c4Btn.setBackground(getResources().getDrawable(R.drawable.right_white_stroke_lightbrown));
                break;
            case 2:
                c1Btn.setBackground(getResources().getDrawable(R.drawable.left_white_stroke_lightbrown));
                c2Btn.setBackground(getResources().getDrawable(R.drawable.center_white_stroke_burgundy));
                c3Btn.setBackground(getResources().getDrawable(R.drawable.center_white_stroke_lightbrown));
                c4Btn.setBackground(getResources().getDrawable(R.drawable.right_white_stroke_lightbrown));
                break;
            case 3:
                c1Btn.setBackground(getResources().getDrawable(R.drawable.left_white_stroke_lightbrown));
                c2Btn.setBackground(getResources().getDrawable(R.drawable.center_white_stroke_lightbrown));
                c3Btn.setBackground(getResources().getDrawable(R.drawable.center_white_stroke_burgundy));
                c4Btn.setBackground(getResources().getDrawable(R.drawable.right_white_stroke_lightbrown));
                break;
            case 4:
                c1Btn.setBackground(getResources().getDrawable(R.drawable.left_white_stroke_lightbrown));
                c2Btn.setBackground(getResources().getDrawable(R.drawable.center_white_stroke_lightbrown));
                c3Btn.setBackground(getResources().getDrawable(R.drawable.center_white_stroke_lightbrown));
                c4Btn.setBackground(getResources().getDrawable(R.drawable.right_white_stroke_burgundy));
                break;
            default:
                c1Btn.setBackground(getResources().getDrawable(R.drawable.left_white_stroke_lightbrown));
                c2Btn.setBackground(getResources().getDrawable(R.drawable.center_white_stroke_lightbrown));
                c3Btn.setBackground(getResources().getDrawable(R.drawable.center_white_stroke_lightbrown));
                c4Btn.setBackground(getResources().getDrawable(R.drawable.right_white_stroke_burgundy));
                break;
        }
    }
    private void requestZaloPay(){
        CreateOrder orderApi = new CreateOrder();

        try {
            JSONObject data = orderApi.createOrder(totalView.getText().toString());
            String code = data.getString("return_code");
            Toast.makeText(getApplicationContext(), "return_code: " + code, Toast.LENGTH_LONG).show();

            if (code.equals("1")) {
//                        txtToken.setText(data.getString("zp_trans_token"));

                String token = data.getString("zp_trans_token");
                ZaloPaySDK.getInstance().payOrder(PaymentActivity.this, token, "demozpdk://app", new PayOrderListener() {
                    @Override
                    public void onPaymentSucceeded(final String transactionId, final String transToken, final String appTransID) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(PaymentActivity.this)
                                        .setTitle("Payment Success")
                                        .setMessage(String.format("TransactionId: %s - TransToken: %s", transactionId, transToken))
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        })
                                        .setNegativeButton("Cancel", null).show();
                            }

                        });
                        Payment payment=new Payment();
                        payment.setType("ZaloPay");
                        payment.setTransactionId(transactionId);
                        payment.setToken(transToken);
                        getCartData(payment);
                    }

                    @Override
                    public void onPaymentCanceled(String zpTransToken, String appTransID) {
                        new AlertDialog.Builder(PaymentActivity.this)
                                .setTitle("User Cancel Payment")
                                .setMessage(String.format("zpTransToken: %s \n", zpTransToken))
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setNegativeButton("Cancel", null).show();
                    }

                    @Override
                    public void onPaymentError(ZaloPayError zaloPayError, String zpTransToken, String appTransID) {
                        new AlertDialog.Builder(PaymentActivity.this)
                                .setTitle("Payment Fail")
                                .setMessage(String.format("ZaloPayErrorCode: %s \nTransToken: %s", zaloPayError.toString(), zpTransToken))
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setNegativeButton("Cancel", null).show();
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void getCartData(Payment payment){
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn=sharedPreferences.getBoolean("isLoggedIn",false);
        String userKey=sharedPreferences.getString("customerKey","");

        if(isLoggedIn && !userKey.isEmpty()){
            firebaseDatabase= FirebaseDatabase.getInstance();
            cartRef=firebaseDatabase.getReference("cart");

            cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<ProductQuantity> productQuantityList=new ArrayList<>();
                    if(snapshot.exists()){
                        for(DataSnapshot cartSnap: snapshot.getChildren()){
                            String customerId=cartSnap.child("customerId").getValue(String.class);
                            if(customerId.equals(userKey)){
                                DataSnapshot productQuantityListSnapshot =cartSnap.child("productQuantityList");

                                for(DataSnapshot snapshot1:productQuantityListSnapshot.getChildren()){
                                    ProductQuantity productQuantity=snapshot1.getValue(ProductQuantity.class);
                                    productQuantityList.add(productQuantity);
                                }
                                cartSnap.child("productQuantityList").getRef().removeValue();
                            }
                        }
                        getProductData(productQuantityList, userKey, payment);
                    }else{

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    private void getProductData(List<ProductQuantity> productKeyList, String userKey, Payment payment){

        firebaseDatabase = FirebaseDatabase.getInstance();
        productRef = firebaseDatabase.getReference("product");
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Product> productList=new ArrayList<>();
                for(DataSnapshot productSnap: snapshot.getChildren()){
                    ProductQuantity productKey=new ProductQuantity(productSnap.getKey(),1);
                    if(checkKeyInList(productKey,productKeyList)){
                        String productName=productSnap.child("name").getValue(String.class);
                        double productPrice=productSnap.child("price").getValue(double.class);
                        int productQuantity=productSnap.child("quantity").getValue(int.class);
                        double productTotal=productPrice*productQuantity;
                        Product product=new Product(productName,productPrice,productQuantity,productTotal);
                        productList.add(product);
                    }
                }
                saveOrder(productList,userKey, payment);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void saveOrder(List<Product> productList, String userKey, Payment payment){
        firebaseDatabase=FirebaseDatabase.getInstance();
        orderRef=firebaseDatabase.getReference("order");
        addressRef=firebaseDatabase.getReference("address");

        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String orderStatus="pending";
                Date orderDate=new Date();
                total=Double.parseDouble(totalView.getText().toString());
                addressRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snap:snapshot.getChildren()){
                            String customerId=snap.child("customerId").getValue(String.class);
                            if(customerId.equals(userKey)){
                                String streetText=snap.child("street").getValue(String.class);
                                String wardText=snap.child("ward").getValue(String.class);
                                String districtText=snap.child("district").getValue(String.class);
                                String provinceText=snap.child("province").getValue(String.class);
                                String postalCodeText=snap.child("postalCode").getValue(String.class);
                                String additionalText=snap.child("additional").getValue(String.class);

                                address=new Address(customerId,streetText,wardText,districtText,provinceText,postalCodeText,"","",additionalText);
                                Order order=new Order(userKey,orderStatus,payment,address,orderDate,total,tip,deliveryStatus,0,productList);
                                String key=orderRef.push().getKey();
                                orderRef.child(key).setValue(order);

                                Intent intent=new Intent(PaymentActivity.this, OrderNotificationActivity.class);
                                startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public boolean checkKeyInList(ProductQuantity key, List<ProductQuantity> keyList) {
        for (ProductQuantity item : keyList) {
            if (item.getProductId().equals(key.getProductId())) {
                return true;
            }
        }
        return false;
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
}