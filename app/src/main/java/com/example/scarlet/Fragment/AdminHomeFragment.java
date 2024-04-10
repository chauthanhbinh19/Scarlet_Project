package com.example.scarlet.Fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class AdminHomeFragment extends Fragment {

    private FirebaseStorage storage;
    private StorageReference storageReference;
    Uri uri;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.admin_fragment_home, container, false);

        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference("product");

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCategoryCheck();
            }
        });
        return view;
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
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}