package com.example.scarlet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.List;


public class SignUpActivity extends AppCompatActivity {

    public EditText username;
    public EditText password;
    public EditText confirmpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        
        Button signUp=findViewById(R.id.sign_up_ek1);
        Button signIn=findViewById(R.id.sign_in_btn);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username=findViewById(R.id.username_or_email);
                password=findViewById(R.id.password);
                confirmpassword=findViewById(R.id.confirm_password);

                String usernameText=username.getText().toString();
                String passwordText=password.getText().toString();
                String confirmPasswordText=confirmpassword.getText().toString();

                if(usernameText.isEmpty()){
                    username.setError("Username can not be empty");
                }else if(passwordText.isEmpty()){
                    password.setError("Password can not be empty");
                }else if(confirmPasswordText.isEmpty()){
                    confirmpassword.setError("Confirmpassword can not be empty");
                }else if (!passwordText.equals(confirmPasswordText)) {
                    confirmpassword.setError("The password and confirmpassword is different");
                } else {
                    SaveData(usernameText,passwordText);
                }

            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

    }
    private void SaveData(String username, String password){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query query = database.getReference("customers").orderByChild("username").equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Username đã tồn tại
                    // Hiển thị thông báo lỗi
                    Toast.makeText(SignUpActivity.this, "Username was existed", Toast.LENGTH_SHORT).show();
                } else {
                    // Username hợp lệ
                    DatabaseReference myRef = database.getReference("customers");

                    String customerId = myRef.push().getKey();
                    Customer customer = new Customer( "user","kcck", "Male", "01/01/1990", "123 Street, City", "0123456789", "customer.doe@email.com", username, password,0);
                    try{
                        myRef.child(customerId).setValue(customer);
                        Toast.makeText(SignUpActivity.this,"Register successfully",Toast.LENGTH_SHORT).show();

                    }catch (DatabaseException e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}