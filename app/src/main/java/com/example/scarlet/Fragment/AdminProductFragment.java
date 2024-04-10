package com.example.scarlet.Fragment;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import com.example.scarlet.Adapter.AdminCategoryDropdownAdapter;
import com.example.scarlet.Adapter.AdminProductAdapter;
import com.example.scarlet.Adapter.GridLayoutDecoration;
import com.example.scarlet.Adapter.ProductAdapter;
import com.example.scarlet.Data.Category;
import com.example.scarlet.Data.Product;
import com.example.scarlet.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminProductFragment extends Fragment {

    RecyclerView productRecycleView;
    List<Product> productList;
    AdminProductAdapter adminProductAdapter;
    Spinner spinner;
    EditText productName, productPoint, productPrice, productDescription;
    ImageView btnImage;
    List<Category> categoryList;
    Uri uri;
    Button btnSave;
    private void BindView(View view){
        productRecycleView=view.findViewById(R.id.product_recyclerView);
        spinner=view.findViewById(R.id.productCategory);
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.admin_fragment_product, container, false);

        BindView(view);
        productList=new ArrayList<>();
        productRecycleView.setLayoutManager(new GridLayoutManager(getContext(),1));
        productRecycleView.addItemDecoration(new GridLayoutDecoration(4,0));
        getProductData();
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInsertPopup();
            }
        });
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                adapter.notifyDataSetChanged();
//                Category selectedCategory = (Category) parent.getItemAtPosition(position);
//                selectedImage = selectedCategory.getImg();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // Không cần xử lý khi không có mục nào được chọn
//            }
//        });
        return view;
    }
    public void showInsertPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.admin_product_popup, null);

        getCategoryData(dialogView);
        productName = dialogView.findViewById(R.id.productName);
        productPoint=dialogView.findViewById(R.id.productPoint);
        productPrice=dialogView.findViewById(R.id.productPrice);
        productDescription=dialogView.findViewById(R.id.productDescription);
        btnImage = dialogView.findViewById(R.id.btnImage);
        Button btnSave=dialogView.findViewById(R.id.btnSave);

        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_circle_white_30));
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        dialog.show();
    }
    private void getProductData(){
        productList=new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference productRef = database.getReference("product");
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot productSnapshot) {
                for (DataSnapshot product : productSnapshot.getChildren()) {
                    String productKey=product.getKey();
                    String categoryId = product.child("categoryId").getValue(String.class);
                    String productName = product.child("name").getValue(String.class);
                    double productPrice = product.child("price").getValue(double.class);
                    int productPoint=product.child("point").getValue(int.class);
                    String productImage = product.child("img").getValue(String.class);
                    Product productWithIcon = new Product(productName, productPrice,productImage, "",productKey,productPoint);
                    productList.add(productWithIcon);
                }
                if(productList.size()>0){
                    adminProductAdapter=new AdminProductAdapter(productList);
                    productRecycleView.setAdapter(adminProductAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });
    }
    private void choosePicture(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            uri=data.getData();
            btnImage.setImageURI(uri);
        }
    }
    private void getCategoryData(View view){
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference= firebaseDatabase.getReference("category");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryList=new ArrayList<>();
                for(DataSnapshot snap: snapshot.getChildren()){
                    String categoryKey=snap.getKey();
                    String categoryName=snap.child("name_category").getValue(String.class);
                    String categoryImg=snap.child("img").getValue(String.class);
                    Category category=new Category(categoryName,categoryImg,categoryKey);
                    categoryList.add(category);
                }
                spinner=view.findViewById(R.id.productCategory);
                AdminCategoryDropdownAdapter adapter=new AdminCategoryDropdownAdapter(getContext(),R.layout.admin_category_dropdown_layout,categoryList);
                spinner.setAdapter(adapter);
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