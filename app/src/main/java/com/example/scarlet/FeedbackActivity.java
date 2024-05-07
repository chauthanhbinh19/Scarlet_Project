package com.example.scarlet;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.scarlet.Data.Feedback;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class FeedbackActivity extends AppCompatActivity {

    RelativeLayout back_btn;
    EditText feedback;
    Button feedbackBtn;
    private void BindView(){
        back_btn=findViewById(R.id.back_btn);
        feedback=findViewById(R.id.feedback);
        feedbackBtn=findViewById(R.id.feedbackBtn);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);

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
        feedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String feedbackString=feedback.getText().toString();
                if(feedbackString==null || feedbackString.isEmpty()){
                    feedback.setError("Feedback can not be empty");
                }else{
                    saveFeedBack();
                }
            }
        });
    }
    private void saveFeedBack(){
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn=sharedPreferences.getBoolean("isLoggedIn",false);
        String userKey=sharedPreferences.getString("customerKey","");
        if(isLoggedIn && !userKey.isEmpty()){
            FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
            DatabaseReference myRef=firebaseDatabase.getReference("feedback");
            String feedbackString=feedback.getText().toString();
            Feedback feedback1=new Feedback(userKey,feedbackString);
            myRef.push().setValue(feedback1);
            Toast.makeText(FeedbackActivity.this,"Send feedback successfully",Toast.LENGTH_SHORT).show();
            feedback.setText("");
        }
    }
}