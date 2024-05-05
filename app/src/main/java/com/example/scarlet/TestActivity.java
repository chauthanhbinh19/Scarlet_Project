package com.example.scarlet;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Adapter.GridLayoutDecoration;
import com.example.scarlet.Adapter.ReviewAdapter;
import com.example.scarlet.Data.Product;
import com.example.scarlet.Data.Review;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TestActivity extends AppCompatActivity {

    RelativeLayout back_btn;
    RecyclerView reviewRecycleView;
    List<Review> reviewList;
    ReviewAdapter adapter;
    private void BindView(){
        reviewRecycleView=findViewById(R.id.review_recyclerView);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        Window window = getWindow();
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.burgundy));
        
        BindView();
        reviewRecycleView.setLayoutManager(new GridLayoutManager(this,1));
        reviewRecycleView.addItemDecoration(new GridLayoutDecoration(5,10));
        getReview();
    }
    private void getReview(){
        reviewList=new ArrayList<>();
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference myRef=firebaseDatabase.getReference("review");
        DatabaseReference userRef=firebaseDatabase.getReference("user");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reviewList.clear();
                for(DataSnapshot snap:snapshot.getChildren()){
                    String productId=snap.child("productId").getValue(String.class);
                    if(productId.equals("-NvAv7eI7xUbsQMgDn90")){
                        String customerId=snap.child("customerId").getValue(String.class);
                        int rating=snap.child("rating").getValue(int.class);
                        String comment=snap.child("comment").getValue(String.class);
                        Date date=snap.child("date").getValue(Date.class);
                        userRef.child(customerId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                if(snapshot1.exists()){
                                    String id=snapshot1.getKey();
                                        String first_name=snapshot1.child("first_name").getValue(String.class);
                                        String last_name=snapshot1.child("last_name").getValue(String.class);
                                        String image=snapshot1.child("avatar_img").getValue(String.class);

                                        Review review=new Review(customerId,"-NvAv7eI7xUbsQMgDn90",rating,comment,date,last_name+" "+first_name,image);
                                        reviewList.add(review);
                                    if(reviewList.size()>0){
                                        List<Review> limitedReviewList = reviewList.subList(0, Math.min(reviewList.size(), 5));
                                        adapter=new ReviewAdapter(limitedReviewList);
                                        reviewRecycleView.setAdapter(adapter);
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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}