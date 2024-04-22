package com.example.dukastore.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dukastore.R;
import com.example.dukastore.activities.PaymentActivity;
import com.example.dukastore.adapters.MyCartAdapter;
import com.example.dukastore.models.MyCartModel;
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


public class cartFragment extends Fragment {
    FirebaseFirestore db;
    FirebaseAuth auth;


    RecyclerView recyclerView;
    MyCartAdapter cartAdapter;
    List<MyCartModel> cartModelList;

    TextView overAllTotalAmount;

    Button  btn_buy;
    RecommendModel recommendModel=null;
    //show All
    ShowAllModel showAllModel=null;

    private int totalBill;


    public cartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_cart, container, false);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        overAllTotalAmount=root.findViewById(R.id.textview_cart_total);
        btn_buy=root.findViewById(R.id.add_to_cart_buy);



//get data from my cart adapter
        LocalBroadcastManager.getInstance(requireContext())
                .registerReceiver(mMessageReceiver, new IntentFilter("MyTotalAmount"));


        //cart rec
        recyclerView = root.findViewById(R.id.cart_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        cartModelList = new ArrayList<>();
        cartAdapter = new MyCartAdapter(requireContext(), cartModelList);
        recyclerView.setAdapter(cartAdapter);

        db.collection("AddToCart1").document(auth.getCurrentUser().getUid())
                .collection("User").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {

                                String documentId=documentSnapshot.getId();
                                MyCartModel cartModel = documentSnapshot.toObject(MyCartModel.class);

                                cartModel.setDocumentId(documentId);

                                cartModelList.add(cartModel);
                                cartAdapter.notifyDataSetChanged(); // Refresh RecyclerView
                            }

                        }

                    }
                });

        if(btn_buy!=null){
            btn_buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(requireActivity(), PaymentActivity.class);
                    intent.putExtra("totalBill",String.valueOf(totalBill));
                    startActivity(intent);

                }
            });

        }else{
            Log.e("cartFragment", "Button is null");
        }





        return root;
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            totalBill = intent.getIntExtra("totalAmount", 0);
            overAllTotalAmount.setText("Total Amount Ksh:"+totalBill);


        }
    };

}