package com.example.scarlet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Adapter.DealAdapter;
import com.example.scarlet.Adapter.DealSecondAdapter;
import com.example.scarlet.Adapter.GridLayoutDecoration;
import com.example.scarlet.Data.Deal;
import com.example.scarlet.Interface.GetKeyCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class SelectVoucherActivity extends AppCompatActivity {

    RelativeLayout back_btn;
    private DealSecondAdapter dealAdapter;
    private List<Deal> dealList;
    RecyclerView recyclerView;
    String deliveryStatus;
    List<String> voucherList;
    GetKeyCallback getKeyCallback;
    Button chooseBtn;
    private void BindView(){
        back_btn=findViewById(R.id.back_btn);
        chooseBtn=findViewById(R.id.chooseBtn);
        recyclerView=findViewById(R.id.voucher_recyclerView);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discount);

        Window window = getWindow();
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.burgundy));

        Intent intent=getIntent();
        if(intent!= null){
            deliveryStatus=intent.getStringExtra("deliveryStatus");
        }
        BindView();
        recyclerView.setLayoutManager(new GridLayoutManager(SelectVoucherActivity.this,1));
        recyclerView.addItemDecoration(new GridLayoutDecoration(0,5));
        voucherList=new ArrayList<>();
        getKeyCallback=new GetKeyCallback() {
            @Override
            public void itemClick(String key, int type) {
                if(type==1){
                    voucherList.add(key);
                }else{
                    voucherList.remove(key);
                }
            }
        };
        getVoucherData(recyclerView, deliveryStatus);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        chooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent();
                Bundle bundle=new Bundle();
                ArrayList<String> convertedArrayList = new ArrayList<>(voucherList);
                bundle.putStringArrayList("voucherList",convertedArrayList);
                intent1.putExtras(bundle);
                setResult(SelectVoucherActivity.RESULT_OK,intent1);
//                finish();
                SelectVoucherActivity.super.onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }
    private void getVoucherData(RecyclerView recyclerView, String deliveryMethodClicked){
        dealList=new ArrayList<>();
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        Query Query= firebaseDatabase.getReference("deal");
        Query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot snap: snapshot.getChildren()){
                        String voucherName=snap.child("name").getValue(String.class);
                        int discount=snap.child("discount").getValue(int.class);
                        Date expiryDate=snap.child("expiryDate").getValue(Date.class);
                        String deliveryMethod=snap.child("deliveryMethod").getValue(String.class);
                        int deliveryMethodIcon=1;
                        if(deliveryMethod.equals("delivery")){
                            deliveryMethodIcon=R.drawable.delivery_bike;
                        }else if(deliveryMethod.equals("instore")){
                            deliveryMethodIcon=R.drawable.store;
                        }else if(deliveryMethod.equals("pickup")){
                            deliveryMethodIcon=R.drawable.food_delivery;
                        }
                        String description=snap.child("description").getValue(String.class);
                        String key=snap.getKey();
                        Deal deal=new Deal(voucherName,discount,expiryDate,deliveryMethod,deliveryMethodIcon,description,key);
                        if(deliveryMethod.equals(deliveryMethodClicked)){
                            dealList.add(deal);
                        }
                    }
                    if(dealList.size()>0){
                        dealAdapter=new DealSecondAdapter(dealList,getKeyCallback);
                        recyclerView.setAdapter(dealAdapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}