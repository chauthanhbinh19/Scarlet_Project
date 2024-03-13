package com.example.scarlet;

import android.app.Activity;
import android.os.Bundle;


import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;

public class LoadingActivity extends Activity {


    private View _bg___loading;
    private View _bg__frame_1_ek1;
    private View ellipse_1;
    private TextView scarlet;
    private ImageView croissant__1__1;
    private ImageView cake_2;
    private ImageView cold_coffee_1;
    private ImageView cake_1;
    private ImageView piece_of_cake_1;
    private ImageView cupcake__1__1;
    private View _bg__status_bar___02_transparent_ek1;
    private View _bg__status_bar___time___01_default_ek1;
    private TextView _09_41;
    private View _bg__battery_ek1;
    private ImageView rectangle;
    private ImageView combined_shape;
    private ImageView rectangle_ek1;
    private ImageView wifi;
    private ImageView mobile_signal;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);


        _bg___loading = (View) findViewById(R.id._bg___loading);
        _bg__frame_1_ek1 = (View) findViewById(R.id._bg__frame_1_ek1);
        ellipse_1 = (View) findViewById(R.id.ellipse_1);
        scarlet = (TextView) findViewById(R.id.scarlet);
        croissant__1__1 = (ImageView) findViewById(R.id.croissant__1__1);
        cake_2 = (ImageView) findViewById(R.id.cake_2);
        cold_coffee_1 = (ImageView) findViewById(R.id.cold_coffee_1);
        cake_1 = (ImageView) findViewById(R.id.cake_1);
        piece_of_cake_1 = (ImageView) findViewById(R.id.piece_of_cake_1);
        cupcake__1__1 = (ImageView) findViewById(R.id.cupcake__1__1);


        //custom code goes here

    }
}

