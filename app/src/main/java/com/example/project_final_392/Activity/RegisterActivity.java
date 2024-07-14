package com.example.project_final_392.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.example.project_final_392.R;
public class RegisterActivity extends AppCompatActivity {

    //các biến UI
    TextInputEditText editTextEmail, editTextPassword, editTextName, confirm_password;
    Button signUp;
    TextView signIn;
    ProgressBar progressBar;
    private static final String TAG = "RegisterActivity";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Khởi tạo các biến UI
        progressBar = findViewById(R.id.progress_bar);
        confirm_password = findViewById(R.id.confirm_password);
        editTextName = findViewById(R.id.name);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        signIn = findViewById(R.id.sign_in);
        signUp = findViewById(R.id.sign_up);

        // Thiết lập sự kiện cho nút "Sign In"
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Thiết lập sự kiện cho nút "Sign Up"
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Lấy dữ liệu từ các trường nhập liệu
                String textName, textEmail, textPassword, textConfirmPassword;
                textName = String.valueOf(editTextName.getText());
                textEmail = String.valueOf(editTextEmail.getText());
                textPassword = String.valueOf(editTextPassword.getText());
                textConfirmPassword = String.valueOf(confirm_password.getText());

                // Kiểm tra dữ liệu đầu vào
                if (TextUtils.isEmpty(textName)){
                    Toast.makeText(RegisterActivity.this, "Please enter your full name", Toast.LENGTH_LONG).show();
                    editTextName.setError("Full Name is required");
                    editTextName.requestFocus();
                }else if (TextUtils.isEmpty(textEmail)){
                    Toast.makeText(RegisterActivity.this, "Please enter your email", Toast.LENGTH_LONG).show();
                    editTextEmail.setError("Email Name is required");
                    editTextEmail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                    Toast.makeText(RegisterActivity.this, "Please r_enter your email", Toast.LENGTH_LONG).show();
                    editTextEmail.setError("Valid email is required");
                    editTextEmail.requestFocus();
                }else if(TextUtils.isEmpty(textPassword)){
                    Toast.makeText(RegisterActivity.this, "Please enter your password", Toast.LENGTH_LONG).show();
                    editTextPassword.setError("Password is required");
                    editTextPassword.requestFocus();
                }else if(textPassword.length() < 6){
                    Toast.makeText(RegisterActivity.this, "please should be at least 6 digits", Toast.LENGTH_LONG).show();
                    editTextPassword.setError("Password too weak");
                    editTextPassword.requestFocus();
                }else if (TextUtils.isEmpty(textConfirmPassword)){
                    Toast.makeText(RegisterActivity.this, "Please confirm your password", Toast.LENGTH_LONG).show();
                    confirm_password.setError("Password confirmation is required ");
                    confirm_password.requestFocus();
                } else if (!textPassword.equals(textConfirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "Please same same password", Toast.LENGTH_LONG).show();
                    confirm_password.setError("Password confirmation is required ");
                    confirm_password.requestFocus();
                    // Xóa mật khẩu đã nhập
                    editTextPassword.clearComposingText();
                    confirm_password.clearComposingText();
                }else {
                    // Hiển thị ProgressBar và tiến hành đăng ký người dùng
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(textName,textEmail,textPassword);
                }


            }

        });

    }

    // Hàm đăng ký người dùng
    private void registerUser(String textName, String textEmail, String textPassword) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(textEmail,textPassword)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates  = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(textName) //đặt tên người dùng
                                    .build();

                            firebaseUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> profileUpdateTask) {
                                    if (profileUpdateTask.isSuccessful()){
                                        // Gửi email xác minh
                                        Toast.makeText(RegisterActivity.this,"User registered successfully", Toast.LENGTH_LONG).show();

                                        firebaseUser.sendEmailVerification();
                                        // Mở trang LoginActivity sau khi đăng ký thành công
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish(); //  Đóng Register Activity
                                    }else {
                                        // Xử lý lỗi khi cập nhật profile
                                        Log.e(TAG, "Error updating profile.");
                                        Toast.makeText(RegisterActivity.this, "Error updating profile.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

//                            Toast.makeText(RegisterActivity.this,"User registered successfully", Toast.LENGTH_LONG).show();
//                            // Gửi email xác minh
//                            firebaseUser.sendEmailVerification();
//                            // Mở trang LoginActivity sau khi đăng ký thành công
//                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                            startActivity(intent);
//                            finish(); //  To close Register Activity

                        }else {
                            try {
                                throw task.getException();
                            }catch (FirebaseAuthWeakPasswordException e){
                                editTextPassword.setError("Your password is too weak. Kindly use mix of alphabets, number and special character");
                                editTextPassword.requestFocus();
                            }catch(FirebaseAuthInvalidCredentialsException e){
                                editTextEmail.setError("Your email is invalid or already in use. Kindly re-enter");
                                editTextEmail.requestFocus();

                            }catch (FirebaseAuthUserCollisionException e){
                                editTextEmail.setError("User is already registered with this email. Use another email");
                                editTextEmail.requestFocus();
                            }catch (Exception e){
                                Log.e(TAG, e.getMessage());
                                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                                // Ẩn ProgressBar khi hoàn tất
                                progressBar.setVisibility(View.GONE);
                            }


                        }

                    }
                });

    }







}


//                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//                firebaseAuth.createUserWithEmailAndPassword(textPassword,textEmail)
//                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                if (task.isSuccessful()){
//                                    Toast.makeText(RegisterActivity.this, "Register Successful",Toast.LENGTH_SHORT).show();
//                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                                    startActivity(intent);
//                                    finish();
//                                }else {
//                                    Toast.makeText(RegisterActivity.this ,"Authentication Failed!", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });