package com.example.scarlet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.scarlet.Data.Review;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;


public class ReviewActivity extends AppCompatActivity {

    RelativeLayout back_btn;
    ImageView star1, star2, star3, star4, star5, imageBtn;
    int star=0;
    Button reviewBtn;
    TextView productName;
    EditText review;
    String keyString;
    private void BindView(){
        back_btn=findViewById(R.id.back_btn);
        star1=findViewById(R.id.star1);
        star2=findViewById(R.id.star2);
        star3=findViewById(R.id.star3);
        star4=findViewById(R.id.star4);
        star5=findViewById(R.id.star5);
        reviewBtn=findViewById(R.id.reviewBtn);
        imageBtn=findViewById(R.id.imageBtn);
        productName=findViewById(R.id.productName);
        review=findViewById(R.id.review);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review);

        Window window = getWindow();
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.burgundy));
        
        BindView();
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setBackgroundResource(R.drawable.star_yellow_active);
                star2.setBackgroundResource(R.drawable.star_yellow_unactive);
                star3.setBackgroundResource(R.drawable.star_yellow_unactive);
                star4.setBackgroundResource(R.drawable.star_yellow_unactive);
                star5.setBackgroundResource(R.drawable.star_yellow_unactive);
                star=1;
            }
        });
        star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setBackgroundResource(R.drawable.star_yellow_active);
                star2.setBackgroundResource(R.drawable.star_yellow_active);
                star3.setBackgroundResource(R.drawable.star_yellow_unactive);
                star4.setBackgroundResource(R.drawable.star_yellow_unactive);
                star5.setBackgroundResource(R.drawable.star_yellow_unactive);
                star=2;
            }
        });
        star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setBackgroundResource(R.drawable.star_yellow_active);
                star2.setBackgroundResource(R.drawable.star_yellow_active);
                star3.setBackgroundResource(R.drawable.star_yellow_active);
                star4.setBackgroundResource(R.drawable.star_yellow_unactive);
                star5.setBackgroundResource(R.drawable.star_yellow_unactive);
                star=3;
            }
        });
        star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setBackgroundResource(R.drawable.star_yellow_active);
                star2.setBackgroundResource(R.drawable.star_yellow_active);
                star3.setBackgroundResource(R.drawable.star_yellow_active);
                star4.setBackgroundResource(R.drawable.star_yellow_active);
                star5.setBackgroundResource(R.drawable.star_yellow_unactive);
                star=4;
            }
        });
        star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setBackgroundResource(R.drawable.star_yellow_active);
                star2.setBackgroundResource(R.drawable.star_yellow_active);
                star3.setBackgroundResource(R.drawable.star_yellow_active);
                star4.setBackgroundResource(R.drawable.star_yellow_active);
                star5.setBackgroundResource(R.drawable.star_yellow_active);
                star=5;
            }
        });
        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ReviewActivity.this, SelectProductActivity.class);
//                startActivity(intent);
                catcherForResult.launch(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        reviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment=review.getText().toString();
                if(comment==null || comment.isEmpty()){
                    review.setError("Review can not be empty");
                }else if(keyString==null || keyString.isEmpty()){
                    Toast.makeText(ReviewActivity.this,"Please choose your product", Toast.LENGTH_SHORT).show();
                }else if(star==0){
                    Toast.makeText(ReviewActivity.this,"Please rate for product", Toast.LENGTH_SHORT).show();
                }else{
                    sendReview();
                }
            }
        });
    }
    ActivityResultLauncher<Intent> catcherForResult=
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if(result.getResultCode()==SelectProductActivity.RESULT_OK){
                                Intent catcher=result.getData();
                                if(catcher!=null){
                                    String key=catcher.getStringExtra("key");
                                    if(!key.equals("0")){
                                        getProduct(key);
                                        keyString=key;
                                    }
                                }
                            }
                        }
                    });
    private void sendReview(){
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn=sharedPreferences.getBoolean("isLoggedIn",false);
        String userKey=sharedPreferences.getString("customerKey","");
        if(isLoggedIn && !userKey.isEmpty()){
            FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
            DatabaseReference reviewRef= firebaseDatabase.getReference("review");
            String comment=review.getText().toString();
            Review review1=new Review(userKey,keyString,star,comment,new Date());
            reviewRef.push().setValue(review1);
            Toast.makeText(ReviewActivity.this,"Send review successfully", Toast.LENGTH_SHORT).show();
        }
    }
    private void getProduct(String key){
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference productRef=firebaseDatabase.getReference("product");
        productRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String productname=snapshot.child("name").getValue(String.class);
                    String productimage=snapshot.child("img").getValue(String.class);

                    productName.setText(productname);
                    Glide.with(ReviewActivity.this).load(productimage).into(imageBtn);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}