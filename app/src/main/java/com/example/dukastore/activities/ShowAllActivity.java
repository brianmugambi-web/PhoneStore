package com.example.dukastore.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dukastore.R;
import com.example.dukastore.adapters.ShowAllAdapter;
import com.example.dukastore.models.ShowAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ShowAllActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ShowAllAdapter showAllAdapter;
    List<ShowAllModel> showAllModelList;

    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all);

        String type=getIntent().getStringExtra("type");



        firestore=FirebaseFirestore.getInstance();
        recyclerView=findViewById(R.id.show_all_rec);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        showAllModelList=new ArrayList<>();
        showAllAdapter=new ShowAllAdapter(this,showAllModelList);
        recyclerView.setAdapter(showAllAdapter);

        String collectionType = getIntent().getStringExtra("collectionType");


        if(type==null || type.isEmpty()){
            firestore.collection("ShowAllCategory")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(DocumentSnapshot doc: task.getResult().getDocuments()){
                                    ShowAllModel showAllModel = doc.toObject(ShowAllModel.class);
                                    showAllModelList.add(showAllModel);

                                }
                                showAllAdapter.notifyDataSetChanged();
                            }

                        }
                    });

        }
        if(type!=null && type.equalsIgnoreCase("men")){
            firestore.collection("ShowAllCategory").whereEqualTo("type","men")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(DocumentSnapshot doc: task.getResult().getDocuments()){
                                    ShowAllModel showAllModel = doc.toObject(ShowAllModel.class);
                                    showAllModelList.add(showAllModel);

                                }
                                showAllAdapter.notifyDataSetChanged();
                            }

                        }
                    });
        }





        if(type!=null && type.equalsIgnoreCase("woman")){
            firestore.collection("ShowAllCategory").whereEqualTo("type","woman")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(DocumentSnapshot doc: task.getResult().getDocuments()){
                                    ShowAllModel showAllModel = doc.toObject(ShowAllModel.class);
                                    showAllModelList.add(showAllModel);

                                }
                                showAllAdapter.notifyDataSetChanged();
                            }

                        }
                    });
        }
        if(type!=null && type.equalsIgnoreCase("watch")){
            firestore.collection("ShowAllCategory").whereEqualTo("type","watch")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(DocumentSnapshot doc: task.getResult().getDocuments()){
                                    ShowAllModel showAllModel = doc.toObject(ShowAllModel.class);
                                    showAllModelList.add(showAllModel);

                                }
                                showAllAdapter.notifyDataSetChanged();
                            }

                        }
                    });
        }
        if(type!=null && type.equalsIgnoreCase("kids")){
            firestore.collection("ShowAllCategory").whereEqualTo("type","kids")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(DocumentSnapshot doc: task.getResult().getDocuments()){
                                    ShowAllModel showAllModel = doc.toObject(ShowAllModel.class);
                                    showAllModelList.add(showAllModel);

                                }
                                showAllAdapter.notifyDataSetChanged();
                            }

                        }
                    });
        }
        if(type!=null && type.equalsIgnoreCase("shoes")){
            firestore.collection("ShowAllCategory").whereEqualTo("type","shoes")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(DocumentSnapshot doc: task.getResult().getDocuments()){
                                    ShowAllModel showAllModel = doc.toObject(ShowAllModel.class);
                                    showAllModelList.add(showAllModel);

                                }
                                showAllAdapter.notifyDataSetChanged();
                            }

                        }
                    });
        }
        if(type!=null && type.equalsIgnoreCase("camera")){
            firestore.collection("ShowAllCategory").whereEqualTo("type","camera")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(DocumentSnapshot doc: task.getResult().getDocuments()){
                                    ShowAllModel showAllModel = doc.toObject(ShowAllModel.class);
                                    showAllModelList.add(showAllModel);

                                }
                                showAllAdapter.notifyDataSetChanged();
                            }

                        }
                    });
        }


        if (collectionType != null && !collectionType.isEmpty()) {
            // Query Firestore based on collectionType
            firestore.collection("ShowAllCategory").whereEqualTo("type", collectionType)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                                    ShowAllModel showAllModel = doc.toObject(ShowAllModel.class);
                                    showAllModelList.add(showAllModel);
                                }
                                showAllAdapter.notifyDataSetChanged();
                            } else {
                                // Handle Firestore query error
                                Toast.makeText(ShowAllActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }
}