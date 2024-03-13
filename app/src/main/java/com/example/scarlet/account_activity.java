package com.example.scarlet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView;

public class account_activity extends Activity {

	
	private View _bg__account;
	private View _bg__group_7_ek1;
	private View rectangle_30;
	private TextView faqs;
	private ImageView logout_1;
	private View rectangle_27;
	private View rectangle_41;
	private View ellipse_23;
	private TextView user_zjh;
	private View _bg__status_bar___02_transparent_ek1;
	private View _bg__status_bar___time___01_default_ek1;
	private TextView _09_41;
	private View _bg__battery_ek1;
	private ImageView rectangle;
	private ImageView combined_shape;
	private ImageView rectangle_ek1;
	private ImageView wifi;
	private ImageView mobile_signal;
	private ImageView user__2__1;
	private View _bg__group_23_ek1;
	private ImageView ellipse_4;
	private TextView home;
	private TextView favourite;
	private TextView deals;
	private TextView account;
	private ImageView ellipse_7;
	private View ellipse_6;
	private TextView cart;
	private ImageView discount;
	private ImageView home_ek1;
	private ImageView shopping_cart;
	private ImageView heart;
	private ImageView user__1__1;
	private View _bg__frame_8_ek1;
	private TextView account_ek1;
	private TextView order;
	private TextView general_information;
	private View _bg__profile_ek1;
	private View rectangle_25;
	private TextView profile_ek2;
	private ImageView edit_1;
	private ImageView right_arrow_3;
	private View _bg__settings_ek1;
	private View rectangle_26;
	private TextView settings_ek2;
	private ImageView setting_1;
	private ImageView right_arrow_4;
	private View _bg__group_8_ek1;
	private View rectangle_28;
	private TextView order_activities;
	private ImageView file_1;
	private View _bg__group_9_ek1;
	private View rectangle_29;
	private TextView policies;
	private View _bg__group_10_ek1;
	private View rectangle_29_ek1;
	private TextView loyalty_program;
	private View info_1;
	private View _bg__group_6_ek1;
	private View rectangle_31;
	private TextView about_app_version;
	private ImageView file__1__1;
	private ImageView file__1__2;
	private ImageView right_arrow_7;
	private ImageView right_arrow_6;
	private ImageView right_arrow_5;
	private View _bg__group_24_ek1;
	private View rectangle_30_ek1;
	private TextView faqs_ek1;
	private View _bg__group_25_ek1;
	private View rectangle_30_ek2;
	private TextView feedback___support;
	private TextView help_center;
	private ImageView right_arrow_8;
	private ImageView right_arrow_10;
	private ImageView right_arrow_11;
	private ImageView feedback_1;
	private ImageView chat_box_1;
	private ImageView information_button_1;
	private View _bg__group_26_ek1;
	private View rectangle_42;
	private TextView sign_out;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.account);

		
		_bg__account = (View) findViewById(R.id._bg__account);

		rectangle_41 = (View) findViewById(R.id.rectangle_41);
		ellipse_23 = (View) findViewById(R.id.ellipse_23);
		user_zjh = (TextView) findViewById(R.id.user_zjh);
		user__2__1 = (ImageView) findViewById(R.id.user__2__1);

		ellipse_4 = (ImageView) findViewById(R.id.ellipse_4);
		home = (TextView) findViewById(R.id.home);
		favourite = (TextView) findViewById(R.id.favourite);
		deals = (TextView) findViewById(R.id.deals);

		ellipse_7 = (ImageView) findViewById(R.id.ellipse_7);
		ellipse_6 = (View) findViewById(R.id.ellipse_6);
		cart = (TextView) findViewById(R.id.cart);
		discount = (ImageView) findViewById(R.id.discount);
		home_ek1 = (ImageView) findViewById(R.id.home_ek1);
		shopping_cart = (ImageView) findViewById(R.id.shopping_cart);
		heart = (ImageView) findViewById(R.id.heart);
		user__1__1 = (ImageView) findViewById(R.id.user__1__1);
		_bg__frame_8_ek1 = (View) findViewById(R.id._bg__frame_8_ek1);
		account_ek1 = (TextView) findViewById(R.id.account_ek1);
		order = (TextView) findViewById(R.id.order);
		general_information = (TextView) findViewById(R.id.general_information);
		_bg__profile_ek1 = (View) findViewById(R.id._bg__profile_ek1);
		rectangle_25 = (View) findViewById(R.id.rectangle_25);
		profile_ek2 = (TextView) findViewById(R.id.profile_ek2);
		edit_1 = (ImageView) findViewById(R.id.edit_1);
		right_arrow_3 = (ImageView) findViewById(R.id.right_arrow_3);

		rectangle_26 = (View) findViewById(R.id.rectangle_26);
		settings_ek2 = (TextView) findViewById(R.id.settings_ek2);
		setting_1 = (ImageView) findViewById(R.id.setting_1);
		right_arrow_4 = (ImageView) findViewById(R.id.right_arrow_4);

		rectangle_28 = (View) findViewById(R.id.rectangle_28);
		order_activities = (TextView) findViewById(R.id.order_activities);
		file_1 = (ImageView) findViewById(R.id.file_1);

		rectangle_29 = (View) findViewById(R.id.rectangle_29);
		policies = (TextView) findViewById(R.id.policies);

		rectangle_29_ek1 = (View) findViewById(R.id.rectangle_29_ek1);
		loyalty_program = (TextView) findViewById(R.id.loyalty_program);

		rectangle_31 = (View) findViewById(R.id.rectangle_31);
		about_app_version = (TextView) findViewById(R.id.about_app_version);
		file__1__1 = (ImageView) findViewById(R.id.file__1__1);
		file__1__2 = (ImageView) findViewById(R.id.file__1__2);
		right_arrow_7 = (ImageView) findViewById(R.id.right_arrow_7);
		right_arrow_6 = (ImageView) findViewById(R.id.right_arrow_6);
		right_arrow_5 = (ImageView) findViewById(R.id.right_arrow_5);
		rectangle_30_ek1 = (View) findViewById(R.id.rectangle_30_ek1);
		faqs_ek1 = (TextView) findViewById(R.id.faqs_ek1);
		rectangle_30_ek2 = (View) findViewById(R.id.rectangle_30_ek2);
		feedback___support = (TextView) findViewById(R.id.feedback___support);
		help_center = (TextView) findViewById(R.id.help_center);
		right_arrow_8 = (ImageView) findViewById(R.id.right_arrow_8);
		right_arrow_10 = (ImageView) findViewById(R.id.right_arrow_10);
		right_arrow_11 = (ImageView) findViewById(R.id.right_arrow_11);
		feedback_1 = (ImageView) findViewById(R.id.feedback_1);
		chat_box_1 = (ImageView) findViewById(R.id.chat_box_1);
		information_button_1 = (ImageView) findViewById(R.id.information_button_1);


		
		//custom code goes here
		RelativeLayout profile_button=findViewById(R.id.profile_button);
		profile_button.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				Intent intent= new Intent(account_activity.this,profile_activity.class);
				startActivity(intent);
			}
		});
	}
}
	
	