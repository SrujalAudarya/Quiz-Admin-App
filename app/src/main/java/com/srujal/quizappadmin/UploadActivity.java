package com.srujal.quizappadmin;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.srujal.quizappadmin.Models.SubCategoryModels;
import com.srujal.quizappadmin.databinding.ActivityUploadBinding;

public class UploadActivity extends AppCompatActivity {

    ActivityUploadBinding binding;
    FirebaseDatabase database;
    private String categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        categoryId = getIntent().getStringExtra("catId");

        binding.btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 String subCatName = binding.etSubjectName.getText().toString();

                 if (subCatName.isEmpty()){
                     binding.etSubjectName.setError("Enter Subject Name");
                 }else {
                     storeData(subCatName);
                 }
            }
        });
    }

    private void storeData(String subCatName) {

        SubCategoryModels models = new SubCategoryModels(subCatName);
        database.getReference().child("Categories").child(categoryId).child("subCategories")
                .push()
                .setValue(models).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(UploadActivity.this, "data uploaded", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UploadActivity.this,e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}