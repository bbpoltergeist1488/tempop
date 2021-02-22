package com.example.ppolab3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ppolab3.DataViewModel.ViewModelRegistration;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private FirebaseUser FB_user;
    private FirebaseAuth reg_auth;
    String email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button regBtn,logBtn;
        EditText emailEdit,passwEdit;
        emailEdit = (EditText) findViewById(R.id.emailEdit);
        passwEdit = (EditText) findViewById(R.id.passwEdit);
        regBtn = (Button) findViewById(R.id.regBtn);
        logBtn = (Button) findViewById(R.id.logBtn);
        reg_auth = FirebaseAuth.getInstance();
        FB_user = reg_auth.getCurrentUser();
        if (FB_user!=null){
            Intent intent = new Intent(MainActivity.this,MenuActivity.class);
            startActivity(intent);
            finish();
        }
        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (emailEdit.getText().length()>0&&  passwEdit.getText().length()>0) {
                    email = emailEdit.getText().toString();
                    password = passwEdit.getText().toString();
                    reg_auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Intent intent = new Intent(MainActivity.this,MenuActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });

                }
            }
        });
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RegistrationActivity.class);
                startActivity(intent);
            }
        });

    }
}