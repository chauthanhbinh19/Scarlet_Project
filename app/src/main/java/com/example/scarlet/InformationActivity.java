package com.example.scarlet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.scarlet.Data.Address;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class InformationActivity extends AppCompatActivity {

    EditText street,ward,province,district,postalCode, additionalInfo;
    TextView totalView;
    Button continue_btn;
    RadioButton delivery_radio_btn,pickup_radio_btn;
    List<Address> addressList;
    String deliveryStatus="delivery";
    String total;
    RelativeLayout back_btn;
    private void BindView(){
        back_btn=findViewById(R.id.back_btn);
        continue_btn=findViewById(R.id.continue_btn);
        street=findViewById(R.id.street);
        ward=findViewById(R.id.ward);
        district=findViewById(R.id.district);
        province=findViewById(R.id.province);
        postalCode=findViewById(R.id.postalCode);
        additionalInfo=findViewById(R.id.addtionalInfo);
        delivery_radio_btn=findViewById(R.id.delivery_radio_btn);
        pickup_radio_btn=findViewById(R.id.pickup_radio_btn);
        totalView=findViewById(R.id.total);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information);

        BindView();
        getAddressData();
        Intent intent=getIntent();
        if(intent!= null){
            String totalT=intent.getStringExtra("total");
            if(totalT!= null){
                totalView.setText(totalT);
                total=totalT;
            }
        }
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pickup_radio_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(delivery_radio_btn.isChecked()){
                    delivery_radio_btn.setChecked(false);
                }
                deliveryStatus="delivery";
            }
        });
        delivery_radio_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pickup_radio_btn.isChecked()){
                    pickup_radio_btn.setChecked(false);
                }
                deliveryStatus="pickup";
            }
        });
        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(street.getText().toString().isEmpty()){
                    street.setError("Street can not be empty");
                }else if(ward.getText().toString().isEmpty()){
                    ward.setError("Ward can not be empty");
                }else if(district.getText().toString().isEmpty()){
                    district.setError("District can not be empty");
                }else if(province.getText().toString().isEmpty()){
                    province.setError("Province can not be empty");
                }else{
                    String streetText=street.getText().toString();
                    String wardText=ward.getText().toString();
                    String districtText=district.getText().toString();
                    String provinceText=province.getText().toString();
                    String postalCodeText=postalCode.getText().toString();
                    String additionalInfoText=additionalInfo.getText().toString();
                    setAddressData(streetText,wardText,districtText,provinceText,postalCodeText,additionalInfoText);
                    Intent intent=new Intent(InformationActivity.this, DeliveryActivity.class);
                    intent.putExtra("deliveryStatus",deliveryStatus);
                    intent.putExtra("street",streetText);
                    intent.putExtra("ward",wardText);
                    intent.putExtra("district",districtText);
                    intent.putExtra("province",provinceText);
                    intent.putExtra("postalCode",postalCodeText);
                    intent.putExtra("additionalInfo",additionalInfoText);
                    intent.putExtra("total",total);
                    startActivity(intent);
                }
            }
        });
    }
    private void getAddressData(){
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn=sharedPreferences.getBoolean("isLoggedIn",false);
        String userKey=sharedPreferences.getString("customerKey","");

        if(isLoggedIn && !userKey.isEmpty()){
            FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
            DatabaseReference myRef=firebaseDatabase.getReference("address");
            addressList=new ArrayList<>();
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        for(DataSnapshot snap:snapshot.getChildren()){
                            String customerId=snap.child("customerId").getValue(String.class);
                            if(customerId.equals(userKey)){
                                String streetText=snap.child("street").getValue(String.class);
                                String wardText=snap.child("ward").getValue(String.class);
                                String districtText=snap.child("district").getValue(String.class);
                                String provinceText=snap.child("province").getValue(String.class);
                                String postalCodeText=snap.child("postalCode").getValue(String.class);
                                String additionalText=snap.child("additional").getValue(String.class);

                                street.setText(streetText);
                                ward.setText(wardText);
                                district.setText(districtText);
                                province.setText(provinceText);
                                postalCode.setText(postalCodeText);
                                additionalInfo.setText(additionalText);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    private void setAddressData(String street, String ward, String district,String province, String postalCode, String additionalInfo){
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn=sharedPreferences.getBoolean("isLoggedIn",false);
        String userKey=sharedPreferences.getString("customerKey","");

        if(isLoggedIn && !userKey.isEmpty()){
            FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
            DatabaseReference myRef=firebaseDatabase.getReference("address");
            addressList=new ArrayList<>();
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean found=false;
                    if(snapshot.exists()){
                        for(DataSnapshot snap:snapshot.getChildren()){
                            String customerId=snap.child("customerId").getValue(String.class);
                            if(customerId.equals(userKey)){
                                found=true;
                            }
                        }
                        if(!found){
                            Address address=new Address(userKey,street,ward,district,province,postalCode,"","",additionalInfo);
                            String key=myRef.push().getKey();
                            myRef.child(key).setValue(address);
                        }
                    }else{
                        Address address=new Address(userKey,street,ward,district,province,postalCode,"","",additionalInfo);
                        myRef.push().setValue(address);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

}