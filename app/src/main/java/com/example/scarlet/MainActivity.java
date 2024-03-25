package com.example.scarlet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.scarlet.Adapter.CategoryAdapter;
import com.example.scarlet.Data.Category;
import com.example.scarlet.Data.text;
import com.example.scarlet.Fragment.AccountFragment;
import com.example.scarlet.Fragment.CartFragment;
import com.example.scarlet.Fragment.DealsFragment;
import com.example.scarlet.Fragment.FavouriteFragment;
import com.example.scarlet.Fragment.HomeFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        text text=new text();
//        text.createProductData();

        MeowBottomNavigation bottomNavigation=findViewById(R.id.bottomNavigation);
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
                        break;
                    case 2:name="Favourite";
                        break;
                    case 3:name="Cart";
                        break;
                    case 4:name="Deals";
                        break;
                    case 5:name="Account";
                        break;
                }
                Toast.makeText(MainActivity.this,name,Toast.LENGTH_SHORT).show();
                return null;
            }
        });
        bottomNavigation.setOnShowListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                String name = null;
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
                    default:name="Home";
                            openHomeFragment();
                            break;
                }
//                bottomNavigation.setCount(5,"1");
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
        fragmentTransaction.replace(R.id.frame_layout,HomeFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    private void openFavouriteFragment(){
        FavouriteFragment favouriteFragment=new FavouriteFragment();
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,favouriteFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    private void openCartFragment(){
        CartFragment cartFragment=new CartFragment();
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,cartFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    private void openDealsFragment(){
        DealsFragment dealsFragment=new DealsFragment();
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,dealsFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    private void openAccountFragment(){
        AccountFragment accountFragment=new AccountFragment();
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,accountFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}