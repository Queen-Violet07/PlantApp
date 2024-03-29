package com.example.plantzone.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plantzone.R;
import com.example.plantzone.models.userModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity  {

    TextView signIn;
    EditText name,email,password;
    Button register;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        auth= FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        progressBar=findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        signIn=findViewById(R.id.sign_in_reg);
        name=findViewById(R.id.name_reg);
        email=findViewById(R.id.email_reg);
        password=findViewById(R.id.password_reg);
        register=findViewById(R.id.button_reg);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent=new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(myIntent);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createUser();
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }
         private void createUser(){

             String userName=name.getText().toString();
             String userEmail=email.getText().toString();
             String userPassword=password.getText().toString();

             if(TextUtils.isEmpty(userName)){
                 Toast.makeText(this, "Name is Empty!", Toast.LENGTH_SHORT).show();
                 return;
             }

             if(TextUtils.isEmpty(userEmail)){
                 Toast.makeText(this, "Email is Empty!", Toast.LENGTH_SHORT).show();
                 return;
             }

             if(TextUtils.isEmpty(userPassword)){
                 Toast.makeText(this, "Password is Empty!", Toast.LENGTH_SHORT).show();
                 return;
             }

             if(userPassword.length()<6){
                 Toast.makeText(this, "Password Length Must Be Greater Than 6 Letters", Toast.LENGTH_SHORT).show();
                 return;
             }

             auth.createUserWithEmailAndPassword(userEmail,userPassword)
                     .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                         @Override
                         public void onComplete(@NonNull Task<AuthResult> task) {
                             if(task.isSuccessful()){
                                 userModel userModel=new userModel(userName,userEmail,userPassword);
                                 String id=task.getResult().getUser().getUid();
                                 database.getReference().child("Users").child(id).setValue(userModel);
                                 progressBar.setVisibility(View.GONE);
                                     Toast.makeText(RegistrationActivity.this,"Registration Successful",Toast.LENGTH_SHORT).show();
                                 Intent myIntent=new Intent(RegistrationActivity.this, MainActivity.class);
                                 startActivity(myIntent);
                              }
                             else{
                                 progressBar.setVisibility(View.GONE);
                                 Toast.makeText(RegistrationActivity.this,"Error"+task.getException(),Toast.LENGTH_SHORT).show();
                             }
                         }
                     }
                     );
         }

}