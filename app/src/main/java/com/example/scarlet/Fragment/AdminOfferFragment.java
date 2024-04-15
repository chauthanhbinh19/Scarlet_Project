package com.example.scarlet.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.scarlet.Dialog.DatePickerDialog;
import com.example.scarlet.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AdminOfferFragment extends Fragment {

    TextView textDate;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.admin_fragment_offer, container, false);

        Button button=view.findViewById(R.id.button);
        textDate=view.findViewById(R.id.date);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(view);
            }
        });
        return view;
    }
    public void showDatePickerDialog(View v){
        DatePickerDialog newFragment=new DatePickerDialog();
        newFragment.setTextDate(textDate);
        newFragment.show(getActivity().getSupportFragmentManager(), "DatePicker");
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}