package com.example.scarlet.Fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.scarlet.Data.Category;
import com.example.scarlet.Data.Product;
import com.example.scarlet.Data.User;
import com.example.scarlet.R;
import com.example.scarlet.SignUpActivity;
import com.github.mikephil.charting.charts.LineChart;
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
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

public class AdminHomeFragment extends Fragment {

    private FirebaseStorage storage;
    private StorageReference storageReference;
    LineChart lineChart;
    ArrayList<Entry> lineEntries;
    Uri uri;
    TextView categoryNumber1, categoryNumber2, categoryNumber3, categoryNumber4,
            categoryName1, categoryName2, categoryName3, categoryName4,
            categoryPercentage1, categoryPercentage2, categoryPercentage3, categoryPercentage4;
    private void BindView(View view){
        categoryNumber1=view.findViewById(R.id.categoryNumber1);
        categoryNumber2=view.findViewById(R.id.categoryNumber2);
        categoryNumber3=view.findViewById(R.id.categoryNumber3);
        categoryNumber4=view.findViewById(R.id.categoryNumber4);
        categoryName1=view.findViewById(R.id.categoryName1);
        categoryName2=view.findViewById(R.id.categoryName2);
        categoryName3=view.findViewById(R.id.categoryName3);
        categoryName4=view.findViewById(R.id.categoryName4);
        categoryPercentage1=view.findViewById(R.id.categoryPercentage1);
        categoryPercentage2=view.findViewById(R.id.categoryPercentage2);
        categoryPercentage3=view.findViewById(R.id.categoryPercentage3);
        categoryPercentage4=view.findViewById(R.id.categoryPercentage4);
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.admin_fragment_home, container, false);

        BindView(view);
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference("product");

//        FloatingActionButton fab = view.findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });
        getChartData(view, 2023);
        return view;
    }
    private void getChartData(View view,int yearString){
        lineChart=view.findViewById(R.id.lineChart);
        lineEntries=new ArrayList<>();
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
                float total=0f;
                for(String key:sortedNewCakeCompare.keySet()){
                    Float value=sortedNewCakeCompare.get(key);
                    intCakeCompare.put(Float.valueOf(count),value);
                    total=total+value;
                    count++;
                }
                for (Float key : intCakeCompare.keySet()) {
                    Float value = intCakeCompare.get(key);
                    lineEntries.add(new BarEntry(key, value));
                }
                LineDataSet dataSet = new LineDataSet(lineEntries, "Category");
                LineData lineData = new LineData(dataSet);
                dataSet.setColor(Color.RED);

                int[] colors = {Color.parseColor("#FF4081"), Color.parseColor("#00000000")};
                GradientDrawable gradient = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
                dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                dataSet.setDrawFilled(true);
                dataSet.setFillDrawable(gradient);

                lineChart.setData(lineData);
                lineChart.getDescription().setEnabled(false);

                XAxis xAxis = lineChart.getXAxis();
                xAxis.setValueFormatter(new IndexAxisValueFormatter(categoryNameList));
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setGranularity(1f);
                xAxis.setLabelCount(categoryNameList.size());
                xAxis.setDrawLabels(true);
                xAxis.setTextSize(6f);
                xAxis.setDrawGridLines(false);

                YAxis yAxis=lineChart.getAxisRight();
                yAxis.setEnabled(false);

                lineChart.invalidate();

                List<Map.Entry<String, Float>> list = new ArrayList<>(sortedNewCakeCompare.entrySet());
                list.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
                List<Map.Entry<String, Float>> highestEntries = list.subList(0, Math.min(4, list.size()));
                for (int i = 0; i < highestEntries.size(); i++) {
                    Map.Entry<String, Float> entry = highestEntries.get(i);
                    DecimalFormat df = new DecimalFormat("#.#");
                    float percent=entry.getValue()/total*100;
                    String formattedPercent = df.format(percent);
                    switch (i) {
                        case 0:
                            categoryNumber1.setText(String.valueOf(entry.getValue().intValue()));
                            categoryName1.setText(entry.getKey());
                            categoryPercentage1.setText(formattedPercent+"%");
                            break;
                        case 1:
                            categoryNumber2.setText(String.valueOf(entry.getValue().intValue()));
                            categoryName2.setText(entry.getKey());
                            categoryPercentage2.setText(formattedPercent+"%");
                            break;
                        case 2:
                            categoryNumber3.setText(String.valueOf(entry.getValue().intValue()));
                            categoryName3.setText(entry.getKey());
                            categoryPercentage3.setText(formattedPercent+"%");
                            break;
                        case 3:
                            categoryNumber4.setText(String.valueOf(entry.getValue().intValue()));
                            categoryName4.setText(entry.getKey());
                            categoryPercentage4.setText(formattedPercent+"%");
                            break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void createCategoryData(){
        uploadCategoryPicture(R.drawable.bubble_tea,"Drinks");
        uploadCategoryPicture(R.drawable.croissant,"Buns");
        uploadCategoryPicture(R.drawable.toast,"Toasts");
        uploadCategoryPicture(R.drawable.sandwich,"Sandwiches");
        uploadCategoryPicture(R.drawable.cinnamon_roll,"Dry cakes");
        uploadCategoryPicture(R.drawable.cake,"Cakes");
        uploadCategoryPicture(R.drawable.cake_slice,"Cake slices");
        uploadCategoryPicture(R.drawable.pudding,"Pudding");
        uploadCategoryPicture(R.drawable.cookie,"Cookies");
    }
    private void uploadCategoryPicture(int imageResourceId,String name) {
        final String randomKey= UUID.randomUUID().toString();
        StorageReference image=storageReference.child("images" +randomKey);
        uri= Uri.parse("android.resource://"+getContext().getPackageName()+"/"+ imageResourceId);
        image.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isComplete());
                        Uri urlImage=uriTask.getResult();

                        Category category=new Category(name,urlImage.toString());

                        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                        DatabaseReference categoryRef= firebaseDatabase.getReference("category");
                        categoryRef.push().setValue(category);
                        Toast.makeText(getContext(),"Upload successfully",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
    private void getCategoryCheck(){
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference myRef1=database.getReference("category");

        myRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot categorySnapshot:snapshot.getChildren()){
                    String categoryKey= categorySnapshot.getKey();
                    String categoryName=categorySnapshot.child("name_category").getValue(String.class);

                    if(categoryName.equals("Drinks")){
                        uploadProductPicture("Black coffee","",categoryKey,"Drinks",28000,100,R.drawable.dsc02313_optimized);
                        uploadProductPicture("Lychee mojito","",categoryKey,"Drinks",48000,100,R.drawable.dsc02977_optimized);
                        uploadProductPicture("Lychee tea","",categoryKey,"Drinks",45000,100,R.drawable.dsc03081_optimized);
                        uploadProductPicture("Peach tea","",categoryKey,"Drinks",48000,100,R.drawable.dsc03071_optimized);
                        uploadProductPicture("Strawberry mojito","",categoryKey,"Drinks",48000,100,R.drawable.dsc03013_optimized);
                        uploadProductPicture("Thai lemon tea","",categoryKey,"Drinks",28000,100,R.drawable.dsc03047_optimized);
                        uploadProductPicture("Thai milk tea","",categoryKey,"Drinks",30000,100,R.drawable.dsc02860_optimized);
                        uploadProductPicture("Strawberry tea","",categoryKey,"Drinks",45000,100,R.drawable.dsc05164_optimized);
                        uploadProductPicture("White coffee","",categoryKey,"Drinks",30000,100,R.drawable.dsc02847_optimized);
                    }else if(categoryName.equals("Buns")){
                        uploadProductPicture("Bacon cheese onion","",categoryKey,"Buns",32000,100,R.drawable.bacon_cheese_onion);
                        uploadProductPicture("Banana chocolate","",categoryKey,"Buns",24000,100,R.drawable.banana_chocolate);
                        uploadProductPicture("Banana peanut","",categoryKey,"Buns",24000,100,R.drawable.banana_peanut);
                        uploadProductPicture("Big eye","",categoryKey,"Buns",34000,100,R.drawable.big_eye);
                        uploadProductPicture("Blueberry custard","",categoryKey,"Buns",20000,100,R.drawable.blueberry_custard);
                        uploadProductPicture("Brown Sugar Injeolmi Korissant","",categoryKey,"Buns",29000,100,R.drawable.brown_sugar_injeolmi_korissant);
                        uploadProductPicture("Cheese Boat","",categoryKey,"Buns",22000,100,R.drawable.cheese_boat);
                        uploadProductPicture("Cheese Croissant","",categoryKey,"Buns",22000,100,R.drawable.cheese_croissant);
                        uploadProductPicture("Cheese Floss","",categoryKey,"Buns",27000,100,R.drawable.cheese_floss);
                        uploadProductPicture("Cheese Lava Croissant","",categoryKey,"Buns",18000,100,R.drawable.cheese_lava_croissant);
                        uploadProductPicture("Cheese Lava Croissant 3pcs","",categoryKey,"Buns",46000,100,R.drawable.cheese_lava_croissant_3pcs);
                        uploadProductPicture("Cheese Lava Croissant 5pcs","",categoryKey,"Buns",72000,100,R.drawable.cheese_lava_croissant_5pcs);
                        uploadProductPicture("Cheese Sausage","",categoryKey,"Buns",30000,100,R.drawable.dsc03210);
                        uploadProductPicture("Chicken Parmesan","",categoryKey,"Buns",34000,100,R.drawable.dsc03298_optimized);
                        uploadProductPicture("Chocolate Donut","",categoryKey,"Buns",28000,100,R.drawable.dsc03270_optimized);
                        uploadProductPicture("Cinnamon Korissant","",categoryKey,"Buns",22000,100,R.drawable.cinnamon_kroissant);

                        uploadProductPicture("Cocktail Bun 3pcs","",categoryKey,"Buns",27000,100,R.drawable.dsc02677_optimized);
                        uploadProductPicture("Cocoa Teddy","",categoryKey,"Buns",22000,100,R.drawable.dsc03556_optimized);
                        uploadProductPicture("Cranberry Cream Cheese","",categoryKey,"Buns",24000,100,R.drawable.dsc03558_optimized);
                        uploadProductPicture("Cream Chez Garlic Bread","",categoryKey,"Buns",36000,100,R.drawable.dsc03254_optimized);
                        uploadProductPicture("Donut Balls","",categoryKey,"Buns",39000,100,R.drawable.dsc02692_optimized);
                        uploadProductPicture("Double Cheese","",categoryKey,"Buns",24000,100,R.drawable.dsc03509_optimized);
                        uploadProductPicture("Durian Egg Tart","",categoryKey,"Buns",20000,100,R.drawable.dsc03025_optimized);
                        uploadProductPicture("Egg Ppang","",categoryKey,"Buns",39000,100,R.drawable.eggppang);
                        uploadProductPicture("Fire Floss","",categoryKey,"Buns",34000,100,R.drawable.dsc03319_optimized);
                        uploadProductPicture("Floss","",categoryKey,"Buns",34000,100,R.drawable.dsc03349_optimized);
                        uploadProductPicture("Floss 4ever","",categoryKey,"Buns",32000,100,R.drawable.flos_4ver);
                        uploadProductPicture("Get Cheese","",categoryKey,"Buns",22000,100,R.drawable.dsc03181_optimized);
                        uploadProductPicture("Golden Lava Bun","",categoryKey,"Buns",22000,100,R.drawable.dsc03528_optimized);
                        uploadProductPicture("Golden Lava Croissant","",categoryKey,"Buns",18000,100,R.drawable.dsc03399_optimized);
                        uploadProductPicture("Golden Lava Croissant 3pcs","",categoryKey,"Buns",46000,100,R.drawable.dsc03460_optimized);
                        uploadProductPicture("Golden Lava Croissant 5pcs","",categoryKey,"Buns",72000,100,R.drawable.dsc03454_optimized);

                        uploadProductPicture("Golden Nacho Cheese","",categoryKey,"Buns",45000,100,R.drawable.golden_nacho);
                        uploadProductPicture("Green Garlic","",categoryKey,"Buns",22000,100,R.drawable.green_garlic);
                        uploadProductPicture("Ham Cheese","",categoryKey,"Buns",29000,100,R.drawable.dsc03133_optimized);
                        uploadProductPicture("Ham Cheese","",categoryKey,"Buns",29000,100,R.drawable.ham_cheese);
                        uploadProductPicture("Ham Roll","",categoryKey,"Buns",22000,100,R.drawable.ham_roll);
                        uploadProductPicture("Kimcheese Korissant","",categoryKey,"Buns",38000,100,R.drawable.kimcheese_croissant);
                        uploadProductPicture("Mushi Mushroom Chicken Dozo","",categoryKey,"Buns",29000,100,R.drawable.dsc02935_optimized);
                        uploadProductPicture("Mushroom Cheese Sausage","",categoryKey,"Buns",34000,100,R.drawable.mushroom_cheese_sausage);
                        uploadProductPicture("Phoenix","",categoryKey,"Buns",24000,100,R.drawable.phoenix);
                        uploadProductPicture("Pillow Raisin","",categoryKey,"Buns",24000,100,R.drawable.dsc03241_optimized);
                        uploadProductPicture("Pork Ribs","",categoryKey,"Buns",28000,100,R.drawable.dsc03584_optimized);
                        uploadProductPicture("Rainbow Donut","",categoryKey,"Buns",28000,100,R.drawable.dsc03292_optimized);
                        uploadProductPicture("Raisin Cream Cheese","",categoryKey,"Buns",24000,100,R.drawable.dsc03558_optimized);
                        uploadProductPicture("Sausage Standard","",categoryKey,"Buns",24000,100,R.drawable.dsc03199_optimized);
                        uploadProductPicture("Set Tart Cheese Mini 4pcs","",categoryKey,"Buns",95000,100,R.drawable.dsc03507_optimized);
                        uploadProductPicture("Smart Aleck","",categoryKey,"Buns",25000,100,R.drawable.dsc03188_optimized);

                        uploadProductPicture("Spring In The City","",categoryKey,"Buns",39000,100,R.drawable.dsc03534_optimized);
                        uploadProductPicture("Sprouted Rye","",categoryKey,"Buns",28000,100,R.drawable.dsc02959_optimized);
                        uploadProductPicture("Sprouted Rye","",categoryKey,"Buns",28000,100,R.drawable.dsc02997_optimized);
                        uploadProductPicture("Sprouted Rye","",categoryKey,"Buns",28000,100,R.drawable.dsc02991_optimized);
                        uploadProductPicture("Sprouted Rye 3pcs","",categoryKey,"Buns",69000,100,R.drawable.dsc03006_optimized);
                        uploadProductPicture("Sugar Donut","",categoryKey,"Buns",28000,100,R.drawable.dsc03593_optimized);
                        uploadProductPicture("T-Cures of Golden Flowers","",categoryKey,"Buns",36000,100,R.drawable.dsc03347_optimized);
                        uploadProductPicture("Tart Golden Cheese","",categoryKey,"Buns",26000,100,R.drawable.dsc03485_optimized);
                        uploadProductPicture("Tart Orig Cheese","",categoryKey,"Buns",26000,100,R.drawable.dsc03490_optimized);
                        uploadProductPicture("Tuna Crossant","",categoryKey,"Buns",30000,100,R.drawable.dsc04823_optimized);
                        uploadProductPicture("Ya Ya Egg Tart","",categoryKey,"Buns",30000,100,R.drawable.dsc02790_optimized);
                    }else if(categoryName.equals("Toasts")){
                        uploadProductPicture("Almond Coffee Milky Way","",categoryKey,"Toasts",108000,100,R.drawable.dsc04839_optimized);
                        uploadProductPicture("Brioche","",categoryKey,"Toasts",90000,100,R.drawable.dsc02364_optimized);
                        uploadProductPicture("California Toast","",categoryKey,"Toasts",49000,100,R.drawable.dsc02270_optimized);
                        uploadProductPicture("Cranberry Cr Cheese 4pcs","",categoryKey,"Toasts",45000,100,R.drawable.dsc02369_optimized);
                        uploadProductPicture("Cranberry Toast","",categoryKey,"Toasts",55000,100,R.drawable.dsc02351_optimized);
                        uploadProductPicture("Dark Rye Toast","",categoryKey,"Toasts",49000,100,R.drawable.dsc02286_optimized);
                        uploadProductPicture("Earthquake Toast","",categoryKey,"Toasts",49000,100,R.drawable.dsc02331_optimized);
                        uploadProductPicture("Fresh Baguette","",categoryKey,"Toasts",45000,100,R.drawable.dsc02384_optimized);
                        uploadProductPicture("Gourment Fruit Loaf","",categoryKey,"Toasts",105000,100,R.drawable.dsc02263_optimized);
                        uploadProductPicture("Mount Greentea Toast","",categoryKey,"Toasts",55000,100,R.drawable.dsc02361_optimized);
                        uploadProductPicture("Premium Toast","",categoryKey,"Toasts",55000,100,R.drawable.dsc02313_optimized_1);
                        uploadProductPicture("Pumpkin Toast","",categoryKey,"Toasts",40000,100,R.drawable.dsc02342_optimized);
                        uploadProductPicture("Standard Toast","",categoryKey,"Toasts",47000,100,R.drawable.dsc02294_optimized);
                        uploadProductPicture("Wholemeal Toast","",categoryKey,"Toasts",40000,100,R.drawable.dsc02307_optimized);
                    }else if(categoryName.equals("Sandwiches")){
                        uploadProductPicture("Bacon&Egg Breakfast SW","",categoryKey,"Sandwiches",45000,100,R.drawable.dsc02900_optimized);
                        uploadProductPicture("Baked Ham&Cheese SW","",categoryKey,"Sandwiches",45000,100,R.drawable.dsc02915_optimized);
                        uploadProductPicture("Chicken SW","",categoryKey,"Sandwiches",45000,100,R.drawable.dsc03094_optimized);
                        uploadProductPicture("Ham&Egg Breakfast SW","",categoryKey,"Sandwiches",45000,100,R.drawable.dsc02869_optimized);
                        uploadProductPicture("Tuna SW","",categoryKey,"Sandwiches",45000,100,R.drawable.dsc03110_optimized);
                    }else if(categoryName.equals("Dry cakes")){
                        uploadProductPicture("Banana Cake","",categoryKey,"Dry cakes",108000,100,R.drawable.dsc02662_optimized);
                        uploadProductPicture("Combo Drycake 3pcs","",categoryKey,"Dry cakes",80000,100,R.drawable.dsc02769_optimized);
                        uploadProductPicture("Combo Drycake 5pcs","",categoryKey,"Dry cakes",130000,100,R.drawable.dsc02774_optimized);
                        uploadProductPicture("Crater Cheese Honey","",categoryKey,"Dry cakes",55000,100,R.drawable.dsc02694_optimized);
                        uploadProductPicture("HOKKAIDO ROLL","",categoryKey,"Dry cakes",169000,100,R.drawable.hokkaido_roll_sp_roll);
                        uploadProductPicture("HOKKAIDO SLICED","",categoryKey,"Dry cakes",69000,100,R.drawable.hokkaido_slice_sp);
                        uploadProductPicture("Japan Light Cheese","",categoryKey,"Dry cakes",129000,100,R.drawable.dsc02649_optimized);
                        uploadProductPicture("PANDAN CHIFFON","",categoryKey,"Dry cakes",115000,100,R.drawable.ca_2_cake);
                        uploadProductPicture("Parmesan Cheese","",categoryKey,"Dry cakes",56000,100,R.drawable.dsc02412_optimized);
                        uploadProductPicture("SR Chocolate Sliced","",categoryKey,"Dry cakes",30000,100,R.drawable.dsc02720_optimized);
                        uploadProductPicture("SR Coffee Sliced","",categoryKey,"Dry cakes",30000,100,R.drawable.dsc05157_optimized);
                        uploadProductPicture("SR Greentea Sliced","",categoryKey,"Dry cakes",30000,100,R.drawable.dsc02752_optimized);
                        uploadProductPicture("SR Raisin Sliced","",categoryKey,"Dry cakes",30000,100,R.drawable.dsc02721_optimized);
                        uploadProductPicture("SR Tiger Skin Sliced","",categoryKey,"Dry cakes",30000,100,R.drawable.dsc02705_optimized);
                    }else if(categoryName.equals("Cakes")){
                        uploadProductPicture("Blueberry cake","",categoryKey,"Cakes",450000,100,R.drawable.dsc02564_optimized);
                        uploadProductPicture("Mocha cake","",categoryKey,"Cakes",450000,100,R.drawable.dsc02514_optimized);
                        uploadProductPicture("Mocha cake","",categoryKey,"Cakes",450000,100,R.drawable.dsc02512_optimized);
                        uploadProductPicture("Mocha cake","",categoryKey,"Cakes",450000,100,R.drawable.dsc02540_optimized);
                        uploadProductPicture("Black Forest C","",categoryKey,"Cakes",490000,100,R.drawable.dsc02466_optimized);
                        uploadProductPicture("Black Forest R","",categoryKey,"Cakes",360000,100,R.drawable.dsc02472_optimized);
                        uploadProductPicture("Fresh Cream","",categoryKey,"Cakes",450000,100,R.drawable.dsc02547_optimized);
                        uploadProductPicture("Fresh Cream (SN00 H10)","",categoryKey,"Cakes",380000,100,R.drawable.dsc02588_optimized);
                        uploadProductPicture("Fresh Cream (SN01)","",categoryKey,"Cakes",390000,100,R.drawable.dsc02574_optimized);
                        uploadProductPicture("Hai! Cheese R","",categoryKey,"Cakes",360000,100,R.drawable.dsc02256_optimized);
                        uploadProductPicture("Les Opera C","",categoryKey,"Cakes",550000,100,R.drawable.dsc05141_optimized);
                        uploadProductPicture("Les Opera R","",categoryKey,"Cakes",390000,100,R.drawable.dsc05138_optimized);
                        uploadProductPicture("Macha Macha","",categoryKey,"Cakes",490000,100,R.drawable.dsc05150_optimized);
                        uploadProductPicture("MangoCoCo Cake","",categoryKey,"Cakes",420000,100,R.drawable.dsc02430_optimized);
                        uploadProductPicture("Mocha Cheese","",categoryKey,"Cakes",490000,100,R.drawable.dsc02426_optimized);
                        uploadProductPicture("Mocha Choco","",categoryKey,"Cakes",490000,100,R.drawable.dsc02354_optimized);
                        uploadProductPicture("Party Pink","",categoryKey,"Cakes",550000,100,R.drawable.dsc02474_optimized);
                        uploadProductPicture("Passion Cheese C","",categoryKey,"Cakes",490000,100,R.drawable.dsc02494_optimized);
                        uploadProductPicture("Passion Cheese R","",categoryKey,"Cakes",360000,100,R.drawable.dsc02641_optimized);
                        uploadProductPicture("Rainbow Bliss C","",categoryKey,"Cakes",550000,100,R.drawable.dsc02437_optimized);
                        uploadProductPicture("Rainbow Bliss R","",categoryKey,"Cakes",390000,100,R.drawable.dsc02446_optimized);
                        uploadProductPicture("Snowy Fruity (C10H10)","",categoryKey,"Cakes",295000,100,R.drawable.dsc02604_optimized);
                        uploadProductPicture("Tiramisu C","",categoryKey,"Cakes",550000,100,R.drawable.dsc02452_optimized);
                        uploadProductPicture("Tiramisu R","",categoryKey,"Cakes",390000,100,R.drawable.dsc02621_optimized);
                        uploadProductPicture("Vanila Corn Cake (SN01)","",categoryKey,"Cakes",395000,100,R.drawable.dsc02344_optimized);
                    }else if(categoryName.equals("Cake slices")){
                        uploadProductPicture("Beary Berry","",categoryKey,"Cake slices",45000,100,R.drawable.dsc02299_optimized);
                        uploadProductPicture("Beary Forest","",categoryKey,"Cake slices",45000,100,R.drawable.dsc02303_optimized);
                        uploadProductPicture("Beary Matcha","",categoryKey,"Cake slices",45000,100,R.drawable.dsc02295_optimized);
                        uploadProductPicture("Beary Vanilla","",categoryKey,"Cake slices",45000,100,R.drawable.dsc02284_optimized);
                        uploadProductPicture("Chocolate Peanut Mousse","",categoryKey,"Cake slices",59000,100,R.drawable.dsc02389_optimized);
                        uploadProductPicture("Hai! Cheese Sliced","",categoryKey,"Cake slices",59000,100,R.drawable.haicheesecup);
                        uploadProductPicture("Les Opera Sliced","",categoryKey,"Cake slices",58000,100,R.drawable.dsc02343_optimized);
                        uploadProductPicture("Oreo Chocolate Cheese","",categoryKey,"Cake slices",59000,100,R.drawable.dsc02388_optimized);
                        uploadProductPicture("Passion Cheese Sliced","",categoryKey,"Cake slices",59000,100,R.drawable.passion_cheese_cup);
                        uploadProductPicture("Rainbow Bliss Sliced","",categoryKey,"Cake slices",48000,100,R.drawable.dsc02333_optimized);
                        uploadProductPicture("Strawberry Yoghurt Mousse","",categoryKey,"Cake slices",59000,100,R.drawable.dsc02397_optimized);
                        uploadProductPicture("Tiramisu Cup","",categoryKey,"Cake slices",59000,100,R.drawable.dsc02401_optimized);
                    }else if(categoryName.equals("Pudding")){
                        uploadProductPicture("Lychee Coconut Pudding","",categoryKey,"Pudding",48000,100,R.drawable.lychee_coconut_pudding);
                        uploadProductPicture("PEACH PUDDING","",categoryKey,"Pudding",48000,100,R.drawable.peach_pudding);
                        uploadProductPicture("STRAWBERRY PUDDING","",categoryKey,"Pudding",48000,100,R.drawable.strawberry_pudding);
                        uploadProductPicture("Tiramisu Pudding","",categoryKey,"Pudding",48000,100,R.drawable.tiramisu_pudding);
                    }else if(categoryName.equals("Cookies")){
                        uploadProductPicture("Almond Cookies","",categoryKey,"Cookies",129000,100,R.drawable.dsc03146_optimized);
                        uploadProductPicture("Chocolate Cookies","",categoryKey,"Cookies",129000,100,R.drawable.dsc03142_optimized);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void uploadProductPicture(String name,String description,String categoryKey,String categoryName,double price,int point,int imageResourceId) {
        final String randomKey= UUID.randomUUID().toString();
        StorageReference image=storageReference.child("images" +randomKey);
        uri= Uri.parse("android.resource://"+getContext().getPackageName()+"/"+ imageResourceId);
        image.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isComplete());
                        Uri urlImage=uriTask.getResult();

                        Product product=new Product(name,description,categoryKey,categoryName,price,point,urlImage.toString());

                        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                        DatabaseReference categoryRef= firebaseDatabase.getReference("product");
                        categoryRef.push().setValue(product);
                        Toast.makeText(getContext(),"Upload successfully",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
    private void createAccount(){
        uploadAccount("testing1@gmail.com","123456","VVVV","AAAA","Male","10/02/2003","0918345660",true,false);
        uploadAccount("testing2@gmail.com","123456","VVVV","AAAA","Female","12/02/2003","0918345661",true,false);
        uploadAccount("testing3@gmail.com","123456","VVVV","AAAA","Male","10/04/2003","0918345662",true,false);
        uploadAccount("testing4@gmail.com","123456","VVVV","AAAA","Female","14/05/2003","0918345663",true,false);
        uploadAccount("testing5@gmail.com","123456","VVVV","AAAA","Male","1/02/2003","0918345664",true,false);
        uploadAccount("testing6@gmail.com","123456","VVVV","AAAA","Female","10/08/2003","0918345665",true,false);
        uploadAccount("testing7@gmail.com","123456","VVVV","AAAA","Male","10/02/2003","0918345666",true,false);
        uploadAccount("testing8@gmail.com","123456","VVVV","AAAA","Female","10/02/2003","0918345667",true,false);
        uploadAccount("testing9@gmail.com","123456","VVVV","AAAA","Male","10/02/2003","0918345668",true,false);
        uploadAccount("testing10@gmail.com","123456","VVVV","AAAA","Female","10/02/2003","0918345669",true,false);
        uploadAccount("testing11@gmail.com","123456","VVVV","AAAA","Male","10/02/2003","0918345670",true,false);
        uploadAccount("testing12@gmail.com","123456","VVVV","AAAA","Female","10/02/2003","0918345671",true,false);
        uploadAccount("testing13@gmail.com","123456","VVVV","AAAA","Male","10/02/2003","0918345672",true,false);
        uploadAccount("testing14@gmail.com","123456","VVVV","AAAA","Female","10/02/2003","0918345673",true,false);
        uploadAccount("testing15@gmail.com","123456","VVVV","AAAA","Male","10/02/2003","0918345674",true,false);
        uploadAccount("testing16@gmail.com","123456","VVVV","AAAA","Female","10/02/2003","0918345675",true,false);
        uploadAccount("testing17@gmail.com","123456","VVVV","AAAA","Male","10/02/2003","0918345676",true,false);
        uploadAccount("testing18@gmail.com","123456","VVVV","AAAA","Female","10/02/2003","0918345677",true,false);
        uploadAccount("testing19@gmail.com","123456","VVVV","AAAA","Male","10/02/2003","0918345678",true,false);
        uploadAccount("testing20@gmail.com","123456","VVVV","AAAA","Female","10/02/2003","0918345679",true,false);

        uploadAccount("admin1@gmail.com","123456","Binh","Thanh","Male","10/02/2003","0918345677",false,true);
        uploadAccount("admin2@gmail.com","123456","Ngan","Thao","Female","10/02/2003","0918345678",false,true);
        uploadAccount("admin3@gmail.com","123456","Thanh","Quang","Male","10/02/2003","0918345679",false,true);
    }
    private void uploadAccount(String email, String password, String firstname, String lastname,String gender,String dateofbirth, String phone, boolean isCustomer, boolean isEmployee){
        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isComplete()){
                            FirebaseUser user=mAuth.getCurrentUser();
                            String uid=user.getUid();

                            DatabaseReference userRef=FirebaseDatabase.getInstance().getReference("user");
                            DatabaseReference newUserRef=userRef.child(uid);

                            User userAdd=new User(uid,firstname,lastname,gender,dateofbirth,phone,email,0,"-1","",isCustomer,isEmployee);
                            userRef.push().setValue(userAdd);
                        }else{

                        }
                    }
                });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}