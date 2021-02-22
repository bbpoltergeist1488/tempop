package com.example.ppolab3;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ppolab3.Adapters.MainAdapter;
import com.example.ppolab3.DataViewModel.ViewModelPreparation;
import com.example.ppolab3.DataViewModel.ViewModelRegistration;
import com.example.ppolab3.DataViewModel.ViewModelSeaFight;
import com.example.ppolab3.Databases.UserDatabase;
import com.example.ppolab3.Enums.Game;
import com.example.ppolab3.SeaFightFiles.TilesField;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class SeaFightActivity extends AppCompatActivity {

    private FirebaseUser Fb_user;
    private String lobby_id, hostPlayerId;
    private RecyclerView firstPlayerRv;
    private RecyclerView secondPlayerRv;
    private boolean host_player = false, gotSecondPlayerId, gotSecondPlayerField;
    private ViewModelSeaFight VMSeaFight;
    private TilesField tilesField1;
    private TilesField tilesField2;
    private ImageView enemyAvatarIv;
    private StorageReference storage;
    private TextView enemyNameTv;
    private String enymyId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seafight_activity);
        VMSeaFight = ViewModelProviders.of(this).get(ViewModelSeaFight.class);
        Fb_user = FirebaseAuth.getInstance().getCurrentUser();
        firstPlayerRv = findViewById(R.id.firstPlayerRv);
        firstPlayerRv.setLayoutManager(new GridLayoutManager(this, 10));
        secondPlayerRv = findViewById(R.id.secondPlayerRv);
        secondPlayerRv.setLayoutManager(new GridLayoutManager(this, 10));
        enemyAvatarIv = findViewById(R.id.enemyAvatarIv);
        enemyNameTv = findViewById(R.id.enemyNameTv);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            lobby_id = bundle.getString("lobby_id");
            hostPlayerId = bundle.getString("player1_id");
        }
        if (Fb_user.getUid().equals(hostPlayerId))
            host_player = true;
        else host_player = false;
        VMSeaFight = ViewModelProviders.of(this).get(ViewModelSeaFight.class);
        VMSeaFight.setReferenceLobby("lobbys/" + lobby_id);
        VMSeaFight.player2IdListener();
        VMSeaFight.setWinnerListener(lobby_id);
        VMSeaFight.setPlayer1TileFieldListener(hostPlayerId);
        VMSeaFight.setGameListener(lobby_id);
        VMSeaFight.userData1Listener(hostPlayerId);

        VMSeaFight.getWinner().observe(this, new Observer<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(String winner) {
                if (!winner.equals("")) {
                    Intent intent = new Intent(SeaFightActivity.this, EndGameActivity.class);
                    intent.putExtra("winner", winner);
                    startActivity(intent);
                    finish();
                    if (host_player) {
                        VMSeaFight.deleteLobby(lobby_id);
                    }
                    VMSeaFight.setUserStatistic(Fb_user.getUid(),hostPlayerId,VMSeaFight.getPlayer2Id().getValue(),winner);
                }
            }
        });
        if (host_player)
            VMSeaFight.getUserData2().observe(this, new Observer<UserDatabase>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onChanged(UserDatabase userDatabase) {
                    setEnemyInfo(userDatabase);
                    setSeaFightFields(userDatabase.user_id);
                }
            });
        else {
            VMSeaFight.getUserData1().observe(this, new Observer<UserDatabase>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onChanged(UserDatabase userDatabase) {
                    setEnemyInfo(userDatabase);
                    setSeaFightFields(userDatabase.user_id);
                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        boolean flag = false;
        if (hostPlayerId.equals(Fb_user.getUid()))
            flag = true;
        VMSeaFight.setReady(flag, lobby_id, Fb_user.getUid());
        Intent intent = new Intent(SeaFightActivity.this, MenuActivity.class);
        startActivity(intent);
        finish();
    }

    @SuppressLint("SetTextI18n")
    public void setEnemyInfo(UserDatabase userDatabase) {
        if (userDatabase.isGravatar) {
            String hash = GravatarFunction.md5Hex(userDatabase.user_email);
            String url = "https://www.gravatar.com/avatar/" + hash + "?s=204&d=404";
            if (url.length() > 0)
                Glide.with(getApplicationContext()).load(url).into(enemyAvatarIv);
        } else {
            storage = FirebaseStorage.getInstance().getReference("storage/" + userDatabase.user_id);
            storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(getApplicationContext()).load(uri).into(enemyAvatarIv);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Glide.with(getApplicationContext()).load("https://cs6.pikabu.ru/post_img/2015/07/04/10/1436029898_1190099444.jpg").into(enemyAvatarIv);
                }
            });
        }
        enemyNameTv.setText("Противник: " + userDatabase.user_name);
    }

    public void setSeaFightFields(String player2_id) {
        enymyId = player2_id;
        VMSeaFight.getPlayer1TileField().observe(this, new Observer<TilesField>() {
            @Override
            public void onChanged(TilesField tilesField) {
                tilesField1 = tilesField;
                MainAdapter adapter1 = new MainAdapter(getApplicationContext(), lobby_id, tilesField1.returnTileList(), host_player, hostPlayerId, player2_id, Fb_user.getUid());
                firstPlayerRv.setAdapter(adapter1);
            }
        });
        VMSeaFight.getPlayer2TileField().observe(this, new Observer<TilesField>() {
            @Override
            public void onChanged(TilesField tilesField) {
                tilesField2 = tilesField;
                MainAdapter adapter2 = new MainAdapter(getApplicationContext(), lobby_id, tilesField2.returnTileList(), !host_player, hostPlayerId, player2_id, Fb_user.getUid());
                secondPlayerRv.setAdapter(adapter2);
            }
        });
    }


}
