package com.example.scarlet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Adapter.GridLayoutDecoration;
import com.example.scarlet.Adapter.ProductCheckboxAdapter;
import com.example.scarlet.Data.Product;
import com.example.scarlet.Dialog.DatePickerDialog;
import com.example.scarlet.Interface.GetKeyCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AdminEditOfferActivity extends AppCompatActivity {

    RelativeLayout back_btn;
    RecyclerView productRecycleView;
    ImageView btnImage;
    EditText offerName,offerDescription,offerPoint;
    TextView offerStartDate,offerEndDate;
    Button btnSave;
    ProgressDialog progressDialog;
    String offername,offerdescription, offerpoint, startdate, enddate, key;
    Uri uri;
    GetKeyCallback getKeyCallback;
    List<String> keyList;
    List<Product> productList;
    ImageButton calendarStart,calendarEnd;
    ProductCheckboxAdapter productCheckboxAdapter;
    private void BindView(){
        back_btn=findViewById(R.id.back_btn);
        btnSave=findViewById(R.id.btnSave);
        offerName=findViewById(R.id.offerName);
        offerDescription=findViewById(R.id.offerDescription);
        offerPoint=findViewById(R.id.offerPoint);
        offerStartDate=findViewById(R.id.offerStartDate);
        offerEndDate=findViewById(R.id.offerEndDate);
        calendarStart=findViewById(R.id.calendarStart);
        calendarEnd=findViewById(R.id.calendarEnd);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_edit_offer);

        Window window = getWindow();
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.burgundy));

        BindView();
        Intent intent=new Intent();
        if(intent!= null){
            offername= getIntent().getStringExtra("offerName");
            offerdescription=getIntent().getStringExtra("offerDescription");
            offerpoint=getIntent().getStringExtra("offerPoint");
            startdate=getIntent().getStringExtra("offerStartDate");
            enddate=getIntent().getStringExtra("offerEndDate");
            key=getIntent().getStringExtra("offerKey");
            offerName.setText(offername);
            offerDescription.setText(offerdescription);
            offerPoint.setText(offerpoint);
            offerStartDate.setText(startdate);
            offerEndDate.setText(enddate);
        }
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        calendarStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(offerStartDate);
            }
        });
        calendarEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(offerEndDate);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(offerName.getText().toString().isEmpty()){
                    offerName.setError("Offer name can not be empty");
                }else if(offerDescription.getText().toString().isEmpty()){
                    offerDescription.setError("Offer description can not be empty");
                }else if(offerPoint.getText().toString().isEmpty()){
                    offerPoint.setText("Offer point can not be empty");
                }else if(offerStartDate.getText().toString().isEmpty()){
                    offerStartDate.setError("Offer start date must be selected");
                }else if(offerEndDate.getText().toString().isEmpty()){
                    offerEndDate.setError("Offer end date must be selected");
                }else{
                    saveOfferData();
                }
            }
        });
    }
    public void showDatePickerDialog(TextView textView){
        DatePickerDialog newFragment=new DatePickerDialog();
        newFragment.setTextDate(textView);
        newFragment.show(getSupportFragmentManager(), "DatePicker");
    }
    private void saveOfferData(){
        progressDialog=new ProgressDialog(AdminEditOfferActivity.this);
        progressDialog.setTitle("Uploading file...");
        progressDialog.show();

        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference myRef= firebaseDatabase.getReference("offer").child(key);
        String name=offerName.getText().toString();
        String description=offerDescription.getText().toString();
        int point=Integer.parseInt(offerPoint.getText().toString());
        String startdate=offerStartDate.getText().toString();
        String enddate=offerEndDate.getText().toString();
        Date date1=new Date();
        Date date2=new Date();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            date1 = dateFormat.parse(startdate);
            date2 = dateFormat.parse(enddate);
            myRef.child("name").setValue(name);
            myRef.child("description").setValue(description);
            myRef.child("point").setValue(point);
            myRef.child("startDate").setValue(date1);
            myRef.child("endDate").setValue(date2);
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            Toast.makeText(AdminEditOfferActivity.this,"Save successfully", Toast.LENGTH_SHORT).show();
        } catch (ParseException e) {
            e.printStackTrace(); // Handle parsing exception
        }
    }
}