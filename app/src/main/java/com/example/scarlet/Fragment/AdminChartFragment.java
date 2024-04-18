package com.example.scarlet.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.scarlet.R;


public class AdminChartFragment extends Fragment {

    View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view =inflater.inflate(R.layout.admin_fragment_chart, container, false);

        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}