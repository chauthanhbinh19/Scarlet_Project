package com.example.scarlet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class ForgotPasswordActivity extends AppCompatActivity {

    EditText username,newPassword,confirmNewPassword;
    ImageButton back_btn;
    Button change_password;
    private void BindView(){
        username=findViewById(R.id.username);
        back_btn=(ImageButton) findViewById(R.id.back_btn);
        change_password=(Button) findViewById(R.id.change_passsword_button);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);

        Window window = getWindow();
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.white));

        BindView();
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ForgotPasswordActivity.this,SignInActivity.class);
                startActivity(intent);
            }
        });
        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameText=username.getText().toString();

                if(usernameText.isEmpty()){
                    username.setError("Username can not be empty");
                }else{
//                    changePassword(usernameText, confirmNewPasswordText);
                    FirebaseAuth.getInstance().sendPasswordResetEmail(usernameText)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(ForgotPasswordActivity.this, "Email khôi phục mật khẩu đã được gửi", Toast.LENGTH_SHORT).show();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ForgotPasswordActivity.this, "Email khôi phục mật khẩu đã lỗi", Toast.LENGTH_SHORT).show();
                                }
                            });
                }

            }
        });

    }
//    private void changePassword(String usernameText,String confirmNewPasswordText){
//        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
//        Query myRef=firebaseDatabase.getReference("user").orderByChild("username").equalTo(usernameText);
//        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    for(DataSnapshot snap : snapshot.getChildren()){
//                        Map<String, Object> updatePassword = new HashMap<>();
//                        updatePassword.put("password", confirmNewPasswordText);
//                        snap.getRef().updateChildren(updatePassword);
//                        Toast.makeText(ForgotPasswordActivity.this,"Successfully",Toast.LENGTH_SHORT).show();
//
//                        Intent intent=new Intent(ForgotPasswordActivity.this,SignInActivity.class);
//                        startActivity(intent);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
}