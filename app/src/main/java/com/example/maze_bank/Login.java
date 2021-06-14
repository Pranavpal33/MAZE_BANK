package com.example.maze_bank;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

public class Login extends AppCompatActivity {
    EditText mEmail,mPassword;
    Button mLoginBtn;
    TextView mCreateBtn,mForgotpswd;
    ProgressBar progressbar;
    FirebaseAuth fAuth;
    FirebaseUser mFirebaseUser;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        mEmail=findViewById(R.id.Email);
        mPassword=findViewById(R.id.Password);
        mLoginBtn=findViewById(R.id.LoginButton);
        mCreateBtn=findViewById(R.id.createText);
        mForgotpswd=findViewById(R.id.ForgotPswd);

        progressbar=findViewById(R.id.progressBar);
        fAuth=FirebaseAuth.getInstance();






         mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v != null) {
                    String email = mEmail.getText().toString().trim();
                    String password = mPassword.getText().toString().trim();

                    if (TextUtils.isEmpty(email)) {
                        mEmail.setError("Email is empty");
                        return;
                    }


                    if (TextUtils.isEmpty(password)) {
                        mEmail.setError("Password is empty");
                        return;
                    }

                    if (password.length() < 6) {
                        mPassword.setError("Password should be greater then 6 digits");
                        return;
                    }


                    progressbar.setVisibility(View.VISIBLE);


                    // authenticate the user
                    fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Login.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            } else {
                                Toast.makeText(Login.this, "ERROR ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                progressbar.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        });
        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });
        mForgotpswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetMail =new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password");
                passwordResetDialog.setMessage("Enter Your Email");
                passwordResetDialog.setView(resetMail);
                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mail=resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Login.this, "Reset Link sent Successfull", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Toast.makeText(Login.this, "Reset Link is not sent"+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                passwordResetDialog.create().show();
            }
        });

        
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseUser = fAuth.getCurrentUser();
        if (mFirebaseUser != null) {

            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

}
