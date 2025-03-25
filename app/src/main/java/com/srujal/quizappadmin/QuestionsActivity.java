package com.srujal.quizappadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.srujal.quizappadmin.Adapter.QuestionsAdapter;
import com.srujal.quizappadmin.Adapter.SubCategoryAdapter;
import com.srujal.quizappadmin.Models.QuestionsModel;
import com.srujal.quizappadmin.Models.SubCategoryModels;
import com.srujal.quizappadmin.databinding.ActivityQuestionsBinding;
import com.srujal.quizappadmin.databinding.ActivitySubCategoryBinding;

import java.util.ArrayList;

public class QuestionsActivity extends AppCompatActivity {

    ActivityQuestionsBinding binding;
    private FirebaseDatabase database;
    ProgressDialog loadingDialog;
    ArrayList<QuestionsModel> list;
    QuestionsAdapter adapter;
    private String categoryId,subCatId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Subjects");
        binding = ActivityQuestionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        categoryId = getIntent().getStringExtra("catId");
        subCatId = getIntent().getStringExtra("subCatId");

        list = new ArrayList<>();

        loadingDialog = new ProgressDialog(this);
        loadingDialog.setTitle("");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recycleView.setLayoutManager(layoutManager);

        adapter = new QuestionsAdapter(this,list,categoryId,subCatId);
        binding.recycleView.setAdapter(adapter);

        database.getReference().child("Categories").child(categoryId).child("subCategories").child(subCatId)
                .child("questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    list.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        QuestionsModel models = dataSnapshot.getValue(QuestionsModel.class);
                        models.setKey(dataSnapshot.getKey());
                        list.add(models);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(QuestionsActivity.this, "No categories found", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QuestionsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        binding.addCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuestionsActivity.this,UploadQuestionsActivity.class);
                intent.putExtra("catId",categoryId);
                intent.putExtra("subCatId",subCatId);
                startActivity(intent);
            }
        });
    }
}