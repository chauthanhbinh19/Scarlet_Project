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
import com.example.scarlet.AdminEditOfferActivity;
import com.example.scarlet.AdminEditProductActivity;
import com.example.scarlet.Data.Offer;
import com.example.scarlet.Data.Product;
import com.example.scarlet.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;

public class AdminOfferHolderView extends RecyclerView.ViewHolder {
    ImageView imageView,imageView2, btnEdit,btnDelete;
    TextView textView1,textView2,textView3,textView4;
    Context context;
    String key,img,name,description, startDate, endDate;
    int point;
    double price;
    public AdminOfferAdapter adapter;
    public AdminOfferHolderView linkAdapter(AdminOfferAdapter adapter){
        this.adapter=adapter;
        return this;
    }

    public AdminOfferHolderView(@NonNull View itemView) {
        super(itemView);
        imageView=itemView.findViewById(R.id.icon);
        textView1=itemView.findViewById(R.id.name_offer);
        textView2=itemView.findViewById(R.id.point_offer);
        textView3=itemView.findViewById(R.id.start_date_offer);
        textView4=itemView.findViewById(R.id.end_date_offer);
        context=itemView.getContext();
        btnEdit=itemView.findViewById(R.id.btnEdit);
        btnDelete=itemView.findViewById(R.id.btnDelete);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, AdminEditOfferActivity.class);
                intent.putExtra("offerName",name);
                intent.putExtra("offerDescription",description);
                intent.putExtra("offerPoint",String.valueOf(point));
                intent.putExtra("offerStartDate",startDate);
                intent.putExtra("offerEndDate",endDate);
                intent.putExtra("offerKey",key);
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
                                DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("offer").child(key);

                                databaseReference.removeValue();
                                adapter.offerList.remove(getAdapterPosition());
                                adapter.notifyDataSetChanged();
                                Toast.makeText(context,"Delete successfully",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel",null)
                        .show();
            }
        });
    }
    public void bindData(Offer offer){
        imageView.setImageResource(offer.getImage());
        textView1.setText(offer.getName());
        textView2.setText(String.valueOf(offer.getPoint()));
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String formattedStartDate = format.format(offer.getStartDate());
        textView3.setText("Day starts: "+formattedStartDate);
        String formattedEndDate = format.format(offer.getEndDate());
        textView4.setText("Day ends: "+formattedEndDate);
        key=offer.getKey();
        name=offer.getName();
        description=offer.getDescription();
        point=offer.getPoint();
        startDate=formattedStartDate;
        endDate=formattedEndDate;

    }
}
