package com.example.scarlet.Dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.scarlet.R;

import java.util.Calendar;

public class DatePickerDialog extends DialogFragment implements android.app.DatePickerDialog.OnDateSetListener {
    TextView textDate;
    public void setTextDate(TextView textDate){
        this.textDate=textDate;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar c=Calendar.getInstance();
        int year=c.get(Calendar.YEAR);
        int month=c.get(Calendar.MONTH);
        int day=c.get(Calendar.DATE);

        return new android.app.DatePickerDialog(getContext(),this,year,month,day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String d=String.valueOf(dayOfMonth);
        String m=String.valueOf(month+1);
        if(dayOfMonth>0 && dayOfMonth <10){
            d="0"+d;
        }
        if(month>0 && month<10){
            m="0"+m;
        }
        textDate.setText(d+"-"+m+"-"+year);
    }

}
