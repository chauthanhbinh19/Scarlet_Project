package com.example.scarlet;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ChangePasswordActivity extends AppCompatActivity {

    RelativeLayout back_btn;
    TextView email;
    Button changePasswordBtn;
    private void BindView(){
        back_btn=findViewById(R.id.back_btn);
        email=findViewById(R.id.email);
        changePasswordBtn=findViewById(R.id.changePasswordBtn);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);

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
        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
    }
    private void changePassword(){
        String emailText=email.getText().toString();
        if(emailText.isEmpty()){
            email.setError("Username can not be empty");
        }else{
            FirebaseAuth.getInstance().sendPasswordResetEmail(emailText)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(ChangePasswordActivity.this, "Email khôi phục mật khẩu đã được gửi", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ChangePasswordActivity.this, "Email khôi phục mật khẩu đã lỗi", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}