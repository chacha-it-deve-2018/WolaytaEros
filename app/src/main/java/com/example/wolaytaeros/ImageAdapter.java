package com.example.wolaytaeros;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private List<ImageModel> imageList;
    private Context context;

    public ImageAdapter(List<ImageModel> imageList, Context context) {
        this.imageList = imageList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 'context' ከኮንስትራክተሩ ስለመጣ እሱን መጠቀም ይቻላል
        View view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (imageList == null || imageList.isEmpty()) return;

        ImageModel model = imageList.get(position);

        // ዳታውን መሙላት
        holder.tvDesc.setText(model.getTitle());

        // ፎቶውን በ Glide መጫን
        Glide.with(context)
                .load(model.getImageUrl())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.stat_notify_error)
                .into(holder.imgPost);

        // ዝርዝሩ ሲነካ ወደ Detail ገጽ ለመሄድ
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PostDetailActivity.class);
            intent.putExtra("imageUrl", model.getImageUrl());
            intent.putExtra("title", model.getTitle());
            intent.putExtra("description", model.getDescription());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return (imageList != null) ? imageList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgPost;
        public TextView tvDesc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPost = itemView.findViewById(R.id.imgPost);
            tvDesc = itemView.findViewById(R.id.tvDesc);
        }
    }
}