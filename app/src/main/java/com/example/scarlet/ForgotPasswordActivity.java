package com.example.scarlet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.scarlet.Data.Customer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);

        EditText username=findViewById(R.id.username);
        EditText newPassword=findViewById(R.id.new_password);
        EditText confirmNewPassword=findViewById(R.id.confirm_new_password);
        ImageButton back_btn=(ImageButton) findViewById(R.id.back_btn);
        Button change_password=(Button) findViewById(R.id.change_passsword_button);
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
                String newPasswordText=newPassword.getText().toString();
                String confirmNewPasswordText=confirmNewPassword.getText().toString();

                if(usernameText.isEmpty()){
                    username.setError("Username can not be empty");
                }else if(newPasswordText.isEmpty()){
                    newPassword.setError("New password can not be empty");
                }else if(confirmNewPasswordText.isEmpty()){
                    confirmNewPassword.setError("confirm new password can not be empty");
                }else if(!newPasswordText.equals(confirmNewPasswordText)){
                    confirmNewPassword.setError("confirm new password must be same as new password");
                }else{
                    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                    Query myRef=firebaseDatabase.getReference("customers").orderByChild("username").equalTo(usernameText);
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                for(DataSnapshot snap : snapshot.getChildren()){
                                    Map<String, Object> updatePassword = new HashMap<>();
                                    updatePassword.put("password", confirmNewPasswordText);
                                    snap.getRef().updateChildren(updatePassword);
                                    Toast.makeText(ForgotPasswordActivity.this,"Successfully",Toast.LENGTH_SHORT).show();

                                    Intent intent=new Intent(ForgotPasswordActivity.this,SignInActivity.class);
                                    startActivity(intent);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }
        });

    }

}