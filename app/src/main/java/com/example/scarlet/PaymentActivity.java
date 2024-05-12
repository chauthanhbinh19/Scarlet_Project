package com.example.scarlet;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scarlet.Adapter.ProductHorizontalAdapter;
import com.example.scarlet.Data.Address;
import com.example.scarlet.Data.CreateOrder;
import com.example.scarlet.Data.DealTransaction;
import com.example.scarlet.Data.Order;
import com.example.scarlet.Data.Payment;
import com.example.scarlet.Data.Product;
import com.example.scarlet.Data.ProductQuantity;
import com.example.scarlet.Interface.DataReadyListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class PaymentActivity extends AppCompatActivity {

    EditText txtAmount;
    RelativeLayout back_btn;
    Button pay, c1Btn, c2Btn, c3Btn, c4Btn, voucherBtn;
    TextView totalView, voucher_name;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef, cartRef,productRef, orderRef, addressRef;
    double total;
    Address address;
    String deliveryStatus;
    TextView delivery_address_content, discount_price, voucher_total, totalAfterDiscount;
    RadioButton radioZaloPay, radioCash;
    int defaultStatus=4;
    int tip=0;
    RecyclerView productRecycleView;
    ProductHorizontalAdapter adapter;
    List<ProductQuantity> productQuantityList;
    String voucherKey="0", voucherName, voucherCode;
    double deliveryfee=0;
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
        productRecycleView=findViewById(R.id.totalProductRecycleView);
        voucherBtn=findViewById(R.id.voucherBtn);
        voucher_name=findViewById(R.id.voucher_name);
        discount_price=findViewById(R.id.discount_price);
        voucher_total=findViewById(R.id.voucher_total);
        totalAfterDiscount=findViewById(R.id.totalAfterDiscount);
        delivery_address_content=findViewById(R.id.delivery_address_content);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);

        Window window = getWindow();
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.white));

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ZaloPaySDK.init(2553, Environment.SANDBOX);

        BindView();
        Intent intent=getIntent();
        if(intent!= null){
            String totalT=intent.getStringExtra("total");
            deliveryStatus=intent.getStringExtra("deliveryStatus");
            String street=intent.getStringExtra("street");
            String ward=intent.getStringExtra("ward");
            String district=intent.getStringExtra("district");
            String province=intent.getStringExtra("province");
            String postalCode=intent.getStringExtra("postalCode");
            String additionalInfo=intent.getStringExtra("street");
            totalView.setText(totalT);
            String a=street+", "+ward+", "+district+", "+province;
            delivery_address_content.setText(a);
            if(!postalCode.isEmpty()){
                a=a+", "+postalCode;
                delivery_address_content.setText(a);
            }
            if(!additionalInfo.isEmpty()){
                a=a+", "+additionalInfo;
                delivery_address_content.setText(a);
            }
            if(deliveryStatus.equals("delivery")) {
                deliveryfee = 10000;
            }
        }
        productQuantityList=new ArrayList<>();
        productRecycleView.setLayoutManager(new LinearLayoutManager(PaymentActivity.this,LinearLayoutManager.HORIZONTAL,false));
        getCartData();
        int price=Integer.parseInt(totalView.getText().toString());
        int tip1=15*price/100;
        c1Btn.setText("15% \n " + String.valueOf(tip1) +"đ");
        int tip2=18*price/100;
        c2Btn.setText("18% \n " + String.valueOf(tip2) +"đ");
        int tip3=20*price/100;
        c3Btn.setText("20% \n " + String.valueOf(tip3) +"đ");
        getStatus();
        voucherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(PaymentActivity.this, SelectVoucherActivity.class);
                intent1.putExtra("deliveryStatus",deliveryStatus);
//                startActivity(intent1);
                catcherForResult.launch(intent1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
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
                if (voucherKey.equals("0")) {
                    if(radioCash.isChecked()){
                        Payment payment=new Payment();
                        payment.setType("Cash");
                        getCartDataToPay(payment);
                    }else if(radioZaloPay.isChecked()){
                        requestZaloPay();
                    }
                } else if (!voucherKey.equals("0")) {
                    if(radioCash.isChecked()){
                        Payment payment=new Payment();
                        payment.setType("Cash");
                        checkOrderVoucher(payment,1);
                    }else if(radioZaloPay.isChecked()){
                        requestZaloPayVoucher();
                    }
                }

            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

    }
    ActivityResultLauncher<Intent> catcherForResult=
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if(result.getResultCode()==SelectVoucherActivity.RESULT_OK){
                                Intent catcher=result.getData();
                                if(catcher!=null){
                                    voucherKey=catcher.getStringExtra("voucherKey");
                                    voucherName=catcher.getStringExtra("voucherName");
                                    voucherCode=catcher.getStringExtra("voucherCode");
                                    if(!voucherKey.equals("0")){
                                        voucher_name.setText(voucherName);
                                        voucher_name.setVisibility(View.VISIBLE);
                                        discount_price.setVisibility(View.VISIBLE);
                                        voucher_total.setVisibility(View.VISIBLE);
                                        totalAfterDiscount.setVisibility(View.VISIBLE);
                                        checkOrderVoucher(new Payment(),2);
                                    }else{
                                        voucher_name.setVisibility(View.GONE);
                                        discount_price.setVisibility(View.GONE);
                                        voucher_total.setVisibility(View.GONE);
                                        totalAfterDiscount.setVisibility(View.GONE);
                                    }
                                }
                            }
                        }
                    });
    private void getCartData(){
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
                            }
                        }
                        getProductData(productQuantityList, userKey);
                    }else{

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    private void getProductData(List<ProductQuantity> productKeyList, String userKey){

        firebaseDatabase = FirebaseDatabase.getInstance();
        productRef = firebaseDatabase.getReference("product");
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Product> productList=new ArrayList<>();
                for(ProductQuantity pq:productKeyList){
                    for(DataSnapshot productSnap: snapshot.getChildren()){
                        String key=productSnap.getKey();

                        if(pq.getProductId().equals(key)){
                            String productName=productSnap.child("name").getValue(String.class);
                            double productPrice=productSnap.child("price").getValue(double.class);
                            int productQuantity=pq.getQuantity();
                            String productImg=productSnap.child("img").getValue(String.class);
                            double productTotal=productPrice*productQuantity;
                            String size=pq.getSize();
                            Product product=new Product(productName,productTotal, productImg, productQuantity,key,size);
                            productList.add(product);
                        }
                    }
                }
                adapter=new ProductHorizontalAdapter(productList);
                productRecycleView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
        total=Double.parseDouble(totalView.getText().toString());
        total=total+tip;
        try {
            JSONObject data = orderApi.createOrder(String.format("%.0f", total));
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
                        getCartDataToPay(payment);
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
    private void requestZaloPayVoucher(){
        CreateOrder orderApi = new CreateOrder();
        total=Double.parseDouble(totalAfterDiscount.getText().toString());
        total=total+tip;
        try {
            JSONObject data = orderApi.createOrder(String.format("%.0f", total));
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
                        checkOrderVoucher(payment,1);
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
    private void getCartDataToPay(Payment payment){
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
                        getProductDataToPay(productQuantityList, userKey, payment);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    private void getProductDataToPay(List<ProductQuantity> productKeyList, String userKey, Payment payment){
        firebaseDatabase = FirebaseDatabase.getInstance();
        productRef = firebaseDatabase.getReference("product");
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double subTotal=0;
                int totalPoint=0;
                List<Product> productList=new ArrayList<>();
                for(ProductQuantity pq:productKeyList){
                    for(DataSnapshot productSnap: snapshot.getChildren()){
                        String key=productSnap.getKey();

                        if(pq.getProductId().equals(key)){
                            String productName=productSnap.child("name").getValue(String.class);
                            double productPrice=productSnap.child("price").getValue(double.class);
                            int productQuantity=pq.getQuantity();
                            int discount=pq.getDiscount();
                            int point=productSnap.child("point").getValue(int.class);
                            String size=pq.getSize();
                            totalPoint=totalPoint+point*productQuantity;
                            String categoryName=productSnap.child("categoryName").getValue(String.class);
                            double productTotal=(productPrice*productQuantity)-(productPrice*productQuantity*discount/100);
                            subTotal=subTotal+productTotal;
                            Product product=new Product(productName,productPrice,productQuantity,productTotal, categoryName, discount, key,size);
                            productList.add(product);
                        }
                    }
                }
                subTotal=subTotal+tip+deliveryfee;
                saveOrder(productList,userKey, payment,subTotal);
                savePoint(totalPoint);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void saveOrder(List<Product> productList, String userKey, Payment payment, double subTotal){
        firebaseDatabase=FirebaseDatabase.getInstance();
        orderRef=firebaseDatabase.getReference("order");
        addressRef=firebaseDatabase.getReference("address");
        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String orderStatus="pending";
                Date orderDate=new Date();
//                Date orderDate=randomDate();
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
                                Order order=new Order(userKey,orderStatus,payment,address,orderDate,subTotal,tip,deliveryStatus,deliveryfee,productList);
                                String key=orderRef.push().getKey();
                                orderRef.child(key).setValue(order);
                                if(!voucherKey.equals("0")){
                                    FirebaseDatabase firebaseDb=FirebaseDatabase.getInstance();
                                    DatabaseReference Dr=firebaseDb.getReference("deal_transaction");
                                    DealTransaction dealTransaction=new DealTransaction(voucherCode,userKey,key);
                                    Dr.push().setValue(dealTransaction);
                                }
                                Intent intent=new Intent(PaymentActivity.this, OrderNotificationActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
    private Date randomDate(){
        Random random = new Random();
        int month = random.nextInt(12) + 1;

        int startYear = 2023;
        int endYear = 2024;
        int year = random.nextInt(endYear - startYear + 1) + startYear;
        if(year==2024 && month >4){
            while(month>4){
                month = random.nextInt(12) + 1;
            }
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1); // Trừ đi 1 vì tháng bắt đầu từ 0
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        Date randomDate=calendar.getTime();
        return randomDate;
    }
    private void checkOrderVoucher(Payment payment, int vcStatus){
        firebaseDatabase=FirebaseDatabase.getInstance();
        myRef=firebaseDatabase.getReference("deal");
        myRef.child(voucherKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> productIdList = new ArrayList<>();
                int discount=snapshot.child("discount").getValue(int.class);
                DataSnapshot dataSnapshot = snapshot.child("productId");
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String productId = childSnapshot.getValue(String.class);
                    productIdList.add(productId);
                }
                getProductBaseOnVoucherList(productIdList, discount, payment, vcStatus);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getProductBaseOnVoucherList(List<String> productIdList, int discount, Payment payment, int vcStatus){
        List<Product> tempProductList=new ArrayList<>();
        firebaseDatabase=FirebaseDatabase.getInstance();
        myRef=firebaseDatabase.getReference("product");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean allChecked=false;
                int statusVoucher=1;
                for(DataSnapshot snap:snapshot.getChildren()){
                    String key=snap.getKey();
                    String name=snap.child("name").getValue(String.class);
                    double price=snap.child("price").getValue(double.class);
                    Product pr=new Product(key,name,price);
                    if(checkKeyInProductIdList(key,productIdList)){
                        tempProductList.add(pr);
                        allChecked=true;
                    }
                }
                if(!allChecked){
                    statusVoucher=2;
                }
                getCartForVoucher(tempProductList,discount, payment, statusVoucher, vcStatus);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getCartForVoucher(List<Product> productList, int discount, Payment payment, int status, int vcStatus){
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn=sharedPreferences.getBoolean("isLoggedIn",false);
        String userKey=sharedPreferences.getString("customerKey","");

        if(isLoggedIn && !userKey.isEmpty()){
            firebaseDatabase= FirebaseDatabase.getInstance();
            cartRef=firebaseDatabase.getReference("cart");
            cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        productQuantityList.clear();
                        for(DataSnapshot cartSnap: snapshot.getChildren()){
                            String customerId=cartSnap.child("customerId").getValue(String.class);
                            if(customerId.equals(userKey)){
                                DataSnapshot productQuantityListSnapshot =cartSnap.child("productQuantityList");
                                for(DataSnapshot snapshot1:productQuantityListSnapshot.getChildren()){
                                    ProductQuantity productQuantity=snapshot1.getValue(ProductQuantity.class);
                                    if(status==1){
                                        if(checkKeyInListV2(productQuantity,productList)){
                                            productQuantity.setDiscount(discount);
                                        }
                                    }else{
                                        productQuantity.setDiscount(discount);
                                    }
                                    productQuantityList.add(productQuantity);
                                }
                                if(vcStatus==1){
                                    cartSnap.child("productQuantityList").getRef().removeValue();
                                }
                            }
                        }
                        if(vcStatus==1){
                            getProductDataToPay(productQuantityList, userKey, payment);
                        }else{
                            changeTotalTextView(productQuantityList);
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    private void changeTotalTextView(List<ProductQuantity> productKeyList){
        firebaseDatabase = FirebaseDatabase.getInstance();
        productRef = firebaseDatabase.getReference("product");
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double subTotal=0;
                int totalPoint=0;
                List<Product> productList=new ArrayList<>();
                for(DataSnapshot productSnap: snapshot.getChildren()){
                    ProductQuantity productKey=new ProductQuantity(productSnap.getKey(),1);
                    if(checkKeyInList(productKey,productKeyList)){
                        String productName=productSnap.child("name").getValue(String.class);
                        double productPrice=productSnap.child("price").getValue(double.class);
                        int productQuantity=getQuantity(productKey,productKeyList);
                        int discount=getDiscount(productKey,productKeyList);
                        int point=productSnap.child("point").getValue(int.class);
                        String size=getSize(productKey,productKeyList);
                        totalPoint=totalPoint+point*productQuantity;
                        String key=productSnap.getKey();
                        String categoryName=productSnap.child("categoryName").getValue(String.class);
                        double productTotal=(productPrice*productQuantity)-(productPrice*productQuantity*discount/100);
                        subTotal=subTotal+productTotal;
                        Product product=new Product(productName,productPrice,productQuantity,productTotal, categoryName, discount, key,size);
                        productList.add(product);
                    }
                }
                subTotal=subTotal+tip;
                total=Double.parseDouble(totalView.getText().toString());
                double temp=total-subTotal;
                temp=-temp;
                discount_price.setText(String.format("%.0f", temp) + " đ");
                totalAfterDiscount.setText(String.format("%.0f", subTotal) +" đ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public int getQuantity(ProductQuantity key, List<ProductQuantity> keyList){
        for (ProductQuantity item : keyList) {
            if (item.getProductId().equals(key.getProductId())) {
                return item.getQuantity();
            }
        }
        return 1;
    }
    public int getDiscount(ProductQuantity key, List<ProductQuantity> keyList){
        for (ProductQuantity item : keyList) {
            if (item.getProductId().equals(key.getProductId())) {
                return item.getDiscount();
            }
        }
        return 0;
    }
    public String getSize(ProductQuantity key, List<ProductQuantity> keyList){
        for (ProductQuantity item : keyList) {
            if (item.getProductId().equals(key.getProductId())) {
                return item.getSize();
            }
        }
        return "";
    }
    public boolean checkKeyInList(ProductQuantity key, List<ProductQuantity> keyList) {
        for (ProductQuantity item : keyList) {
            if (item.getProductId().equals(key.getProductId())) {
                return true;
            }
        }
        return false;
    }
    public boolean checkKeyInListV2(ProductQuantity key, List<Product> keyList) {
        for (Product item : keyList) {
            if (item.getKey().equals(key.getProductId())) {
                return true;
            }
        }
        return false;
    }
    public boolean checkKeyInProductIdList(String key, List<String> keyList){
        for(String a: keyList){
            if(a.equals(key)){
                return true;
            }
        }
        return false;
    }
    public boolean checkNameInProductIdList(String key, List<Product> productList){
        for(Product p:productList){
            if(p.getName().equals(key)){
                return true;
            }
        }
        return false;
    }
    private void savePoint(int newPoint){
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn=sharedPreferences.getBoolean("isLoggedIn",false);
        String userKey=sharedPreferences.getString("customerKey","");

        if(isLoggedIn && !userKey.isEmpty()){
            firebaseDatabase=FirebaseDatabase.getInstance();
            myRef=firebaseDatabase.getReference("user");
            myRef.child(userKey).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        int oldPoint=snapshot.child("point").getValue(int.class);
                        int oldRankPoint=snapshot.child("rankPoint").getValue(int.class);
                        int totalPoint=newPoint+oldPoint;
                        int totalRankPoint=oldRankPoint+newPoint;
                        myRef.child(userKey).child("point").setValue(totalPoint);
                        myRef.child(userKey).child("rankPoint").setValue(totalRankPoint);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
}