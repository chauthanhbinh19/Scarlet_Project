package com.example.scarlet.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.scarlet.ChangePasswordActivity;
import com.example.scarlet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SettingFragment extends Fragment {

    RelativeLayout back_btn, delete, change;
    final Handler handler = new Handler();
    int delay=100;
    TextView setup_account,security,settings_1;

    private void BindView(View view){
        back_btn=view.findViewById(R.id.back_btn);
        setup_account=view.findViewById(R.id.setup_account);
        security=view.findViewById(R.id.security);
        delete=view.findViewById(R.id.delete);
        change=view.findViewById(R.id.change);
        settings_1=view.findViewById(R.id.settings_1);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.setting, container, false);

        BindView(view);
        getAnimation();
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getParentFragmentManager().popBackStack();
                AccountFragment accountFragment=new AccountFragment();
                FragmentManager fragmentManager=getParentFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                fragmentTransaction.replace(R.id.frame_layout,accountFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                builder.setMessage("Do you want to delete your account?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                FirebaseUser user = mAuth.getCurrentUser();
                                if(user!=null){
                                    user.delete()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isComplete()){
                                                        Toast.makeText(getContext(),"User account deleted.", Toast.LENGTH_SHORT).show();
                                                    }else{
                                                        Toast.makeText(getContext(),"Failed to delete user account", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }else{
                                    Toast.makeText(getContext(),"No user is currently signed in.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancel",null)
                        .show();
            }
        });
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), ChangePasswordActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        return view;
    }
    private void getAnimation(){
        Animation setup_accountAnim= AnimationUtils.loadAnimation(setup_account.getContext(), android.R.anim.slide_in_left);
        Animation securityAnim= AnimationUtils.loadAnimation(security.getContext(), android.R.anim.slide_in_left);
        Animation deleteAnim= AnimationUtils.loadAnimation(delete.getContext(), android.R.anim.slide_in_left);
        Animation changeAnim= AnimationUtils.loadAnimation(change.getContext(), android.R.anim.slide_in_left);
        Animation settingAnim= AnimationUtils.loadAnimation(settings_1.getContext(), android.R.anim.fade_in);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setup_account.startAnimation(setup_accountAnim);
            }
        },delay*0);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                settings_1.startAnimation(settingAnim);
            }
        },delay*0);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                delete.startAnimation(deleteAnim);
            }
        },delay*1);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                security.startAnimation(securityAnim);
            }
        },delay*2);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                change.startAnimation(changeAnim);
            }
        },delay*3);
    }
}