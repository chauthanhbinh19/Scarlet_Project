package com.example.scarlet.Fragment;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdminProductFragment extends Fragment {

    RecyclerView productRecycleView;
    List<Product> productList;
    AdminProductAdapter adminProductAdapter;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    Spinner spinner;
    EditText productName, productPoint, productPrice, productDescription;
    TextView btnImageError;
    ImageView btnImage;
    List<Category> categoryList;
    Uri uri;
    ProgressDialog progressDialog;
    String categoryId, categoryName;
    EditText search_bar;
    Button btnSave;
    final Handler handler = new Handler();
    int delay=150;
    private int currentPage=1;
    private int pageSize=10;
    private void BindView(View view){
        productRecycleView=view.findViewById(R.id.product_recyclerView);
        search_bar=view.findViewById(R.id.search_bar);
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.admin_fragment_product, container, false);

        BindView(view);
        productList=new ArrayList<>();
        productRecycleView.setLayoutManager(new GridLayoutManager(getContext(),1));
        productRecycleView.addItemDecoration(new GridLayoutDecoration(4,0));
        getProductData();
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
                adminProductAdapter.filterBySearch(keyword);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }
    private void getAnimation(){
        Animation searchAnim= AnimationUtils.loadAnimation(search_bar.getContext(), android.R.anim.slide_in_left);
        Animation recycleViewAnim= AnimationUtils.loadAnimation(productRecycleView.getContext(), android.R.anim.fade_in);
        search_bar.startAnimation(searchAnim);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                productRecycleView.startAnimation(recycleViewAnim);
            }
        },delay*3);
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
        ImageButton btnClose=dialogView.findViewById(R.id.btnClose);
        Button btnSave=dialogView.findViewById(R.id.btnSave);
        spinner=dialogView.findViewById(R.id.productCategory);
        btnImageError=dialogView.findViewById(R.id.btnImageError);

        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_circle_white_30));
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Category selectedCategory=(Category) parent.getItemAtPosition(position);
                categoryId=selectedCategory.getKey();
                categoryName=selectedCategory.getName_category();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(productName.getText().toString().isEmpty()){
                    productName.setError("Product name can not be empty");
                }else if(productPoint.getText().toString().isEmpty()){
                    productPoint.setError("Product point can not be empty");
                }else if(productPrice.getText().toString().isEmpty()){
                    productPrice.setError("Product price can not be empty");
                }else if(uri==null){
                    btnImageError.setText("Image can not be empty");
                    btnImageError.setVisibility(View.VISIBLE);
                }else{
                    saveProductData();
                }
            }
        });

        dialog.show();
    }
    private void getProductData(){
        productList=new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference productRef = database.getReference("product");
        int startOffset=(currentPage-1)*pageSize;
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot productSnapshot) {
                productList.clear();
                for (DataSnapshot product : productSnapshot.getChildren()) {
                    String productKey=product.getKey();
                    String categoryId = product.child("categoryId").getValue(String.class);
                    String productName = product.child("name").getValue(String.class);
                    String categoryName=product.child("categoryName").getValue(String.class);
                    String productDescription=product.child("description").getValue(String.class);
                    double productPrice = product.child("price").getValue(double.class);
                    int productPoint=product.child("point").getValue(int.class);
                    String productImage = product.child("img").getValue(String.class);
                    Product productWithIcon = new Product(productName,productDescription,categoryId,categoryName, productPrice,productPoint,productImage,productKey);
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
    private void saveProductData(){
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setTitle("Uploading file...");
        progressDialog.show();

        SimpleDateFormat formatter=new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss", Locale.US);
        Date now=new Date();
        String fileName= formatter.format(now);

        storage= FirebaseStorage.getInstance();
        storageReference=storage.getReference("product/"+fileName);

        storageReference.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isComplete());
                        Uri urlImage=uriTask.getResult();

                        String pName=productName.getText().toString();
                        String pDescription=productDescription.getText().toString();
                        double pPrice=Double.parseDouble(productPrice.getText().toString());
                        int pPoint=Integer.parseInt(productPoint.getText().toString());
                        Product product=new Product(pName,pDescription,categoryId,categoryName,pPrice,pPoint,urlImage.toString());

                        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                        DatabaseReference productRef= firebaseDatabase.getReference("product");
                        productRef.push().setValue(product);
                        if(progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                        Toast.makeText(getContext(),"Save successfully",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                        Toast.makeText(getContext(),"Save failed",Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}