package com.example.scarlet;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.scarlet.Adapter.AdminCategoryDropdownAdapter;
import com.example.scarlet.Data.Category;
import com.example.scarlet.Data.Product;
import com.example.scarlet.Dialog.DatePickerDialog;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class AdminEditCustomerActivity extends AppCompatActivity {

    RelativeLayout back_btn;
    ImageView btnImage;
    String customerfirstname, customerlastname, customerdateofbirth, customergender,customerphone, oldCustomerImg, userKey;
    Button btnSave;
    ProgressDialog progressDialog;
    EditText customerFirstName, customerLastName, customerDateofBirth, customerGender, customerPhone;
    Uri uri;
    TextView btnImageError;
    ImageView calendar;
    private void BindView(){
        customerFirstName = findViewById(R.id.customerFirstName);
        customerLastName=findViewById(R.id.customerLastName);
        customerDateofBirth=findViewById(R.id.customerDateofBirth);
        customerGender=findViewById(R.id.customerGender);
        customerPhone=findViewById(R.id.customerPhone);
        btnImage = findViewById(R.id.btnImage);
        btnSave=findViewById(R.id.btnSave);
        back_btn=findViewById(R.id.back_btn);
        btnImageError=findViewById(R.id.btnImageError);
        calendar=findViewById(R.id.calendar);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_edit_customer);

        Window window = getWindow();
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.burgundy));

        BindView();
        Intent intent=new Intent();
        if(intent!= null){
            customerfirstname= getIntent().getStringExtra("customerFirstName");
            customerlastname= getIntent().getStringExtra("customerLastName");
            oldCustomerImg=getIntent().getStringExtra("img");
            customerdateofbirth=getIntent().getStringExtra("customerDateofBirth");
            customergender=getIntent().getStringExtra("customerGender");
            customerphone=getIntent().getStringExtra("customerPhone");
            userKey=getIntent().getStringExtra("customerKey");
            customerFirstName.setText(customerfirstname);
            customerLastName.setText(customerlastname);
            customerDateofBirth.setText(customerdateofbirth);
            customerGender.setText(customergender);
            customerPhone.setText(customerphone);
            Glide.with(AdminEditCustomerActivity.this).load(oldCustomerImg).into(btnImage);
        }
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(customerFirstName.getText().toString().isEmpty()){
                    customerFirstName.setError("Customer first name can not be empty");
                }else if(customerLastName.getText().toString().isEmpty()){
                    customerLastName.setError("Customer last name can not be empty");
                }else if(customerGender.getText().toString().isEmpty()){
                    customerGender.setError("Customer gender can not be empty");
                }else if(customerDateofBirth.getText().toString().isEmpty()){
                    customerDateofBirth.setError("Customer date of birth can not be empty");
                }else if(customerPhone.getText().toString().isEmpty()){
                    customerPhone.setError("Customer phone can not be empty");
                }else{
                    saveData();
                }
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
        newFragment.setTextDate(customerDateofBirth);
        newFragment.show(getSupportFragmentManager(), "DatePicker");
    }
    private void saveData(){
        progressDialog=new ProgressDialog(AdminEditCustomerActivity.this);
        progressDialog.setTitle("Uploading file...");
        progressDialog.show();

        SimpleDateFormat formatter=new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss", Locale.US);
        Date now=new Date();
        String fileName= formatter.format(now);

        customerfirstname=customerFirstName.getText().toString();
        customerlastname=customerLastName.getText().toString();
        customergender=customerGender.getText().toString();
        customerdateofbirth=customerDateofBirth.getText().toString();
        customerphone=customerPhone.getText().toString();
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
                        Toast.makeText(AdminEditCustomerActivity.this,"Save successfully",Toast.LENGTH_SHORT).show();

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
//                            Toast.makeText(AdminEditCustomerActivity.this,"Save successfully",Toast.LENGTH_SHORT).show();
                            showSuccessDialog();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
//                            Toast.makeText(AdminEditCustomerActivity.this,"Save failed",Toast.LENGTH_SHORT).show();
                            showFailedDialog();
                        }
                    });
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
    public void showSuccessDialog(){
        final Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.success_dialog_2);

        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_circle_white_30));
        dialog.show();
    }
    public void showFailedDialog(){
        final Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.failed_dialog);

        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_circle_white_30));
        dialog.show();
    }
}