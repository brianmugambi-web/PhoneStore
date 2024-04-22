package com.example.dukastore.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.dukastore.R;
import com.example.dukastore.activities.ShowAllActivity;
import com.example.dukastore.adapters.RecommendAdapter;
import com.example.dukastore.adapters.ShowAllAdapter;
import com.example.dukastore.adapters.homeAdapter;
import com.example.dukastore.models.RecommendModel;
import com.example.dukastore.models.ShowAllModel;
import com.example.dukastore.models.homeCategory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    TextView catShowAll,recommendShowAll;

    ScrollView scrollView;
    ProgressBar progressBar;


    // popular items


    RecyclerView homeCatRec,recommendRec,recyclerViewSearch;
    FirebaseFirestore db;
    FirebaseAuth auth;

    //home Category
    List<homeCategory> categoryList;
    homeAdapter homeadapter;
    //recommended

    RecommendAdapter recommendAdapter;
    List<RecommendModel> recommendModelList;




    /////////////Search view
    EditText search_box;
    private List<ShowAllModel> showAllModelList;
    private ShowAllAdapter showAllAdapter;



    private HomeFragment listener;
    public HomeFragment(){

    }

    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_home,container,false);

        auth=FirebaseAuth.getInstance();
        homeCatRec=root.findViewById(R.id.explore_rec);
        recommendRec=root.findViewById(R.id.reccomend_rec);
        scrollView=root.findViewById(R.id.scroll_id_home);
        progressBar=root.findViewById(R.id.progressbar_id_home_fragment);
        catShowAll=root.findViewById(R.id.categorie_products_see_all);
        recommendShowAll=root.findViewById(R.id.recommend_products_see_all);

        //see all on click listener

        catShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), ShowAllActivity.class);
                startActivity(intent);
            }
        });
        recommendShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), ShowAllActivity.class);
                startActivity(intent);
            }
        });








        // slider code
        ImageSlider imageSlider=root.findViewById(R.id.image_slider);
        List<SlideModel> slideModels=new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.banner1,"Discount on shoes", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.banner2,"Discount on perfume", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.banner3,"save for your christmas", ScaleTypes.CENTER_CROP));
        imageSlider.setImageList(slideModels);

        imageSlider.setItemClickListener(new ItemClickListener() {
            @Override
            public void doubleClick(int i) {

            }

            @Override
            public void onItemSelected(int position) {
                // Handle image click event based on position
                if (position == 0) {
                    navigateToShowAllActivity("kids");
                } else if (position == 1) {
                    navigateToShowAllActivity("women");
                } else if (position == 2) {
                    navigateToShowAllActivity("watch");
                }

            }
        });




        progressBar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);


        db = FirebaseFirestore.getInstance();


        //home category
        homeCatRec.setLayoutManager(new GridLayoutManager(getActivity(),3));
        categoryList=new ArrayList<>();
        homeadapter= new homeAdapter(getActivity(),categoryList);
        homeCatRec.setAdapter(homeadapter);

        db.collection("HomeCategory")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint({"RestrictedApi", "NotifyDataSetChanged"})
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                               homeCategory  homecategory =document.toObject(homeCategory.class);
                                categoryList.add(homecategory);
                            }
                            if (homeadapter != null) {
                                homeadapter.notifyDataSetChanged();
                            }
                            progressBar.setVisibility(View.GONE);
                            scrollView.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(getActivity(),"Error"+task.getException(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        //recommend section
        recommendRec.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        recommendModelList=new ArrayList<>();
        recommendAdapter= new RecommendAdapter(getActivity(),recommendModelList);
        recommendRec.setAdapter(recommendAdapter);

        db.collection("Recommend")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint({"RestrictedApi", "NotifyDataSetChanged"})
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                RecommendModel  recommendModel =document.toObject(RecommendModel.class);
                                recommendModelList.add(recommendModel);
                            }
                            if (recommendAdapter != null) {
                                recommendAdapter.notifyDataSetChanged();
                            }
                            progressBar.setVisibility(View.GONE);
                            scrollView.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(getActivity(),"Error"+task.getException(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //////////search all
        recyclerViewSearch=root.findViewById(R.id.search_rec);
        search_box=root.findViewById(R.id.search_box);
        showAllModelList=new ArrayList<>();
        showAllAdapter=new ShowAllAdapter(getContext(),showAllModelList);
        recyclerViewSearch.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewSearch.setAdapter(showAllAdapter);
        recyclerViewSearch.setHasFixedSize(true);
        search_box.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().isEmpty()){
                    showAllModelList.clear();
                    showAllAdapter.notifyDataSetChanged();
                } else {
                    searchProduct(s.toString());
                }

            }
        });




        return root;
    }

    private void searchProduct(String type) {
        if(!type.isEmpty()){
            db.collection("ShowAllCategory").whereEqualTo("type",type).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful() && task.getResult()!=null){
                                showAllModelList.clear();
                                showAllAdapter.notifyDataSetChanged();
                                for(DocumentSnapshot doc:task.getResult().getDocuments()){
                                    ShowAllModel showAllModel=doc.toObject(ShowAllModel.class);
                                    showAllModelList.add(showAllModel);
                                    showAllAdapter.notifyDataSetChanged();
                                }

                            }

                        }
                    });
        }


    }
    private void navigateToShowAllActivity(String collectionType) {
        Intent intent = new Intent(getActivity(), ShowAllActivity.class);
        intent.putExtra("collectionType", collectionType);
        startActivity(intent);
    }

}