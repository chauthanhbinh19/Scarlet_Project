package com.example.scarlet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

//        Button _create_account_ =findViewById(R.id._create_account_);
//        _create_account_.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick (View v){
//                Intent intent= new Intent(MainActivity.this,sign_up_activity.class);
//                startActivity(intent);
//            }
//        });
//        Button forgot_password_ =findViewById(R.id.forgot_password_);
//        forgot_password_.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick (View v){
//                Intent intent= new Intent(MainActivity.this,sign_up_activity.class);
//                startActivity(intent);
//            }
//        });
//
//        final EditText username=findViewById(R.id.username_or_email);
//        final EditText password=findViewById(R.id.password);
//        Button signIn = findViewById(R.id.sign_in_button);
//        signIn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick (View v){
//                Intent intent= new Intent(MainActivity.this,sign_up_activity.class);
//                startActivity(intent);
//            }
//        });
//        RelativeLayout profile_button=findViewById(R.id.profile_button);
//        profile_button.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                Intent intent= new Intent(MainActivity.this,profile_activity.class);
//                startActivity(intent);
//            }
//        });
    }
}