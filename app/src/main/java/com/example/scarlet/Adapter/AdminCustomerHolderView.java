package com.example.scarlet.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.scarlet.AdminEditCustomerActivity;
import com.example.scarlet.AdminEditProductActivity;
import com.example.scarlet.Data.Product;
import com.example.scarlet.Data.User;
import com.example.scarlet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class AdminCustomerHolderView extends RecyclerView.ViewHolder {
    ImageView imageView,imageView2, btnEdit,btnDelete;
    TextView textView1,textView2,textView3;
    Context context;
    String userkey,img,firstname,lastname,phone,dateofbirth, gender;
    int point;
    double price;
    public AdminCustomerAdapter adapter;
    public AdminCustomerHolderView linkAdapter(AdminCustomerAdapter adapter){
        this.adapter=adapter;
        return this;
    }

    public AdminCustomerHolderView(@NonNull View itemView) {
        super(itemView);
        imageView=itemView.findViewById(R.id.icon);
        textView1=itemView.findViewById(R.id.name_customer);
        textView2=itemView.findViewById(R.id.phone_customer);
        textView3=itemView.findViewById(R.id.email_customer);
        context=itemView.getContext();
        btnEdit=itemView.findViewById(R.id.btnEdit);
        btnDelete=itemView.findViewById(R.id.btnDelete);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, AdminEditCustomerActivity.class);
                intent.putExtra("customerFirstName",firstname);
                intent.putExtra("customerLastName",lastname);
                intent.putExtra("img",img);
                intent.putExtra("customerDateofBirth",dateofbirth);
                intent.putExtra("customerGender",gender);
                intent.putExtra("customerPhone",phone);
                intent.putExtra("customerKey",userkey);
                context.startActivity(intent);
            }
        });
//        btnDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder builder=new AlertDialog.Builder(context);
//                builder.setMessage("Do you want to delete it?")
//                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
//                                DatabaseReference databaseReference=firebaseDatabase.getReference("user").child(userkey);
//                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                        for(DataSnapshot snap: snapshot.getChildren()){
//                                            String uid=snap.child("uid").getValue(String.class);
//                                            FirebaseAuth auth=FirebaseAuth.getInstance();
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError error) {
//
//                                    }
//                                });
//                            }
//                        })
//                        .setNegativeButton("Cancel",null)
//                        .show();
//            }
//        });
    }
    public void bindData(User user){
//        imageView.setImageResource(user.ge());
        Glide.with(context).load(user.getAvatar_img()).into(imageView);
        textView1.setText(user.getLast_name()+" "+user.getFirst_name());
        textView2.setText(user.getPhone_number());
        textView3.setText(user.getEmail());
        lastname=user.getLast_name();
        firstname=user.getFirst_name();
        phone=user.getPhone_number();
        dateofbirth=user.getDate_of_birth();
        userkey=user.getKey();
        gender=user.getGender();
        img=user.getAvatar_img();
    }
}
