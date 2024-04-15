package com.example.scarlet.Data;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.example.scarlet.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateDataDefault {
    public void createCategoryData(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("category");

        List<Category> categoryList=new ArrayList<>();
//        myRef.push().setValue(new Category("Drinks", R.drawable.bubble_tea));
//        myRef.push().setValue(new Category("Buns",R.drawable.croissant));
//        myRef.push().setValue(new Category("Toasts",R.drawable.toast));
//        myRef.push().setValue(new Category("Sandwiches",R.drawable.sandwich));
//        myRef.push().setValue(new Category("Dry cakes",R.drawable.cinnamon_roll));
//        myRef.push().setValue(new Category("Cakes",R.drawable.cake));
//        myRef.push().setValue(new Category("Cake slices",R.drawable.cake_slice));
//        myRef.push().setValue(new Category("Pudding",R.drawable.pudding));
//        myRef.push().setValue(new Category("Cookies",R.drawable.cookie));

    }

    public void createProductData(){
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference myRef=database.getReference("product");
        DatabaseReference myRef1=database.getReference("category");
        List<Product> productList=new ArrayList<>();

        myRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot categorySnapshot:snapshot.getChildren()){
                    String categoryKey= categorySnapshot.getKey();
                    String categoryName=categorySnapshot.child("name_category").getValue(String.class);

//                    if(categoryName.equals("Drinks")){
//                        //Drinks data
//                        myRef.push().setValue(new Product("Black coffee","",categoryKey,"Drinks",28000,100,R.drawable.dsc02313_optimized));
//                        myRef.push().setValue(new Product("Lychee mojito","",categoryKey,"Drinks",48000,100,R.drawable.dsc02977_optimized));
//                        myRef.push().setValue(new Product("Lychee tea","",categoryKey,"Drinks",45000,100,R.drawable.dsc03081_optimized));
//                        myRef.push().setValue(new Product("Peach tea","",categoryKey,"Drinks",48000,100,R.drawable.dsc03071_optimized));
//                        myRef.push().setValue(new Product("Strawberry mojito","",categoryKey,"Drinks",48000,100,R.drawable.dsc03013_optimized));
//                        myRef.push().setValue(new Product("Thai lemon tea","",categoryKey,"Drinks",28000,100,R.drawable.dsc03047_optimized));
//                        myRef.push().setValue(new Product("Thai milk tea","",categoryKey,"Drinks",30000,100,R.drawable.dsc02860_optimized));
//                        myRef.push().setValue(new Product("Strawberry tea","",categoryKey,"Drinks",45000,100,R.drawable.dsc05164_optimized));
//                        myRef.push().setValue(new Product("White coffee","",categoryKey,"Drinks",30000,100,R.drawable.dsc02847_optimized));
//                    }else if(categoryName.equals("Buns")){
//                        //Buns
//                        myRef.push().setValue(new Product("Bacon cheese onion","",categoryKey,"Buns",32000,100,R.drawable.bacon_cheese_onion));
//                        myRef.push().setValue(new Product("Banana chocolate","",categoryKey,"Buns",24000,100,R.drawable.banana_chocolate));
//                        myRef.push().setValue(new Product("Banana peanut","",categoryKey,"Buns",24000,100,R.drawable.banana_peanut));
//                        myRef.push().setValue(new Product("Big eye","",categoryKey,"Buns",34000,100,R.drawable.big_eye));
//                        myRef.push().setValue(new Product("Blueberry custard","",categoryKey,"Buns",20000,100,R.drawable.blueberry_custard));
//                        myRef.push().setValue(new Product("Brown Sugar Injeolmi Korissant","",categoryKey,"Buns",29000,100,R.drawable.brown_sugar_injeolmi_korissant));
//                        myRef.push().setValue(new Product("Cheese Boat","",categoryKey,"Buns",22000,100,R.drawable.cheese_boat));
//                        myRef.push().setValue(new Product("Cheese Croissant","",categoryKey,"Buns",22000,100,R.drawable.cheese_croissant));
//                        myRef.push().setValue(new Product("Cheese Floss","",categoryKey,"Buns",27000,100,R.drawable.cheese_floss));
//                        myRef.push().setValue(new Product("Cheese Lava Croissant","",categoryKey,"Buns",18000,100,R.drawable.cheese_lava_croissant));
//                        myRef.push().setValue(new Product("Cheese Lava Croissant 3pcs","",categoryKey,"Buns",46000,100,R.drawable.cheese_lava_croissant_3pcs));
//                        myRef.push().setValue(new Product("Cheese Lava Croissant 5pcs","",categoryKey,"Buns",72000,100,R.drawable.cheese_lava_croissant_5pcs));
//                        myRef.push().setValue(new Product("Cheese Sausage","",categoryKey,"Buns",30000,100,R.drawable.dsc03210));
//                        myRef.push().setValue(new Product("Chicken Parmesan","",categoryKey,"Buns",34000,100,R.drawable.dsc03298_optimized));
//                        myRef.push().setValue(new Product("Chocolate Donut","",categoryKey,"Buns",28000,100,R.drawable.dsc03270_optimized));
//                        myRef.push().setValue(new Product("Cinnamon Korissant","",categoryKey,"Buns",22000,100,R.drawable.cinnamon_kroissant));
//
//                        myRef.push().setValue(new Product("Cocktail Bun 3pcs","",categoryKey,"Buns",27000,100,R.drawable.dsc02677_optimized));
//                        myRef.push().setValue(new Product("Cocoa Teddy","",categoryKey,"Buns",22000,100,R.drawable.dsc03556_optimized));
//                        myRef.push().setValue(new Product("Cranberry Cream Cheese","",categoryKey,"Buns",24000,100,R.drawable.dsc03558_optimized));
//                        myRef.push().setValue(new Product("Cream Chez Garlic Bread","",categoryKey,"Buns",36000,100,R.drawable.dsc03254_optimized));
//                        myRef.push().setValue(new Product("Donut Balls","",categoryKey,"Buns",39000,100,R.drawable.dsc02692_optimized));
//                        myRef.push().setValue(new Product("Double Cheese","",categoryKey,"Buns",24000,100,R.drawable.dsc03509_optimized));
//                        myRef.push().setValue(new Product("Durian Egg Tart","",categoryKey,"Buns",20000,100,R.drawable.dsc03025_optimized));
//                        myRef.push().setValue(new Product("Egg Ppang","",categoryKey,"Buns",39000,100,R.drawable.eggppang));
//                        myRef.push().setValue(new Product("Fire Floss","",categoryKey,"Buns",34000,100,R.drawable.dsc03319_optimized));
//                        myRef.push().setValue(new Product("Floss","",categoryKey,"Buns",34000,100,R.drawable.dsc03349_optimized));
//                        myRef.push().setValue(new Product("Floss 4ever","",categoryKey,"Buns",32000,100,R.drawable.flos_4ver));
//                        myRef.push().setValue(new Product("Get Cheese","",categoryKey,"Buns",22000,100,R.drawable.dsc03181_optimized));
//                        myRef.push().setValue(new Product("Golden Lava Bun","",categoryKey,"Buns",22000,100,R.drawable.dsc03528_optimized));
//                        myRef.push().setValue(new Product("Golden Lava Croissant","",categoryKey,"Buns",18000,100,R.drawable.dsc03399_optimized));
//                        myRef.push().setValue(new Product("Golden Lava Croissant 3pcs","",categoryKey,"Buns",46000,100,R.drawable.dsc03460_optimized));
//                        myRef.push().setValue(new Product("Golden Lava Croissant 5pcs","",categoryKey,"Buns",72000,100,R.drawable.dsc03454_optimized));
//
//                        myRef.push().setValue(new Product("Golden Nacho Cheese","",categoryKey,"Buns",45000,100,R.drawable.golden_nacho));
//                        myRef.push().setValue(new Product("Green Garlic","",categoryKey,"Buns",22000,100,R.drawable.green_garlic));
//                        myRef.push().setValue(new Product("Ham Cheese","",categoryKey,"Buns",29000,100,R.drawable.dsc03133_optimized));
//                        myRef.push().setValue(new Product("Ham Cheese","",categoryKey,"Buns",29000,100,R.drawable.ham_cheese));
//                        myRef.push().setValue(new Product("Ham Roll","",categoryKey,"Buns",22000,100,R.drawable.ham_roll));
//                        myRef.push().setValue(new Product("Kimcheese Korissant","",categoryKey,"Buns",38000,100,R.drawable.kimcheese_croissant));
//                        myRef.push().setValue(new Product("Mushi Mushroom Chicken Dozo","",categoryKey,"Buns",29000,100,R.drawable.dsc02935_optimized));
//                        myRef.push().setValue(new Product("Mushroom Cheese Sausage","",categoryKey,"Buns",34000,100,R.drawable.mushroom_cheese_sausage));
//                        myRef.push().setValue(new Product("Phoenix","",categoryKey,"Buns",24000,100,R.drawable.phoenix));
//                        myRef.push().setValue(new Product("Pillow Raisin","",categoryKey,"Buns",24000,100,R.drawable.dsc03241_optimized));
//                        myRef.push().setValue(new Product("Pork Ribs","",categoryKey,"Buns",28000,100,R.drawable.dsc03584_optimized));
//                        myRef.push().setValue(new Product("Rainbow Donut","",categoryKey,"Buns",28000,100,R.drawable.dsc03292_optimized));
//                        myRef.push().setValue(new Product("Raisin Cream Cheese","",categoryKey,"Buns",24000,100,R.drawable.dsc03558_optimized));
//                        myRef.push().setValue(new Product("Sausage Standard","",categoryKey,"Buns",24000,100,R.drawable.dsc03199_optimized));
//                        myRef.push().setValue(new Product("Set Tart Cheese Mini 4pcs","",categoryKey,"Buns",95000,100,R.drawable.dsc03507_optimized));
//                        myRef.push().setValue(new Product("Smart Aleck","",categoryKey,"Buns",25000,100,R.drawable.dsc03188_optimized));
//
//                        myRef.push().setValue(new Product("Spring In The City","",categoryKey,"Buns",39000,100,R.drawable.dsc03534_optimized));
//                        myRef.push().setValue(new Product("Sprouted Rye","",categoryKey,"Buns",28000,100,R.drawable.dsc02959_optimized));
//                        myRef.push().setValue(new Product("Sprouted Rye","",categoryKey,"Buns",28000,100,R.drawable.dsc02997_optimized));
//                        myRef.push().setValue(new Product("Sprouted Rye","",categoryKey,"Buns",28000,100,R.drawable.dsc02991_optimized));
//                        myRef.push().setValue(new Product("Sprouted Rye 3pcs","",categoryKey,"Buns",69000,100,R.drawable.dsc03006_optimized));
//                        myRef.push().setValue(new Product("Sugar Donut","",categoryKey,"Buns",28000,100,R.drawable.dsc03593_optimized));
//                        myRef.push().setValue(new Product("T-Cures of Golden Flowers","",categoryKey,"Buns",36000,100,R.drawable.dsc03347_optimized));
//                        myRef.push().setValue(new Product("Tart Golden Cheese","",categoryKey,"Buns",26000,100,R.drawable.dsc03485_optimized));
//                        myRef.push().setValue(new Product("Tart Orig Cheese","",categoryKey,"Buns",26000,100,R.drawable.dsc03490_optimized));
//                        myRef.push().setValue(new Product("Tuna Crossant","",categoryKey,"Buns",30000,100,R.drawable.dsc04823_optimized));
//                        myRef.push().setValue(new Product("Ya Ya Egg Tart","",categoryKey,"Buns",30000,100,R.drawable.dsc02790_optimized));
//                    }else if(categoryName.equals("Toasts")){
//                        //Toasts
//                        myRef.push().setValue(new Product("Almond Coffee Milky Way","",categoryKey,"Toasts",108000,100,R.drawable.dsc04839_optimized));
//                        myRef.push().setValue(new Product("Brioche","",categoryKey,"Toasts",90000,100,R.drawable.dsc02364_optimized));
//                        myRef.push().setValue(new Product("California Toast","",categoryKey,"Toasts",49000,100,R.drawable.dsc02270_optimized));
//                        myRef.push().setValue(new Product("Cranberry Cr Cheese 4pcs","",categoryKey,"Toasts",45000,100,R.drawable.dsc02369_optimized));
//                        myRef.push().setValue(new Product("Cranberry Toast","",categoryKey,"Toasts",55000,100,R.drawable.dsc02351_optimized));
//                        myRef.push().setValue(new Product("Dark Rye Toast","",categoryKey,"Toasts",49000,100,R.drawable.dsc02286_optimized));
//                        myRef.push().setValue(new Product("Earthquake Toast","",categoryKey,"Toasts",49000,100,R.drawable.dsc02331_optimized));
//                        myRef.push().setValue(new Product("Fresh Baguette","",categoryKey,"Toasts",45000,100,R.drawable.dsc02384_optimized));
//                        myRef.push().setValue(new Product("Gourment Fruit Loaf","",categoryKey,"Toasts",105000,100,R.drawable.dsc02263_optimized));
//                        myRef.push().setValue(new Product("Mount Greentea Toast","",categoryKey,"Toasts",55000,100,R.drawable.dsc02361_optimized));
//                        myRef.push().setValue(new Product("Premium Toast","",categoryKey,"Toasts",55000,100,R.drawable.dsc02313_optimized_1));
//                        myRef.push().setValue(new Product("Pumpkin Toast","",categoryKey,"Toasts",40000,100,R.drawable.dsc02342_optimized));
//                        myRef.push().setValue(new Product("Standard Toast","",categoryKey,"Toasts",47000,100,R.drawable.dsc02294_optimized));
//                        myRef.push().setValue(new Product("Wholemeal Toast","",categoryKey,"Toasts",40000,100,R.drawable.dsc02307_optimized));
//                    }else if(categoryName.equals("Sandwiches")){
//                        //Sandwich
//                        myRef.push().setValue(new Product("Bacon&Egg Breakfast SW","",categoryKey,"Sandwiches",45000,100,R.drawable.dsc02900_optimized));
//                        myRef.push().setValue(new Product("Baked Ham&Cheese SW","",categoryKey,"Sandwiches",45000,100,R.drawable.dsc02915_optimized));
//                        myRef.push().setValue(new Product("Chicken SW","",categoryKey,"Sandwiches",45000,100,R.drawable.dsc03094_optimized));
//                        myRef.push().setValue(new Product("Ham&Egg Breakfast SW","",categoryKey,"Sandwiches",45000,100,R.drawable.dsc02869_optimized));
//                        myRef.push().setValue(new Product("Tuna SW","",categoryKey,"Sandwiches",45000,100,R.drawable.dsc03110_optimized));
//                    }else if(categoryName.equals("Dry cakes")){
//                        //Dry cakes
//                        myRef.push().setValue(new Product("Banana Cake","",categoryKey,"Dry cakes",108000,100,R.drawable.dsc02662_optimized));
//                        myRef.push().setValue(new Product("Combo Drycake 3pcs","",categoryKey,"Dry cakes",80000,100,R.drawable.dsc02769_optimized));
//                        myRef.push().setValue(new Product("Combo Drycake 5pcs","",categoryKey,"Dry cakes",130000,100,R.drawable.dsc02774_optimized));
//                        myRef.push().setValue(new Product("Crater Cheese Honey","",categoryKey,"Dry cakes",55000,100,R.drawable.dsc02694_optimized));
//                        myRef.push().setValue(new Product("HOKKAIDO ROLL","",categoryKey,"Dry cakes",169000,100,R.drawable.hokkaido_roll_sp_roll));
//                        myRef.push().setValue(new Product("HOKKAIDO SLICED","",categoryKey,"Dry cakes",69000,100,R.drawable.hokkaido_slice_sp));
//                        myRef.push().setValue(new Product("Japan Light Cheese","",categoryKey,"Dry cakes",129000,100,R.drawable.dsc02649_optimized));
//                        myRef.push().setValue(new Product("PANDAN CHIFFON","",categoryKey,"Dry cakes",115000,100,R.drawable.ca_2_cake));
//                        myRef.push().setValue(new Product("Parmesan Cheese","",categoryKey,"Dry cakes",56000,100,R.drawable.dsc02412_optimized));
//                        myRef.push().setValue(new Product("SR Chocolate Sliced","",categoryKey,"Dry cakes",30000,100,R.drawable.dsc02720_optimized));
//                        myRef.push().setValue(new Product("SR Coffee Sliced","",categoryKey,"Dry cakes",30000,100,R.drawable.dsc05157_optimized));
//                        myRef.push().setValue(new Product("SR Greentea Sliced","",categoryKey,"Dry cakes",30000,100,R.drawable.dsc02752_optimized));
//                        myRef.push().setValue(new Product("SR Raisin Sliced","",categoryKey,"Dry cakes",30000,100,R.drawable.dsc02721_optimized));
//                        myRef.push().setValue(new Product("SR Tiger Skin Sliced","",categoryKey,"Dry cakes",30000,100,R.drawable.dsc02705_optimized));
//                    }else if(categoryName.equals("Cakes")){
//                        //Cakes
//                        myRef.push().setValue(new Product("Blueberry cake","",categoryKey,"Cakes",450000,100,R.drawable.dsc02564_optimized));
//                        myRef.push().setValue(new Product("Mocha cake","",categoryKey,"Cakes",450000,100,R.drawable.dsc02514_optimized));
//                        myRef.push().setValue(new Product("Mocha cake","",categoryKey,"Cakes",450000,100,R.drawable.dsc02512_optimized));
//                        myRef.push().setValue(new Product("Mocha cake","",categoryKey,"Cakes",450000,100,R.drawable.dsc02540_optimized));
//                        myRef.push().setValue(new Product("Black Forest C","",categoryKey,"Cakes",490000,100,R.drawable.dsc02466_optimized));
//                        myRef.push().setValue(new Product("Black Forest R","",categoryKey,"Cakes",360000,100,R.drawable.dsc02472_optimized));
//                        myRef.push().setValue(new Product("Fresh Cream","",categoryKey,"Cakes",450000,100,R.drawable.dsc02547_optimized));
//                        myRef.push().setValue(new Product("Fresh Cream (SN00 H10)","",categoryKey,"Cakes",380000,100,R.drawable.dsc02588_optimized));
//                        myRef.push().setValue(new Product("Fresh Cream (SN01)","",categoryKey,"Cakes",390000,100,R.drawable.dsc02574_optimized));
//                        myRef.push().setValue(new Product("Hai! Cheese R","",categoryKey,"Cakes",360000,100,R.drawable.dsc02256_optimized));
//                        myRef.push().setValue(new Product("Les Opera C","",categoryKey,"Cakes",550000,100,R.drawable.dsc05141_optimized));
//                        myRef.push().setValue(new Product("Les Opera R","",categoryKey,"Cakes",390000,100,R.drawable.dsc05138_optimized));
//                        myRef.push().setValue(new Product("Macha Macha","",categoryKey,"Cakes",490000,100,R.drawable.dsc05150_optimized));
//                        myRef.push().setValue(new Product("MangoCoCo Cake","",categoryKey,"Cakes",420000,100,R.drawable.dsc02430_optimized));
//                        myRef.push().setValue(new Product("Mocha Cheese","",categoryKey,"Cakes",490000,100,R.drawable.dsc02426_optimized));
//                        myRef.push().setValue(new Product("Mocha Choco","",categoryKey,"Cakes",490000,100,R.drawable.dsc02354_optimized));
//                        myRef.push().setValue(new Product("Party Pink","",categoryKey,"Cakes",550000,100,R.drawable.dsc02474_optimized));
//                        myRef.push().setValue(new Product("Passion Cheese C","",categoryKey,"Cakes",490000,100,R.drawable.dsc02494_optimized));
//                        myRef.push().setValue(new Product("Passion Cheese R","",categoryKey,"Cakes",360000,100,R.drawable.dsc02641_optimized));
//                        myRef.push().setValue(new Product("Rainbow Bliss C","",categoryKey,"Cakes",550000,100,R.drawable.dsc02437_optimized));
//                        myRef.push().setValue(new Product("Rainbow Bliss R","",categoryKey,"Cakes",390000,100,R.drawable.dsc02446_optimized));
//                        myRef.push().setValue(new Product("Snowy Fruity (C10H10)","",categoryKey,"Cakes",295000,100,R.drawable.dsc02604_optimized));
//                        myRef.push().setValue(new Product("Tiramisu C","",categoryKey,"Cakes",550000,100,R.drawable.dsc02452_optimized));
//                        myRef.push().setValue(new Product("Tiramisu R","",categoryKey,"Cakes",390000,100,R.drawable.dsc02621_optimized));
//                        myRef.push().setValue(new Product("Vanila Corn Cake (SN01)","",categoryKey,"Cakes",395000,100,R.drawable.dsc02344_optimized));
//                    }else if(categoryName.equals("Cake slices")){
//                        //Cake slices
//                        myRef.push().setValue(new Product("Beary Berry","",categoryKey,"Cake slices",45000,100,R.drawable.dsc02299_optimized));
//                        myRef.push().setValue(new Product("Beary Forest","",categoryKey,"Cake slices",45000,100,R.drawable.dsc02303_optimized));
//                        myRef.push().setValue(new Product("Beary Matcha","",categoryKey,"Cake slices",45000,100,R.drawable.dsc02295_optimized));
//                        myRef.push().setValue(new Product("Beary Vanilla","",categoryKey,"Cake slices",45000,100,R.drawable.dsc02284_optimized));
//                        myRef.push().setValue(new Product("Chocolate Peanut Mousse","",categoryKey,"Cake slices",59000,100,R.drawable.dsc02389_optimized));
//                        myRef.push().setValue(new Product("Hai! Cheese Sliced","",categoryKey,"Cake slices",59000,100,R.drawable.haicheesecup));
//                        myRef.push().setValue(new Product("Les Opera Sliced","",categoryKey,"Cake slices",58000,100,R.drawable.dsc02343_optimized));
//                        myRef.push().setValue(new Product("Oreo Chocolate Cheese","",categoryKey,"Cake slices",59000,100,R.drawable.dsc02388_optimized));
//                        myRef.push().setValue(new Product("Passion Cheese Sliced","",categoryKey,"Cake slices",59000,100,R.drawable.passion_cheese_cup));
//                        myRef.push().setValue(new Product("Rainbow Bliss Sliced","",categoryKey,"Cake slices",48000,100,R.drawable.dsc02333_optimized));
//                        myRef.push().setValue(new Product("Strawberry Yoghurt Mousse","",categoryKey,"Cake slices",59000,100,R.drawable.dsc02397_optimized));
//                        myRef.push().setValue(new Product("Tiramisu Cup","",categoryKey,"Cake slices",59000,100,R.drawable.dsc02401_optimized));
//                    }else if(categoryName.equals("Pudding")){
//                        //Pudding
//                        myRef.push().setValue(new Product("Lychee Coconut Pudding","",categoryKey,"Pudding",48000,100,R.drawable.lychee_coconut_pudding));
//                        myRef.push().setValue(new Product("PEACH PUDDING","",categoryKey,"Pudding",48000,100,R.drawable.peach_pudding));
//                        myRef.push().setValue(new Product("STRAWBERRY PUDDING","",categoryKey,"Pudding",48000,100,R.drawable.strawberry_pudding));
//                        myRef.push().setValue(new Product("Tiramisu Pudding","",categoryKey,"Pudding",48000,100,R.drawable.tiramisu_pudding));
//                    }else if(categoryName.equals("Cookies")){
//                        //Cookies
//                        myRef.push().setValue(new Product("Almond Cookies","",categoryKey,"Cookies",129000,100,R.drawable.dsc03146_optimized));
//                        myRef.push().setValue(new Product("Chocolate Cookies","",categoryKey,"Cookies",129000,100,R.drawable.dsc03142_optimized));
//                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void createVoucherData()  {
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference myRef=database.getReference("deal");
        List<Deal> dealList=new ArrayList<>();

        List<String> productIdList=new ArrayList<>();
        productIdList.add("0");

        Date date = new Date();
        myRef.push().setValue(new Deal("Discount 10% for cake","A11A22A30",productIdList,10,date,"discount 10% for black coffee","delivery",R.drawable.delivery_bike));
        myRef.push().setValue(new Deal("Discount 10% for cake","A11A22A31",productIdList,10,date,"discount 10% for black coffee","delivery",R.drawable.delivery_bike));
        myRef.push().setValue(new Deal("Discount 10% for cake","A11A22A33",productIdList,10,date,"discount 10% for black coffee","delivery",R.drawable.delivery_bike));
        myRef.push().setValue(new Deal("Discount 10% for cake","A11A22A34",productIdList,10,date,"discount 10% for black coffee","delivery",R.drawable.delivery_bike));
        myRef.push().setValue(new Deal("Discount 10% for cake","A11A22A35",productIdList,10,date,"discount 10% for black coffee","delivery",R.drawable.delivery_bike));
        myRef.push().setValue(new Deal("Discount 10% for cake","A11A22A36",productIdList,10,date,"discount 10% for black coffee","delivery",R.drawable.delivery_bike));
        myRef.push().setValue(new Deal("Discount 10% for cake","A11A22A37",productIdList,10,date,"discount 10% for black coffee","delivery",R.drawable.delivery_bike));
        myRef.push().setValue(new Deal("Discount 10% for cake","A11A22A48",productIdList,10,date,"discount 10% for black coffee","delivery",R.drawable.delivery_bike));

        myRef.push().setValue(new Deal("Discount 10% for cake","A11A22A38",productIdList,10,date,"discount 10% for black coffee","instore",R.drawable.store));
        myRef.push().setValue(new Deal("Discount 10% for cake","A11A22A39",productIdList,10,date,"discount 10% for black coffee","instore",R.drawable.store));
        myRef.push().setValue(new Deal("Discount 10% for cake","A11A22A40",productIdList,10,date,"discount 10% for black coffee","instore",R.drawable.store));
        myRef.push().setValue(new Deal("Discount 10% for cake","A11A22A41",productIdList,10,date,"discount 10% for black coffee","instore",R.drawable.store));
        myRef.push().setValue(new Deal("Discount 10% for cake","A11A22A42",productIdList,10,date,"discount 10% for black coffee","instore",R.drawable.store));

        myRef.push().setValue(new Deal("Discount 10% for cake","A11A22A43",productIdList,10,date,"discount 10% for black coffee","pickup",R.drawable.food_delivery));
        myRef.push().setValue(new Deal("Discount 10% for cake","A11A22A44",productIdList,10,date,"discount 10% for black coffee","pickup",R.drawable.food_delivery));
        myRef.push().setValue(new Deal("Discount 10% for cake","A11A22A45",productIdList,10,date,"discount 10% for black coffee","pickup",R.drawable.food_delivery));
        myRef.push().setValue(new Deal("Discount 10% for cake","A11A22A46",productIdList,10,date,"discount 10% for black coffee","pickup",R.drawable.food_delivery));
        myRef.push().setValue(new Deal("Discount 10% for cake","A11A22A47",productIdList,10,date,"discount 10% for black coffee","pickup",R.drawable.food_delivery));

    }
    public void createMembershipData(){
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference myRef=database.getReference("membership");
        List<Membership> membershipList=new ArrayList<>();
        List<String> newList=new ArrayList<>();
        myRef.push().setValue(new Membership("New",0,newList));
        List<String>bronzeList=new ArrayList<>();
        bronzeList.add("Get 1 piece of birthday cake");
        bronzeList.add("Privileges to redeem incentives with accumulated points");
        bronzeList.add("Free 1 snack for orders over 100,000 VND");
        myRef.push().setValue(new Membership("Bronze",1,bronzeList));
        List<String>silverList=new ArrayList<>();
        silverList.add("Get 1 piece of birthday cake");
        silverList.add("Privileges to redeem incentives with accumulated points");
        silverList.add("Buy 2 get 1 free");
        myRef.push().setValue(new Membership("Silver",2,silverList));
        List<String>goldList=new ArrayList<>();
        goldList.add("Get 1 piece of birthday cake");
        goldList.add("Privileges to redeem incentives with accumulated points");
        goldList.add("1 free coffee or tea");
        myRef.push().setValue(new Membership("Gold",3,goldList));
        List<String>diamondList=new ArrayList<>();
        diamondList.add("Get 1 piece of birthday cake");
        diamondList.add("Privileges to redeem incentives with accumulated points");
        diamondList.add("1 free drink of any kind");
        diamondList.add("Receive 1.5 accumulated points every day");
        diamondList.add("Opportunity to experience privileges for the first time");
        myRef.push().setValue(new Membership("Diamond",4,diamondList));
    }
    public void createOfferData(){
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference myRef=database.getReference("offer");
        List<Offer> offerList=new ArrayList<>();
        Date startDate=new Date();
        Date endDate=new Date();
        myRef.push().setValue(new Offer("The Scarlet coffee house","Tiramisu pudding only 20000VND",300,R.drawable.tiramisu_pudding,startDate,endDate));
        myRef.push().setValue(new Offer("The Scarlet coffee house","Tiramisu pudding only 18000VND",350,R.drawable.tiramisu_pudding,startDate,endDate));
        myRef.push().setValue(new Offer("The Scarlet coffee house","Tiramisu pudding only 20000VND",300,R.drawable.tiramisu_pudding,startDate,endDate));
        myRef.push().setValue(new Offer("The Scarlet coffee house","Tiramisu pudding only 30000VND",400,R.drawable.tiramisu_pudding,startDate,endDate));
        myRef.push().setValue(new Offer("The Scarlet coffee house","Tiramisu pudding only 20000VND",300,R.drawable.tiramisu_pudding,startDate,endDate));
        myRef.push().setValue(new Offer("The Scarlet coffee house","Tiramisu pudding only 20000VND",300,R.drawable.tiramisu_pudding,startDate,endDate));
        myRef.push().setValue(new Offer("The Scarlet coffee house","Tiramisu pudding only 20000VND",300,R.drawable.tiramisu_pudding,startDate,endDate));
        myRef.push().setValue(new Offer("The Scarlet coffee house","Tiramisu pudding only 20000VND",300,R.drawable.tiramisu_pudding,startDate,endDate));
        myRef.push().setValue(new Offer("The Scarlet coffee house","Tiramisu pudding only 20000VND",300,R.drawable.tiramisu_pudding,startDate,endDate));
        myRef.push().setValue(new Offer("The Scarlet coffee house","Tiramisu pudding only 20000VND",300,R.drawable.tiramisu_pudding,startDate,endDate));
    }
    public void createOfferTransactionData(){
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference myRef=database.getReference("offer_transaction");
        List<OfferTransaction> offerTransactionList=new ArrayList<>();
        offerTransactionList.add(new OfferTransaction("-NtPjHAcxZSIj1aKHS3B","0","AAA",new Date(),100));
        offerTransactionList.add(new OfferTransaction("-NtPjHAcxZSIj1aKHS3B","0","AAA",new Date(),100));
        offerTransactionList.add(new OfferTransaction("-NtPjHAcxZSIj1aKHS3B","0","AAA",new Date(),100));
        offerTransactionList.add(new OfferTransaction("-NtPjHAcxZSIj1aKHS3B","0","AAA",new Date(),100));
        offerTransactionList.add(new OfferTransaction("-NtPjHAcxZSIj1aKHS3B","0","AAA",new Date(),100));
        myRef.setValue(offerTransactionList);
    }
    public void createCartData(){
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference myRef=database.getReference("cart");
        List<Cart> cartList=new ArrayList<>();
        List<ProductQuantity> productQuantityList=new ArrayList<>();
        productQuantityList.add(new ProductQuantity("0",1));
        productQuantityList.add(new ProductQuantity("1",1));
        cartList.add(new Cart("-NtPjHAcxZSIj1aKHS3B",productQuantityList));
        myRef.setValue(cartList);
    }
}
