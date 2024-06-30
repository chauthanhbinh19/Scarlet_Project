package com.example.scarlet.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.scarlet.AdminEditCategoryActivity;
import com.example.scarlet.AdminEditProductActivity;
import com.example.scarlet.Data.Cart;
import com.example.scarlet.Data.Product;
import com.example.scarlet.Data.ProductQuantity;
import com.example.scarlet.ProductDetailActivity;
import com.example.scarlet.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class AdminProductHolderView extends RecyclerView.ViewHolder {
    ImageView imageView,imageView2, btnEdit,btnDelete;
    TextView textView1,textView2,textView3;
    Context context;
    String productKey,img,name,description, categoryId, categoryName;
    int point;
    double price;
    public AdminProductAdapter adapter;
    private Activity activity;
    public AdminProductHolderView linkAdapter(AdminProductAdapter adapter){
        this.adapter=adapter;
        return this;
    }

    public AdminProductHolderView(@NonNull View itemView) {
        super(itemView);
        imageView=itemView.findViewById(R.id.icon);
        textView1=itemView.findViewById(R.id.name_product);
        textView2=itemView.findViewById(R.id.price_product);
        textView3=itemView.findViewById(R.id.point_product);
        context=itemView.getContext();
        activity=(Activity) itemView.getContext();
        btnEdit=itemView.findViewById(R.id.btnEdit);
        btnDelete=itemView.findViewById(R.id.btnDelete);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup();
            }
        });
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup();
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup();
            }
        });
        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup();
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, AdminEditProductActivity.class);
                intent.putExtra("productName",name);
                intent.putExtra("img",img);
                intent.putExtra("productPoint",String.valueOf(point));
                intent.putExtra("productPrice",String.valueOf(price));
                intent.putExtra("productDescription",description);
                intent.putExtra("productKey",productKey);
                intent.putExtra("categoryId",categoryId);
                intent.putExtra("categoryName",categoryName);
                context.startActivity(intent);
            }
        });
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
                                DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("product").child(productKey);

                                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        databaseReference.removeValue();
                                        adapter.productList.remove(getAdapterPosition());
                                        adapter.notifyDataSetChanged();
//                                        Toast.makeText(context,"Delete successfully",Toast.LENGTH_SHORT).show();
                                        showSuccessDialog();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Cancel",null)
                        .show();
            }
        });
    }
    public void bindData(Product product){
//        imageView.setImageResource(product.getImg());
        Glide.with(context).load(product.getImg()).into(imageView);
        textView1.setText(product.getName());
        textView2.setText(String.valueOf(product.getPrice()));
        textView3.setText(String.valueOf(product.getPoint()));
        productKey=product.getKey();
        name=product.getName();
        point=product.getPoint();
        price=product.getPrice();
        description=product.getDescription();
        img=product.getImg();
        categoryId=product.getCategoryId();
        categoryName=product.getCategoryName();
    }
    public void showPopup(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.admin_product_detail, null);

        TextView productName=dialogView.findViewById(R.id.productName);
        TextView productPoint=dialogView.findViewById(R.id.productPoint);
        TextView productPrice=dialogView.findViewById(R.id.productPrice);
        TextView productDescription=dialogView.findViewById(R.id.productDescription);
        TextView productCategory=dialogView.findViewById(R.id.productCategory);
        ImageView btnImage = dialogView.findViewById(R.id.btnImage);
        ImageButton btnClose=dialogView.findViewById(R.id.btnClose);
        builder.setView(dialogView);

        android.app.AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.rectangle_circle_white_30));

        productName.setText(name);
        productPoint.setText(String.valueOf(point));
        productPrice.setText(String.format("%.0f",price)+" đ");
        productDescription.setText(description);
        productCategory.setText(categoryName);
        Glide.with(context).load(img).into(btnImage);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
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
