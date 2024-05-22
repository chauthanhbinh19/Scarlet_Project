package com.example.scarlet.Fragment;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class AdminCategoryFragment extends Fragment {

    private List<Category> categoryList;
    private AdminCategoryAdapter categoryAdapter;
    RecyclerView categoryRecyclerView;
    Uri uri;
    ImageView btnImage;
    EditText categoryName;
    View view;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    ProgressDialog progressDialog;
    private void BindView(View view){
        categoryRecyclerView= view.findViewById(R.id.category_recyclerView);
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view =inflater.inflate(R.layout.admin_fragment_category, container, false);
        BindView(view);
        
        categoryList=new ArrayList<>();
        categoryRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        categoryRecyclerView.addItemDecoration(new GridLayoutDecoration(10,0));

        getCategoryData();
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInsertPopup();
            }
        });

        return view;
    }
    public void showInsertPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.admin_category_popup, null);

        categoryName = dialogView.findViewById(R.id.categoryName);
        btnImage = dialogView.findViewById(R.id.btnImage);
        Button btnSave=dialogView.findViewById(R.id.btnSave);
        ImageButton btnClose=dialogView.findViewById(R.id.btnClose);
        TextView ImageError=dialogView.findViewById(R.id.btnImageError);

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
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(categoryName.getText().toString().isEmpty()){
                    categoryName.setError("Category name can not be empty");
                }else if(uri==null){
                    ImageError.setText("Image can not be empty");
                    ImageError.setVisibility(View.VISIBLE);
                }else{
                    uploadCategoryPicture();
                }
            }
        });

        dialog.show();
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
    private void uploadCategoryPicture() {
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setTitle("Uploading file...");
        progressDialog.show();

        SimpleDateFormat formatter=new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss", Locale.US);
        Date now=new Date();
        String fileName= formatter.format(now);

        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference("category/"+fileName);

        storageReference.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isComplete());
                        Uri urlImage=uriTask.getResult();

                        Category category=new Category(categoryName.getText().toString(),urlImage.toString());

                        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                        DatabaseReference categoryRef= firebaseDatabase.getReference("category");
                        categoryRef.push().setValue(category);
                        if(progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                        showSuccessDialog();
                        getCategoryData();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                        showFailedDialog();
                    }
                });
    }
    private void getCategoryData(){
        Query query = FirebaseDatabase.getInstance().getReference("category");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryList.clear();
                for(DataSnapshot snap:snapshot.getChildren()){
                    String categoryName=snap.child("name_category").getValue(String.class);
                    String categoryImage=snap.child("img").getValue(String.class);
                    String categoryKey=snap.getKey();
                    Category category=new Category(categoryName,categoryImage,categoryKey);
                    categoryList.add(category);
                }
                if(categoryList.size()>0){
                    categoryAdapter=new AdminCategoryAdapter(categoryList);
                    categoryRecyclerView.setAdapter(categoryAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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