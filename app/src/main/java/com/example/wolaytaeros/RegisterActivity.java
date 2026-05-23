package com.example.wolaytaeros;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns; // 🔥 የኢሜይል ፎርማት ለመፈተሽ ያስፈልጋል
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText emailEdit, passEdit;
    private Button regBtn;
    private TextView toLoginTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // View-ዎችን ማገናኘት
        emailEdit = findViewById(R.id.regEmail);
        passEdit = findViewById(R.id.regPassword);
        regBtn = findViewById(R.id.btnRegister);
        toLoginTxt = findViewById(R.id.txtToLogin);

        // "አካውንት አለህ? ግባ" የሚለው ሲነካ
        toLoginTxt.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        // የምዝገባ በተኑ ሲነካ
        regBtn.setOnClickListener(v -> {
            String email = emailEdit.getText().toString().trim();
            String pass = passEdit.getText().toString().trim();

            // 1. መረጃው ባዶ አለመሆኑን ማረጋገጥ
            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "ሁሉንም መረጃ ያስገቡ", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "እባክዎ ትክክለኛ የኢሜይል አድራሻ ያስገቡ (e.g. @gmail.com)", Toast.LENGTH_LONG).show();
                return;
            }


            if (pass.length() < 4) {
                Toast.makeText(this, "የይለፍ ቃል ርዝመት ቢያንስ 4 ፊደላት መሆን አለበት!", Toast.LENGTH_LONG).show();
                return;
            }
            // =========================================================================

            String url = "http://10.207.21.241/wolayta_db/register_user.php";
            StringRequest request = new StringRequest(Request.Method.POST, url,
                    response -> {
                        if(response.trim().equals("success")) {
                            Toast.makeText(this, "ምዝገባ ተሳክቷል! አሁን ይግቡ", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(this, "ምዝገባ አልተሳካም፡ " + response, Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> Toast.makeText(this, "የኔትወርክ ስህተት፡ " + error.getMessage(), Toast.LENGTH_SHORT).show()){
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
}