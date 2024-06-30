package com.example.scarlet;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.scarlet.Adapter.GridLayoutDecoration;
import com.example.scarlet.Adapter.ProductCheckboxAdapter;
import com.example.scarlet.Data.Category;
import com.example.scarlet.Data.Deal;
import com.example.scarlet.Data.Product;
import com.example.scarlet.Dialog.DatePickerDialog;
import com.example.scarlet.Interface.GetKeyCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class AdminEditVoucherActivity extends AppCompatActivity {

    RelativeLayout back_btn;
    RecyclerView productRecycleView;
    ImageView btnImage;
    EditText voucherName, voucherDiscount, voucherDiscription;
    Button btnSave;
    ProgressDialog progressDialog;
    String vouchername,voucherdiscount, voucherdiscription, expirydate, key;
    Uri uri;
    ImageView calendar;
    RadioButton radioProduct, radioAll;
    ScrollView scrollView;
    TextView expiryDate;
    GetKeyCallback getKeyCallback;
    List<String> keyList;
    List<Product> productList;
    RadioGroup deliveryMethod;
    String deliverymethodText="delivery";
    ProductCheckboxAdapter productCheckboxAdapter;
    private RadioButton radioDelivery, radioPickup, radioInstore;
    private void BindView(){
        back_btn=findViewById(R.id.back_btn);
        btnSave=findViewById(R.id.btnSave);
        voucherName = findViewById(R.id.voucherName);
        deliveryMethod=findViewById(R.id.deliveryMethod);
        voucherDiscount=findViewById(R.id.voucherDiscount);
        voucherDiscription=findViewById(R.id.voucherDescription);
        expiryDate=findViewById(R.id.voucherExpiryDate);
        productRecycleView=findViewById(R.id.product_recyclerView);
        calendar=findViewById(R.id.calendar);
        radioProduct=findViewById(R.id.radioProduct);
        radioAll=findViewById(R.id.radioAll);
        scrollView=findViewById(R.id.scrollProduct);
        radioDelivery = findViewById(R.id.radioDelivery);
        radioPickup = findViewById(R.id.radioPickup);
        radioInstore=findViewById(R.id.radioInstore);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_edit_voucher);

        Window window = getWindow();
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.burgundy));

        BindView();
        deliveryMethod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.radioDelivery){
                    deliverymethodText="delivery";
                }else if(checkedId==R.id.radioPickup){
                    deliverymethodText="pickup";
                }else if(checkedId==R.id.radioInstore){
                    deliverymethodText="instore";
                }
            }
        });
        Intent intent=new Intent();
        if(intent!= null){
            vouchername= getIntent().getStringExtra("voucherName");
            voucherdiscount=getIntent().getStringExtra("voucherDiscount");
            deliverymethodText=getIntent().getStringExtra("deliveryMethod");
            voucherdiscription=getIntent().getStringExtra("voucherDiscription");
            expirydate=getIntent().getStringExtra("expiryDate");
            key=getIntent().getStringExtra("voucherKey");
            voucherName.setText(vouchername);
            voucherDiscount.setText(voucherdiscount);
            if (deliverymethodText.equals("delivery")) {
                radioDelivery.setChecked(true); // Đánh dấu RadioButton Delivery là đã chọn
            } else if (deliverymethodText.equals("pickup")) {
                radioPickup.setChecked(true); // Đánh dấu RadioButton Pickup là đã chọn
            }else if (deliverymethodText.equals("instore")) {
                radioInstore.setChecked(true); // Đánh dấu RadioButton Pickup là đã chọn
            }
            voucherDiscription.setText(voucherdiscription);
            expiryDate.setText(expirydate);
        }
        productRecycleView.setLayoutManager(new GridLayoutManager(AdminEditVoucherActivity.this,1));
        productRecycleView.addItemDecoration(new GridLayoutDecoration(5,5));
        getKeyCallback=new GetKeyCallback() {
            @Override
            public void itemClick(String key, int type) {
                if(type==1){
                    keyList.add(key);
                }else{
                    keyList.remove(key);
                }
            }
        };
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        getProductData(getKeyCallback,productRecycleView);
        radioProduct.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    scrollView.setVisibility(View.VISIBLE);
                }
            }
        });
        radioAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    scrollView.setVisibility(View.GONE);
                }
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(voucherName.getText().toString().isEmpty()){
                    voucherName.setError("Voucher name can not be empty");
                }else if(voucherDiscount.getText().toString().isEmpty()){
                    voucherDiscount.setError("Delivery discount can not be empty");
                }else if(voucherDiscription.getText().toString().isEmpty()){
                    voucherDiscription.setError("Voucher description can not be empty");
                }else if(expiryDate.getText().toString().isEmpty()){
                    expiryDate.setError("Expiry date can not be empty");
                }else{
                    saveVoucherData();
                }
            }
        });
    }
    public void showDatePickerDialog(){
        DatePickerDialog newFragment=new DatePickerDialog();
        newFragment.setTextDate(expiryDate);
        newFragment.show(getSupportFragmentManager(), "DatePicker");
    }
    private void getProductData(GetKeyCallback getKeyCallback, RecyclerView productRecycleView){
        productList=new ArrayList<>();
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference myRef=firebaseDatabase.getReference("product");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap: snapshot.getChildren()){
                    String productName=snap.child("name").getValue(String.class);
                    String key=snap.getKey();
                    Product product=new Product(key,productName);
                    productList.add(product);
                }
                productCheckboxAdapter=new ProductCheckboxAdapter(productList, getKeyCallback);
                productRecycleView.setAdapter(productCheckboxAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void saveVoucherData(){
        progressDialog=new ProgressDialog(AdminEditVoucherActivity.this);
        progressDialog.setTitle("Uploading file...");
        progressDialog.show();

        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference myRef= firebaseDatabase.getReference("deal").child(key);
        String vouchername=voucherName.getText().toString();
        int discount=Integer.parseInt(voucherDiscount.getText().toString());
        String voucherdescription=voucherDiscription.getText().toString();
        String deliverymethod=deliverymethodText;
        String date=expiryDate.getText().toString();
        Date expirydate;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            expirydate = dateFormat.parse(date);
            myRef.child("name").setValue(vouchername);
            myRef.child("discount").setValue(discount);
            myRef.child("deliveryMethod").setValue(deliverymethod);
            myRef.child("description").setValue(voucherdescription);
            myRef.child("expiryDate").setValue(expirydate);
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
//            Toast.makeText(AdminEditVoucherActivity.this,"Save successfully", Toast.LENGTH_SHORT).show();
            showSuccessDialog();
        } catch (ParseException e) {
            e.printStackTrace(); // Handle parsing exception
        }
    }
    public void showSuccessDialog(){
        final Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.success_dialog_2);

        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_circle_white_30));
        dialog.show();
    }
    public void showFailedDialog(){
        final Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.failed_dialog);

        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_circle_white_30));
        dialog.show();
    }
}