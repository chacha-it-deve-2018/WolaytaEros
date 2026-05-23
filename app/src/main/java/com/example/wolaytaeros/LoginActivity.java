package com.example.wolaytaeros;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer; // 🔥 ለሰዓት መቆጠሪያ አስፈላጊ ነው
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEdit, passEdit;
    private Button loginBtn;
    private TextView btnGoToRegister;

    // 🔥 የደህንነት ተለዋዋጮች (Security Variables)
    private int loginAttempts = 0; // የተሳሳቱ ሙከራዎችን ለመቁጠር
    private final int MAX_ATTEMPTS = 3; // ከፍተኛው የተፈቀደ ሙከራ (3 ጊዜ)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEdit = findViewById(R.id.loginEmail);
        passEdit = findViewById(R.id.loginPassword);
        loginBtn = findViewById(R.id.btnLogin);
        btnGoToRegister = findViewById(R.id.btnGoToRegister);

        // =========================================================================
        // 🔥 🚨 አዲስ የተጨመረ የደህንነት መቆጣጠሪያ (Check Intent for Logout Success Notification)
        // ተጠቃሚው Logout ብሎ ከሌላ ገጽ ሲመጣ መልዕክቱን እዚህ ጋር ተቀብሎ ያሳያል
        if (getIntent() != null && getIntent().getBooleanExtra("show_logout_toast", false)) {
            Toast.makeText(this, "Logout successful", Toast.LENGTH_SHORT).show();
        }
        // =========================================================================

        btnGoToRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        loginBtn.setOnClickListener(v -> {
            String email = emailEdit.getText().toString().trim();
            String pass = passEdit.getText().toString().trim();

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "እባክዎ ሁሉንም ይሙሉ", Toast.LENGTH_SHORT).show();
                return;
            }

            String url = "http://10.207.21.241/wolayta_db/login_user.php";
            StringRequest request = new StringRequest(Request.Method.POST, url,
                    response -> {
                        if(response.trim().equalsIgnoreCase("success")) {
                            // ✅ መግባት ከተሳካ ቆጣሪውን ወደ 0 እንመልሰዋለን
                            loginAttempts = 0;

                            SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("email", email);
                            editor.putBoolean("isLoggedIn", true);
                            editor.apply();

                            Toast.makeText(this, "እንኳን ደህና መጡ!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, MainActivity.class));
                            finish();
                        } else {
                            // ❌ ከተሳሳተ የሙከራ ቁጥሩን በአንድ እንጨምራለን
                            loginAttempts++;

                            // 🚨 ከተፈቀደው በላይ ከተሳሳተ አፑን እንቆልፋለን
                            if (loginAttempts >= MAX_ATTEMPTS) {
                                startLockoutTimer();
                            } else {
                                int remaining = MAX_ATTEMPTS - loginAttempts;
                                Toast.makeText(this, "email or password erorr. remaining attempts: " + remaining, Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    error -> Toast.makeText(this, "network eror፡ " + error.getMessage(), Toast.LENGTH_SHORT).show()){
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", email);
                    params.put("password", pass);
                    return params;
                }
            };
            Volley.newRequestQueue(this).add(request);
        });
    }

    // 🔥 አፑን ለጊዜው የመቆለፊያ አልጎሪዝም (Lockout Mechanism)
    private void startLockoutTimer() {
        loginBtn.setEnabled(false); // ቁልፉ እንዳይነካ ማጥፋት
        emailEdit.setEnabled(false); // የፅሁፍ ሳጥኑን መቆለፍ
        passEdit.setEnabled(false); // የፅሁፍ ሳጥኑን መቆለፍ

        // ለ30 ሰከንድ (30000 ሚሊሰከንድ) በየሴኮንዱ (1000 ሚሊሰከንድ) የሚቆጥር
        new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // በየሴኮንዱ የተረፈውን ሰዓት ቁልፉ ላይ ያሳያል
                loginBtn.setText("closd (" + (millisUntilFinished / 1000) + " second) pleas enter corect username and password");
            }

            @Override
            public void onFinish() {
                // ሰዓቱ ሲያልቅ ሁሉንም መልሶ ይከፍታል
                loginAttempts = 0;
                loginBtn.setEnabled(true);
                emailEdit.setEnabled(true);
                passEdit.setEnabled(true);
                loginBtn.setText("ግባ (Login)");
                Toast.makeText(LoginActivity.this, "pleas try again", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }
}