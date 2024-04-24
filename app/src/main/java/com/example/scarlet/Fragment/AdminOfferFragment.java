package com.example.scarlet.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Adapter.AdminOfferAdapter;
import com.example.scarlet.Adapter.GridLayoutDecoration;
import com.example.scarlet.Data.Category;
import com.example.scarlet.Data.Offer;
import com.example.scarlet.Data.User;
import com.example.scarlet.Dialog.DatePickerDialog;
import com.example.scarlet.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdminOfferFragment extends Fragment {

    TextView textDate;
    RecyclerView offerRecycleView;
    List<Offer> offerList;
    AdminOfferAdapter adminOfferAdapter;
    EditText offerName,offerDescription,offerPoint, search_bar;
    TextView offerStartDate,offerEndDate;
    ImageButton calendarStart,calendarEnd;
    ProgressDialog progressDialog;
    private void BindView(View view){
        offerRecycleView=view.findViewById(R.id.offer_recyclerView);
        search_bar=view.findViewById(R.id.search_bar);
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.admin_fragment_offer, container, false);

        BindView(view);
        offerList=new ArrayList<>();
        offerRecycleView.setLayoutManager(new GridLayoutManager(getContext(),1));
        offerRecycleView.addItemDecoration(new GridLayoutDecoration(5,0));

        getOfferData();
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInsertPopup();
            }
        });
        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String keyword=s.toString().trim();
                if(keyword==null || keyword.isEmpty()){
                    getOfferData();
                }else{
                    searchOfferData(keyword);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }
    public void showInsertPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.admin_offer_popup, null);

        offerName=dialogView.findViewById(R.id.offerName);
        offerDescription=dialogView.findViewById(R.id.offerDescription);
        offerPoint=dialogView.findViewById(R.id.offerPoint);
        offerStartDate=dialogView.findViewById(R.id.offerStartDate);
        offerEndDate=dialogView.findViewById(R.id.offerEndDate);
        calendarStart=dialogView.findViewById(R.id.calendarStart);
        calendarEnd=dialogView.findViewById(R.id.calendarEnd);
        ImageButton btnClose=dialogView.findViewById(R.id.btnClose);
        Button btnSave=dialogView.findViewById(R.id.btnSave);

        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_circle_white_30));
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        calendarStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(offerStartDate);
            }
        });
        calendarEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(offerEndDate);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(offerName.getText().toString().isEmpty()){
                    offerName.setError("Offer name can not be empty");
                }else if(offerDescription.getText().toString().isEmpty()){
                    offerDescription.setError("Offer description can not be empty");
                }else if(offerPoint.getText().toString().isEmpty()){
                    offerPoint.setText("Offer point can not be empty");
                }else if(offerStartDate.getText().toString().isEmpty()){
                    offerStartDate.setError("Offer start date must be selected");
                }else if(offerEndDate.getText().toString().isEmpty()){
                    offerEndDate.setError("Offer end date must be selected");
                }else{
                    saveOfferData();
                }
            }
        });

        dialog.show();
    }
    public void showDatePickerDialog(TextView textView){
        DatePickerDialog newFragment=new DatePickerDialog();
        newFragment.setTextDate(textView);
        newFragment.show(getActivity().getSupportFragmentManager(), "DatePicker");
    }
    private void getOfferData(){
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference myRef= firebaseDatabase.getReference("offer");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                offerList.clear();
                for(DataSnapshot snap:snapshot.getChildren()){
                    String key=snap.getKey();
                    Date startDate=snap.child("startDate").getValue(Date.class);
                    Date endDate=snap.child("endDate").getValue(Date.class);
                    String name=snap.child("name").getValue(String.class);
                    String description=snap.child("description").getValue(String.class);
                    int point=snap.child("point").getValue(int.class);
                    Offer offer=new Offer(name,description,point,R.drawable.discount_offer,startDate,endDate,key);
                    offerList.add(offer);
                }
                adminOfferAdapter=new AdminOfferAdapter(offerList);
                offerRecycleView.setAdapter(adminOfferAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void searchOfferData(String keyword){
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference myRef= firebaseDatabase.getReference("offer");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                offerList.clear();
                for(DataSnapshot snap:snapshot.getChildren()){
                    String key=snap.getKey();
                    Date startDate=snap.child("startDate").getValue(Date.class);
                    Date endDate=snap.child("endDate").getValue(Date.class);
                    String name=snap.child("name").getValue(String.class);
                    String description=snap.child("description").getValue(String.class);
                    int point=snap.child("point").getValue(int.class);
                    Offer offer=new Offer(name,description,point,R.drawable.discount_offer,startDate,endDate,key);
                    offerList.add(offer);
                }
                offerList=filterOffer(offerList,keyword);
                adminOfferAdapter=new AdminOfferAdapter(offerList);
                offerRecycleView.setAdapter(adminOfferAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private List<Offer> filterOffer(List<Offer> offerList, String keyword){
        List<Offer> result=new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        for(Offer offer : offerList){
            if(offer.getName().toLowerCase().contains(keyword.toLowerCase())){
                result.add(offer);
            }else if(String.valueOf(offer.getPoint()).toLowerCase().contains(keyword.toLowerCase())){
                result.add(offer);
            }else if(format.format(offer.getStartDate()).toLowerCase().contains(keyword.toLowerCase())){
                result.add(offer);
            }else if(format.format(offer.getEndDate()).toLowerCase().contains(keyword.toLowerCase())){
                result.add(offer);
            }
        }
        return result;
    }
    private void saveOfferData(){
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setTitle("Uploading file...");
        progressDialog.show();

        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference myRef= firebaseDatabase.getReference("offer");
        String name=offerName.getText().toString();
        String description=offerDescription.getText().toString();
        int point=Integer.parseInt(offerPoint.getText().toString());
        String startdate=offerStartDate.getText().toString();
        String enddate=offerEndDate.getText().toString();
        Date date1=new Date();
        Date date2=new Date();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            date1 = dateFormat.parse(startdate);
            date2 = dateFormat.parse(enddate);
            Offer offer=new Offer(name,description,point,0,date1,date2);
            myRef.push().setValue(offer);
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            Toast.makeText(getContext(),"Insert successfully", Toast.LENGTH_SHORT).show();
        }catch(ParseException e){
            e.printStackTrace();
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}