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
import com.srujal.quizappadmin.Models.QuestionsModel;
import com.srujal.quizappadmin.Models.SubCategoryModels;
import com.srujal.quizappadmin.QuestionsActivity;
import com.srujal.quizappadmin.R;
import com.srujal.quizappadmin.databinding.SubcategorydesignBinding;

import java.util.ArrayList;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.viewHolder> {

    Context context;
    ArrayList<QuestionsModel> list;
    private String catId;
    private String subCatId;

    public QuestionsAdapter(Context context, ArrayList<QuestionsModel> list, String catId, String subCatId) {
        this.context = context;
        this.list = list;
        this.catId = catId;
        this.subCatId = subCatId;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.subcategorydesign, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        QuestionsModel models = list.get(position);

        holder.binding.tvsubject.setText(models.getQuestion());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder= new AlertDialog.Builder(context);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure, you want to delete this Question");

                builder.setPositiveButton("Yes",(dialogInterface, i) -> {
                    FirebaseDatabase.getInstance().getReference().child("Categories").child(catId).child("subCategories").child(subCatId).child("questions").
                            child(models.getKey())
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
