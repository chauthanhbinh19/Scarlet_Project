package com.example.scarlet.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.scarlet.MainActivity;
import com.example.scarlet.R;

public class FavouriteFragment extends Fragment {
    private EditText editText;
    private MeowBottomNavigation bottomNavigation;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.favourite, container, false);
        EditText editText=view.findViewById(R.id.edit_btn_1);
        bottomNavigation=getActivity().findViewById(R.id.bottomNavigation);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        return view;
    }

}