package com.example.wolaytaeros;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import java.util.HashMap;
import java.util.Map;

public class FullDetailsActivity extends AppCompatActivity {

    private TextView fullTitle, fullDesc;
    private ImageView fullImage;
    private int postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_details);

        Toolbar toolbar = findViewById(R.id.toolbar_full);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Post Details");
        }

        fullImage = findViewById(R.id.detailImageView);
        fullTitle = findViewById(R.id.detailTitleTextView);
        fullDesc = findViewById(R.id.detailDescTextView);
        Button btnDelete = findViewById(R.id.btn_full_delete);

        // ዳታ መቀበል - Key ስሙ "image_path" መሆኑን አረጋግጥ
        postId = getIntent().getIntExtra("id", -1);
        fullTitle.setText(getIntent().getStringExtra("title"));
        fullDesc.setText(getIntent().getStringExtra("desc"));
        String finalImageUrl = getIntent().getStringExtra("image_path");

        // ፎቶውን መጫን
        Glide.with(this)
                .load(finalImageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.stat_notify_error)
                .into(fullImage);

        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete")
                    .setMessage("Are you sure you want to delete this post?")
                    .setPositiveButton("Yes", (dialog, which) -> deletePost(postId))
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    private void deletePost(int id) {
        String url = "http://10.207.21.241/wolayta_db/delete_post.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    if (response.trim().equalsIgnoreCase("success")) {
                        Toast.makeText(this, "Deleted!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                },
                error -> Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(id));
                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}