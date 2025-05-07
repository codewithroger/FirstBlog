package com.example.firstblog;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class NewPostActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView postImageView;
    private Button selectImageButton, uploadButton;
    private EditText postTitleInput, postContentInput;
    private Uri imageUri;
    private Cloudinary cloudinary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);  // Make sure the layout file name matches

        postImageView = findViewById(R.id.post_image);
        selectImageButton = findViewById(R.id.select_image_button);
        uploadButton = findViewById(R.id.upload_button);
        postTitleInput = findViewById(R.id.post_title_input);
        postContentInput = findViewById(R.id.post_content_input);

        // Initialize Cloudinary
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", getString(R.string.cloudinary_cloud_name),
                "api_key", getString(R.string.cloudinary_api_key),
                "api_secret", getString(R.string.cloudinary_api_secret)
        ));

        // Select image when button is clicked
        selectImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        // Handle post upload when upload button is clicked
        uploadButton.setOnClickListener(v -> uploadPost());
    }

    // Handle image selection result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_REQUEST) {
            imageUri = data.getData();
            postImageView.setImageURI(imageUri);  // Display the selected image
        }
    }

    private void uploadPost() {
        String title = postTitleInput.getText().toString().trim();
        String content = postContentInput.getText().toString().trim();

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Please enter a title and content", Toast.LENGTH_SHORT).show();
            return;
        }

        if (imageUri != null) {
            File imageFile = new File(getRealPathFromURI(imageUri));

            // Upload image to Cloudinary in background thread
            new Thread(() -> {
                try {
                    // Upload image to Cloudinary
                    Map uploadResult = cloudinary.uploader().upload(imageFile, ObjectUtils.emptyMap());
                        String imageUrl = (String) uploadResult.get("CLOUDINARY_URL=cloudinary://271153139714856:ZLHc01ut_WFzuvyuJZZSOeVb_TQ@ddaaj9mgz");

                    // Create post object with uploaded image URL
                    Post post = new Post(title, content, imageUrl);

                    // Save the post (to Firebase or any other database)
                    // Example:
                    // FirebaseDatabase.getInstance().getReference().child("Posts").push().setValue(post);
                    runOnUiThread(() -> {
                        Toast.makeText(NewPostActivity.this, "Post uploaded successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(NewPostActivity.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }
            }).start();
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String path = cursor.getString(columnIndex);
            cursor.close();
            return path;
        }
        return null;
    }
}
