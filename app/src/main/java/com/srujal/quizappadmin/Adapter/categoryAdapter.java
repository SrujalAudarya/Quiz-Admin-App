package com.srujal.quizappadmin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.srujal.quizappadmin.Models.categoryModels;
import com.srujal.quizappadmin.R;
import com.srujal.quizappadmin.databinding.ItemCategoryBinding;

import java.util.ArrayList;

public class categoryAdapter extends RecyclerView.Adapter<categoryAdapter.viewHolder>{

    Context context;
    ArrayList<categoryModels> list;

    public categoryAdapter(Context context, ArrayList<categoryModels> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        categoryModels models = list.get(position);
        holder.binding.categoryName.setText(models.getCategoryName());

        Picasso.get()
                .load(models.getCategoryImage())
                .placeholder(R.drawable.placeholder)
                .into(holder.binding.categoryImg);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(context, )
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        ItemCategoryBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding = ItemCategoryBinding.bind(itemView);

        }
    }

}
