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

import java.util.List;

public class ProductCheckboxHolderView extends RecyclerView.ViewHolder {

    public CheckBox checkBox;
    String key;
    GetKeyCallback getKeyCallback;
    public ProductCheckboxAdapter adapter;
    public ProductCheckboxHolderView linkAdapter(ProductCheckboxAdapter productCheckboxAdapter){
        this.adapter=productCheckboxAdapter;
        return this;
    }
    public ProductCheckboxHolderView(@NonNull View itemView) {
        super(itemView);
        checkBox=itemView.findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    getKeyCallback.itemClick(key,1);
                    adapter.productList.get(getAdapterPosition()).setChecked(true);
                }else{
                    getKeyCallback.itemClick(key,0);
                    adapter.productList.get(getAdapterPosition()).setChecked(false);
                }
            }
        });
    }
    public void bindData(Product product, GetKeyCallback getKeyCallback1){
        checkBox.setText(product.getName());
        key= product.getKey();
        checkBox.setChecked(product.isChecked());
        this.getKeyCallback=getKeyCallback1;
    }
}
