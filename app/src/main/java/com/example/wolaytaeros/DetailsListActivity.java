package com.example.wolaytaeros;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailsListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private List<NewsModel> newsList;
    private String selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_list);

        Toolbar toolbar = findViewById(R.id.toolbar_list);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.menu_category));
        }

        selectedCategory = getIntent().getStringExtra("category_key");
        recyclerView = findViewById(R.id.recyclerViewDetails);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        newsList = new ArrayList<>();
        adapter = new NewsAdapter(newsList, this);
        recyclerView.setAdapter(adapter);

        if (selectedCategory != null) {
            loadDataFromXAMPP(selectedCategory);
        }
    }

    public void loadDataFromXAMPP(String category) {
        String url = "http://10.207.21.241/wolayta_db/get_posts_by_category.php?category=" + category;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    newsList.clear();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);
                            NewsModel model = new NewsModel();
                            model.setId(obj.getInt("id"));
                            model.setTitle(obj.getString("title"));
                            model.setDescription(obj.getString("description"));
                            model.setImagePath(obj.getString("image_path"));
                            newsList.add(model);
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) { e.printStackTrace(); }
                },
                error -> Toast.makeText(this, "Network Error!", Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(request);
    }

    public void deletePost(int postId) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.btn_delete))
                .setMessage(getString(R.string.confirm_delete))
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                    // Space የተስተካከለበት URL
                    String url = "http://10.207.21.241/wolayta_db/delete_post.php";
                    StringRequest request = new StringRequest(Request.Method.POST, url,
                            response -> {
                                if (response.trim().equalsIgnoreCase("success")) {
                                    Toast.makeText(this, getString(R.string.deleted_success), Toast.LENGTH_SHORT).show();
                                    loadDataFromXAMPP(selectedCategory);
                                } else {
                                    Toast.makeText(this, "Server Response: " + response, Toast.LENGTH_SHORT).show();
                                }
                            },
                            error -> Toast.makeText(this, "Volley Error!", Toast.LENGTH_SHORT).show()) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("id", String.valueOf(postId));
                            return params;
                        }
                    };
                    Volley.newRequestQueue(this).add(request);
                })
                .setNegativeButton(getString(R.string.no), null)
                .show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}