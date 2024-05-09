package com.example.scarlet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.scarlet.Adapter.ReviewAdapter;
import com.example.scarlet.Adapter.WalkthroughtAdapter;
import com.example.scarlet.Data.Review;
import com.example.scarlet.Fragment.WalkthroughtFragment;

import java.util.ArrayList;
import java.util.List;


public class WalkthroughActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    RelativeLayout back_btn;
    RecyclerView reviewRecycleView;
    List<Review> reviewList;
    ReviewAdapter adapter;
    Button btn1, btn2, btn3, btn4, skip;

    private void BindView(){
        reviewRecycleView=findViewById(R.id.review_recyclerView);
        btn1=findViewById(R.id.btn1);
        btn2=findViewById(R.id.btn2);
        btn3=findViewById(R.id.btn3);
        btn4=findViewById(R.id.btn4);
        skip=findViewById(R.id.skip);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.walkthrought);

        Window window = getWindow();
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.burgundy));
        
        BindView();
        ViewPager viewPager = findViewById(R.id.viewPager);
        List<WalkthroughtFragment> fragments = new ArrayList<>();
        fragments.add(new WalkthroughtFragment(R.drawable.red_velvet_cake_pana, "Welcome to Scarlet app",
                "   Welcome to Cake App, your one-stop shop for all things cake! Whether you're a " +
                        "seasoned baker or just starting out, our app has something for everyone. " +
                        "Browse our extensive collection of cake recipes, find inspiration for your next baking project, " +
                        "and order custom cakes for any occasion.",1));
        fragments.add(new WalkthroughtFragment(R.drawable.privacy_policy_rafiki, "Discover a World of Cake Delights",
                "   Delve into our vast library of cake recipes, featuring a variety of flavors, styles, and techniques. " +
                        "From classic chocolate cakes to trendy red velvet creations, we have something to satisfy every sweet " +
                        "tooth. Each recipe includes detailed instructions, helpful tips, and beautiful images to guide you " +
                        "through the baking process.",2));
        fragments.add(new WalkthroughtFragment(R.drawable.rainbow_cake_pana, "Unleash Your Inner Cake Artist",
                "   Get inspired by our curated galleries of stunning cakes, showcasing the creativity and artistry of " +
                        "bakers worldwide. Browse by occasion, type of cake, or decorating style to find ideas for your next " +
                        "masterpiece. Whether you're planning a birthday cake, a wedding cake, or a simple treat for yourself, " +
                        "we'll help you create a cake that's both delicious and visually captivating.",3));
        fragments.add(new WalkthroughtFragment(R.drawable.take_away_pana, "Let Us Create Your Dream Cake",
                "   For those special occasions that call for something truly extraordinary, our custom cake ordering service " +
                        "is here to make your dreams come true. Simply share your vision with our talented cake designers, and " +
                        "we'll craft a personalized cake that reflects your unique style and taste. From intricate designs to " +
                        "personalized messages, we'll ensure your cake is the centerpiece of any celebration.",4));

        WalkthroughtAdapter adapter = new WalkthroughtAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1,true);
                btn1.setVisibility(View.GONE);
                btn2.setVisibility(View.VISIBLE);
                btn3.setVisibility(View.GONE);
                btn4.setVisibility(View.GONE);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2,true);
                btn1.setVisibility(View.GONE);
                btn2.setVisibility(View.GONE);
                btn3.setVisibility(View.VISIBLE);
                btn4.setVisibility(View.GONE);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(3,true);
                btn1.setVisibility(View.GONE);
                btn2.setVisibility(View.GONE);
                btn3.setVisibility(View.GONE);
                btn4.setVisibility(View.VISIBLE);
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(WalkthroughActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(WalkthroughActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int a=position;
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}