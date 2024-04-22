package com.example.dukastore.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dukastore.R;
import com.example.dukastore.adapters.AddressAdapter;
import com.example.dukastore.models.AddressModel;
import com.example.dukastore.models.RecommendModel;
import com.example.dukastore.models.ShowAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class AddressActivity extends AppCompatActivity implements AddressAdapter.SelectedAddress {
    Button addAddress,paymentBtn;
    RecyclerView recyclerView;
    private List<AddressModel> addressModelList;
    private AddressAdapter addressAdapter;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
//    Toolbar toolbar;
    String mAddress="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

//        toolbar=findViewById(R.id.address_toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addAddress=findViewById(R.id.add_address_btn_2);
        paymentBtn=findViewById(R.id.payment_btn);
        firestore=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        recyclerView=findViewById(R.id.address_recycler);
        // get Data from detailed Activity
        Object obj=getIntent().getSerializableExtra("item");



        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        addressModelList=new ArrayList<>();
        addressAdapter=new AddressAdapter(getApplicationContext(),addressModelList,this);
        recyclerView.setAdapter(addressAdapter);

        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                        .collection("Address").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(DocumentSnapshot doc:task.getResult().getDocuments()){
                                AddressModel addressModel=doc.toObject(AddressModel.class);
                                addressModelList.add(addressModel);
                                addressAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });

        if(paymentBtn!=null){
            paymentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    double amount=0.0;
                    if(obj instanceof ShowAllModel){
                        ShowAllModel showAllModel=(ShowAllModel) obj;
                        amount= Double.parseDouble(showAllModel.getPrice());


                    }
                    if (obj instanceof RecommendModel) {
                        RecommendModel recommendModel = (RecommendModel) obj;
                        amount = Double.parseDouble(recommendModel.getPrice());
                    }
                    Intent intent=new Intent(AddressActivity.this,PaymentActivity.class);
                    intent.putExtra("amount",amount);
                    startActivity(intent);

                }
            });


        }




        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddressActivity.this,AddAddressActivity.class));
            }
        });
    }

    @Override
    public void setAddress(String address) {
        mAddress=address;

    }
}