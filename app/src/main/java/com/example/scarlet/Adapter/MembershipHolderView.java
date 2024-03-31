package com.example.scarlet.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Data.Deal;
import com.example.scarlet.Data.Membership;
import com.example.scarlet.R;

public class MembershipHolderView extends RecyclerView.ViewHolder {
    TextView privileges;

    public MembershipHolderView(@NonNull View itemView) {
        super(itemView);
        privileges=itemView.findViewById(R.id.priviliges);

    }
    public void bindData(String privilige){
        privileges.setText(privilige);
    }
}
