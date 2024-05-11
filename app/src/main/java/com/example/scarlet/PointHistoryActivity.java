package com.example.scarlet;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Adapter.GridLayoutDecoration;
import com.example.scarlet.Adapter.OfferAdapter;
import com.example.scarlet.Data.Offer;
import com.example.scarlet.Interface.GetPointCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class PointHistoryActivity extends AppCompatActivity {

    private OfferAdapter offerAdapter;
    private List<Offer> offerList;
    RelativeLayout back_btn;
    TextView voucher_point, voucher_text_1;
    RecyclerView recyclerView;
    GetPointCallback getPointCallback;
    private void BindView(){
        back_btn=findViewById(R.id.back_btn);
        recyclerView=findViewById(R.id.exchange_point_history_recyclerView);
        voucher_point=findViewById(R.id.voucher_point);
        voucher_text_1=findViewById(R.id.voucher_text_1);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.point_history);

        Window window = getWindow();
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.white));

        BindView();
        recyclerView.setLayoutManager(new GridLayoutManager(PointHistoryActivity.this,1));
        recyclerView.addItemDecoration(new GridLayoutDecoration(0,10));
        getPointCallback=new GetPointCallback() {
            @Override
            public void itemClick(int point, int type) {

            }
        };
        getPointHistoryData(recyclerView,getPointCallback);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }
    private void getPointHistoryData(RecyclerView recyclerView, GetPointCallback getPointCallback){
        SharedPreferences sharedPreferences =getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn=sharedPreferences.getBoolean("isLoggedIn",false);
        String userKey=sharedPreferences.getString("customerKey","");
        if(isLoggedIn && !userKey.isEmpty()){
            offerList=new ArrayList<>();
            FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
            DatabaseReference offerRef=firebaseDatabase.getReference("offer");
            DatabaseReference offerTransactionRef=firebaseDatabase.getReference("offer_transaction");
            offerTransactionRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot1) {
                    if(snapshot1.exists()){
                        for(DataSnapshot snap1:snapshot1.getChildren()){
                            if(snap1.child("customerId").getValue(String.class).equals(userKey)){
                                String offerId=snap1.child("offerId").getValue(String.class);
                                offerRef.child(offerId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                        if(snapshot2.exists()){
                                                String offerName=snapshot2.child("name").getValue(String.class);
                                                String offerDescription=snapshot2.child("description").getValue(String.class);
                                                int offerPoint=snapshot2.child("point").getValue(int.class);
                                                int offerImage=snapshot2.child("image").getValue(int.class);
                                                Offer offer=new Offer(offerName,offerDescription,offerPoint,offerImage);
                                                offerList.add(offer);
                                        }
                                        if(offerList.size()>0){
                                            offerAdapter=new OfferAdapter(offerList,getPointCallback);
                                            recyclerView.setAdapter(offerAdapter);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
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

}