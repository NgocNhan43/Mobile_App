package com.example.project_final_392.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.project_final_392.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserProfileActivity extends AppCompatActivity {
    TextView textViewName;
    TextView textViewEmail;
    Button logOutBtn, backBtn;
    Button ChangepassBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        textViewName = findViewById(R.id.name);
        textViewEmail = findViewById(R.id.email);
        logOutBtn = findViewById(R.id.btn_logout);
        ChangepassBtn = findViewById(R.id.btn_change_password);
        backBtn = findViewById(R.id.btn_back);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        // Hiển thị thông tin lên TextViews
        if (currentUser != null) {
            String name = currentUser.getDisplayName();
            String email = currentUser.getEmail();

            if (name != null && !name.isEmpty()) {
                textViewName.setText(name);
            } else {
                textViewName.setText("N/A");
            }

            if (email != null && !email.isEmpty()) {
                textViewEmail.setText(email);
            } else {
                textViewEmail.setText("N/A");
            }
        } else {
            // Handle the case where the user is not logged in
            textViewName.setText("N/A");
            textViewEmail.setText("N/A");
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }


        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Toast.makeText(UserProfileActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish(); // Close UserProfileActivity
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        ChangepassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfileActivity.this, ChangePassWordActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}