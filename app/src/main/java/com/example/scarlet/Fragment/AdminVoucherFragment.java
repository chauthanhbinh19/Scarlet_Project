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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Adapter.AdminDealAdapter;
import com.example.scarlet.Adapter.DealAdapter;
import com.example.scarlet.Adapter.GridLayoutDecoration;
import com.example.scarlet.Adapter.ProductCheckboxAdapter;
import com.example.scarlet.Data.Category;
import com.example.scarlet.Data.Deal;
import com.example.scarlet.Data.Offer;
import com.example.scarlet.Data.Product;
import com.example.scarlet.Dialog.DatePickerDialog;
import com.example.scarlet.Interface.GetKeyCallback;
import com.example.scarlet.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class AdminVoucherFragment extends Fragment {

    private int defaultStatus=1;
    private int keyStatus=0;
    RelativeLayout btnAll, btnDelivery, btnInstore, btnPickup;
    ImageView imageAll, imageDelivery, imageInstore, imagePickup;
    TextView textAll, textDelivery, textInstore, textPickup;
    private AdminDealAdapter dealAdapter;
    private List<Deal> dealList;
    RecyclerView recyclerView,productRecycleView;
    ProductCheckboxAdapter productCheckboxAdapter;
    ImageView calendar;
    EditText voucherName, voucherDiscount, deliveryMethod, voucherDiscription, search_bar;
    TextView expiryDate;
    List<Product> productList;
    GetKeyCallback getKeyCallback;
    List<String> keyList;
    ProgressDialog progressDialog;
    private void BindView(View view){
        btnAll=view.findViewById(R.id.btnAll);
        btnDelivery=view.findViewById(R.id.btnDelivery);
        btnInstore=view.findViewById(R.id.btnInstore);
        btnPickup=view.findViewById(R.id.btnPickup);
        imageAll=view.findViewById(R.id.image1);
        imageDelivery=view.findViewById(R.id.image2);
        imageInstore=view.findViewById(R.id.image3);
        imagePickup=view.findViewById(R.id.image4);
        textAll=view.findViewById(R.id.text1);
        textDelivery=view.findViewById(R.id.text2);
        textInstore=view.findViewById(R.id.text3);
        textPickup=view.findViewById(R.id.text4);
        recyclerView=view.findViewById(R.id.voucher_recyclerView);
        search_bar=view.findViewById(R.id.search_bar);
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.admin_fragment_voucher, container, false);

        BindView(view);
        toggleButtonEvent("");
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        recyclerView.addItemDecoration(new GridLayoutDecoration(0,5));
        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultStatus=1;
                toggleButtonEvent("");
            }
        });
        btnDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultStatus=2;
                toggleButtonEvent("");
            }
        });
        btnInstore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultStatus=3;
                toggleButtonEvent("");
            }
        });
        btnPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultStatus=4;
                toggleButtonEvent("");
            }
        });
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
                    keyStatus=0;
                    toggleButtonEvent("");
                }else{
                    keyStatus=1;
                    toggleButtonEvent(keyword);
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
        View dialogView = inflater.inflate(R.layout.admin_voucher_popup, null);

        voucherName = dialogView.findViewById(R.id.voucherName);
        deliveryMethod=dialogView.findViewById(R.id.deliveryMethod);
        voucherDiscount=dialogView.findViewById(R.id.voucherDiscount);
        voucherDiscription=dialogView.findViewById(R.id.voucherDescription);
        expiryDate=dialogView.findViewById(R.id.voucherExpiryDate);
        Button btnSave=dialogView.findViewById(R.id.btnSave);
        productRecycleView=dialogView.findViewById(R.id.product_recyclerView);
        calendar=dialogView.findViewById(R.id.calendar);
        RadioButton radioProduct=dialogView.findViewById(R.id.radioProduct);
        RadioButton radioAll=dialogView.findViewById(R.id.radioAll);
        ImageButton btnClose=dialogView.findViewById(R.id.btnClose);
        ScrollView scrollView=dialogView.findViewById(R.id.scrollProduct);

        productRecycleView.setLayoutManager(new GridLayoutManager(getContext(),1));
        productRecycleView.addItemDecoration(new GridLayoutDecoration(5,5));

        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_circle_white_30));
        keyList=new ArrayList<>();
        getKeyCallback=new GetKeyCallback() {
            @Override
            public void itemClick(String key, int type) {
                if(type==1){
                    keyList.add(key);
                }else{
                    keyList.remove(key);
                }
            }
        };
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        getProductData(getKeyCallback,productRecycleView);
        radioProduct.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    scrollView.setVisibility(View.VISIBLE);
                }
            }
        });
        radioAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    scrollView.setVisibility(View.GONE);
                }
            }
        });
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(dialogView);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(voucherName.getText().toString().isEmpty()){
                    voucherName.setError("Voucher name can not be empty");
                }else if(deliveryMethod.getText().toString().isEmpty()){
                    deliveryMethod.setError("Delivery method can not be empty");
                }else if(voucherDiscount.getText().toString().isEmpty()){
                    voucherDiscount.setError("Delivery discount can not be empty");
                }else if(voucherDiscription.getText().toString().isEmpty()){
                    voucherDiscription.setError("Voucher description can not be empty");
                }else if(expiryDate.getText().toString().isEmpty()){
                    expiryDate.setError("Expiry date can not be empty");
                }else{
                    saveVoucherData();
                }
            }
        });
        dialog.show();
    }
    public void showDatePickerDialog(View v){
        DatePickerDialog newFragment=new DatePickerDialog();
        newFragment.setTextDate(expiryDate);
        newFragment.show(getActivity().getSupportFragmentManager(), "DatePicker");
    }
    private void toggleButtonEvent(String keyword){
        if(defaultStatus==1){
            btnAll.setBackground(getResources().getDrawable(R.drawable.rectangle_burgundy_radius));
            imageAll.setImageResource(R.drawable.menu_12);
            textAll.setTextColor(getResources().getColor(R.color.white));

            btnDelivery.setBackground(getResources().getDrawable(R.drawable.rectangle_lightpink_radius));
            btnInstore.setBackground(getResources().getDrawable(R.drawable.rectangle_lightpink_radius));
            btnPickup.setBackground(getResources().getDrawable(R.drawable.rectangle_lightpink_radius));
            textDelivery.setTextColor(getResources().getColor(R.color.burgundy));
            textInstore.setTextColor(getResources().getColor(R.color.burgundy));
            textPickup.setTextColor(getResources().getColor(R.color.burgundy));

            if(keyStatus==1){
                searchAllVoucherData(recyclerView,keyword);
            }else{
                getAllVoucherData(recyclerView);
            }
        }else if(defaultStatus==2){
            btnDelivery.setBackground(getResources().getDrawable(R.drawable.rectangle_burgundy_radius));
            textDelivery.setTextColor(getResources().getColor(R.color.white));

            btnAll.setBackground(getResources().getDrawable(R.drawable.rectangle_lightpink_radius));
            btnInstore.setBackground(getResources().getDrawable(R.drawable.rectangle_lightpink_radius));
            btnPickup.setBackground(getResources().getDrawable(R.drawable.rectangle_lightpink_radius));
            textAll.setTextColor(getResources().getColor(R.color.burgundy));
            textInstore.setTextColor(getResources().getColor(R.color.burgundy));
            textPickup.setTextColor(getResources().getColor(R.color.burgundy));
            imageAll.setImageResource(R.drawable.menu_11);

            if(keyStatus==1){
                searchVoucherData(recyclerView,"delivery", R.drawable.delivery_bike, keyword);
            }else{
                getVoucherData(recyclerView,"delivery", R.drawable.delivery_bike);
            }
        }else if(defaultStatus==3){
            btnInstore.setBackground(getResources().getDrawable(R.drawable.rectangle_burgundy_radius));
            textInstore.setTextColor(getResources().getColor(R.color.white));

            btnDelivery.setBackground(getResources().getDrawable(R.drawable.rectangle_lightpink_radius));
            btnAll.setBackground(getResources().getDrawable(R.drawable.rectangle_lightpink_radius));
            btnPickup.setBackground(getResources().getDrawable(R.drawable.rectangle_lightpink_radius));
            textDelivery.setTextColor(getResources().getColor(R.color.burgundy));
            textAll.setTextColor(getResources().getColor(R.color.burgundy));
            textPickup.setTextColor(getResources().getColor(R.color.burgundy));
            imageAll.setImageResource(R.drawable.menu_11);

            if(keyStatus==1){
                searchVoucherData(recyclerView,"instore", R.drawable.store, keyword);
            }else{
                getVoucherData(recyclerView,"instore", R.drawable.store);
            }
        }else if(defaultStatus==4){
            btnPickup.setBackground(getResources().getDrawable(R.drawable.rectangle_burgundy_radius));
            textPickup.setTextColor(getResources().getColor(R.color.white));

            btnDelivery.setBackground(getResources().getDrawable(R.drawable.rectangle_lightpink_radius));
            btnInstore.setBackground(getResources().getDrawable(R.drawable.rectangle_lightpink_radius));
            btnAll.setBackground(getResources().getDrawable(R.drawable.rectangle_lightpink_radius));
            textDelivery.setTextColor(getResources().getColor(R.color.burgundy));
            textInstore.setTextColor(getResources().getColor(R.color.burgundy));
            textAll.setTextColor(getResources().getColor(R.color.burgundy));
            imageAll.setImageResource(R.drawable.menu_11);

            if(keyStatus==1){
                searchVoucherData(recyclerView,"pickup", R.drawable.food_delivery, keyword);
            }else{
                getVoucherData(recyclerView,"pickup", R.drawable.food_delivery);
            }
        }
    }
    private void getAllVoucherData(RecyclerView recyclerView){
        dealList=new ArrayList<>();
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        Query Query= firebaseDatabase.getReference("deal");
        Query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    dealList.clear();
                    for (DataSnapshot snap: snapshot.getChildren()){
                        String voucherName=snap.child("name").getValue(String.class);
                        int discount=snap.child("discount").getValue(int.class);
                        Date expiryDate=snap.child("expiryDate").getValue(Date.class);
                        String deliveryMethod=snap.child("deliveryMethod").getValue(String.class);
                        String description=snap.child("description").getValue(String.class);
                        String key=snap.getKey();
                        int deliveryMethodIcon=1;
                        if(deliveryMethod.equals("delivery")){
                            deliveryMethodIcon=R.drawable.delivery_bike;
                        }else if(deliveryMethod.equals("instore")){
                            deliveryMethodIcon=R.drawable.store;
                        }else if(deliveryMethod.equals("pickup")){
                            deliveryMethodIcon=R.drawable.food_delivery;
                        }
                        Deal deal=new Deal(voucherName,discount,expiryDate,deliveryMethod,deliveryMethodIcon,description,key);
                        dealList.add(deal);
                    }
                    if(dealList.size()>0){
                        dealAdapter=new AdminDealAdapter(dealList);
                        recyclerView.setAdapter(dealAdapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void searchAllVoucherData(RecyclerView recyclerView, String keyword){
        dealList=new ArrayList<>();
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        Query Query= firebaseDatabase.getReference("deal");
        Query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    dealList.clear();
                    for (DataSnapshot snap: snapshot.getChildren()){
                        String voucherName=snap.child("name").getValue(String.class);
                        int discount=snap.child("discount").getValue(int.class);
                        Date expiryDate=snap.child("expiryDate").getValue(Date.class);
                        String deliveryMethod=snap.child("deliveryMethod").getValue(String.class);
                        String description=snap.child("description").getValue(String.class);
                        String key=snap.getKey();
                        int deliveryMethodIcon=1;
                        if(deliveryMethod.equals("delivery")){
                            deliveryMethodIcon=R.drawable.delivery_bike;
                        }else if(deliveryMethod.equals("instore")){
                            deliveryMethodIcon=R.drawable.store;
                        }else if(deliveryMethod.equals("pickup")){
                            deliveryMethodIcon=R.drawable.food_delivery;
                        }
                        Deal deal=new Deal(voucherName,discount,expiryDate,deliveryMethod,deliveryMethodIcon,description,key);
                        dealList.add(deal);
                    }
                    dealList=filterVoucher(dealList,keyword);
                    if(dealList.size()>0){
                        dealAdapter=new AdminDealAdapter(dealList);
                        recyclerView.setAdapter(dealAdapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private List<Deal> filterVoucher(List<Deal> dealList, String keyword){
        List<Deal> result=new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        for(Deal deal : dealList){
            if(deal.getName().toLowerCase().contains(keyword.toLowerCase())){
                result.add(deal);
            }else if(String.valueOf(deal.getDiscount()).toLowerCase().contains(keyword.toLowerCase())){
                result.add(deal);
            }else if(format.format(deal.getExpiryDate()).toLowerCase().contains(keyword.toLowerCase())){
                result.add(deal);
            }
        }
        return result;
    }
    private void getVoucherData(RecyclerView recyclerView, String deliveryMethodClicked, int deliveryMethodIcon){
        dealList=new ArrayList<>();
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        Query Query= firebaseDatabase.getReference("deal");
        Query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    dealList.clear();
                    for (DataSnapshot snap: snapshot.getChildren()){
                        String voucherName=snap.child("name").getValue(String.class);
                        int discount=snap.child("discount").getValue(int.class);
                        Date expiryDate=snap.child("expiryDate").getValue(Date.class);
                        String deliveryMethod=snap.child("deliveryMethod").getValue(String.class);
                        String description=snap.child("description").getValue(String.class);
                        String key=snap.getKey();
                        Deal deal=new Deal(voucherName,discount,expiryDate,deliveryMethod,deliveryMethodIcon,description, key);
                        if(deliveryMethod.equals(deliveryMethodClicked)){
                            dealList.add(deal);
                        }
                    }
                    if(dealList.size()>0){
                        dealAdapter=new AdminDealAdapter(dealList);
                        recyclerView.setAdapter(dealAdapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void searchVoucherData(RecyclerView recyclerView, String deliveryMethodClicked, int deliveryMethodIcon, String keyword){
        dealList=new ArrayList<>();
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        Query Query= firebaseDatabase.getReference("deal");
        Query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    dealList.clear();
                    for (DataSnapshot snap: snapshot.getChildren()){
                        String voucherName=snap.child("name").getValue(String.class);
                        int discount=snap.child("discount").getValue(int.class);
                        Date expiryDate=snap.child("expiryDate").getValue(Date.class);
                        String deliveryMethod=snap.child("deliveryMethod").getValue(String.class);
                        String description=snap.child("description").getValue(String.class);
                        String key=snap.getKey();
                        Deal deal=new Deal(voucherName,discount,expiryDate,deliveryMethod,deliveryMethodIcon,description, key);
                        if(deliveryMethod.equals(deliveryMethodClicked)){
                            dealList.add(deal);
                        }
                    }
                    dealList=filterVoucher(dealList,keyword);
                    if(dealList.size()>0){
                        dealAdapter=new AdminDealAdapter(dealList);
                        recyclerView.setAdapter(dealAdapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getProductData(GetKeyCallback getKeyCallback, RecyclerView productRecycleView){
        productList=new ArrayList<>();
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference myRef=firebaseDatabase.getReference("product");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap: snapshot.getChildren()){
                    String productName=snap.child("name").getValue(String.class);
                    String key=snap.getKey();
                    Product product=new Product(key,productName);
                    productList.add(product);
                }
                productCheckboxAdapter=new ProductCheckboxAdapter(productList, getKeyCallback);
                productRecycleView.setAdapter(productCheckboxAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void saveVoucherData(){
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setTitle("Uploading file...");
        progressDialog.show();

        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference myRef= firebaseDatabase.getReference("deal");
        String code=generateCode();
        String vouchername=voucherName.getText().toString();
        int discount=Integer.parseInt(voucherDiscount.getText().toString());
        String voucherdescription=voucherDiscription.getText().toString();
        String deliverymethod=deliveryMethod.getText().toString();
        String date=expiryDate.getText().toString();
        Date expirydate;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            expirydate = dateFormat.parse(date);
            Deal deal=new Deal(vouchername,code,keyList,discount,expirydate, voucherdescription,deliverymethod);
            myRef.push().setValue(deal);
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            Toast.makeText(getContext(),"Insert successfully", Toast.LENGTH_SHORT).show();
        } catch (ParseException e) {
            e.printStackTrace(); // Handle parsing exception
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
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}