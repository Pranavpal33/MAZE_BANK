package com.example.maze_bank;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    EditText mFullName,mEmail,mPassword,mPhone,mAccount;
    Button mRegisterBtn;
    TextView mLoginBtn,DOB;
    FirebaseAuth fAuth;
    ProgressBar progressbar;
    String userID,Date;
    FirebaseFirestore fStore;
    Calendar cal;
    private DatePickerDialog.OnDateSetListener mDateSetListener;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);



        mFullName=findViewById(R.id.fullName);
        mEmail=findViewById(R.id.Email);
        mPassword=findViewById(R.id.Password);
        mPhone=findViewById(R.id.Phone);
        mRegisterBtn=findViewById(R.id.RegisterBtn);
        mLoginBtn=findViewById(R.id.createText);
        DOB=findViewById(R.id.DOB);
        mAccount=findViewById(R.id.account);




        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        cal=Calendar.getInstance();


        DOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog =new DatePickerDialog(Register.this, android.R.style.Theme_Holo_Dialog_MinWidth,mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Date=dayOfMonth+"/"+(month+1)+"/"+year;
                DOB.setText(Date);
            }
        };


        progressbar=findViewById(R.id.ProgressBar);
        if(fAuth.getCurrentUser()!=null)
        {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=mEmail.getText().toString().trim();
                String password=mPassword.getText().toString().trim();
                String Fname=mFullName.getText().toString();
                String Pno=mPhone.getText().toString();
                String dob=DOB.getText().toString();
                String account=mAccount.getText().toString();


                if(TextUtils.isEmpty(Fname))
                {
                    mFullName.setError("Name field cannot be empty");
                }
                if(TextUtils.isEmpty(email))
                {
                    mEmail.setError("Email is empty");
                    return;
                }



                if(TextUtils.isEmpty(password))
                {
                    mEmail.setError("Password is empty");
                    return;
                }

                if(password.length()<6)
                {
                    mPassword.setError("Password should be greater then 6 digits");
                    return;
                }
                if(TextUtils.isEmpty(Pno))
                {
                    mPhone.setError("Phone Field cannot be empty");
                }
                if(TextUtils.isEmpty(dob))
                {
                    DOB.setError("Date Of Birth cannot be empty");
                }
                if(TextUtils.isEmpty(account))
                {
                    mAccount.setError("Account Field cannot be empty");
                }



               progressbar.setVisibility(View.VISIBLE);




                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                           Toast.makeText(Register.this,"User Created",Toast.LENGTH_SHORT).show();
                           userID=fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference=fStore.collection("users").document(userID);
                            Map<String,String> user=new HashMap<>();
                            user.put("fName",Fname);
                            user.put("Email",email);
                            user.put("PhoneNumber",Pno);
                            user.put("Date Of Birth",dob);



                            DocumentReference documentReference1=fStore.collection("users").document(userID).collection("Accounts").document(account);
                            Map<String,String> useracc=new HashMap<>();
                            useracc.put("Account Number",account);
                            useracc.put("Balance","0");
                            documentReference1.set(useracc, SetOptions.merge());
                                saveContext(account);



                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("TAG","OnSuccess: user profile is created for "+userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull @NotNull Exception e) {
                                    Log.d("TAG","OnFaliure: "+e.toString());
                                }
                            });

                           startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                        else
                        {
                            Toast.makeText(Register.this,"ERROR ! "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            progressbar.setVisibility(View.GONE);
                        }

                    }
                });

            }
        });
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();
            }
        });

    }
    private void saveContext(String curac) {
        SharedPreferences SP= getApplicationContext().getSharedPreferences("currantaccount",MODE_PRIVATE);
        SharedPreferences.Editor editor= SP.edit();
        editor.putString("acc",curac);
        editor.commit();

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
