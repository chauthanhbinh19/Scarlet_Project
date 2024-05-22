package com.example.scarlet.Adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.scarlet.AdminEditCategoryActivity;
import com.example.scarlet.Data.Category;
import com.example.scarlet.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class AdminCategoryHolderView extends RecyclerView.ViewHolder {
    ImageView imageView, btnDelete,btnEdit;
    TextView textView;
    ImageView btnImage;
    EditText categoryName;
    String categoryKey, name;
    ProgressDialog progressDialog;
    Uri uri;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    String img;
    Context context;
    public AdminCategoryAdapter adapter;
    public AdminCategoryHolderView linkAdapter(AdminCategoryAdapter adapter){
        this.adapter=adapter;
        return this;
    }
    public AdminCategoryHolderView(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.icon);
        textView = itemView.findViewById(R.id.name_category);
        btnDelete=itemView.findViewById(R.id.btnDelete);
        btnEdit=itemView.findViewById(R.id.btnEdit);
        context=itemView.getContext();
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setMessage("Do you want to delete it?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();
                                StorageReference storageReference= firebaseStorage.getReferenceFromUrl(img);
                                DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("category").child(categoryKey);
                                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        databaseReference.removeValue();
                                        adapter.categoryList.remove(getAdapterPosition());
                                        adapter.notifyDataSetChanged();
//                                        Toast.makeText(context,"Delete successfully",Toast.LENGTH_SHORT).show();
                                        showSuccessDialog();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, AdminEditCategoryActivity.class);
                intent.putExtra("categoryName",name);
                intent.putExtra("img",img);
                intent.putExtra("categoryKey",categoryKey);
                context.startActivity(intent);
            }
        });
    }
    public void bindData(Category category){
//        imageView.setImageResource(category.getImg());
        Glide.with(context).load(category.getImg()).into(imageView);
        textView.setText(category.getName_category());
        categoryKey=category.getKey();
        img=category.getImg();
        name=category.getName_category();
    }
    public void showSuccessDialog(){
        final Dialog dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.success_dialog_2);

        TextView mess=dialog.findViewById(R.id.mess);
        mess.setText("Delete successfully");

        dialog.getWindow().setBackgroundDrawable(context.getDrawable(R.drawable.rectangle_circle_white_30));
        dialog.show();
    }

}
