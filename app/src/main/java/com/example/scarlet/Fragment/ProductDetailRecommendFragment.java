package com.example.scarlet.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.R;

public class ProductDetailRecommendFragment extends Fragment {
    RecyclerView recyclerView;
    public ProductDetailRecommendFragment(){

    }
    private void BindView(View view){
        recyclerView=view.findViewById(R.id.membership_recyclerView);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.product_detail_tabpage3, container, false);

        BindView(view);

        return view;
    }

}