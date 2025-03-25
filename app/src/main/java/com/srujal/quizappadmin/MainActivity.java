package com.srujal.quizappadmin;

import android.app.ActionBar;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.srujal.quizappadmin.Adapter.categoryAdapter;
import com.srujal.quizappadmin.Models.categoryModels;
import com.srujal.quizappadmin.databinding.ActivityMainBinding;
import com.squareup.picasso.Picasso;

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

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseDatabase database;
    private EditText categoryName;
    private AppCompatButton uploadBtn;
    private ImageView categoryImg;
    private Dialog dialog;
    private static String IMGUR_CLIENT_ID;
    private static final int IMAGE_PICK_CODE = 15;
    private Uri selectedImageUri;
    private String uploadedImageUrl;
    ProgressDialog progressDialog;
    ArrayList<categoryModels> list;
    categoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Branches");
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();

        list = new ArrayList<>();

        IMGUR_CLIENT_ID = getString(R.string.imgur_client_iD);

        setupDialog();
        setupListeners();

        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        binding.recycleView.setLayoutManager(layoutManager);

        adapter = new categoryAdapter(this,list);
        binding.recycleView.setAdapter(adapter);

        database.getReference().child("Categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    list.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String name = dataSnapshot.child("categoryName").getValue(String.class);
                        String image = dataSnapshot.child("categoryImage").getValue(String.class);
                        String key = dataSnapshot.getKey();
                        Integer setNum = dataSnapshot.child("setNum").getValue(Integer.class);

                        // Check for null values before using them
                        if (name != null && image != null && setNum != null) {
                            list.add(new categoryModels(name, image, key, setNum));
                        } else {
                            Log.e("FirebaseError", "Skipping category with missing data: " + dataSnapshot.getKey());
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "No categories found", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.admin_add_category);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(true);
        }
        uploadBtn = dialog.findViewById(R.id.category_btn);
        categoryName = dialog.findViewById(R.id.category_name);
        categoryImg = dialog.findViewById(R.id.category_img);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.setMessage("Please Wait...");
    }

    private void setupListeners() {
        findViewById(R.id.addCategoryBtn).setOnClickListener(view -> dialog.show());

        categoryImg.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, IMAGE_PICK_CODE);
        });

        uploadBtn.setOnClickListener(view -> {
            String name = categoryName.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(this, "Enter category name", Toast.LENGTH_SHORT).show();
                return;
            }
            if (uploadedImageUrl == null) {
                Toast.makeText(this, "Wait for image upload", Toast.LENGTH_SHORT).show();
                return;
            }
            progressDialog.show();
            saveCategoryToFirebase(name, uploadedImageUrl);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            categoryImg.setImageURI(selectedImageUri);
            uploadImageToImgur();
            progressDialog.dismiss();
        }
    }

    private void uploadImageToImgur() {
        if (selectedImageUri == null) return;

        try {
            File imageFile = compressImage(selectedImageUri);
            if (imageFile == null) {
                Log.e("ImgurUpload", "Error compressing image");
                return;
            }

            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("image", imageFile.getName(),
                            RequestBody.create(imageFile, MediaType.parse("image/jpeg")))
                    .build();

            Request request = new Request.Builder()
                    .url("https://api.imgur.com/3/image")
                    .addHeader("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.e("ImgurUpload", "Upload failed: " + e.getMessage());
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show());
                    progressDialog.dismiss();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String responseBody = response.body().string();
                    Log.d("ImgurUpload", "Response: " + responseBody);

                    if (response.isSuccessful()) {
                        uploadedImageUrl = parseImageUrl(responseBody);
                        runOnUiThread(() -> {
                            Toast.makeText(MainActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                            Picasso.get().load(uploadedImageUrl).into(categoryImg);
                            progressDialog.dismiss();
                        });
                    } else {
                        Log.e("ImgurUpload", "Upload failed: " + response.code());
                        runOnUiThread(() -> Toast.makeText(MainActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show());
                        progressDialog.dismiss();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private File compressImage(Uri imageUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            File tempFile = new File(getCacheDir(), "compressedImage.jpg");
            FileOutputStream outputStream = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream); // Ensuring it's JPEG
            outputStream.flush();
            outputStream.close();
            return tempFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private String parseImageUrl(String responseBody) {
        try {
            JSONObject json = new JSONObject(responseBody);
            return json.getJSONObject("data").getString("link");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveCategoryToFirebase(String name, String imageUrl) {
        String key = database.getReference().child("Categories").push().getKey();
        database.getReference().child("Categories").child(name) // Using category name as key
                .setValue(new categoryModels(name, imageUrl,key, 0))
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(MainActivity.this, "Category Added", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    uploadedImageUrl = null;
                    progressDialog.dismiss();
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Failed to add category", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
    }
}