package com.example.scarlet;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.scarlet.Dialog.DatePickerDialog;
import com.example.scarlet.Interface.GetKeyCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class EditProfileActivity extends AppCompatActivity {

    RelativeLayout back_btn;
    Button saveBtn;
    EditText first_name, last_name, gender, birthday, phone;
    ImageView calendar, btnImage;
    Uri uri;
    ProgressDialog progressDialog;
    GetKeyCallback getKeyCallback;
    String customerfirstname, customerlastname, customerdateofbirth, customergender,customerphone, oldCustomerImg, userKey;
    private void BindView(){
        back_btn=(RelativeLayout) findViewById(R.id.back_btn);
        saveBtn=findViewById(R.id.saveBtn);
        calendar=findViewById(R.id.calendar);
        first_name=findViewById(R.id.first_name);
        last_name=findViewById(R.id.last_name);
        gender=findViewById(R.id.gender);
        birthday=findViewById(R.id.birthday);
        phone=findViewById(R.id.phone);
        btnImage=findViewById(R.id.btnImage);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        Window window = getWindow();
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.white));
        BindView();
        getKeyCallback=new GetKeyCallback() {
            @Override
            public void itemClick(String key, int type) {
                oldCustomerImg=key;
            }
        };
        getCustomerInfo(getKeyCallback);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("reload","1");
                setResult(EditProfileActivity.RESULT_OK,intent);
                EditProfileActivity.super.onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();

            }
        });
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });

    }
    private void getCustomerInfo(GetKeyCallback getKeyCallback){
        SharedPreferences sharedPreferences =getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn=sharedPreferences.getBoolean("isLoggedIn",false);
        String userKey=sharedPreferences.getString("customerKey","");
        if(isLoggedIn && !userKey.isEmpty()){
            FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
            DatabaseReference myRef=firebaseDatabase.getReference("user").child(userKey);
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        String firstname=snapshot.child("first_name").getValue(String.class);
                        String lastname=snapshot.child("last_name").getValue(String.class);
                        String genderString=snapshot.child("gender").getValue(String.class);
                        String dateofbirth=snapshot.child("date_of_birth").getValue(String.class);
                        String phoneString=snapshot.child("phone_number").getValue(String.class);
                        String avatar=snapshot.child("avatar_img").getValue(String.class);

                        first_name.setText(firstname);
                        last_name.setText(lastname);
                        gender.setText(genderString);
                        birthday.setText(dateofbirth);
                        phone.setText(phoneString);
                        getKeyCallback.itemClick(avatar,1);
                        if(!avatar.isEmpty()){
                            ImageView customerAvatarView=findViewById(R.id.btnImage);
                            Glide.with(EditProfileActivity.this).load(avatar).into(customerAvatarView);
                        }

                        int blackcolor= ContextCompat.getColor(EditProfileActivity.this,R.color.black);
                        first_name.setTextColor(blackcolor);
                        last_name.setTextColor(blackcolor);
                        gender.setTextColor(blackcolor);
                        birthday.setTextColor(blackcolor);
                        phone.setTextColor(blackcolor);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    public void showDatePickerDialog(){
        DatePickerDialog newFragment=new DatePickerDialog();
        newFragment.setTextDate(birthday);
        newFragment.show(getSupportFragmentManager(), "DatePicker");
    }
    private void updateUser(){
        progressDialog=new ProgressDialog(EditProfileActivity.this);
        progressDialog.setTitle("Uploading file...");
        progressDialog.show();

        SimpleDateFormat formatter=new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss", Locale.US);
        Date now=new Date();
        String fileName= formatter.format(now);

        customerfirstname=first_name.getText().toString();
        customerlastname=last_name.getText().toString();
        customergender=gender.getText().toString();
        customerdateofbirth=birthday.getText().toString();
        customerphone=phone.getText().toString();
        SharedPreferences sharedPreferences =getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn=sharedPreferences.getBoolean("isLoggedIn",false);
        String userKey=sharedPreferences.getString("customerKey","");
        if(isLoggedIn && !userKey.isEmpty()){
            if(uri==null && !oldCustomerImg.isEmpty()){
                FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                DatabaseReference customerRef= firebaseDatabase.getReference("user");
                customerRef.child(userKey).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            customerRef.child(userKey).child("first_name").setValue(customerfirstname);
                            customerRef.child(userKey).child("last_name").setValue(customerlastname);
                            customerRef.child(userKey).child("gender").setValue(customergender);
                            customerRef.child(userKey).child("date_of_birth").setValue(customerdateofbirth);
                            customerRef.child(userKey).child("phone_number").setValue(customerphone);
                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            Toast.makeText(EditProfileActivity.this,"Save successfully",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }else{
                StorageReference storageReference= FirebaseStorage.getInstance().getReferenceFromUrl(oldCustomerImg);
                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });
                StorageReference newStorageReference=FirebaseStorage.getInstance().getReference("user/"+fileName);
                newStorageReference.putFile(uri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                                while(!uriTask.isComplete());
                                Uri urlImage=uriTask.getResult();

                                FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                                DatabaseReference categoryRef= firebaseDatabase.getReference("user");
                                categoryRef.child(userKey).child("first_name").setValue(customerfirstname);
                                categoryRef.child(userKey).child("last_name").setValue(customerlastname);
                                categoryRef.child(userKey).child("gender").setValue(customergender);
                                categoryRef.child(userKey).child("date_of_birth").setValue(customerdateofbirth);
                                categoryRef.child(userKey).child("phone").setValue(customerphone);
                                categoryRef.child(userKey).child("avatar_img").setValue(urlImage.toString());
                                if(progressDialog.isShowing()){
                                    progressDialog.dismiss();
                                }
                                Toast.makeText(EditProfileActivity.this,"Save successfully",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                if(progressDialog.isShowing()){
                                    progressDialog.dismiss();
                                }
                                Toast.makeText(EditProfileActivity.this,"Save failed",Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }
    private void choosePicture(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            uri=data.getData();
            btnImage.setImageURI(uri);
        }
    }
}