package com.example.ppolab3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.ppolab3.DataViewModel.ViewModelRegistration;

import com.example.ppolab3.Databases.UserDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {
    private FirebaseUser FB_user;
    private FirebaseAuth reg_auth;
    ViewModelRegistration RegModel;
    String email,password, nickname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        RegModel = ViewModelProviders.of(this).get(ViewModelRegistration.class);
        EditText nickEdit, mailEdit, passEdit;
        Button registBtn;
        nickEdit = (EditText) findViewById(R.id.nickEdit);
        mailEdit = (EditText) findViewById(R.id.mailEdit);
        passEdit = (EditText) findViewById(R.id.passEdit);
        registBtn = (Button) findViewById(R.id.registBtn);
        reg_auth = FirebaseAuth.getInstance();
        registBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nickEdit.getText().length()>0 && mailEdit.getText().length()>0 && passEdit.getText().length()>=8){
                    email = mailEdit.getText().toString();
                    password = passEdit.getText().toString();
                    nickname = nickEdit.getText().toString();
                    reg_auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            FB_user = reg_auth.getCurrentUser();
                            UserDatabase userDB = new UserDatabase(email,nickname,false,FB_user.getUid());
                            RegModel.UserCreation(userDB,FB_user.getUid());
                            Intent intent = new Intent(RegistrationActivity.this,MenuActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            DatabaseReference ref123 = FirebaseDatabase.getInstance().getReference("123");
                            ref123.setValue(123);
                        }
                    });
                }
            }
        });

    }



}
