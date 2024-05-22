package com.example.scarlet.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
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
import java.util.Random;

public class AdminOfferFragment extends Fragment {

    TextView textDate;
    RecyclerView offerRecycleView;
    List<Offer> offerList;
    AdminOfferAdapter adminOfferAdapter;
    EditText offerName,offerDescription,offerPoint, search_bar;
    TextView offerStartDate,offerEndDate;
    ImageButton calendarStart,calendarEnd;
    ProgressDialog progressDialog;
    final Handler handler = new Handler();
    int delay=150;
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
        getAnimation();
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
                adminOfferAdapter.filterBySearch(keyword);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }
    private void getAnimation(){
        Animation searchAnim= AnimationUtils.loadAnimation(search_bar.getContext(), android.R.anim.slide_in_left);
        Animation recycleViewAnim= AnimationUtils.loadAnimation(offerRecycleView.getContext(), android.R.anim.fade_in);
        search_bar.startAnimation(searchAnim);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                offerRecycleView.startAnimation(recycleViewAnim);
            }
        },delay*3);
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
            String code=generateCode();
            Offer offer=new Offer(name,code,description,point,0,date1,date2);
            myRef.push().setValue(offer);
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
//            Toast.makeText(getContext(),"Insert successfully", Toast.LENGTH_SHORT).show();
            showSuccessDialog();
        }catch(ParseException e){
            e.printStackTrace();
        }
    }
    private String generateCode() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
        String currentTime = dateFormat.format(new Date());

        // Extract the first 8 characters
        String timeCode = currentTime.substring(0, 8);

        // Generate the remaining 4 characters
        StringBuilder codeBuilder = new StringBuilder(timeCode);
        Random random = new Random();
        int codeSizeSmall=6;
        for (int i = 0; i < codeSizeSmall; i++) {
            int charType = random.nextInt(3); // 0: lowercase, 1: uppercase, 2: number
            if (charType == 0) {
                codeBuilder.append((char) (random.nextInt(26) + 'a'));
            } else if (charType == 1) {
                codeBuilder.append((char) (random.nextInt(26) + 'A'));
            } else {
                codeBuilder.append(random.nextInt(10));
            }
        }

        return codeBuilder.toString();
    }
    public void showSuccessDialog(){
        final Dialog dialog=new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.success_dialog_2);

        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_circle_white_30));
        dialog.show();
    }
    public void showFailedDialog(){
        final Dialog dialog=new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.failed_dialog);

        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_circle_white_30));
        dialog.show();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}