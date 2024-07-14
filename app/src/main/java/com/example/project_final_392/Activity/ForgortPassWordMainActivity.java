package com.example.project_final_392.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import com.example.project_final_392.R;

public class ForgortPassWordMainActivity extends AppCompatActivity {

    EditText editTextEmail;
    Button backBtn;
    Button ResetBtn;
    ProgressBar progressBar;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgort_pass_word_main);
        editTextEmail = findViewById(R.id.edit_text_email);
        backBtn = findViewById(R.id.btn_back);
        ResetBtn = findViewById(R.id.btn_reset);
        progressBar = findViewById(R.id.progress_bar);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgortPassWordMainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        ResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = String.valueOf(editTextEmail.getText());
                if (TextUtils.isEmpty(email)){
                    Toast.makeText(ForgortPassWordMainActivity.this,"Please enter your register email", Toast.LENGTH_SHORT).show();
                    editTextEmail.setError("Email is required");
                    editTextEmail.requestFocus();
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(ForgortPassWordMainActivity.this,"Please enter Vaild eamil",Toast.LENGTH_SHORT).show();
                    editTextEmail.setError("Valid email is required");

                }else {
                    progressBar.setVisibility(View.VISIBLE);
                    resetPassword(email);
                }
            }
        });

    }

    private void resetPassword(String email) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ForgortPassWordMainActivity.this, "Please Check your inbox for password reset Link", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(ForgortPassWordMainActivity.this, LoginActivity.class);

                    //Clear Stack  to prevent user coming back to Forgot PasswordActivity
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                }else {
                    Toast.makeText(ForgortPassWordMainActivity.this,"Something failure, Please do again",Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}