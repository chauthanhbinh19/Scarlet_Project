package com.example.scarlet.Fragment;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.scarlet.Interface.GetListStringCallback;
import com.example.scarlet.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class AdminChartFragment extends Fragment {

    View view;
    int defaultStauts=1;
    RelativeLayout bar, line, pie, barBox, lineBox, pieBox;
    TextView textbar, textline, textpie;
    BarChart barChart, barChart1, barChart2;
    LineChart lineChart, lineChart1, lineChart2;
    PieChart pieChart, pieChart1, pieChart2;
    ArrayList<BarEntry> barEntries, barEntries1, barEntries2;
    ArrayList<Entry> lineEntries, lineEntries1, lineEntries2;
    List<PieEntry> pieEntries, pieEntries1, pieEntries2;
    String[] monthLabels = new String[]{"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};
    String[] monthLabels1 = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Step", "Oct", "Nov", "Dec"};
    String[] monthLabels2 = new String[]{"","Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Step", "Oct", "Nov", "Dec"};
    final Handler handler = new Handler();
    int delay=150;
    Spinner spinner1, spinner2, spinner3, spinner4, spinner5, spinner6, spinner7, spinner8, spinner9,
            spinner10, spinner11, spinner12;
    GetListStringCallback getListStringCallback;
    List<String> categoryList;
    private int selectedYear = 0, type1=0, type2=0, type3=0;
    private String selectedCategory = "";
    private void BindView(View view){
        bar=view.findViewById(R.id.barchartBox);
        line=view.findViewById(R.id.linechartBox);
        pie=view.findViewById(R.id.piechartBox);
        textbar=view.findViewById(R.id.textbar);
        textline=view.findViewById(R.id.textline);
        textpie=view.findViewById(R.id.textpie);
        barBox=view.findViewById(R.id.barBox);
        lineBox=view.findViewById(R.id.lineBox);
        pieBox=view.findViewById(R.id.pieBox);

        spinner1=view.findViewById(R.id.spinner1);
        spinner2=view.findViewById(R.id.spinner2);
        spinner3=view.findViewById(R.id.spinner3);
        spinner4=view.findViewById(R.id.spinner4);
        spinner5=view.findViewById(R.id.spinner5);
        spinner6=view.findViewById(R.id.spinner6);
        spinner7=view.findViewById(R.id.spinner7);
        spinner8=view.findViewById(R.id.spinner8);
        spinner9=view.findViewById(R.id.spinner9);
        spinner10=view.findViewById(R.id.spinner10);
        spinner11=view.findViewById(R.id.spinner11);
        spinner12=view.findViewById(R.id.spinner12);


        barChart = view.findViewById(R.id.barChart);
        barChart1=view.findViewById(R.id.barChart1);
        barChart2=view.findViewById(R.id.barChart2);

        lineChart = view.findViewById(R.id.lineChart);
        lineChart1=view.findViewById(R.id.lineChart1);
        lineChart2=view.findViewById(R.id.lineChart2);

        pieChart = view.findViewById(R.id.pieChart);
        pieChart1=view.findViewById(R.id.pieChart1);
        pieChart2=view.findViewById(R.id.pieChart2);
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view =inflater.inflate(R.layout.admin_fragment_chart, container, false);

        BindView(view);
        barEntries = new ArrayList<>();
        barEntries1=new ArrayList<>();
        barEntries2=new ArrayList<>();

        lineEntries = new ArrayList<>();
        lineEntries1=new ArrayList<>();
        lineEntries2=new ArrayList<>();

        pieEntries = new ArrayList<>();
        pieEntries1=new ArrayList<>();
        pieEntries2=new ArrayList<>();

        getDefaultStatus();
        getAnimation();
        List<String> years = new ArrayList<>();
        categoryList=new ArrayList<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 2023; i <= currentYear; i++) {
            years.add(String.valueOf(i));
        }
        getListStringCallback=new GetListStringCallback() {
            @Override
            public void getItem(List<String> items) {
                categoryList=items;
                ArrayAdapter<String> adapter1 = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categoryList);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner10.setAdapter(adapter1);
                spinner11.setAdapter(adapter1);
                spinner12.setAdapter(adapter1);
            }
        };
        getCategoryName(getListStringCallback);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, years);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(adapter);
        spinner4.setAdapter(adapter);
        spinner7.setAdapter(adapter);
        spinner2.setAdapter(adapter);
        spinner5.setAdapter(adapter);
        spinner8.setAdapter(adapter);
        spinner3.setAdapter(adapter);
        spinner6.setAdapter(adapter);
        spinner9.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedYear = parent.getItemAtPosition(position).toString();
                int yearInt=Integer.parseInt(selectedYear);
                getTotalRevenueByMonth(yearInt,1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedYear = parent.getItemAtPosition(position).toString();
                int yearInt=Integer.parseInt(selectedYear);
                getTotalRevenueByMonth(yearInt,2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner7.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedYear = parent.getItemAtPosition(position).toString();
                int yearInt=Integer.parseInt(selectedYear);
                getTotalRevenueByMonth(yearInt,3);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedYearString = parent.getItemAtPosition(position).toString();
                selectedYear=Integer.parseInt(selectedYearString);
                type1=1;
                handleSpinnerSelection();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner10.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String categoryNameString = parent.getItemAtPosition(position).toString();
                selectedCategory=categoryNameString;
                type1=1;
                handleSpinnerSelection();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedYearString = parent.getItemAtPosition(position).toString();
                selectedYear=Integer.parseInt(selectedYearString);
                type1=2;
                handleSpinnerSelection();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner11.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String categoryNameString = parent.getItemAtPosition(position).toString();
                selectedCategory=categoryNameString;
                type1=2;
                handleSpinnerSelection();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner8.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedYearString = parent.getItemAtPosition(position).toString();
                selectedYear=Integer.parseInt(selectedYearString);
                type1=3;
                handleSpinnerSelection();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner12.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String categoryNameString = parent.getItemAtPosition(position).toString();
                selectedCategory=categoryNameString;
                type1=3;
                handleSpinnerSelection();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedYear = parent.getItemAtPosition(position).toString();
                int yearInt=Integer.parseInt(selectedYear);
                getCakeTypeCompare(yearInt,1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedYear = parent.getItemAtPosition(position).toString();
                int yearInt=Integer.parseInt(selectedYear);
                getCakeTypeCompare(yearInt,2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner9.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedYear = parent.getItemAtPosition(position).toString();
                int yearInt=Integer.parseInt(selectedYear);
                getCakeTypeCompare(yearInt,3);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultStauts=1;
                getDefaultStatus();
            }
        });
        line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultStauts=2;
                getDefaultStatus();
            }
        });
        pie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultStauts=3;
                getDefaultStatus();
            }
        });
        return view;
    }
    private void getAnimation(){
        Animation barAnim= AnimationUtils.loadAnimation(bar.getContext(), android.R.anim.slide_in_left);
        Animation lineAnim= AnimationUtils.loadAnimation(line.getContext(), android.R.anim.slide_in_left);
        Animation pieAnim= AnimationUtils.loadAnimation(pie.getContext(), android.R.anim.slide_in_left);
        bar.startAnimation(barAnim);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                line.startAnimation(lineAnim);
            }
        },delay*0);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pie.startAnimation(pieAnim);
            }
        },delay*0);
    }
    private void getDefaultStatus(){
        switch (defaultStauts){
            case 1:
                bar.setBackground(getResources().getDrawable(R.drawable.rectangle_burgundy_radius));
                line.setBackground(getResources().getDrawable(R.drawable.rectangle_lightpink_radius));
                pie.setBackground(getResources().getDrawable(R.drawable.rectangle_lightpink_radius));
                textbar.setTextColor(getResources().getColor(R.color.white));
                textline.setTextColor(getResources().getColor(R.color.burgundy));
                textpie.setTextColor(getResources().getColor(R.color.burgundy));
                barBox.setVisibility(View.VISIBLE);
                lineBox.setVisibility(View.GONE);
                pieBox.setVisibility(View.GONE);

                spinner1.setVisibility(View.VISIBLE);
                spinner2.setVisibility(View.VISIBLE);
                spinner3.setVisibility(View.VISIBLE);
                spinner10.setVisibility(View.VISIBLE);

                spinner4.setVisibility(View.GONE);
                spinner5.setVisibility(View.GONE);
                spinner6.setVisibility(View.GONE);

                spinner7.setVisibility(View.GONE);
                spinner8.setVisibility(View.GONE);
                spinner9.setVisibility(View.GONE);
                openBarChart();
                break;
            case 2:
                bar.setBackground(getResources().getDrawable(R.drawable.rectangle_lightpink_radius));
                line.setBackground(getResources().getDrawable(R.drawable.rectangle_burgundy_radius));
                pie.setBackground(getResources().getDrawable(R.drawable.rectangle_lightpink_radius));
                textbar.setTextColor(getResources().getColor(R.color.burgundy));
                textline.setTextColor(getResources().getColor(R.color.white));
                textpie.setTextColor(getResources().getColor(R.color.burgundy));
                barBox.setVisibility(View.GONE);
                lineBox.setVisibility(View.VISIBLE);
                pieBox.setVisibility(View.GONE);
                openLineChart();

                spinner1.setVisibility(View.GONE);
                spinner2.setVisibility(View.GONE);
                spinner3.setVisibility(View.GONE);

                spinner4.setVisibility(View.VISIBLE);
                spinner5.setVisibility(View.VISIBLE);
                spinner6.setVisibility(View.VISIBLE);
                spinner11.setVisibility(View.VISIBLE);

                spinner7.setVisibility(View.GONE);
                spinner8.setVisibility(View.GONE);
                spinner9.setVisibility(View.GONE);
                break;
            case 3:
                bar.setBackground(getResources().getDrawable(R.drawable.rectangle_lightpink_radius));
                line.setBackground(getResources().getDrawable(R.drawable.rectangle_lightpink_radius));
                pie.setBackground(getResources().getDrawable(R.drawable.rectangle_burgundy_radius));
                textbar.setTextColor(getResources().getColor(R.color.burgundy));
                textline.setTextColor(getResources().getColor(R.color.burgundy));
                textpie.setTextColor(getResources().getColor(R.color.white));
                barBox.setVisibility(View.GONE);
                lineBox.setVisibility(View.GONE);
                pieBox.setVisibility(View.VISIBLE);
                openPieChart();

                spinner1.setVisibility(View.GONE);
                spinner2.setVisibility(View.GONE);
                spinner3.setVisibility(View.GONE);

                spinner4.setVisibility(View.GONE);
                spinner5.setVisibility(View.GONE);
                spinner6.setVisibility(View.GONE);

                spinner7.setVisibility(View.VISIBLE);
                spinner8.setVisibility(View.VISIBLE);
                spinner9.setVisibility(View.VISIBLE);
                spinner12.setVisibility(View.VISIBLE);
                break;
        }
    }
    private void openBarChart(){
        getTotalRevenueByMonth(2024,1);
        getCakeTypeByMonth(2024,1, "Cookies");
        getCakeTypeCompare(2024,1);
    }
    private void openLineChart(){
        getTotalRevenueByMonth(2024,2);
        getCakeTypeByMonth(2024,2, "Cookies");
        getCakeTypeCompare(2024,2);
    }
    private void openPieChart(){
        getTotalRevenueByMonth(2024,3);
        getCakeTypeByMonth(2024,3, "Cookies");
        getCakeTypeCompare(2024,3);
    }
    private void handleSpinnerSelection(){
        if (selectedYear != 0 && !selectedCategory.isEmpty() && type1==1) {
            getCakeTypeByMonth(selectedYear, 1, selectedCategory);
        }else if(selectedYear != 0 && !selectedCategory.isEmpty() && type1==2){
            getCakeTypeByMonth(selectedYear, 2, selectedCategory);
        }else if(selectedYear != 0 && !selectedCategory.isEmpty() && type1==3){
            getCakeTypeByMonth(selectedYear, 3, selectedCategory);
        }
    }
    private void getTotalRevenueByMonth(int yearString, int status){
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference myRef= firebaseDatabase.getReference("order");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<Float, Float> revenueByMonth = new HashMap<>();
                for(DataSnapshot snap: snapshot.getChildren()){
                    Date orderDate=snap.child("orderDate").getValue(Date.class);
                    Calendar calendar=Calendar.getInstance();
                    calendar.setTime(orderDate);
                    int year=calendar.get(Calendar.YEAR);
                    String orderStatus=snap.child("orderStatus").getValue(String.class);
                    if(year==yearString && orderStatus.equals("done")){
                        float orderTotal=snap.child("total").getValue(float.class);
                        float tip=snap.child("tip").getValue(float.class);
                        float deliveryFee=snap.child("deliveryFee").getValue(float.class);
                        float month=calendar.get(Calendar.MONTH)+1;
                        revenueByMonth.put(month, revenueByMonth.getOrDefault(month, Float.valueOf(0)) + orderTotal+tip+deliveryFee);
                    }
                }
                for (int i = 1; i <= 12; i++) {
                    revenueByMonth.putIfAbsent(Float.valueOf(i), Float.valueOf(0));
                }
                TreeMap<Float, Float> sortedRevenueByMonth = new TreeMap<>(revenueByMonth);
                if(status==1){
                    barChart.clear();
                    barEntries.clear();
                    for (Float month : sortedRevenueByMonth.keySet()) {
                        Float value = sortedRevenueByMonth.get(month);
                        barEntries.add(new BarEntry(month-1, value));
                    }
                    BarDataSet dataSet = new BarDataSet(barEntries, "Revenue");
                    BarData barData = new BarData(dataSet);
                    barChart.getDescription().setEnabled(false);
                    barChart.setData(barData);

                    XAxis xAxis = barChart.getXAxis();
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(monthLabels1));
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Đặt vị trí của trục X ở dưới biểu đồ
                    xAxis.setGranularity(1f); // Đặt độ chia giữa các điểm trên trục X
                    xAxis.setLabelCount(monthLabels1.length); // Đặt số lượng nhãn trên trục X
                    xAxis.setDrawLabels(true); // Hiển thị nhãn trên trục X
                    xAxis.setDrawGridLines(false); // Ẩn các đường grid trên trục X

                    YAxis yAxis=barChart.getAxisRight();
                    yAxis.setEnabled(false);

                    barChart.invalidate();
                }else if(status==2){
                    lineChart.clear();
                    lineEntries.clear();
                    for (Float key : sortedRevenueByMonth.keySet()) {
                        Float value = sortedRevenueByMonth.get(key);
                        lineEntries.add(new BarEntry(key, value));
                    }
                    LineDataSet dataSet = new LineDataSet(lineEntries, "Revenue");
                    LineData lineData = new LineData(dataSet);
                    lineChart.setData(lineData);
                    lineChart.getDescription().setEnabled(false);

                    XAxis xAxis = lineChart.getXAxis();
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(monthLabels2));
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setGranularity(1f);
                    xAxis.setLabelCount(monthLabels2.length);
                    xAxis.setDrawLabels(true);
                    xAxis.setDrawGridLines(false);

                    YAxis yAxis=lineChart.getAxisRight();
                    yAxis.setEnabled(false);

                    lineChart.invalidate();
                }else if(status==3){
                    pieChart.clear();
                    pieEntries.clear();
                    float totalPie = 0f;
                    for (Float value : sortedRevenueByMonth.values()) {
                        totalPie += value;
                    }
                    for (Float key : sortedRevenueByMonth.keySet()) {
                        Float value = sortedRevenueByMonth.get(key);
                        float percentage = (value / totalPie) * 100;
                        int monthString=key.intValue();
                        pieEntries.add(new PieEntry(percentage, String.valueOf(monthString)));
                    }
                    PieDataSet dataSet = new PieDataSet(pieEntries, "Revenue");
                    dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                    dataSet.setValueTextSize(14f);
                    PieData pieData = new PieData(dataSet);
                    pieChart.getDescription().setEnabled(false);
                    pieChart.setData(pieData);
                    pieChart.setUsePercentValues(true);
                    pieChart.setCenterText("Revenue by Month");
                    pieChart.setCenterTextSize(12f);
                    pieChart.setCenterTextColor(Color.GRAY);
                    pieChart.setCenterTextTypeface(Typeface.DEFAULT_BOLD);
                    pieChart.invalidate();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getCakeTypeByMonth(int yearString,int status, String categoryname){
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference myRef= firebaseDatabase.getReference("order");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<Float, Float> cakeTypeByMonth = new HashMap<>();
                for(DataSnapshot snap: snapshot.getChildren()){
                    Date orderDate=snap.child("orderDate").getValue(Date.class);
                    Calendar calendar=Calendar.getInstance();
                    calendar.setTime(orderDate);
                    int year=calendar.get(Calendar.YEAR);
                    if(year==yearString){
                        float month=calendar.get(Calendar.MONTH)+1;
                        for(DataSnapshot productSnap:snap.child("productList").getChildren()){
                            if(productSnap.child("categoryName").getValue(String.class).equals(categoryname)){
                                int productQuantity=productSnap.child("quantity").getValue(int.class);
                                cakeTypeByMonth.put(month, cakeTypeByMonth.getOrDefault(month, Float.valueOf(0)) + productQuantity);
                            }
                        }
                    }
                }
                for (int i = 1; i <= 12; i++) {
                    cakeTypeByMonth.putIfAbsent(Float.valueOf(i), Float.valueOf(0));
                }
                TreeMap<Float, Float> sortedRevenueByMonth = new TreeMap<>(cakeTypeByMonth);
                if(status==1){
                    barChart1.clear();
                    barEntries1.clear();
                    for (Float key : sortedRevenueByMonth.keySet()) {
                        Float value = sortedRevenueByMonth.get(key);
                        barEntries1.add(new BarEntry(key, value));
                    }
                    BarDataSet dataSet = new BarDataSet(barEntries1, categoryname);
                    BarData barData = new BarData(dataSet);

                    dataSet.setColor(Color.GREEN);
                    barChart1.setData(barData);
                    barChart1.getDescription().setEnabled(false);

                    XAxis xAxis = barChart1.getXAxis();
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(monthLabels1));
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setGranularity(1f);
                    xAxis.setLabelCount(monthLabels1.length);
                    xAxis.setDrawLabels(true);
                    xAxis.setDrawGridLines(false);

                    YAxis yAxis=barChart1.getAxisRight();
                    yAxis.setEnabled(false);

                    barChart1.invalidate();
                }else if(status==2){
                    lineChart1.clear();
                    lineEntries1.clear();
                    for (Float key : sortedRevenueByMonth.keySet()) {
                        Float value = sortedRevenueByMonth.get(key);
                        lineEntries1.add(new BarEntry(key, value));
                    }
                    LineDataSet dataSet = new LineDataSet(lineEntries1, categoryname);
                    LineData lineData = new LineData(dataSet);
                    dataSet.setColor(Color.GREEN);

                    lineChart1.setData(lineData);
                    lineChart1.getDescription().setEnabled(false);

                    XAxis xAxis = lineChart1.getXAxis();
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(monthLabels2));
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setGranularity(1f);
                    xAxis.setLabelCount(monthLabels2.length);
                    xAxis.setDrawLabels(true);
                    xAxis.setDrawGridLines(false);

                    YAxis yAxis=lineChart1.getAxisRight();
                    yAxis.setEnabled(false);

                    lineChart1.invalidate();
                }else if(status==3){
                    pieChart1.clear();
                    pieEntries1.clear();
                    float totalPie = 0f;
                    for (Float value : sortedRevenueByMonth.values()) {
                        totalPie += value;
                    }
                    for (Float key : sortedRevenueByMonth.keySet()) {
                        Float value = sortedRevenueByMonth.get(key);
                        float percentage = (value / totalPie) * 100;
                        int monthString=key.intValue();
                        pieEntries1.add(new PieEntry(percentage, String.valueOf(monthString)));
                    }
                    PieDataSet dataSet = new PieDataSet(pieEntries1, categoryname);
                    dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                    dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                    dataSet.setValueTextSize(14f);

                    PieData pieData = new PieData(dataSet);
                    pieChart1.getDescription().setEnabled(false);
                    pieChart1.setData(pieData);
                    pieChart1.setUsePercentValues(true);
                    pieChart1.setCenterText("Cookies");
                    pieChart1.setCenterTextSize(12f);
                    pieChart1.setCenterTextColor(Color.GRAY);
                    pieChart1.setCenterTextTypeface(Typeface.DEFAULT_BOLD);
                    pieChart1.invalidate();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getCakeTypeCompare(int yearString, int status){
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference categoryRef=firebaseDatabase.getReference("category");
        DatabaseReference myRef= firebaseDatabase.getReference("order");
        List<String> categoryNameList=new ArrayList<>();
        categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap: snapshot.getChildren()){
                    String name=snap.child("name_category").getValue(String.class);
                    categoryNameList.add(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Float> cakeTypeCompare = new HashMap<>();
                for(DataSnapshot snap: snapshot.getChildren()){
                    Date orderDate=snap.child("orderDate").getValue(Date.class);
                    Calendar calendar=Calendar.getInstance();
                    calendar.setTime(orderDate);
                    int year=calendar.get(Calendar.YEAR);
                    List<String>a=categoryNameList;
                    if(year==yearString){
                        for(DataSnapshot productSnap:snap.child("productList").getChildren()){
                            String categoryName=productSnap.child("categoryName").getValue(String.class);
                            int productQuantity=productSnap.child("quantity").getValue(int.class);
                            cakeTypeCompare.put(categoryName, cakeTypeCompare.getOrDefault(categoryName, Float.valueOf(0)) + productQuantity);
                        }
                    }
                }
                HashMap<String, Float> newCakeTypeCompare = new HashMap<>();
                TreeMap<String, Float> sortedCakeCompare = new TreeMap<>(cakeTypeCompare);
                for(String name:categoryNameList){
                    boolean found=false;
                    for(String sortedname: sortedCakeCompare.keySet()){
                        if(name.equals(sortedname)){
                            found=true;
                            Float value=sortedCakeCompare.get(sortedname);
                            newCakeTypeCompare.put(sortedname,value);
                            break;
                        }
                    }
                    if(!found){
                        newCakeTypeCompare.put(name,Float.valueOf(0));
                    }
                }
                Collections.sort(categoryNameList);
                TreeMap<String, Float> sortedNewCakeCompare = new TreeMap<>(newCakeTypeCompare);
                HashMap<Float,Float> intCakeCompare=new HashMap<>();
                int count =0;
                for(String key:sortedNewCakeCompare.keySet()){
                    Float value=sortedNewCakeCompare.get(key);
                    intCakeCompare.put(Float.valueOf(count),value);
                    count++;
                }
                if(status==1){
                    barChart2.clear();
                    barEntries2.clear();
                    for (Float key : intCakeCompare.keySet()) {
                        Float value = intCakeCompare.get(key);
                        barEntries2.add(new BarEntry(key, value));
                    }
                    BarDataSet dataSet = new BarDataSet(barEntries2, "Category");
                    BarData barData = new BarData(dataSet);

                    dataSet.setColor(Color.RED);
                    barChart2.setData(barData);
                    barChart2.getDescription().setEnabled(false);

                    XAxis xAxis = barChart2.getXAxis();
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(categoryNameList));
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setGranularity(1f);
                    xAxis.setLabelCount(categoryNameList.size());
                    xAxis.setDrawLabels(true);
                    xAxis.setTextSize(6f);
                    xAxis.setDrawGridLines(false);

                    YAxis yAxis=barChart2.getAxisRight();
                    yAxis.setEnabled(false);

                    barChart2.invalidate();
                }else if(status==2){
                    lineChart2.clear();
                    lineEntries2.clear();
                    for (Float key : intCakeCompare.keySet()) {
                        Float value = intCakeCompare.get(key);
                        lineEntries2.add(new BarEntry(key, value));
                    }
                    LineDataSet dataSet = new LineDataSet(lineEntries2, "Category");
                    LineData lineData = new LineData(dataSet);
                    dataSet.setColor(Color.RED);

                    lineChart2.setData(lineData);
                    lineChart2.getDescription().setEnabled(false);

                    XAxis xAxis = lineChart2.getXAxis();
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(categoryNameList));
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setGranularity(1f);
                    xAxis.setLabelCount(categoryNameList.size());
                    xAxis.setDrawLabels(true);
                    xAxis.setTextSize(6f);
                    xAxis.setDrawGridLines(false);

                    YAxis yAxis=lineChart2.getAxisRight();
                    yAxis.setEnabled(false);

                    lineChart2.invalidate();
                }else if(status==3){
                    pieChart2.clear();
                    pieEntries2.clear();
                    float totalPie = 0f;
                    for (Float value : intCakeCompare.values()) {
                        totalPie += value;
                    }
                    for (Float key : intCakeCompare.keySet()) {
                        Float value = intCakeCompare.get(key);
                        float percentage = (value / totalPie) * 100;
                        int monthString=key.intValue();
                        pieEntries2.add(new PieEntry(percentage, String.valueOf(monthString)));
                    }
                    PieDataSet dataSet = new PieDataSet(pieEntries2, "Category");
                    dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                    dataSet.setValueTextSize(14f);

                    PieData pieData = new PieData(dataSet);
                    pieChart2.getDescription().setEnabled(false);
                    pieChart2.setData(pieData);
                    pieChart2.setUsePercentValues(true);
                    pieChart2.setCenterText("Category");
                    pieChart2.setCenterTextSize(12f);
                    pieChart2.setCenterTextColor(Color.GRAY);
                    pieChart2.setCenterTextTypeface(Typeface.DEFAULT_BOLD);
                    pieChart2.invalidate();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getCategoryName(GetListStringCallback getListStringCallback){
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference myRef=firebaseDatabase.getReference("category");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap:snapshot.getChildren()){
                    String name=snap.child("name_category").getValue(String.class);
                    categoryList.add(name);
                }
                getListStringCallback.getItem(categoryList);
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