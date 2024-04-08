package com.example.scarlet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class DeliveryActivity extends AppCompatActivity {

    TextView address, additionalInfo;
    TextView totalView;
    RelativeLayout back_btn;
    Button continue_btn;
    String total,deliveryStatus;
    private void BindView(){
        back_btn=findViewById(R.id.back_btn);
        address=findViewById(R.id.delivery_address_content);
        additionalInfo=findViewById(R.id.delivery_additional_content);
        totalView=findViewById(R.id.total);
        continue_btn=findViewById(R.id.continue_btn);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivery);
        BindView();
        Intent intent=getIntent();
        if(intent!= null){
            deliveryStatus=intent.getStringExtra("deliveryStatus");
            String street=intent.getStringExtra("street");
            String ward=intent.getStringExtra("ward");
            String district=intent.getStringExtra("district");
            String province=intent.getStringExtra("province");
            String postalCode=intent.getStringExtra("postalCode");
            String additionalInfo1=intent.getStringExtra("additionalInfo");
            total=intent.getStringExtra("total");

            String text=street+", "+ward+", "+district+", "+province;
            address.setText(text);
            additionalInfo.setText(additionalInfo1);
            totalView.setText(total);
        }
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(DeliveryActivity.this, PaymentActivity.class);
                intent1.putExtra("total",total);
                intent1.putExtra("deliveryStatus",deliveryStatus);
                startActivity(intent1);
            }
        });
    }

}