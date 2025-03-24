package com.srujal.quizappadmin;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.srujal.quizappadmin.Adapter.SubCategoryAdapter;
import com.srujal.quizappadmin.Adapter.categoryAdapter;
import com.srujal.quizappadmin.Models.SubCategoryModels;
import com.srujal.quizappadmin.Models.categoryModels;
import com.srujal.quizappadmin.databinding.ActivityMainBinding;
import com.srujal.quizappadmin.databinding.ActivitySubCategoryBinding;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SubCategoryActivity extends AppCompatActivity {

    private ActivitySubCategoryBinding binding;
    private FirebaseDatabase database;
    ProgressDialog loadingDialog;
    ArrayList<SubCategoryModels> list;
    SubCategoryAdapter adapter;

    private String categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Admin App");
        binding = ActivitySubCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        categoryId = getIntent().getStringExtra("catId");

        list = new ArrayList<>();

        loadingDialog = new ProgressDialog(this);
        loadingDialog.setTitle("");

        GridLayoutManager layoutManager = new GridLayoutManager(this,1);
        binding.recycleView.setLayoutManager(layoutManager);

        adapter = new SubCategoryAdapter(this,list);
        binding.recycleView.setAdapter(adapter);

        database.getReference().child("Categories").child(categoryId).child("subCategories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    list.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        SubCategoryModels models = dataSnapshot.getValue(SubCategoryModels.class);
                        models.setKey(dataSnapshot.getKey());
                        list.add(models);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(SubCategoryActivity.this, "No categories found", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SubCategoryActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        binding.addCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SubCategoryActivity.this,UploadActivity.class);
                intent.putExtra("catId",categoryId);
                startActivity(intent);
            }
        });
    }
}