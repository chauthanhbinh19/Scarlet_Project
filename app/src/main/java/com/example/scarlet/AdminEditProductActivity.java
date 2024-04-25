package com.example.scarlet;

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
import java.util.List;
import java.util.Locale;


public class AdminEditProductActivity extends AppCompatActivity {

    RelativeLayout back_btn;
    ImageView btnImage;
    EditText productName, productPoint, productPrice, productDescription;
    Button btnSave;
    ProgressDialog progressDialog;
    String productname,productkey, oldProductImg, productpoint,productprice,productdescription, categoryId, categoryName;
    Uri uri;
    TextView btnImageError;
    List<Category> categoryList;
    Spinner spinner;
    private void BindView(){
        back_btn=findViewById(R.id.back_btn);
        productName=findViewById(R.id.productName);
        btnImage=findViewById(R.id.btnImage);
        btnSave=findViewById(R.id.btnSave);
        productPoint=findViewById(R.id.productPoint);
        productPrice=findViewById(R.id.productPrice);
        productDescription=findViewById(R.id.productDescription);
        btnImageError=findViewById(R.id.btnImageError);
        spinner=findViewById(R.id.productCategory);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_edit_product);

        Window window = getWindow();
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.burgundy));

        BindView();
        Intent intent=new Intent();
        if(intent!= null){
            productname= getIntent().getStringExtra("productName");
            oldProductImg=getIntent().getStringExtra("img");
            productkey=getIntent().getStringExtra("productKey");
            productpoint=getIntent().getStringExtra("productPoint");
            productprice=getIntent().getStringExtra("productPrice");
            productdescription=getIntent().getStringExtra("productDescription");
            productName.setText(productname);
            productPoint.setText(productpoint);
            productPrice.setText(productprice);
            productDescription.setText(productdescription);
            Glide.with(AdminEditProductActivity.this).load(oldProductImg).into(btnImage);
        }
        getCategoryData();
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
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Category selectedCategory=(Category) parent.getItemAtPosition(position);
                categoryId=selectedCategory.getKey();
                categoryName=selectedCategory.getName_category();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(productName.getText().toString().isEmpty()){
                    productName.setError("Product name can not be empty");
                }else if(productPoint.getText().toString().isEmpty()){
                    productPoint.setError("Product point can not be empty");
                }else if(productPrice.getText().toString().isEmpty()){
                    productPrice.setError("Product price can not be empty");
                }else{
                    saveData();
                }
            }
        });
    }
    private void getCategoryData(){
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference= firebaseDatabase.getReference("category");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryList=new ArrayList<>();
                for(DataSnapshot snap: snapshot.getChildren()){
                    String categoryKey=snap.getKey();
                    String categoryName=snap.child("name_category").getValue(String.class);
                    String categoryImg=snap.child("img").getValue(String.class);
                    Category category=new Category(categoryName,categoryImg,categoryKey);
                    categoryList.add(category);
                }
                AdminCategoryDropdownAdapter adapter=new AdminCategoryDropdownAdapter(AdminEditProductActivity.this,R.layout.admin_category_dropdown_layout,categoryList);
                spinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void saveData(){
        progressDialog=new ProgressDialog(AdminEditProductActivity.this);
        progressDialog.setTitle("Uploading file...");
        progressDialog.show();

        SimpleDateFormat formatter=new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss", Locale.US);
        Date now=new Date();
        String fileName= formatter.format(now);

        productname=productName.getText().toString();
        productdescription=productDescription.getText().toString();
        productprice=productPrice.getText().toString();
        productpoint=productPoint.getText().toString();
        if(uri==null && !oldProductImg.isEmpty()){
            Product product=new Product(productname,productdescription,categoryId,categoryName,Double.parseDouble(productprice),Integer.parseInt(productpoint),oldProductImg);

            FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
            DatabaseReference categoryRef= firebaseDatabase.getReference("product");
            categoryRef.child(productkey).setValue(product);
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            Toast.makeText(AdminEditProductActivity.this,"Save successfully",Toast.LENGTH_SHORT).show();
        }else{
            StorageReference storageReference= FirebaseStorage.getInstance().getReferenceFromUrl(oldProductImg);
            storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {

                }
            });
            StorageReference newStorageReference=FirebaseStorage.getInstance().getReference("product/"+fileName);
            newStorageReference.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                            while(!uriTask.isComplete());
                            Uri urlImage=uriTask.getResult();

                            Product product=new Product(productname,productdescription,categoryId,categoryName,Double.parseDouble(productprice),Integer.parseInt(productpoint),urlImage.toString());

                            FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                            DatabaseReference categoryRef= firebaseDatabase.getReference("product");
                            categoryRef.child(productkey).setValue(product);
                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            Toast.makeText(AdminEditProductActivity.this,"Save successfully",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            Toast.makeText(AdminEditProductActivity.this,"Save failed",Toast.LENGTH_SHORT).show();
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
}