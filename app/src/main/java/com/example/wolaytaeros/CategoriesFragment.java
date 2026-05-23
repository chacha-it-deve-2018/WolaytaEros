package com.example.wolaytaeros;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class CategoriesFragment extends Fragment {

    private RecyclerView recyclerView;
    private CategoryAdapter adapter;
    private List<Category> categoryList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // የደህንነት ማረጋገጫ (User Session Authentication)
        SharedPreferences pref = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn = pref.getBoolean("isLoggedIn", false);

        if (!isLoggedIn) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            return inflater.inflate(R.layout.fragment_home, container, false);
        }

        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        TextView title = view.findViewById(R.id.tv_category_title);
        if (title != null) { title.setSelected(true); }

        recyclerView = view.findViewById(R.id.recycler_categories);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

            categoryList = new ArrayList<>();
            addCategoriesData(); // ካቴጎሪዎችን በስርዓቱ መጫኛ ፈንክሽን

            // ተጠቃሚው ካቴጎሪ ሲነካ ወደ ዝርዝር ገጽ የሚያስተላልፍ አሰራር
            adapter = new CategoryAdapter(categoryList, category -> {
                Intent intent = new Intent(getActivity(), DetailsListActivity.class);

                // 🔥 ፍጹም ማስተካከያ፦ አፑ ምንም ቋንቋ ላይ ቢሆን ለዳታቤዙ ቋሚውን የእንግሊዝኛ ቁልፍ ይልካል!
                // ⚠️ ማሳሰቢያ፦ በ DetailsListActivity ላይ መረጃው የሚነበበው በ "category_key" መሆኑን አረጋግጥ!
                intent.putExtra("category_key", category.getDatabaseKey());

                startActivity(intent);
            });
            recyclerView.setAdapter(adapter);
        }

        return view;
    }

    private void addCategoriesData() {
        // 🔥 የ 3ኛው ቃል ማስተካከያ (Database Key Matrix)
        // ይህ 3ኛ ቃል በ XAMPP phpMyAdmin ውስጥ ካለው የሰንጠረዥ ጽሑፍ ጋር ፍጹም አንድ አይነት (Case-Sensitive) መሆን አለበት!

        categoryList.add(new Category(getString(R.string.cat_politics), R.drawable.ic_politics, "politics"));
        categoryList.add(new Category(getString(R.string.cat_food), R.drawable.ic_foods, "cultural food")); // 👈 ባህላዊ ምግቦች
        categoryList.add(new Category(getString(R.string.cat_culture), R.drawable.ic_culture, "culture"));
        categoryList.add(new Category(getString(R.string.cat_ads), R.drawable.ic_ads, "advertisement")); // 👈 ማስታወቂያ ( notice ከነበረው ወደ advertisement ተቀይሯል)
        categoryList.add(new Category(getString(R.string.cat_edu), R.drawable.ic_ed, "education"));
        categoryList.add(new Category(getString(R.string.cat_infra), R.drawable.infir, "infrastructure"));
        categoryList.add(new Category(getString(R.string.cat_nature), R.drawable.ic_nature, "nature"));
        categoryList.add(new Category(getString(R.string.cat_leader), R.drawable.ic_leader, "Leader of wolayta")); // 👈 መሪዎች (leadership ተብሏል)
        categoryList.add(new Category(getString(R.string.cat_animals), R.drawable.ic_animals, "Wild and Domestic Animal")); // 👈 እንስሳት (animals ተብሏል)
        categoryList.add(new Category(getString(R.string.cat_jobs), R.drawable.ic_jobs, "job Work"));
    }
}