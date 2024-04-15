package com.example.scarlet.Adapter;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Data.Product;
import com.example.scarlet.Interface.GetKeyCallback;
import com.example.scarlet.Interface.GetStringCallback;
import com.example.scarlet.R;

public class ProductCheckboxHolderView extends RecyclerView.ViewHolder {

    CheckBox checkBox;
    String key;
    GetKeyCallback getKeyCallback;
    public ProductCheckboxHolderView(@NonNull View itemView) {
        super(itemView);
        checkBox=itemView.findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    getKeyCallback.itemClick(key,1);
                }else{
                    getKeyCallback.itemClick(key,0);
                }
            }
        });
    }
    public void bindData(Product product, GetKeyCallback getKeyCallback1){
        checkBox.setText(product.getName());
        key= product.getKey();
        this.getKeyCallback=getKeyCallback1;
    }
}
