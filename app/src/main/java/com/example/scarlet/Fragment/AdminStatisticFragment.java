package com.example.scarlet.Fragment;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Adapter.AdminCategoryAdapter;
import com.example.scarlet.Adapter.GridLayoutDecoration;
import com.example.scarlet.Data.Category;
import com.example.scarlet.Data.Product;
import com.example.scarlet.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;


public class AdminStatisticFragment extends Fragment {

    View view;
    Spinner spinner;
    TextView title,column1;
    private TableLayout tableLayout1, tableLayout2, tableLayout3;
    HashMap<String, Product> productMap = new HashMap<>();
    RelativeLayout productBox, revenueBox, categoryBox;
    int status=1;
    int yearInt=0;
    private void BindView(View view){
        tableLayout1=view.findViewById(R.id.tableLayout1);
        tableLayout2=view.findViewById(R.id.tableLayout2);
        tableLayout3=view.findViewById(R.id.tableLayout3);
        spinner=view.findViewById(R.id.spinner);
        title=view.findViewById(R.id.title);
        productBox=view.findViewById(R.id.productBox);
        revenueBox=view.findViewById(R.id.revenueBox);
        categoryBox=view.findViewById(R.id.categoryBox);
        column1=view.findViewById(R.id.column1);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view =inflater.inflate(R.layout.admin_fragment_statistics, container, false);
        BindView(view);
        List<String> years = new ArrayList<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 2023; i <= currentYear; i++) {
            years.add(String.valueOf(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, years);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedYear = parent.getItemAtPosition(position).toString();
                yearInt=Integer.parseInt(selectedYear);
                changeStatus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        productBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status=1;
                changeStatus();
            }
        });
        revenueBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status=2;
                changeStatus();
            }
        });
        categoryBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status=3;
                changeStatus();
            }
        });

        return view;
    }
    private void changeStatus(){
        switch (status){
            case 1:
                title.setText("Product");
                column1.setText("Name");
                tableLayout1.setVisibility(View.VISIBLE);
                tableLayout2.setVisibility(View.GONE);
                tableLayout3.setVisibility(View.GONE);
                getProductData(yearInt);
                break;
            case 2:
                title.setText("Revenue");
                column1.setText("Month");
                tableLayout1.setVisibility(View.GONE);
                tableLayout2.setVisibility(View.VISIBLE);
                tableLayout3.setVisibility(View.GONE);
                getRevenueData(yearInt);
                break;
            case 3:
                title.setText("Category");
                column1.setText("Name");
                tableLayout1.setVisibility(View.GONE);
                tableLayout2.setVisibility(View.GONE);
                tableLayout3.setVisibility(View.VISIBLE);
                getCategoryData(yearInt);
                break;
        }
    }
    private void getProductData(int yearInput){
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference myRef= firebaseDatabase.getReference("order");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productMap.clear();
                for(DataSnapshot snap: snapshot.getChildren()){
                    DataSnapshot productSnapshot=snap.child("productList");
                    Date date=snap.child("orderDate").getValue(Date.class);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    int year=calendar.get(Calendar.YEAR);
                    if(year==yearInput){
                        for(DataSnapshot snapshot1:productSnapshot.getChildren()){
                            String name=snapshot1.child("name").getValue(String.class);
                            double total=snapshot1.child("total").getValue(double.class);
                            int quantity=snapshot1.child("quantity").getValue(int.class);

                            // Kiểm tra xem sản phẩm đã tồn tại trong productMap hay chưa
                            if(productMap.containsKey(name)) {
                                // Nếu tồn tại, cập nhật số lượng và tổng giá của sản phẩm
                                Product existingProduct = productMap.get(name);
                                existingProduct.setQuantity(existingProduct.getQuantity() + quantity);
                                existingProduct.setTotal(existingProduct.getTotal() + total);
                            } else {
                                // Nếu không tồn tại, tạo một sản phẩm mới và thêm vào productMap
                                Product newProduct = new Product(name, quantity, total);
                                productMap.put(name, newProduct);
                            }
                        }
                    }
                }
                int count =0;
                tableLayout1.removeAllViews();
                tableLayout2.removeAllViews();
                tableLayout3.removeAllViews();
                for (Map.Entry<String, Product> entry : productMap.entrySet()) {
                    String productName = entry.getKey();
                    Product product = entry.getValue();

                    TableRow newRow = new TableRow(getContext());
                    TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                            0, // width
                            TableRow.LayoutParams.WRAP_CONTENT, // height
                            1f // weight
                    );


                    TextView nameTextView = new TextView(getContext());
                    nameTextView.setText(productName);
                    nameTextView.setTextSize(13);
                    nameTextView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                    nameTextView.setLayoutParams(layoutParams);
                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) nameTextView.getLayoutParams();
                    marginLayoutParams.setMargins(30, 10, marginLayoutParams.rightMargin, 10);
                    nameTextView.setLayoutParams(marginLayoutParams);

                    newRow.addView(nameTextView);

                    TextView quantityTextView = new TextView(getContext());
                    quantityTextView.setText(String.valueOf(product.getQuantity()));
                    quantityTextView.setTextSize(15);
                    quantityTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER);
                    quantityTextView.setLayoutParams(layoutParams);

                    newRow.addView(quantityTextView);

                    TextView priceTextView = new TextView(getContext());
                    priceTextView.setText(String.format("%.0f",product.getTotal())+" đ");
                    priceTextView.setTextSize(15);
                    priceTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
                    priceTextView.setLayoutParams(layoutParams);
                    ViewGroup.MarginLayoutParams marginLayoutParams1 = (ViewGroup.MarginLayoutParams) priceTextView.getLayoutParams();
                    marginLayoutParams1.setMargins(marginLayoutParams1.leftMargin, 10, 30, 10);
                    priceTextView.setLayoutParams(marginLayoutParams1);

                    newRow.addView(priceTextView);
                    newRow.setPadding(5,10,5,10);
                    if (count % 2 == 0) {
                        newRow.setBackgroundColor(getResources().getColor(R.color.white));
                    } else {
                        newRow.setBackgroundColor(getResources().getColor(R.color.white2));
                    }
                    count++;

                    tableLayout1.addView(newRow);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getCategoryData(int yearInput){
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference myRef= firebaseDatabase.getReference("order");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productMap.clear();
                for(DataSnapshot snap: snapshot.getChildren()){
                    DataSnapshot productSnapshot=snap.child("productList");
                    Date date=snap.child("orderDate").getValue(Date.class);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    int monthInt = calendar.get(Calendar.MONTH);
                    int year=calendar.get(Calendar.YEAR);
                    if(year==yearInput){
                        for(DataSnapshot snapshot1:productSnapshot.getChildren()){
                            String name=snapshot1.child("categoryName").getValue(String.class);
                            double total=snapshot1.child("total").getValue(double.class);
                            int quantity=snapshot1.child("quantity").getValue(int.class);

                            // Kiểm tra xem sản phẩm đã tồn tại trong productMap hay chưa
                            if(productMap.containsKey(name)) {
                                // Nếu tồn tại, cập nhật số lượng và tổng giá của sản phẩm
                                Product existingProduct = productMap.get(name);
                                existingProduct.setQuantity(existingProduct.getQuantity() + quantity);
                                existingProduct.setTotal(existingProduct.getTotal() + total);
                            } else {
                                // Nếu không tồn tại, tạo một sản phẩm mới và thêm vào productMap
                                Product newProduct = new Product(name, quantity, total);
                                productMap.put(name, newProduct);
                            }
                        }
                    }
                }
                int count =0;
                tableLayout1.removeAllViews();
                tableLayout2.removeAllViews();
                tableLayout3.removeAllViews();
                for (Map.Entry<String, Product> entry : productMap.entrySet()) {
                    String productName = entry.getKey();
                    Product product = entry.getValue();

                    TableRow newRow = new TableRow(getContext());
                    TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                            0, // width
                            TableRow.LayoutParams.WRAP_CONTENT, // height
                            1f // weight
                    );


                    TextView nameTextView = new TextView(getContext());
                    nameTextView.setText(productName);
                    nameTextView.setTextSize(13);
                    nameTextView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                    nameTextView.setLayoutParams(layoutParams);
                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) nameTextView.getLayoutParams();
                    marginLayoutParams.setMargins(30, 10, marginLayoutParams.rightMargin, 10);
                    nameTextView.setLayoutParams(marginLayoutParams);

                    newRow.addView(nameTextView);

                    TextView quantityTextView = new TextView(getContext());
                    quantityTextView.setText(String.valueOf(product.getQuantity()));
                    quantityTextView.setTextSize(15);
                    quantityTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER);
                    quantityTextView.setLayoutParams(layoutParams);

                    newRow.addView(quantityTextView);

                    TextView priceTextView = new TextView(getContext());
                    priceTextView.setText(String.format("%.0f",product.getTotal())+" đ");
                    priceTextView.setTextSize(15);
                    priceTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
                    priceTextView.setLayoutParams(layoutParams);
                    ViewGroup.MarginLayoutParams marginLayoutParams1 = (ViewGroup.MarginLayoutParams) priceTextView.getLayoutParams();
                    marginLayoutParams1.setMargins(marginLayoutParams1.leftMargin, 10, 30, 10);
                    priceTextView.setLayoutParams(marginLayoutParams1);

                    newRow.addView(priceTextView);
                    newRow.setPadding(5,10,5,10);
                    if (count % 2 == 0) {
                        newRow.setBackgroundColor(getResources().getColor(R.color.white));
                    } else {
                        newRow.setBackgroundColor(getResources().getColor(R.color.white2));
                    }
                    count++;

                    tableLayout3.addView(newRow);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getRevenueData(int yearInput){
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference myRef= firebaseDatabase.getReference("order");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productMap.clear();
                for(DataSnapshot snap: snapshot.getChildren()){
                    DataSnapshot productSnapshot=snap.child("productList");
                    Date date=snap.child("orderDate").getValue(Date.class);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    int monthInt = calendar.get(Calendar.MONTH);
                    int year=calendar.get(Calendar.YEAR);
                    if(year==yearInput){
                        String monthName;
                        DateFormatSymbols dfs = new DateFormatSymbols(Locale.ENGLISH);
                        String[] months = dfs.getMonths();
                        if (monthInt >= 0 && monthInt <= 11) {
                            monthName = months[monthInt];
                        } else {
                            monthName = "Invalid month";
                        }
                        for(DataSnapshot snapshot1:productSnapshot.getChildren()){
                            double total=snapshot1.child("total").getValue(double.class);
                            int quantity=snapshot1.child("quantity").getValue(int.class);

                            // Kiểm tra xem sản phẩm đã tồn tại trong productMap hay chưa
                            if(productMap.containsKey(monthName)) {
                                // Nếu tồn tại, cập nhật số lượng và tổng giá của sản phẩm
                                Product existingProduct = productMap.get(monthName);
                                existingProduct.setQuantity(existingProduct.getQuantity() + quantity);
                                existingProduct.setTotal(existingProduct.getTotal() + total);
                            } else {
                                // Nếu không tồn tại, tạo một sản phẩm mới và thêm vào productMap
                                Product newProduct = new Product(monthName, quantity, total);
                                productMap.put(monthName, newProduct);
                            }
                        }
                    }
                }
                TreeMap<String, Product> sortedProductMap = new TreeMap<>(new Comparator<String>() {
                    DateFormatSymbols dfs = new DateFormatSymbols(Locale.ENGLISH);
                    String[] months = dfs.getMonths();

                    @Override
                    public int compare(String month1, String month2) {
                        int index1 = Arrays.asList(months).indexOf(month1);
                        int index2 = Arrays.asList(months).indexOf(month2);
                        return Integer.compare(index1, index2);
                    }
                });
                sortedProductMap.putAll(productMap);
                int count =0;
                tableLayout1.removeAllViews();
                tableLayout2.removeAllViews();
                tableLayout3.removeAllViews();
                for (Map.Entry<String, Product> entry : sortedProductMap.entrySet()) {
                    String productName = entry.getKey();
                    Product product = entry.getValue();

                    TableRow newRow = new TableRow(getContext());
                    TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                            0, // width
                            TableRow.LayoutParams.WRAP_CONTENT, // height
                            1f // weight
                    );


                    TextView nameTextView = new TextView(getContext());
                    nameTextView.setText(productName);
                    nameTextView.setTextSize(13);
                    nameTextView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                    nameTextView.setLayoutParams(layoutParams);
                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) nameTextView.getLayoutParams();
                    marginLayoutParams.setMargins(30, 10, marginLayoutParams.rightMargin, 10);
                    nameTextView.setLayoutParams(marginLayoutParams);

                    newRow.addView(nameTextView);

                    TextView quantityTextView = new TextView(getContext());
                    quantityTextView.setText(String.valueOf(product.getQuantity()));
                    quantityTextView.setTextSize(15);
                    quantityTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER);
                    quantityTextView.setLayoutParams(layoutParams);

                    newRow.addView(quantityTextView);

                    TextView priceTextView = new TextView(getContext());
                    priceTextView.setText(String.format("%.0f",product.getTotal())+" đ");
                    priceTextView.setTextSize(15);
                    priceTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
                    priceTextView.setLayoutParams(layoutParams);
                    ViewGroup.MarginLayoutParams marginLayoutParams1 = (ViewGroup.MarginLayoutParams) priceTextView.getLayoutParams();
                    marginLayoutParams1.setMargins(marginLayoutParams1.leftMargin, 10, 30, 10);
                    priceTextView.setLayoutParams(marginLayoutParams1);

                    newRow.addView(priceTextView);
                    newRow.setPadding(5,10,5,10);
                    if (count % 2 == 0) {
                        newRow.setBackgroundColor(getResources().getColor(R.color.white));
                    } else {
                        newRow.setBackgroundColor(getResources().getColor(R.color.white2));
                    }
                    count++;

                    tableLayout2.addView(newRow);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void test(){
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference myRef= firebaseDatabase.getReference("order");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productMap.clear();
                for(DataSnapshot snap: snapshot.getChildren()){
                    DataSnapshot productSnapshot=snap.child("productList");

                    for(DataSnapshot snapshot1:productSnapshot.getChildren()){
                        double total=snapshot1.child("total").getValue(double.class);
                        int quantity=snapshot1.child("quantity").getValue(int.class);

                        DatabaseReference productRef = snapshot1.getRef();
                        productRef.child("size").setValue("");

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}