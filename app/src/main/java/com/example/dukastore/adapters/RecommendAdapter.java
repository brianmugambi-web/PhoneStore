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
import com.example.dukastore.models.RecommendModel;

import java.util.List;

public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.viewHolder> {
    Context context;
    List<RecommendModel> list;

    public RecommendAdapter(Context context, List<RecommendModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecommendAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recommeded_items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendAdapter.viewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getImg_url()).into(holder.imageView);
        holder.name.setText(list.get(position).getName());
        holder.price.setText(list.get(position).getPrice());
        holder.desc.setText(list.get(position).getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, DetailedAcitivity.class);
                final int currentPosition = holder.getAdapterPosition();
                intent.putExtra("detailed", list.get(currentPosition));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name,price,desc;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.rec_img);
            name=itemView.findViewById(R.id.rec_name);
            price=itemView.findViewById(R.id.rec_price);
            desc=itemView.findViewById(R.id.rec_description);

        }
    }
}
