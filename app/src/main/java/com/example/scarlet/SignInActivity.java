package com.example.scarlet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.scarlet.Fragment.AccountFragment;
import com.example.scarlet.Fragment.CartFragment;
import com.example.scarlet.Fragment.DealsFragment;
import com.example.scarlet.Fragment.FavouriteFragment;
import com.example.scarlet.Fragment.HomeFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    ImageView gmailBtn;
    RelativeLayout gmailBox;
    Button createAccount,forgotPassword,signin, continueBtn;
    private void BindView(){
        createAccount=(Button) findViewById(R.id._create_account_);
        forgotPassword=(Button) findViewById(R.id.forgot_password_);
        username =(EditText) findViewById(R.id.username_or_email);
        password=(EditText) findViewById(R.id.password);
        signin=(Button) findViewById(R.id.sign_in_button);
        continueBtn=findViewById(R.id.continueBtn);
        gmailBtn=findViewById(R.id.gmailBtn);
        gmailBox=findViewById(R.id.gmailBox);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        Window window = getWindow();
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.white));
        BindView();
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignInActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignInActivity.this,ForgotPasswordActivity.class);
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
                    validateUser(usernameText,passwordText);
                }
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount googleSignInAccount=GoogleSignIn.getLastSignedInAccount(this);
        if(googleSignInAccount != null ){
            Intent intent=new Intent(SignInActivity.this, AdminMainActivity.class);
            startActivity(intent);
        }
        ActivityResultLauncher activityResultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                handleSignInTask(task);
            }
        });
        gmailBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent=mGoogleSignInClient.getSignInIntent();
                activityResultLauncher.launch(signInIntent);
            }
        });
    }

    private void handleSignInTask(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account=task.getResult(ApiException.class);

            final String getFullName=account.getDisplayName();
            final String getEmail=account.getEmail();
            final Uri photoUri=account.getPhotoUrl();

            Intent intent=new Intent(SignInActivity.this, AdminMainActivity.class);
            startActivity(intent);
        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(SignInActivity.this, "Failed or cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    private void validateUser(String email, String password){
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isComplete()){
                            FirebaseUser user=mAuth.getCurrentUser();
                            if(user!=null){
                                String uid=user.getUid();
                                getUserData(uid);
                            }else{
                                Toast.makeText(SignInActivity.this, "Email or password is incorrect", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
    private void getUserData(String uid){
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("user");
        Query query=usersRef.orderByChild("uid").equalTo(uid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap: snapshot.getChildren()){
                    String userUid=snap.child("uid").getValue(String.class);
                    if(userUid.equals(uid)){
                        String userKey=snap.getKey();
                            SharedPreferences sharedPreferences=getSharedPreferences("MyPrefs",MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putBoolean("isLoggedIn",true);
                            editor.putString("customerKey",userKey);

                            Boolean isEmployee=snap.child("employee").getValue(boolean.class);
                            Boolean isCustomer=snap.child("customer").getValue(boolean.class);
                            if(isEmployee){
                                editor.putString("userType","employee");
                                Intent intent=new Intent(SignInActivity.this, AdminMainActivity.class);
                                startActivity(intent);
                            }
                            if(isCustomer){
                                editor.putString("userType","customer");
                                Intent intent=new Intent(SignInActivity.this,MainActivity.class);
                                startActivity(intent);
                            }
                            editor.apply();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}