package com.example.scarlet;

import android.app.Activity;
import android.os.Bundle;


import android.view.View;
import android.widget.TextView;
import android.content.Intent;
import android.widget.ImageView;

public class sign_up_activity extends Activity {

	
	private View _bg__sign_up;
	private TextView sign_up;
	private TextView or;
	private View rectangle_1;
	private View rectangle_2;
	private View rectangle_4;
	private View rectangle_3;
	private TextView password;
	private TextView confirm_password;
	private TextView username_or_email;
	private TextView _sign_up_ek1;
	private ImageView icons8_facebook_240_1;
	private ImageView icons8_google_240_1;
	private ImageView mask_group;
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
		setContentView(R.layout.sign_up);

		
		_bg__sign_up = (View) findViewById(R.id._bg__sign_up);
		sign_up = (TextView) findViewById(R.id.sign_up_1);
		or = (TextView) findViewById(R.id.or);
		rectangle_1 = (View) findViewById(R.id.rectangle_1);
		rectangle_2 = (View) findViewById(R.id.rectangle_2);
		rectangle_4 = (View) findViewById(R.id.rectangle_4);
		password = (TextView) findViewById(R.id.password);
		confirm_password = (TextView) findViewById(R.id.confirm_password);
		username_or_email = (TextView) findViewById(R.id.username_or_email);
		icons8_facebook_240_1 = (ImageView) findViewById(R.id.icons8_facebook_240_1);
		icons8_google_240_1 = (ImageView) findViewById(R.id.icons8_google_240_1);
		mask_group = (ImageView) findViewById(R.id.mask_group);
	

		
		
		//custom code goes here
	
	}
}
	
	