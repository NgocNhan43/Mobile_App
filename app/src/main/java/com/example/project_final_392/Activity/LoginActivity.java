package com.example.project_final_392.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.OAuthProvider;
import com.example.project_final_392.R;
public class LoginActivity extends AppCompatActivity {
    TextInputEditText editTextEmail, editTextPassword;
    Button signIn;
    TextView forgotPassword;

    TextView signUp;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    ProgressBar progressBar;
    private static final String TAG = "LoginActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        progressBar = findViewById(R.id.progress_bar);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        signIn = findViewById(R.id.sign_in);
        signUp = findViewById(R.id.sign_up);
        forgotPassword = findViewById(R.id.Forgot_btn);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgortPassWordMainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email, password;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(LoginActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    editTextEmail.setError("Email is required");
                    editTextEmail.requestFocus();

                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(LoginActivity.this, "Please re-enter email", Toast.LENGTH_SHORT).show();
                    editTextEmail.setError("Valid is required");
                    editTextEmail.requestFocus();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                    editTextPassword.setError("Password is required");
                    editTextEmail.requestFocus();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    loginUser(email, password);
                }


            }
        });

    }

    private void loginUser(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
//                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                            startActivity(intent);
//                            finish();
                            //Get instance of current user
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            //check if email is verified before user can access their profile
                            if (firebaseUser.isEmailVerified()){
                                Toast.makeText(LoginActivity.this, "Login Successful",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                //Open user profile
                            }else {
                                firebaseUser.sendEmailVerification();
                                firebaseAuth.signOut();
                                showAlertDialog();
                            }

                        } else {
                            try {
                                throw task.getException();

                            }catch (FirebaseAuthInvalidUserException e){
                                editTextEmail.setError("User dose not exist. Please register Again !");
                                editTextEmail.requestFocus();
                            }catch (FirebaseAuthInvalidCredentialsException e){
                                editTextPassword.setError("Invalid Pass, check and re_enter ");
                                editTextPassword.requestFocus();
                            }catch (Exception e){
                                Log.e(TAG, e.getMessage());
                                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void showAlertDialog() {
        //setup alert
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Email Not Verified");
        builder.setMessage("Please verify your email Now. You can not login without email verification ");
        //open email apps if users click, continue button
        builder.setPositiveButton("Continue ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // to email in new windows and not  within in our app
                startActivity(intent);
            }
        });
        //Create the AlertDialog
        AlertDialog alertDialog = builder.create();
        // Show the AlerDialog
        alertDialog.show();
    }

    //check ìf user is already logged in .In such case . Straightaway take the user to  the user's profile
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        if(firebaseAuth.getCurrentUser() != null){
            Toast.makeText(LoginActivity.this,"Already Login In ",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(LoginActivity.this,"You can login Now ",Toast.LENGTH_SHORT).show();

        }
    }

}


//                firebaseAuth.signInWithEmailAndPassword(email, password)
//                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                if (task.isSuccessful()){
//                                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT ).show();
//                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                     startActivity(intent);
//                                    finish();
//                                }else {
//                                    Toast.makeText(LoginActivity.this, "Authentication Failed" , Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });