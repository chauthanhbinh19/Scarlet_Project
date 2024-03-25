package com.example.scarlet.Data;

import com.example.scarlet.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class text {
    public void createCategoryData(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("category");

        List<Category> categoryList=new ArrayList<>();
        categoryList.add(new Category("Drinks", R.drawable.bubble_tea));
        categoryList.add(new Category("Buns",R.drawable.croissant));
        categoryList.add(new Category("Toasts",R.drawable.toast));
        categoryList.add(new Category("Sandwiches",R.drawable.sandwich));
        categoryList.add(new Category("Dry cakes",R.drawable.cinnamon_roll));
        categoryList.add(new Category("Cakes",R.drawable.cake));
        categoryList.add(new Category("Cake slices",R.drawable.cake_slice));
        categoryList.add(new Category("Pudding",R.drawable.pudding));
        categoryList.add(new Category("Cookies",R.drawable.cookie));

        myRef.setValue(categoryList);
    }
    public void createProductData(){
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference myRef=database.getReference("product");
        List<Product> productList=new ArrayList<>();
        //Drinks data
        productList.add(new Product("Black coffee","","0","Drinks",28000,100,R.drawable.dsc02313_optimized));
        productList.add(new Product("Lychee mojito","","0","Drinks",48000,100,R.drawable.dsc02977_optimized));
        productList.add(new Product("Lychee tea","","0","Drinks",45000,100,R.drawable.dsc03081_optimized));
        productList.add(new Product("Peach tea","","0","Drinks",48000,100,R.drawable.dsc03071_optimized));
        productList.add(new Product("Strawberry mojito","","0","Drinks",48000,100,R.drawable.dsc03013_optimized));
        productList.add(new Product("Thai lemon tea","","0","Drinks",28000,100,R.drawable.dsc03047_optimized));
        productList.add(new Product("Thai milk tea","","0","Drinks",30000,100,R.drawable.dsc02860_optimized));
        productList.add(new Product("Strawberry tea","","0","Drinks",45000,100,R.drawable.dsc05164_optimized));
        productList.add(new Product("White coffee","","0","Drinks",30000,100,R.drawable.dsc02847_optimized));
        //Buns
        productList.add(new Product("Bacon cheese onion","","1","Buns",32000,100,R.drawable.bacon_cheese_onion));
        productList.add(new Product("Banana chocolate","","1","Buns",24000,100,R.drawable.banana_chocolate));
        productList.add(new Product("Banana peanut","","1","Buns",24000,100,R.drawable.banana_peanut));
        productList.add(new Product("Big eye","","1","Buns",34000,100,R.drawable.big_eye));
        productList.add(new Product("Blueberry custard","","1","Buns",20000,100,R.drawable.blueberry_custard));
        productList.add(new Product("Brown Sugar Injeolmi Korissant","","1","Buns",29000,100,R.drawable.brown_sugar_injeolmi_korissant));
        productList.add(new Product("Cheese Boat","","1","Buns",22000,100,R.drawable.cheese_boat));
        productList.add(new Product("Cheese Croissant","","1","Buns",22000,100,R.drawable.cheese_croissant));
        productList.add(new Product("Cheese Floss","","1","Buns",27000,100,R.drawable.cheese_floss));
        productList.add(new Product("Cheese Lava Croissant","","1","Buns",18000,100,R.drawable.cheese_lava_croissant));
        productList.add(new Product("Cheese Lava Croissant 3pcs","","1","Buns",46000,100,R.drawable.cheese_lava_croissant_3pcs));
        productList.add(new Product("Cheese Lava Croissant 5pcs","","1","Buns",72000,100,R.drawable.cheese_lava_croissant_5pcs));
        productList.add(new Product("Cheese Sausage","","1","Buns",30000,100,R.drawable.dsc03210));
        productList.add(new Product("Chicken Parmesan","","1","Buns",34000,100,R.drawable.dsc03298_optimized));
        productList.add(new Product("Chocolate Donut","","1","Buns",28000,100,R.drawable.dsc03270_optimized));
        productList.add(new Product("Cinnamon Korissant","","1","Buns",22000,100,R.drawable.cinnamon_kroissant));

        productList.add(new Product("Cocktail Bun 3pcs","","1","Buns",27000,100,R.drawable.dsc02677_optimized));
        productList.add(new Product("Cocoa Teddy","","1","Buns",22000,100,R.drawable.dsc03556_optimized));
        productList.add(new Product("Cranberry Cream Cheese","","1","Buns",24000,100,R.drawable.dsc03558_optimized));
        productList.add(new Product("Cream Chez Garlic Bread","","1","Buns",36000,100,R.drawable.dsc03254_optimized));
        productList.add(new Product("Donut Balls","","1","Buns",39000,100,R.drawable.dsc02692_optimized));
        productList.add(new Product("Double Cheese","","1","Buns",24000,100,R.drawable.dsc03509_optimized));
        productList.add(new Product("Durian Egg Tart","","1","Buns",20000,100,R.drawable.dsc03025_optimized));
        productList.add(new Product("Egg Ppang","","1","Buns",39000,100,R.drawable.eggppang));
        productList.add(new Product("Fire Floss","","1","Buns",34000,100,R.drawable.dsc03319_optimized));
        productList.add(new Product("Floss","","1","Buns",34000,100,R.drawable.dsc03349_optimized));
        productList.add(new Product("Floss 4ever","","1","Buns",32000,100,R.drawable.flos_4ver));
        productList.add(new Product("Get Cheese","","1","Buns",22000,100,R.drawable.dsc03181_optimized));
        productList.add(new Product("Golden Lava Bun","","1","Buns",22000,100,R.drawable.dsc03528_optimized));
        productList.add(new Product("Golden Lava Croissant","","1","Buns",18000,100,R.drawable.dsc03399_optimized));
        productList.add(new Product("Golden Lava Croissant 3pcs","","1","Buns",46000,100,R.drawable.dsc03460_optimized));
        productList.add(new Product("Golden Lava Croissant 5pcs","","1","Buns",72000,100,R.drawable.dsc03454_optimized));

        productList.add(new Product("Golden Nacho Cheese","","1","Buns",45000,100,R.drawable.golden_nacho));
        productList.add(new Product("Green Garlic","","1","Buns",22000,100,R.drawable.green_garlic));
        productList.add(new Product("Ham Cheese","","1","Buns",29000,100,R.drawable.dsc03133_optimized));
        productList.add(new Product("Ham Cheese","","1","Buns",29000,100,R.drawable.ham_cheese));
        productList.add(new Product("Ham Roll","","1","Buns",22000,100,R.drawable.ham_roll));
        productList.add(new Product("Kimcheese Korissant","","1","Buns",38000,100,R.drawable.kimcheese_croissant));
        productList.add(new Product("Mushi Mushroom Chicken Dozo","","1","Buns",29000,100,R.drawable.dsc02935_optimized));
        productList.add(new Product("Mushroom Cheese Sausage","","1","Buns",34000,100,R.drawable.mushroom_cheese_sausage));
        productList.add(new Product("Phoenix","","1","Buns",24000,100,R.drawable.phoenix));
        productList.add(new Product("Pillow Raisin","","1","Buns",24000,100,R.drawable.dsc03241_optimized));
        productList.add(new Product("Pork Ribs","","1","Buns",28000,100,R.drawable.dsc03584_optimized));
        productList.add(new Product("Rainbow Donut","","1","Buns",28000,100,R.drawable.dsc03292_optimized));
        productList.add(new Product("Raisin Cream Cheese","","1","Buns",24000,100,R.drawable.dsc03558_optimized));
        productList.add(new Product("Sausage Standard","","1","Buns",24000,100,R.drawable.dsc03199_optimized));
        productList.add(new Product("Set Tart Cheese Mini 4pcs","","1","Buns",95000,100,R.drawable.dsc03507_optimized));
        productList.add(new Product("Smart Aleck","","1","Buns",25000,100,R.drawable.dsc03188_optimized));

        productList.add(new Product("Spring In The City","","1","Buns",39000,100,R.drawable.dsc03534_optimized));
        productList.add(new Product("Sprouted Rye","","1","Buns",28000,100,R.drawable.dsc02959_optimized));
        productList.add(new Product("Sprouted Rye","","1","Buns",28000,100,R.drawable.dsc02997_optimized));
        productList.add(new Product("Sprouted Rye","","1","Buns",28000,100,R.drawable.dsc02991_optimized));
        productList.add(new Product("Sprouted Rye 3pcs","","1","Buns",69000,100,R.drawable.dsc03006_optimized));
        productList.add(new Product("Sugar Donut","","1","Buns",28000,100,R.drawable.dsc03593_optimized));
        productList.add(new Product("T-Cures of Golden Flowers","","1","Buns",36000,100,R.drawable.dsc03347_optimized));
        productList.add(new Product("Tart Golden Cheese","","1","Buns",26000,100,R.drawable.dsc03485_optimized));
        productList.add(new Product("Tart Orig Cheese","","1","Buns",26000,100,R.drawable.dsc03490_optimized));
        productList.add(new Product("Tuna Crossant","","1","Buns",30000,100,R.drawable.dsc04823_optimized));
        productList.add(new Product("Ya Ya Egg Tart","","1","Buns",30000,100,R.drawable.dsc02790_optimized));
        //Toasts
        productList.add(new Product("Almond Coffee Milky Way","","2","Toasts",108000,100,R.drawable.dsc04839_optimized));
        productList.add(new Product("Brioche","","2","Toasts",90000,100,R.drawable.dsc02364_optimized));
        productList.add(new Product("California Toast","","2","Toasts",49000,100,R.drawable.dsc02270_optimized));
        productList.add(new Product("Cranberry Cr Cheese 4pcs","","2","Toasts",45000,100,R.drawable.dsc02369_optimized));
        productList.add(new Product("Cranberry Toast","","2","Toasts",55000,100,R.drawable.dsc02351_optimized));
        productList.add(new Product("Dark Rye Toast","","2","Toasts",49000,100,R.drawable.dsc02286_optimized));
        productList.add(new Product("Earthquake Toast","","2","Toasts",49000,100,R.drawable.dsc02331_optimized));
        productList.add(new Product("Fresh Baguette","","2","Toasts",45000,100,R.drawable.dsc02384_optimized));
        productList.add(new Product("Gourment Fruit Loaf","","2","Toasts",105000,100,R.drawable.dsc02263_optimized));
        productList.add(new Product("Mount Greentea Toast","","2","Toasts",55000,100,R.drawable.dsc02361_optimized));
        productList.add(new Product("Premium Toast","","2","Toasts",55000,100,R.drawable.dsc02313_optimized_1));
        productList.add(new Product("Pumpkin Toast","","2","Toasts",40000,100,R.drawable.dsc02342_optimized));
        productList.add(new Product("Standard Toast","","2","Toasts",47000,100,R.drawable.dsc02294_optimized));
        productList.add(new Product("Wholemeal Toast","","2","Toasts",40000,100,R.drawable.dsc02307_optimized));
        //Sandwich
        productList.add(new Product("Bacon&Egg Breakfast SW","","3","Sandwiches",45000,100,R.drawable.dsc02900_optimized));
        productList.add(new Product("Baked Ham&Cheese SW","","3","Sandwiches",45000,100,R.drawable.dsc02915_optimized));
        productList.add(new Product("Chicken SW","","3","Sandwiches",45000,100,R.drawable.dsc03094_optimized));
        productList.add(new Product("Ham&Egg Breakfast SW","","3","Sandwiches",45000,100,R.drawable.dsc02869_optimized));
        productList.add(new Product("Tuna SW","","3","Sandwiches",45000,100,R.drawable.dsc03110_optimized));
        //Dry cakes
        productList.add(new Product("Banana Cake","","4","Dry cakes",108000,100,R.drawable.dsc02662_optimized));
        productList.add(new Product("Combo Drycake 3pcs","","4","Dry cakes",80000,100,R.drawable.dsc02769_optimized));
        productList.add(new Product("Combo Drycake 5pcs","","4","Dry cakes",130000,100,R.drawable.dsc02774_optimized));
        productList.add(new Product("Crater Cheese Honey","","4","Dry cakes",55000,100,R.drawable.dsc02694_optimized));
        productList.add(new Product("HOKKAIDO ROLL","","4","Dry cakes",169000,100,R.drawable.hokkaido_roll_sp_roll));
        productList.add(new Product("HOKKAIDO SLICED","","4","Dry cakes",69000,100,R.drawable.hokkaido_slice_sp));
        productList.add(new Product("Japan Light Cheese","","4","Dry cakes",129000,100,R.drawable.dsc02649_optimized));
        productList.add(new Product("PANDAN CHIFFON","","4","Dry cakes",115000,100,R.drawable.ca_2_cake));
        productList.add(new Product("Parmesan Cheese","","4","Dry cakes",56000,100,R.drawable.dsc02412_optimized));
        productList.add(new Product("SR Chocolate Sliced","","4","Dry cakes",30000,100,R.drawable.dsc02720_optimized));
        productList.add(new Product("SR Coffee Sliced","","4","Dry cakes",30000,100,R.drawable.dsc05157_optimized));
        productList.add(new Product("SR Greentea Sliced","","4","Dry cakes",30000,100,R.drawable.dsc02752_optimized));
        productList.add(new Product("SR Raisin Sliced","","4","Dry cakes",30000,100,R.drawable.dsc02721_optimized));
        productList.add(new Product("SR Tiger Skin Sliced","","4","Dry cakes",30000,100,R.drawable.dsc02705_optimized));
        //Cakes
        productList.add(new Product("Blueberry cake","","5","Cakes",450000,100,R.drawable.dsc02564_optimized));
        productList.add(new Product("Mocha cake","","5","Cakes",450000,100,R.drawable.dsc02514_optimized));
        productList.add(new Product("Mocha cake","","5","Cakes",450000,100,R.drawable.dsc02512_optimized));
        productList.add(new Product("Mocha cake","","5","Cakes",450000,100,R.drawable.dsc02540_optimized));
        productList.add(new Product("Black Forest C","","5","Cakes",490000,100,R.drawable.dsc02466_optimized));
        productList.add(new Product("Black Forest R","","5","Cakes",360000,100,R.drawable.dsc02472_optimized));
        productList.add(new Product("Fresh Cream","","5","Cakes",450000,100,R.drawable.dsc02547_optimized));
        productList.add(new Product("Fresh Cream (SN00 H10)","","5","Cakes",380000,100,R.drawable.dsc02588_optimized));
        productList.add(new Product("Fresh Cream (SN01)","","5","Cakes",390000,100,R.drawable.dsc02574_optimized));
        productList.add(new Product("Hai! Cheese R","","5","Cakes",360000,100,R.drawable.dsc02256_optimized));
        productList.add(new Product("Les Opera C","","5","Cakes",550000,100,R.drawable.dsc05141_optimized));
        productList.add(new Product("Les Opera R","","5","Cakes",390000,100,R.drawable.dsc05138_optimized));
        productList.add(new Product("Macha Macha","","5","Cakes",490000,100,R.drawable.dsc05150_optimized));
        productList.add(new Product("MangoCoCo Cake","","5","Cakes",420000,100,R.drawable.dsc02430_optimized));
        productList.add(new Product("Mocha Cheese","","5","Cakes",490000,100,R.drawable.dsc02426_optimized));
        productList.add(new Product("Mocha Choco","","5","Cakes",490000,100,R.drawable.dsc02354_optimized));
        productList.add(new Product("Party Pink","","5","Cakes",550000,100,R.drawable.dsc02474_optimized));
        productList.add(new Product("Passion Cheese C","","5","Cakes",490000,100,R.drawable.dsc02494_optimized));
        productList.add(new Product("Passion Cheese R","","5","Cakes",360000,100,R.drawable.dsc02641_optimized));
        productList.add(new Product("Rainbow Bliss C","","5","Cakes",550000,100,R.drawable.dsc02437_optimized));
        productList.add(new Product("Rainbow Bliss R","","5","Cakes",390000,100,R.drawable.dsc02446_optimized));
        productList.add(new Product("Snowy Fruity (C10H10)","","5","Cakes",295000,100,R.drawable.dsc02604_optimized));
        productList.add(new Product("Tiramisu C","","5","Cakes",550000,100,R.drawable.dsc02452_optimized));
        productList.add(new Product("Tiramisu R","","5","Cakes",390000,100,R.drawable.dsc02621_optimized));
        productList.add(new Product("Vanila Corn Cake (SN01)","","5","Cakes",395000,100,R.drawable.dsc02344_optimized));
        //Cake slices
        productList.add(new Product("Beary Berry","","6","Cake slices",45000,100,R.drawable.dsc02299_optimized));
        productList.add(new Product("Beary Forest","","6","Cake slices",45000,100,R.drawable.dsc02303_optimized));
        productList.add(new Product("Beary Matcha","","6","Cake slices",45000,100,R.drawable.dsc02295_optimized));
        productList.add(new Product("Beary Vanilla","","6","Cake slices",45000,100,R.drawable.dsc02284_optimized));
        productList.add(new Product("Chocolate Peanut Mousse","","6","Cake slices",59000,100,R.drawable.dsc02389_optimized));
        productList.add(new Product("Hai! Cheese Sliced","","6","Cake slices",59000,100,R.drawable.haicheesecup));
        productList.add(new Product("Les Opera Sliced","","6","Cake slices",58000,100,R.drawable.dsc02343_optimized));
        productList.add(new Product("Oreo Chocolate Cheese","","6","Cake slices",59000,100,R.drawable.dsc02388_optimized));
        productList.add(new Product("Passion Cheese Sliced","","6","Cake slices",59000,100,R.drawable.passion_cheese_cup));
        productList.add(new Product("Rainbow Bliss Sliced","","6","Cake slices",48000,100,R.drawable.dsc02333_optimized));
        productList.add(new Product("Strawberry Yoghurt Mousse","","6","Cake slices",59000,100,R.drawable.dsc02397_optimized));
        productList.add(new Product("Tiramisu Cup","","6","Cake slices",59000,100,R.drawable.dsc02401_optimized));
        //Pudding
        productList.add(new Product("Lychee Coconut Pudding","","7","Pudding",48000,100,R.drawable.lychee_coconut_pudding));
        productList.add(new Product("PEACH PUDDING","","7","Pudding",48000,100,R.drawable.peach_pudding));
        productList.add(new Product("STRAWBERRY PUDDING","","7","Pudding",48000,100,R.drawable.strawberry_pudding));
        productList.add(new Product("Tiramisu Pudding","","7","Pudding",48000,100,R.drawable.tiramisu_pudding));
        //Cookies
        productList.add(new Product("Almond Cookies","","8","Cookies",129000,100,R.drawable.dsc03146_optimized));
        productList.add(new Product("Chocolate Cookies","","8","Cookies",129000,100,R.drawable.dsc03142_optimized));

        myRef.setValue(productList);
    }
}
