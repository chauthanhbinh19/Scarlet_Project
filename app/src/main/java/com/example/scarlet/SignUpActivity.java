package com.example.scarlet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.scarlet.Data.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class SignUpActivity extends AppCompatActivity {

    public EditText email;
    public EditText password;
    public EditText confirmpassword;
    Button signUp,signIn;
    private void BindView(){
        signUp=findViewById(R.id.sign_up_ek1);
        signIn=findViewById(R.id.sign_in_btn);
        email=findViewById(R.id.username_or_email);
        password=findViewById(R.id.password);
        confirmpassword=findViewById(R.id.confirm_password);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        BindView();
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameText=email.getText().toString();
                String passwordText=password.getText().toString();
                String confirmPasswordText=confirmpassword.getText().toString();

                if(usernameText.isEmpty()){
                    email.setError("Username can not be empty");
                }else if(passwordText.isEmpty()){
                    password.setError("Password can not be empty");
                }else if(confirmPasswordText.isEmpty()){
                    confirmpassword.setError("Confirmpassword can not be empty");
                }else if (!passwordText.equals(confirmPasswordText)) {
                    confirmpassword.setError("The password and confirmpassword is different");
                } else {
                    saveAccountData(usernameText,passwordText);
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
    private void saveAccountData(String email, String password){
        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isComplete()){
                            FirebaseUser user=mAuth.getCurrentUser();
                            String uid=user.getUid();

                            DatabaseReference userRef=FirebaseDatabase.getInstance().getReference("user");
                            DatabaseReference newUserRef=userRef.child(uid);

                            User userAdd=new User(uid,"","","","","",email,0,"-1","",true,false);
                            userRef.push().setValue(userAdd);
                            Toast.makeText(SignUpActivity.this,"Register successfully",Toast.LENGTH_SHORT).show();
                        }else{

                        }
                    }
                });
    }

}