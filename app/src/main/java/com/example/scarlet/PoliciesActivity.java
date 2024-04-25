package com.example.scarlet;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


public class PoliciesActivity extends AppCompatActivity {

    RelativeLayout back_btn;
    private void BindView(){
        back_btn=findViewById(R.id.back_btn);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.policies);

        Window window = getWindow();
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.burgundy));

        BindView();
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}