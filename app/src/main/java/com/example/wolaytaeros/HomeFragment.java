package com.example.wolaytaeros;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * ሆም ፍራግመንት - ስለ ዎላይታ አጠቃላይ መረጃን ብቻ የሚያሳይ።
 * አሁን ከዳታቤዝ መረጃ አይጠራም፤ በ XML የተሰራውን ቋሚ መግለጫ ብቻ ይጠቀማል።
 */
public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // የላክኸውን አዲሱን ዲዛይን (fragment_home.xml) ይጠቀማል
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}