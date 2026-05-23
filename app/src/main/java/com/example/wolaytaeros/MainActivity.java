package com.example.wolaytaeros;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Toolbar Setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 2. Navigation Drawer Setup
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        // 3. Header Setup (User Info)
        View headerView = navigationView.getHeaderView(0);
        TextView userName = headerView.findViewById(R.id.nav_user_name);
        TextView userEmail = headerView.findViewById(R.id.nav_user_email);
        TextView userPhone = headerView.findViewById(R.id.nav_user_phone);

        if (userName != null) userName.setText("IT section 2 group five");
        if (userEmail != null) userEmail.setText("chalachewbelay43@gmail.com");
        if (userPhone != null) userPhone.setText("0919961315 / 0991011038");

        // 4. Toggle Button
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // 5. Bottom Navigation Setup
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setItemIconTintList(null);

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int id = item.getItemId();

            if (id == R.id.nav_home) selectedFragment = new HomeFragment();
            else if (id == R.id.nav_categories) selectedFragment = new CategoriesFragment();
            else if (id == R.id.nav_upload) selectedFragment = new UploadFragment();
            else if (id == R.id.nav_account) selectedFragment = new AccountFragment();
            else if (id == R.id.nav_logout) { logoutUser(); return true; }

            if (selectedFragment != null) loadFragment(selectedFragment);
            return true;
        });

        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    // 🔥 🚨 የደህንነት አልጎሪዝም፡ ተጠቃሚው አፑን ተመልሶ በከፈተ ቁጥር የጊዜ ገደቡን ያረጋግጣል
    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        boolean isLoggedIn = pref.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            long lastActiveTime = pref.getLong("lastActiveTime", 0);
            long currentTime = System.currentTimeMillis(); // የአሁኑን ሰዓት በ ሚሊሰከንድ መውሰድ

            // ⏰ የሴሽን የጊዜ ገደብ መወሰኛ (15 ደቂቃ = 15 * 60 * 1000 ሚሊሰከንድ)
            long sessionTimeout = 1 * 60 * 1000;

            if (lastActiveTime != 0 && (currentTime - lastActiveTime > sessionTimeout)) {
                // 🚨 ሰዓቱ አልፏል! ዳታውን አጽዳና በራስ-ሰር አስወጣው
                pref.edit().clear().apply();

                Toast.makeText(this, "የሴሽን ጊዜዎ አልቋል! እባክዎ እንደገና ይግቡ።", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                // ✅ ተጠቃሚው አሁንም በሰዓቱ ውስጥ ከሆነ ሰዓቱን እናድሳለን (Update)
                pref.edit().putLong("lastActiveTime", currentTime).apply();
            }
        }
    }

    // ተጠቃሚው አፑ ውስጥ አንድ ገጽ ሲቀይር ወይም ሲነካ ሰዓቱን ያድሳል
    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        if (pref.getBoolean("isLoggedIn", false)) {
            pref.edit().putLong("lastActiveTime", System.currentTimeMillis()).apply();
        }
    }

    // 6. Side Menu Click Events
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            loadFragment(new HomeFragment());
        }
        else if (id == R.id.nav_settings) {
            loadFragment(new SettingsFragment());
        }
        else if (id == R.id.nav_share) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String shareMessage = getString(R.string.app_name) + " App\nLink: https://play.google.com/store/apps/details?id=" + getPackageName();
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        }
        else if (id == R.id.nav_contact) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:0919961315"));
            startActivity(intent);
        }
        else if (id == R.id.nav_about) {
            String aboutDesc = getString(R.string.menu_about) + "\nDeveloped by: WSU IT Students\nSection: 2, Group: 5\n\nContacts:\n0919961315 / 0991011038\nEmail: chalachewbelay43@gmail.com";
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.menu_about))
                    .setMessage(aboutDesc)
                    .setPositiveButton(getString(R.string.yes), null)
                    .show();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void logoutUser() {
        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        pref.edit().clear().apply();
        Toast.makeText(this, getString(R.string.deleted_success), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}