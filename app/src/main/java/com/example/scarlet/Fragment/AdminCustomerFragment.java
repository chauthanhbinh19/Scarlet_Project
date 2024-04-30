package com.example.scarlet.Fragment;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scarlet.Adapter.AdminCustomerAdapter;
import com.example.scarlet.Adapter.AdminProductAdapter;
import com.example.scarlet.Adapter.GridLayoutDecoration;
import com.example.scarlet.AdminEditCategoryActivity;
import com.example.scarlet.AdminEditCustomerActivity;
import com.example.scarlet.Data.Category;
import com.example.scarlet.Data.Product;
import com.example.scarlet.Data.User;
import com.example.scarlet.Dialog.DatePickerDialog;
import com.example.scarlet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class AdminCustomerFragment extends Fragment {

    RecyclerView customerRecycleView;
    private AdminCustomerAdapter adminCustomerAdapter;
    List<User> userList;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    Uri uri;
    ImageView btnImage;
    EditText customerFirstName, customerLastName, customerGender, customerPhone, customerEmail, customerPassword, search_bar;
    TextView btnImageError, customerDateofBirth;
    ProgressDialog progressDialog;
    ImageButton calendar;
    final Handler handler = new Handler();
    int delay=150;
    RelativeLayout sortIcon;
    private void BindView(View view){
        customerRecycleView=view.findViewById(R.id.customer_recyclerView);
        search_bar=view.findViewById(R.id.search_bar);
        sortIcon=view.findViewById(R.id.sortIcon);
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.admin_fragment_customer, container, false);

        BindView(view);
        userList=new ArrayList<>();
        customerRecycleView.setLayoutManager(new GridLayoutManager(getContext(),1));
        customerRecycleView.addItemDecoration(new GridLayoutDecoration(4,0));

        getCustomerData();
        getAnimation();
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInsertPopup();
            }
        });
        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String keyword=s.toString().trim();
                if(keyword==null || keyword.isEmpty()){
                    getCustomerData();
                }else{
                    searchCustomerData(keyword);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }
    private void getAnimation(){
        Animation searchAnim= AnimationUtils.loadAnimation(search_bar.getContext(), android.R.anim.slide_in_left);
        Animation sortIconAnim= AnimationUtils.loadAnimation(sortIcon.getContext(), android.R.anim.slide_in_left);
        Animation recycleViewAnim= AnimationUtils.loadAnimation(customerRecycleView.getContext(), android.R.anim.fade_in);
        search_bar.startAnimation(searchAnim);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sortIcon.startAnimation(sortIconAnim);
            }
        },delay*0);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                customerRecycleView.startAnimation(recycleViewAnim);
            }
        },delay*3);
    }
    public void showInsertPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.admin_customer_popup, null);

        customerFirstName = dialogView.findViewById(R.id.customerFirstName);
        customerLastName=dialogView.findViewById(R.id.customerLastName);
        customerDateofBirth=dialogView.findViewById(R.id.customerDateofBirth);
        customerGender=dialogView.findViewById(R.id.customerGender);
        customerPhone=dialogView.findViewById(R.id.customerPhone);
        customerEmail=dialogView.findViewById(R.id.customerEmail);
        customerPassword=dialogView.findViewById(R.id.customerPassword);
        btnImage = dialogView.findViewById(R.id.btnImage);
        Button btnSave=dialogView.findViewById(R.id.btnSave);
        ImageButton btnClose=dialogView.findViewById(R.id.btnClose);
        btnImageError=dialogView.findViewById(R.id.btnImageError);
        calendar=dialogView.findViewById(R.id.calendar);

        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_circle_white_30));
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(dialogView);
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(customerFirstName.getText().toString().isEmpty()){
                    customerFirstName.setError("First name can not be empty");
                }else if(customerLastName.getText().toString().isEmpty()){
                    customerLastName.setError("Last name can not be empty");
                }else if(customerDateofBirth.getText().toString().isEmpty()){
                    customerDateofBirth.setError("Date of birth can not be empty");
                }else if(customerGender.getText().toString().isEmpty()){
                    customerGender.setError("Gender can not be empty");
                }else if(customerPhone.getText().toString().isEmpty()){
                    customerPhone.setError("Phone number can not be empty");
                }else if(customerEmail.getText().toString().isEmpty()){
                    customerEmail.setError("Email can not be empty");
                }else if(customerPassword.getText().toString().isEmpty()){
                    customerPassword.setError("Password can not be empty");
                }else if(uri==null){
                    btnImageError.setText("Image can not be empty");
                    btnImageError.setVisibility(View.VISIBLE);
                }else{
                    String email=customerEmail.getText().toString();
                    String password=customerPassword.getText().toString();
                    String firstname=customerFirstName.getText().toString();
                    String lastname=customerLastName.getText().toString();
                    String gender=customerGender.getText().toString();
                    String dateofbirth=customerDateofBirth.getText().toString();
                    String phone=customerPhone.getText().toString();
                    saveCustomerData(email,password,firstname,lastname,gender,dateofbirth, phone, true,false);
                }
            }
        });

        dialog.show();
    }
    public void showDatePickerDialog(View v){
        DatePickerDialog newFragment=new DatePickerDialog();
        newFragment.setTextDate(customerDateofBirth);
        newFragment.show(getActivity().getSupportFragmentManager(), "DatePicker");
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
    private void getCustomerData(){
        userList=new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference productRef = database.getReference("user");
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot productSnapshot) {
                userList.clear();
                for (DataSnapshot user : productSnapshot.getChildren()) {
                    String userKey=user.getKey();
                    String uid = user.child("uid").getValue(String.class);
                    String firstname = user.child("first_name").getValue(String.class);
                    String lastname=user.child("last_name").getValue(String.class);
                    String dateofbirth=user.child("date_of_birth").getValue(String.class);
                    String phone = user.child("phone_number").getValue(String.class);
                    String email=user.child("email").getValue(String.class);
                    int point=user.child("point").getValue(int.class);
                    String gender=user.child("gender").getValue(String.class);
                    String img=user.child("avatar_img").getValue(String.class);
                    User user1 = new User(uid,firstname,lastname,gender,dateofbirth, phone,email,point,"",img,userKey);
                    userList.add(user1);
                }
                if(userList.size()>0){
                    adminCustomerAdapter=new AdminCustomerAdapter(userList);
                    customerRecycleView.setAdapter(adminCustomerAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });
    }
    private void searchCustomerData(String keyword){
        userList=new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference productRef = database.getReference("user");
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot productSnapshot) {
                userList.clear();
                for (DataSnapshot user : productSnapshot.getChildren()) {
                    String userKey=user.getKey();
                    String uid = user.child("uid").getValue(String.class);
                    String firstname = user.child("first_name").getValue(String.class);
                    String lastname=user.child("last_name").getValue(String.class);
                    String dateofbirth=user.child("date_of_birth").getValue(String.class);
                    String phone = user.child("phone_number").getValue(String.class);
                    String email=user.child("email").getValue(String.class);
                    int point=user.child("point").getValue(int.class);
                    String gender=user.child("gender").getValue(String.class);
                    String img=user.child("avatar_img").getValue(String.class);
                    User user1 = new User(uid,firstname,lastname,gender,dateofbirth, phone,email,point,"",img,userKey);
                    userList.add(user1);
                }
                userList=filterCustomer(userList,keyword);
                if(userList.size()>0){
                    adminCustomerAdapter=new AdminCustomerAdapter(userList);
                    customerRecycleView.setAdapter(adminCustomerAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });
    }
    private List<User> filterCustomer(List<User> customerList,String keyword){
        List<User> result=new ArrayList<>();
        for(User user : customerList){
            if(user.getFirst_name().toLowerCase().contains(keyword.toLowerCase())){
                result.add(user);
            }else if(user.getLast_name().toLowerCase().contains(keyword.toLowerCase())){
                result.add(user);
            }else if(user.getEmail().toLowerCase().contains(keyword.toLowerCase())){
                result.add(user);
            }else if(user.getPhone_number().toLowerCase().contains(keyword.toLowerCase())){
                result.add(user);
            }
        }
        return result;
    }
    private void saveCustomerData(String email,String password, String firstname, String lastname,String gender,String dateofbirth, String phone, boolean isCustomer, boolean isEmployee){
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setTitle("Uploading file...");
        progressDialog.show();

        SimpleDateFormat formatter=new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss", Locale.US);
        Date now=new Date();
        String fileName= formatter.format(now);

        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isComplete()){
                            FirebaseUser user=mAuth.getCurrentUser();
                            String uid=user.getUid();

                            storage= FirebaseStorage.getInstance();
                            storageReference=storage.getReference("user/"+fileName);
                            storageReference.putFile(uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                                            while(!uriTask.isComplete());
                                            Uri urlImage=uriTask.getResult();

                                            DatabaseReference userRef=FirebaseDatabase.getInstance().getReference("user");
                                            DatabaseReference newUserRef=userRef.child(uid);

                                            User userAdd=new User(uid,firstname,lastname,gender,dateofbirth,phone,email,0,"-1",urlImage.toString(),isCustomer,isEmployee);
                                            userRef.push().setValue(userAdd);
                                            if(progressDialog.isShowing()){
                                                progressDialog.dismiss();
                                            }
                                            Toast.makeText(getContext(),"Save successfully",Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            if(progressDialog.isShowing()){
                                                progressDialog.dismiss();
                                            }
                                            Toast.makeText(getContext(),"Save failed",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }else{

                        }
                    }
                });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}