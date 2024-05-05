package com.example.scarlet.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.scarlet.Adapter.GridLayoutDecoration;
import com.example.scarlet.Adapter.MembershipAdapter;
import com.example.scarlet.Data.Cart;
import com.example.scarlet.Data.Membership;
import com.example.scarlet.Data.ProductQuantity;
import com.example.scarlet.ProductDetailActivity;
import com.example.scarlet.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailInfoFragment extends Fragment {
    RecyclerView recyclerView;
    public ProductDetailInfoFragment(){
    }
    private void BindView(View view){
        recyclerView=view.findViewById(R.id.membership_recyclerView);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.product_detail_tabpage1, container, false);

        BindView(view);
        return view;
    }
}