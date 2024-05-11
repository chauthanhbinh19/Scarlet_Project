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

import com.example.scarlet.Data.User;
import com.example.scarlet.Dialog.DatePickerDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class SignUpActivity extends AppCompatActivity {

    public EditText username;
    public EditText password;
    public EditText confirmpassword;
    EditText first_name, last_name, gender,birthday, phone;
    Button signUp,signIn;
    ImageButton calendar;
    private void BindView(){
        signUp=findViewById(R.id.sign_up_ek1);
        signIn=findViewById(R.id.sign_in_btn);
        username=findViewById(R.id.username_or_email);
        password=findViewById(R.id.password);
        confirmpassword=findViewById(R.id.confirm_password);
        first_name=findViewById(R.id.first_name);
        last_name=findViewById(R.id.last_name);
        gender=findViewById(R.id.gender);
        birthday=findViewById(R.id.birthday);
        phone=findViewById(R.id.phone);
        calendar=findViewById(R.id.calendar);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        Window window = getWindow();
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.white));

        BindView();
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameText=username.getText().toString();
                String passwordText=password.getText().toString();
                String confirmPasswordText=confirmpassword.getText().toString();
                String firstName=first_name.getText().toString();
                String lastname=last_name.getText().toString();
                String genderText=gender.getText().toString();
                String phoneText=phone.getText().toString();
                String dateOfBirth=birthday.getText().toString();

                if(usernameText.isEmpty()){
                    username.setError("Email can not be empty");
                }else if(passwordText.isEmpty()){
                    password.setError("Password can not be empty");
                }else if(confirmPasswordText.isEmpty()){
                    confirmpassword.setError("Confirmpassword can not be empty");
                }else if (!passwordText.equals(confirmPasswordText)) {
                    confirmpassword.setError("The password and confirmpassword is different");
                }else if (firstName.isEmpty()) {
                    first_name.setError("First name can not be empty");
                }else if (lastname.isEmpty()) {
                    first_name.setError("Last name can not be empty");
                }else if (genderText.isEmpty()) {
                    first_name.setError("Gender can not be empty");
                }else if (phoneText.isEmpty()) {
                    first_name.setError("Phone can not be empty");
                }else if (dateOfBirth.isEmpty()) {
                    first_name.setError("Date of birth can not be empty");
                }    else {
                    saveEmailAccountData(usernameText,passwordText, firstName,lastname,genderText,phoneText, dateOfBirth);
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
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

    }
    public void showDatePickerDialog(){
        DatePickerDialog newFragment=new DatePickerDialog();
        newFragment.setTextDate(birthday);
        newFragment.show(getSupportFragmentManager(), "DatePicker");
    }
    private void saveEmailAccountData(String email, String password, String firstname, String lastname, String genderText, String phoneText, String dateOfBirth){
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

                            User userAdd=new User(uid,firstname,lastname,genderText,dateOfBirth,phoneText,email,0,"-1","",true,false,0);
                            userRef.push().setValue(userAdd);
                            Toast.makeText(SignUpActivity.this,"Register successfully",Toast.LENGTH_SHORT).show();

                            Intent intent=new Intent(SignUpActivity.this, SignInActivity.class);
                            startActivity(intent);
                        }else{

                        }
                    }
                });
    }
}