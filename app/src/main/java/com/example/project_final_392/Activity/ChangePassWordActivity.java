package com.example.project_final_392.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;





import com.example.project_final_392.R;
public class ChangePassWordActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    EditText editTextPwdCurr, editTextPwdNew, editTextPwdConfirmNew;
    TextView textViewAuthenticated;
    Button buttonChangePwd, buttonReAuthenticate, buttonBack;
    ProgressBar progressBar;
    String userPwdCurrent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass_word);


        editTextPwdNew = findViewById(R.id.editText_change_pwd_new);
        editTextPwdCurr = findViewById(R.id.editText_change_pwd_current);
        editTextPwdConfirmNew = findViewById(R.id.editText_change_pwd_new_confirm);
        //notification
        textViewAuthenticated = findViewById(R.id.textView_change_pwd_authenticated);
        progressBar = findViewById(R.id.progress_bar);
        //button
        buttonChangePwd = findViewById(R.id.button_change_pwd);
        buttonReAuthenticate = findViewById(R.id.button_change_pwd_authenticate);
        buttonBack = findViewById(R.id.button_back);



        //buttonBack
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangePassWordActivity.this,UserProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });




        //Disable editText for new passWord, Confirm new password and make change pwd button
        //Unclick able  till user  is authenticated

        editTextPwdNew.setEnabled(false);
        editTextPwdConfirmNew.setEnabled(false);
        buttonChangePwd.setEnabled(false);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser.equals("")){
            Toast.makeText(ChangePassWordActivity.this, "SomeThing went wrong!, User's detail not available",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ChangePassWordActivity.this, UserProfileActivity.class);
            startActivity(intent);
            finish();

        }else {
            reAuthenticateUser(firebaseUser);
        }
    }


    //reAuthenticateUser before change password
    private void reAuthenticateUser(FirebaseUser firebaseUser) {
        buttonReAuthenticate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userPwdCurrent = editTextPwdCurr.getText().toString();

                if (TextUtils.isEmpty(userPwdCurrent)){
                    Toast.makeText(ChangePassWordActivity.this,"Password is needed",Toast.LENGTH_SHORT).show();
                    editTextPwdCurr.setError("Please enter your current password to authenticate");
                    editTextPwdCurr.requestFocus();
                }else {
                    progressBar.setVisibility(View.VISIBLE);

                    //ReAuthenticate User now
                    AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(),userPwdCurrent);
                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                progressBar.setVisibility(View.GONE);
                                //Disable editText for current Password. Enable for new password and cofirm password new password
                                editTextPwdCurr.setEnabled(false);
                                editTextPwdNew.setEnabled(true);
                                editTextPwdConfirmNew.setEnabled(true);

                                //Enable change pwd button. Disable Authentication button

                                buttonReAuthenticate.setEnabled(false);
                                buttonChangePwd.setEnabled(true);

                                //Set TextView to show use is authentication /verified
                                textViewAuthenticated.setText("You are authentication/verified."+"You can change password now");
                                Toast.makeText(ChangePassWordActivity.this, "Password has been verified",Toast.LENGTH_SHORT).show();

                                //change color password button

                                buttonChangePwd.setBackgroundTintList(ContextCompat.getColorStateList(ChangePassWordActivity.this, R.color.dark_Green));

                                buttonChangePwd.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        changePwd(firebaseUser);
                                    }
                                });
                            }else {
                                Toast.makeText(ChangePassWordActivity.this, "Your current password is incorrect. Please re-enter",Toast.LENGTH_SHORT).show();

                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
    }

    private void changePwd(FirebaseUser firebaseUser) {
        String userPwdNew = editTextPwdNew.getText().toString();
        String userPwdConfirmNew = editTextPwdConfirmNew.getText().toString();

        if (TextUtils.isEmpty(userPwdNew)){
            Toast.makeText(ChangePassWordActivity.this,"New passWord is needed",Toast.LENGTH_SHORT).show();
            editTextPwdNew.setError("Please re_Enter your new password");
            editTextPwdNew.requestFocus();
        }else if (TextUtils.isEmpty(userPwdConfirmNew)){
            Toast.makeText(ChangePassWordActivity.this,"Please confirm Your new password",Toast.LENGTH_SHORT).show();
            editTextPwdConfirmNew.setError("Please re_Enter your new password");
            editTextPwdConfirmNew.requestFocus();

        }else if (!userPwdNew.matches(userPwdConfirmNew)){
            Toast.makeText(ChangePassWordActivity.this,"PassWord did not match",Toast.LENGTH_SHORT).show();
            editTextPwdConfirmNew.setError("Please re_Enter your same password");
            editTextPwdConfirmNew.requestFocus();

        }else if (userPwdCurrent.equals(userPwdNew)){
            Toast.makeText(ChangePassWordActivity.this,"New password can not be same as old password",Toast.LENGTH_SHORT).show();
            editTextPwdNew.setError("Please enter your new password");
            editTextPwdNew.requestFocus();

        }else {
            progressBar.setVisibility(View.GONE);
            firebaseUser.updatePassword(userPwdNew).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(ChangePassWordActivity.this,"Password has been changed",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ChangePassWordActivity.this, UserProfileActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(ChangePassWordActivity.this,"SomeThing Went Wrong, Please Check Again",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }
}