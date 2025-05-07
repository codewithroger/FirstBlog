package com.example.firstblog;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

public class PostViewHolder extends RecyclerView.ViewHolder {
    TextView titleTextView, contentTextView;
    ImageView imageView;

    public PostViewHolder(View itemView) {
        super(itemView);
        titleTextView = itemView.findViewById(R.id.post_title);
        contentTextView = itemView.findViewById(R.id.post_content);
        imageView = itemView.findViewById(R.id.post_image_view);
    }

    // Method to bind title
    public void setTitle(String title) {
        titleTextView.setText(title);
    }

    // Method to bind content
    public void setContent(String content) {
        contentTextView.setText(content);
    }

    // Method to load image
    public void setImage(String imageUrl) {
        Glide.with(itemView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.placeholder_image) // Optional: Use placeholder
                .into(imageView);
    }
}
