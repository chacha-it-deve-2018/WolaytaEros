package com.example.wolaytaeros;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

public class PostDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        ImageView detailImage = findViewById(R.id.detailImageView);
        TextView detailTitle = findViewById(R.id.detailTitleView);
        TextView detailDesc = findViewById(R.id.detailDescriptionView);

        // ዳታ መቀበያ
        String title = getIntent().getStringExtra("pTitle");
        String description = getIntent().getStringExtra("pDesc");
        String imageUrl = getIntent().getStringExtra("pImage");

        if (title != null) detailTitle.setText(title);
        if (description != null) detailDesc.setText(description);

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this).load(imageUrl).into(detailImage);
        }
    }
}