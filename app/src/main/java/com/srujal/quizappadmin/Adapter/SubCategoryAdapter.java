package com.srujal.quizappadmin.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.srujal.quizappadmin.Models.SubCategoryModels;
import com.srujal.quizappadmin.Models.categoryModels;
import com.srujal.quizappadmin.QuestionsActivity;
import com.srujal.quizappadmin.R;
import com.srujal.quizappadmin.SubCategoryActivity;
import com.srujal.quizappadmin.databinding.ItemCategoryBinding;
import com.srujal.quizappadmin.databinding.SubcategorydesignBinding;

import java.util.ArrayList;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.viewHolder> {

    Context context;
    ArrayList<SubCategoryModels> list;
    private String catId;
    private String subCatId;

    public SubCategoryAdapter(Context context, ArrayList<SubCategoryModels> list, String catId) {
        this.context = context;
        this.list = list;
        this.catId = catId;
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, QuestionsActivity.class);
                intent.putExtra("catId",catId);
                intent.putExtra("subCatId",models.getKey());
                context.startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder= new AlertDialog.Builder(context);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure, you want to delete this Subject");

                builder.setPositiveButton("Yes",(dialogInterface, i) -> {
                    FirebaseDatabase.getInstance().getReference().child("Categories").child(catId).child("subCategories").child(models.getKey())
                            .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                public void onSuccess(Void unused) {
                                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                }
                            });
                });
                builder.setNegativeButton("No",(dialogInterface, i) -> {
                    dialogInterface.cancel();
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                return true;
            }
        });


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
