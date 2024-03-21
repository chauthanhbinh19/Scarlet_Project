package com.example.scarlet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.scarlet.Fragment.AccountFragment;
import com.example.scarlet.Fragment.CartFragment;
import com.example.scarlet.Fragment.DealsFragment;
import com.example.scarlet.Fragment.FavouriteFragment;
import com.example.scarlet.Fragment.HomeFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class SignInActivity extends AppCompatActivity {

    public EditText username;
    public EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        Button createAccount=(Button) findViewById(R.id._create_account_);
        Button forgotPassword=(Button) findViewById(R.id.forgot_password_);
        username =(EditText) findViewById(R.id.username_or_email);
        password=(EditText) findViewById(R.id.password);
        Button signin=(Button) findViewById(R.id.sign_in_button);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameText=username.getText().toString();
                String passwordText=password.getText().toString();
                if(usernameText.isEmpty()){
                    username.setError("Username can not be empty");
                }else if(passwordText.isEmpty()){
                    password.setError("Password can not be empty");
                }else{
                    ValidateUser(usernameText,passwordText);
                }
            }
        });

    }
    private void ValidateUser(String usernameInput, String passwordInput){
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        Query query=database.getReference("customers").orderByChild("username").equalTo(usernameInput);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String customerPassword = userSnapshot.child("password").getValue(String.class);
                        if (customerPassword.equals(passwordInput)) {
                            // Password matches
                            Toast.makeText(SignInActivity.this, "Sign in successfully", Toast.LENGTH_SHORT).show();
                            break;
                        } else {
                            // Password does not match
                            password.setError("Password is not correct");
                        }
                    }
                }else{
                    Toast.makeText(SignInActivity.this,"Username does not exist",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SignInActivity.this, "Sign in failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}