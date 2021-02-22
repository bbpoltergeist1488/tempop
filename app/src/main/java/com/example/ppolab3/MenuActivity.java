package com.example.ppolab3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.ppolab3.DataViewModel.ViewModelLobby;
import com.example.ppolab3.Databases.LobbyDatabase;
import com.example.ppolab3.Enums.Game;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class MenuActivity extends AppCompatActivity {
    EditText lobbyEdit;
    ViewModelLobby VMLobby;
    FirebaseUser fb_user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity);

        Button profileBtn,exitBtn,createBtn,connectBtn;
        VMLobby = ViewModelProviders.of(this).get(ViewModelLobby.class);
        VMLobby.initLobby();
        lobbyEdit = (EditText) findViewById(R.id.lobbyEdit);
        profileBtn = (Button) findViewById(R.id.profileBtn);
        exitBtn = (Button) findViewById(R.id.exitBtn);
        createBtn = (Button) findViewById(R.id.createBtn);
        connectBtn = (Button) findViewById(R.id.connectBtn);
        fb_user = FirebaseAuth.getInstance().getCurrentUser();

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MenuActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lobbyEdit.getText().length()>0){
                    LobbyDatabase DBlobby = new LobbyDatabase(lobbyEdit.getText().toString(),fb_user.getUid(),"", Game.PAUSED,false,false,"");
                    VMLobby.create_lobby(DBlobby);
                }
            }
        });
        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lobbyEdit.getText().length()>0){
                    lobbyExistsListener();
                }
            }
        });

    }

    public void lobbyExistsListener(){
        ValueEventListener valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    Intent prep = new Intent(MenuActivity.this, PreparationActivity.class);
                    prep.putExtra("lobby_id",lobbyEdit.getText().toString());
                    prep.putExtra("user_id",fb_user.getUid());
                    startActivity(prep);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        VMLobby.setListenerOnLobbyExists(valueEventListener,lobbyEdit.getText().toString());
    }
}
