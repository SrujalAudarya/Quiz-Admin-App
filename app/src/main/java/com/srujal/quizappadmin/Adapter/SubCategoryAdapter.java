package com.srujal.quizappadmin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.srujal.quizappadmin.Models.SubCategoryModels;
import com.srujal.quizappadmin.Models.categoryModels;
import com.srujal.quizappadmin.R;
import com.srujal.quizappadmin.databinding.ItemCategoryBinding;
import com.srujal.quizappadmin.databinding.SubcategorydesignBinding;

import java.util.ArrayList;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.viewHolder> {

    Context context;
    ArrayList<SubCategoryModels> list;

    public SubCategoryAdapter(Context context, ArrayList<SubCategoryModels> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.subcategorydesign, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        SubCategoryModels models = list.get(position);

        holder.binding.tvsubject.setText(models.getCategoryName());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        SubcategorydesignBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SubcategorydesignBinding.bind(itemView);
        }
    }
}
