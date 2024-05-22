package com.example.scarlet;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.example.scarlet.Data.Category;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class AdminEditCategoryActivity extends AppCompatActivity {

    RelativeLayout back_btn;
    ImageView btnImage;
    EditText categoryName;
    Button btnSave;
    ProgressDialog progressDialog;
    String categoryname,categorykey, oldCategoryImg;
    Uri uri;
    TextView btnImageError;
    private void BindView(){
        back_btn=findViewById(R.id.back_btn);
        categoryName=findViewById(R.id.categoryName);
        btnImage=findViewById(R.id.btnImage);
        btnSave=findViewById(R.id.btnSave);
        btnImageError=findViewById(R.id.btnImageError);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_edit_category);

        Window window = getWindow();
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.burgundy));

        BindView();
        Intent intent=new Intent();
        if(intent!= null){
            categoryname= getIntent().getStringExtra("categoryName");
            oldCategoryImg=getIntent().getStringExtra("img");
            categorykey=getIntent().getStringExtra("categoryKey");
            categoryName.setText(categoryname);
            Glide.with(AdminEditCategoryActivity.this).load(oldCategoryImg).into(btnImage);
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
                if(categoryName.getText().toString().isEmpty()){
                    categoryName.setError("Category name can not be empty");
                }else if(uri==null){
                    btnImageError.setText("Image can not be empty");
                    btnImageError.setVisibility(View.VISIBLE);
                }else{
                    saveData();
                }
            }
        });
    }
    private void saveData(){
        progressDialog=new ProgressDialog(AdminEditCategoryActivity.this);
        progressDialog.setTitle("Uploading file...");
        progressDialog.show();

        SimpleDateFormat formatter=new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss", Locale.US);
        Date now=new Date();
        String fileName= formatter.format(now);

        if(uri==null && !oldCategoryImg.isEmpty()){
            Category category=new Category(categoryName.getText().toString(),oldCategoryImg);

            FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
            DatabaseReference categoryRef= firebaseDatabase.getReference("category");
            categoryRef.child(categorykey).setValue(category);
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }else{
            StorageReference storageReference= FirebaseStorage.getInstance().getReferenceFromUrl(oldCategoryImg);
            storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {

                }
            });

            StorageReference newStorageReference=FirebaseStorage.getInstance().getReference("category/"+fileName);
            newStorageReference.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                            while(!uriTask.isComplete());
                            Uri urlImage=uriTask.getResult();

                            Category category=new Category(categoryName.getText().toString(),urlImage.toString());

                            FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                            DatabaseReference categoryRef= firebaseDatabase.getReference("category");
                            categoryRef.child(categorykey).setValue(category);
                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
//                            Toast.makeText(AdminEditCategoryActivity.this,"Save successfully",Toast.LENGTH_SHORT).show();
                            showSuccessDialog();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
//                            Toast.makeText(AdminEditCategoryActivity.this,"Save failed",Toast.LENGTH_SHORT).show();
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