package com.example.ppolab3;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ppolab3.Adapters.PreparationAdapter;
import com.example.ppolab3.DataViewModel.ViewModelPreparation;
import com.example.ppolab3.Enums.TileState;
import com.example.ppolab3.SeaFightFiles.TilesField;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PreparationActivity extends AppCompatActivity {
    private RecyclerView preparationFieldRv;
    private String lobby_id, user_id, ref, lobby_ref,refPlayer1Id;
    private TilesField tilesField = new TilesField();
    private PreparationAdapter prepAdapter;
    private ViewModelPreparation VMprep;
    private boolean orientation = false;
    private int shipSize = 1;
    private String player1_id;
    private List<Integer> amountOfShipsLeft = new ArrayList<>();
    private Button smallShipBtn, mediumShipBtn, bigShipBtn,orientationBtn,cleanFieldBtn,goToGameBtn;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(PreparationActivity.this, MenuActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prep_activity);
        amountOfShipsLeft.add(4);
        amountOfShipsLeft.add(3);
        amountOfShipsLeft.add(2);
        VMprep = ViewModelProviders.of(this).get(ViewModelPreparation.class);
       preparationFieldRv = findViewById(R.id.preparationFieldRv);
        smallShipBtn = findViewById(R.id.smallShipBtn);
        mediumShipBtn = findViewById(R.id.mediumShipBtn);
        bigShipBtn = findViewById(R.id.bigShipBtn);
        orientationBtn = findViewById(R.id.orientationBtn);
        cleanFieldBtn = findViewById(R.id.cleanFieldBtn);
        goToGameBtn = findViewById(R.id.goToGameBtn);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            lobby_id = bundle.getString("lobby_id");
            user_id = bundle.getString("user_id");
        }
        ref = "lobbys/" + lobby_id + "/TilesField/" + user_id;
        lobby_ref = "lobbys/" + lobby_id+"/amountOfShips/"+user_id;
        refPlayer1Id = "lobbys/" + lobby_id+"/player1_id";
        prepAdapter = new PreparationAdapter(getApplicationContext(), tilesField.returnTileList(), ref,lobby_ref, shipSize, orientation);
        preparationFieldRv.setLayoutManager(new GridLayoutManager(this, 10));
        preparationFieldRv.setAdapter(prepAdapter);
        VMprep.setRefField(ref);
        VMprep.setRefLobby(lobby_ref);
        VMprep.setField(tilesField.returnTileList());
        VMprep.setAmountOfShips();
         VMprep.getPlayer1_id().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String id) {
                player1_id = id;
            }
        });
        VMprep.getPlayer1IdFromDb(refPlayer1Id);
        fieldListener();
        amountOfShipsLeftListener();
        smallShipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shipSize!=1)
                { shipSize = 1;
                    setPrepAdapter();}
            }
        });
        mediumShipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shipSize!=2)
                {shipSize = 2;
                    prepAdapter = new PreparationAdapter(getApplicationContext(), tilesField.returnTileList(), ref,lobby_ref, shipSize, orientation);
                    preparationFieldRv.setAdapter(prepAdapter);}
            }
        });
        bigShipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shipSize!=3)
                {shipSize = 3;
                    setPrepAdapter();}
            }
        });
        orientationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orientation=!orientation;
                setPrepAdapter();
            }
        });
        cleanFieldBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tilesField = new TilesField();
                VMprep.setField(tilesField.returnTileList());
                prepAdapter.notifyDataSetChanged();
                goToGameBtn.setEnabled(false);
                VMprep.setAmountOfShips();
            }
        });
        goToGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(player1_id.equals(user_id))
                {
                    VMprep.setReady(true,user_id,lobby_id);
                }
                else VMprep.setReady(false,user_id,lobby_id);
                Intent intent = new Intent(PreparationActivity.this, WaitingRoom.class);
                intent.putExtra("lobby_id", lobby_id);
                intent.putExtra("player1_id",player1_id);
                startActivity(intent);
                finish();
            }
        });
    }

    public void setPrepAdapter(){
        prepAdapter = new PreparationAdapter(getApplicationContext(), tilesField.returnTileList(), ref, lobby_ref,shipSize, orientation);
        preparationFieldRv.setAdapter(prepAdapter);
    }

    public void fieldListener(){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<TileState> list = new ArrayList<>();
                    for (DataSnapshot state : snapshot.getChildren()) {
                        list.add(state.getValue(TileState.class));
                    }
                    tilesField.setTilesList(list);
                }
                setPrepAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        VMprep.updateField(valueEventListener);
    }

    public void amountOfShipsLeftListener(){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<Integer> list = new ArrayList<>();
                    for (DataSnapshot amount : snapshot.getChildren()) {
                        list.add(amount.getValue(Integer.class));
                    }
                    amountOfShipsLeft = list;
                }
                if(amountOfShipsLeft.get(0)==0&&amountOfShipsLeft.get(1)==0&&amountOfShipsLeft.get(2)==0){
                    goToGameBtn.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        VMprep.setRefLobbyListener(valueEventListener);
    }
}
