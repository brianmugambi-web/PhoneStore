package com.example.dukastore.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dukastore.R;
import com.example.dukastore.activities.DetailedAcitivity;
import com.example.dukastore.activities.ShowAllActivity;
import com.example.dukastore.models.homeCategory;

import java.util.List;

public class homeAdapter extends RecyclerView.Adapter<homeAdapter.ViewHolder> {
    Context context;
    List<homeCategory> CategoryList;

    public homeAdapter(Context context, List<homeCategory> categoryList) {
        this.context = context;
        CategoryList = categoryList;
    }

    @NonNull
    @Override
    public homeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_cat_images,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull homeAdapter.ViewHolder holder, int position) {

        Glide.with(context).load(CategoryList.get(position).getImg_url()).into(holder.catImg);
        holder.name.setText(CategoryList.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ShowAllActivity.class);
                final int currentPosition = holder.getAdapterPosition();
                intent.putExtra("type", CategoryList.get(currentPosition).getType());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return CategoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView catImg;
        TextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            catImg=itemView.findViewById(R.id.home_cat_img);
            name=itemView.findViewById(R.id.cat_img_name);

        }
    }
}
