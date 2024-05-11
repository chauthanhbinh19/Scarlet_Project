package com.example.scarlet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.scarlet.Data.CreateDataDefault;
import com.example.scarlet.Fragment.AccountFragment;
import com.example.scarlet.Fragment.CartFragment;
import com.example.scarlet.Fragment.DealsFragment;
import com.example.scarlet.Fragment.FavouriteFragment;
import com.example.scarlet.Fragment.HomeFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class MainActivity extends AppCompatActivity {
    MeowBottomNavigation bottomNavigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window window = getWindow();
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.white));
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.burgundy));
        getWindow().getDecorView().setSystemUiVisibility(0);

        openHomeFragment();
        createBottomNavigation(savedInstanceState);
//        CreateDataDefault text=new CreateDataDefault();
//        text.deleteOfferTransaction();
//        text.createCategoryData();
//        text.createProductData();
//        text.createMembershipData();
//        text.createOfferData();
//        text.createVoucherData();
//        text.createAccount();
//        text.createReview();
    }
    private void createBottomNavigation(Bundle savedInstanceState){
        bottomNavigation=findViewById(R.id.bottomNavigation);
        bottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.home_ek1 ));
        bottomNavigation.add(new MeowBottomNavigation.Model(2,R.drawable.heart));
        bottomNavigation.add(new MeowBottomNavigation.Model(3,R.drawable.shopping_cart));
        bottomNavigation.add(new MeowBottomNavigation.Model(4,R.drawable.discount));
        bottomNavigation.add(new MeowBottomNavigation.Model(5,R.drawable.user));
        bottomNavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                String name=null;
                switch(model.getId()){
                    case 1:name="Home";
                        openHomeFragment();
                        break;
                    case 2:name="Favourite";
                        openFavouriteFragment();
                        break;
                    case 3:name="Cart";
                        openCartFragment();
                        break;
                    case 4:name="Deals";
                        openDealsFragment();
                        break;
                    case 5:name="Account";
                        openAccountFragment();
                        break;
                }
//                Toast.makeText(MainActivity.this,name,Toast.LENGTH_SHORT).show();
                return null;
            }
        });
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
        }
        bottomNavigation.show(1,true);
    }
    private void openHomeFragment(){
        HomeFragment HomeFragment=new HomeFragment();
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.frame_layout,HomeFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    private void openFavouriteFragment(){
        FavouriteFragment favouriteFragment=new FavouriteFragment();
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.frame_layout,favouriteFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    private void openCartFragment(){
        CartFragment cartFragment=new CartFragment();
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.frame_layout,cartFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    private void openDealsFragment(){
        DealsFragment dealsFragment=new DealsFragment();
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.frame_layout,dealsFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    private void openAccountFragment(){
        AccountFragment accountFragment=new AccountFragment();
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.frame_layout,accountFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}