package com.example.wolaytaeros;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import java.util.Locale;

public class SettingsFragment extends Fragment {

    private TextView tvCurrentLang;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        SwitchCompat switchDarkMode = view.findViewById(R.id.switch_dark_mode);
        CardView cardLanguage = view.findViewById(R.id.card_language);
        tvCurrentLang = view.findViewById(R.id.tv_current_language);

        // ቋንቋውን ከሜሞሪ ማንበብ
        SharedPreferences prefs = getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String currentLang = prefs.getString("My_Lang", "en");
        updateLanguageLabel(currentLang);

        // Dark Mode
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            switchDarkMode.setChecked(true);
        }
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        });

        // Language Click
        cardLanguage.setOnClickListener(v -> showLanguageDialog());

        return view;
    }

    private void showLanguageDialog() {
        final String[] languages = {"Wolayttattu", "Amharic (አማርኛ)", "English"};
        final String[] langCodes = {"wal", "am", "en"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.change_lang));
        builder.setItems(languages, (dialog, which) -> {
            setLocale(langCodes[which]);
        });
        builder.show();
    }

    private void setLocale(String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());

        SharedPreferences.Editor editor = getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE).edit();
        editor.putString("My_Lang", langCode);
        editor.apply();

        getActivity().recreate();
    }

    private void updateLanguageLabel(String code) {
        if (code.equals("wal")) tvCurrentLang.setText("Wolayttattu");
        else if (code.equals("am")) tvCurrentLang.setText("አማርኛ");
        else tvCurrentLang.setText("English");
    }
}