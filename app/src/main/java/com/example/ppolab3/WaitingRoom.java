package com.example.ppolab3;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.ppolab3.DataViewModel.ViewModelSeaFight;
import com.example.ppolab3.DataViewModel.ViewModelWaiting;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WaitingRoom extends AppCompatActivity {
    String lobby_id, player1_id;
    ViewModelWaiting VMwaiting;
    boolean player1, player2;
    FirebaseUser FB_user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting_room);
        VMwaiting = ViewModelProviders.of(this).get(ViewModelWaiting.class);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            lobby_id = bundle.getString("lobby_id");
            player1_id = bundle.getString("player1_id");
        }
        String refPlayer1Set = "lobbys/"+lobby_id+"/player1SetField";
        String refPlayer2Set = "lobbys/"+lobby_id+"/player2SetField";
        FB_user = FirebaseAuth.getInstance().getCurrentUser();
        VMwaiting.getPlayer1SetField().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean ready) {
                player1 = ready;
                if(player1&&player2){
                    Intent intent = new Intent(WaitingRoom.this, SeaFightActivity.class);
                    intent.putExtra("lobby_id", lobby_id);
                    intent.putExtra("player1_id",player1_id);
                    if(FB_user.getUid().equals(player1_id))
                        VMwaiting.setTurnToShoot(player1_id,lobby_id);
                    startActivity(intent);
                    finish();
                }
            }
        });
        VMwaiting.getPlayer2SetField().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean ready) {
                player2 = ready;
                if(player1&&player2){
                    Intent intent = new Intent(WaitingRoom.this, SeaFightActivity.class);
                    intent.putExtra("lobby_id", lobby_id);
                    intent.putExtra("player1_id",player1_id);
                    if(FB_user.getUid().equals(player1_id))
                        VMwaiting.setTurnToShoot(player1_id,lobby_id);
                    startActivity(intent);
                    finish();
                }
            }
        });
        VMwaiting.player1SetFieldListener(refPlayer1Set);
        VMwaiting.player2SetFieldListener(refPlayer2Set);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        boolean flag = false;
        if(player1_id.equals(FB_user.getUid()))
            flag = true;
        VMwaiting.setReady(flag,lobby_id);
        Intent intent = new Intent(WaitingRoom.this, MenuActivity.class);
        startActivity(intent);
        finish();
    }
}
