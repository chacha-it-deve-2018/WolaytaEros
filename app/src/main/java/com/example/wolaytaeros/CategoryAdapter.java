package com.example.wolaytaeros;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<Category> categories;
    private OnCategoryClickListener listener;

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }

    public CategoryAdapter(List<Category> categories, OnCategoryClickListener listener) {
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // item_category.xml ፋይልን ይጠራዋል
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categories.get(position);

        if (category != null) {
            holder.nameText.setText(category.getName());

            // ምስሉ በ drawable ውስጥ መኖሩን ቼክ እያደረገ ይጭናል
            try {
                if (category.getImageRes() != 0) {
                    holder.iconImage.setImageResource(category.getImageRes());
                } else {
                    // ምስል ከሌለ default ምስል እንዲጠቀም
                    holder.iconImage.setImageResource(R.drawable.ic_launcher_background);
                }
            } catch (Exception e) {
                holder.iconImage.setImageResource(R.drawable.ic_launcher_background);
            }

            // ካርዱ ሲነካ ወደ ተመረጠው ካቴጎሪ ይወስዳል
            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCategoryClick(category);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return categories != null ? categories.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iconImage;
        TextView nameText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iconImage = itemView.findViewById(R.id.categoryIcon);
            nameText = itemView.findViewById(R.id.categoryName);
        }
    }
}