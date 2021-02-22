package com.example.ppolab3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.example.ppolab3.DataViewModel.ViewModelEndedGame;
import com.example.ppolab3.DataViewModel.ViewModelSeaFight;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EndGameActivity extends Activity {

    FirebaseUser Fb_user;
    String winner_name,player2_id,hostPlayerId;
    TextView resultTextView;
    ViewModelEndedGame VMEndedGame;
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(EndGameActivity.this, MenuActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.end_game_activity);
        resultTextView = findViewById(R.id.resultTextView);
        Fb_user = FirebaseAuth.getInstance().getCurrentUser();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            winner_name = bundle.getString("winner");
            player2_id = bundle.getString("player2_id");
            hostPlayerId = bundle.getString("hostPlayerId");
        }
        if (Fb_user.getUid().equals(winner_name)) {
            resultTextView.setText("Вы победили!");
        } else {
            resultTextView.setText("Вы проиграли");
        }


    }
}
