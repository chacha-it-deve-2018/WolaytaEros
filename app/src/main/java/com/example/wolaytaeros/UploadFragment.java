package com.example.wolaytaeros;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UploadFragment extends Fragment {

    private EditText titleEdit, descEdit;
    private Spinner categorySpinner;
    private ImageView imgUpload;
    private Bitmap bitmap;
    private ProgressDialog pd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // 1. መጀመሪያ ተጠቃሚው መግባቱን ቼክ እናድርግ
        SharedPreferences pref = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn = pref.getBoolean("isLoggedIn", false);

        if (!isLoggedIn) {
            Toast.makeText(getActivity(), "መረጃ ለመጫን መጀመሪያ መግባት አለብዎት!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            return inflater.inflate(R.layout.fragment_home, container, false);
        }

        // ⚠️ ወሳኝ ማስታወሻ፦ የ XML ፋይልህ ስም 'activity_upload' ከሆነ እዚህ ጋር 'R.layout.activity_upload' አድርገው
        View view = inflater.inflate(R.layout.fragment_upload, container, false);

        pd = new ProgressDialog(getActivity());
        pd.setMessage("መረጃው ወደ XAMPP እየተጫነ ነው...");
        pd.setCancelable(false);

        // ዘመናዊ ከሆነው የ XML ዲዛይን IDዎች ጋር በትክክል ተስተካክሏል
        titleEdit = view.findViewById(R.id.etTitle);
        descEdit = view.findViewById(R.id.etDescription);
        categorySpinner = view.findViewById(R.id.spinnerCategory);
        imgUpload = view.findViewById(R.id.imgSelect);
        Button postBtn = view.findViewById(R.id.btnUpload);

        String[] categories = {
                "Politics", "Cultural Food", "Culture", "Advertisement",
                "Education", "Infrastructure", "Nature", "Leader of Wolayta",
                "Wild and Domestic Animal", "Job Work"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, categories);
        categorySpinner.setAdapter(adapter);

        ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                            bitmap = getResizedBitmap(bitmap, 600);
                            imgUpload.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

        // ከተጨማሪ በተን ይልቅ ቀጥታ ምስሉን (CardView/ImageView) ሲነካ ጋለሪ እንዲከፍት ተደርጓል
        imgUpload.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            launcher.launch(intent);
        });

        postBtn.setOnClickListener(v -> uploadDataToXAMPP());

        return view;
    }

    private void uploadDataToXAMPP() {
        final String title = titleEdit.getText().toString().trim();
        final String desc = descEdit.getText().toString().trim();
        final String category = categorySpinner.getSelectedItem().toString();

        if (title.isEmpty() || desc.isEmpty() || bitmap == null) {
            Toast.makeText(getActivity(), "እባክዎ ሁሉንም (ፎቶውንም ጭምር) ይሙሉ", Toast.LENGTH_SHORT).show();
            return;
        }

        pd.show();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        final String encodedImage = Base64.encodeToString(bytes, Base64.DEFAULT);

        String url = "http://10.207.21.241/wolayta_db/upload_post.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    pd.dismiss();
                    if (response.trim().equalsIgnoreCase("Success")) {
                        Toast.makeText(getActivity(), "በተሳካ ሁኔታ ተጭኗል!", Toast.LENGTH_SHORT).show();
                        clearForm();
                    } else {
                        Toast.makeText(getActivity(), "XAMPP መልስ፡ " + response, Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    pd.dismiss();
                    Toast.makeText(getActivity(), "Network Error! XAMPP መገናኘቱን አረጋግጥ", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("title", title);
                params.put("description", desc);
                params.put("category", category);
                params.put("image", encodedImage);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(15000, 1, 1.0f));
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();
        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void clearForm() {
        titleEdit.setText("");
        descEdit.setText("");
        imgUpload.setImageResource(android.R.drawable.ic_menu_gallery);
        bitmap = null;
    }
}