package com.example.wolaytaeros;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AccountFragment extends Fragment {
    private Button logoutBtn, goToLoginBtn;
    private TextView userEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        logoutBtn = view.findViewById(R.id.btnLogout);
        goToLoginBtn = view.findViewById(R.id.btnGoToLogin);
        userEmail = view.findViewById(R.id.txtUserEmail);

        SharedPreferences pref = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn = pref.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            String email = pref.getString("email", "ኢሜይል አልተገኘም");
            userEmail.setText(email);
            logoutBtn.setVisibility(View.VISIBLE);
            goToLoginBtn.setVisibility(View.GONE);
        } else {
            userEmail.setText("እባክዎ መጀመሪያ ይግቡ");
            logoutBtn.setVisibility(View.GONE);
            goToLoginBtn.setVisibility(View.VISIBLE);
        }

        // Logout ሲደረግ
        logoutBtn.setOnClickListener(v -> {
            SharedPreferences.Editor editor = pref.edit();
            editor.clear(); // 1. የ SharedPreferences ዳታን ሙሉ በሙሉ ማጥፋት
            editor.apply();

            // ከዚህ ፍራግመንት መውጣቱን የሚገልጸውን ቶስት እዚህ ማሳየት አያስፈልግም፣
            // ምክንያቱም ቀጥታ ወደ LoginActivity ስለሚሄድ መልዕክቱን እዚያ ያየዋል።

            // 2. ✅ የደህንነት ማረጋገጫ፡ ሙሉ የ Activity ታሪክን (Backstack) መዝጋት
            Intent intent = new Intent(getActivity(), LoginActivity.class);


            intent.putExtra("show_logout_toast", true);
            // =========================================================================

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        // Login ገጽ ለመሄድ
        goToLoginBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        });

        return view;
    }
}