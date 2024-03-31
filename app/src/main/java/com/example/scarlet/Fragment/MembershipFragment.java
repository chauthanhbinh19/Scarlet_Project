package com.example.scarlet.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Adapter.DealAdapter;
import com.example.scarlet.Adapter.GridLayoutDecoration;
import com.example.scarlet.Adapter.MembershipAdapter;
import com.example.scarlet.Data.Deal;
import com.example.scarlet.Data.Membership;
import com.example.scarlet.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MembershipFragment extends Fragment {
    private String tabTitle;
    private MembershipAdapter membershipAdapter;
    private List<Membership> membershipList;
    public static MembershipFragment newInstance(String tabTitle) {
        MembershipFragment fragment = new MembershipFragment();
        Bundle args = new Bundle();
        args.putString("tabTitle", tabTitle);
        fragment.setArguments(args);
        return fragment;
    }
    public MembershipFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.membership_tabpage, container, false);
        Bundle bundle = getArguments();
        String tabTitle = bundle.getString("tabTitle");

        RecyclerView recyclerView=view.findViewById(R.id.membership_recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        recyclerView.addItemDecoration(new GridLayoutDecoration(0,10));
        getMembershipData(recyclerView, tabTitle);

        return view;
    }
    private void getMembershipData(RecyclerView recyclerView, String membershipClicked){
        membershipList=new ArrayList<>();
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        Query Query= firebaseDatabase.getReference("membership");
        Query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot snap: snapshot.getChildren()){
                        if(snap.child("name").getValue(String.class).equals(membershipClicked)){
                            Object privilegesObject =snap.child("privileges").getValue(Object.class);
                            if (privilegesObject  instanceof List) {
                                List<String> privileges = (List<String>) privilegesObject;
                                membershipAdapter=new MembershipAdapter(privileges);
                                recyclerView.setAdapter(membershipAdapter);
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}