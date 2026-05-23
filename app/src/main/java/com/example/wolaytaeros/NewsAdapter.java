package com.example.wolaytaeros;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<NewsModel> newsList;
    private Context context;

    public NewsAdapter(List<NewsModel> newsList, Context context) {
        this.newsList = newsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 1. ለአስተማማኝ አሠራር context እዚህ ጋር ከ parent ይወሰዳል
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (newsList == null || newsList.isEmpty()) return;

        NewsModel model = newsList.get(position);

        holder.title.setText(model.getTitle());
        holder.description.setText(model.getDescription());

        String path = model.getImagePath();
        String finalImageUrl;

        if (path != null && path.startsWith("http")) {
            finalImageUrl = path;
        } else {
            finalImageUrl = "http://10.207.21.241/wolayta_db/uploads/" + path;
        }

        Glide.with(context)
                .load(finalImageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.stat_notify_error)
                .into(holder.image);

        // ዝርዝሩ ሲነካ ወደ ዝርዝር ማሳያ ገጽ ለመሄድ
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, FullDetailsActivity.class);
            intent.putExtra("id", model.getId());
            intent.putExtra("title", model.getTitle());
            intent.putExtra("desc", model.getDescription());
            intent.putExtra("image_path", finalImageUrl);
            context.startActivity(intent);
        });

        // ፖስቱን ለመሰረዝ
        holder.btnDelete.setOnClickListener(v -> {
            Context btnContext = v.getContext();
            if (btnContext instanceof DetailsListActivity) {
                ((DetailsListActivity) btnContext).deletePost(model.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsList != null ? newsList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;
        ImageView image;
        ImageButton btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.newsTitleTextView);
            description = itemView.findViewById(R.id.newsDescTextView);
            image = itemView.findViewById(R.id.newsImageView);
            btnDelete = itemView.findViewById(R.id.btn_delete_item);
        }
    }
}