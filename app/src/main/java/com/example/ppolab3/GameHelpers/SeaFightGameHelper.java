package com.example.ppolab3.GameHelpers;

import android.service.quicksettings.Tile;

import androidx.annotation.NonNull;

import com.example.ppolab3.Enums.Game;
import com.example.ppolab3.Enums.TileState;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class SeaFightGameHelper {

    private String lobby_id;
    private boolean host_player;
    private String hostPlayerId,yourId, enemyId;
    private int shootPosition;
    private List<TileState> tileStateList;
    private String turn_to_shoot;


    public SeaFightGameHelper(String lobby_id, String hostPlayerId,String yourId, String enemyId, int shootPosition, List<TileState> tileStateList) {
        this.lobby_id = lobby_id;
        this.hostPlayerId = hostPlayerId;
        this.enemyId = enemyId;
        this.shootPosition = shootPosition;
        this.yourId = yourId;
        if (hostPlayerId != enemyId)
            host_player = true;
        else
            host_player = false;
        this.tileStateList = tileStateList;

    }

    public void tryToShoot() {
        boolean shot = true;
        DatabaseReference shootTurnReference = FirebaseDatabase.getInstance().getReference("lobbys/" + lobby_id + "/turn_to_shoot");
        shootTurnReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    TileState shot_position=tileStateList.get(shootPosition);
                    turn_to_shoot = snapshot.getValue(String.class);
                    if (turn_to_shoot.equals(yourId)){
                        if(shot_position!=TileState.HIT&&shot_position!=TileState.MISS){
                            DatabaseReference seaFightFieldRef = FirebaseDatabase.getInstance().getReference("lobbys/"+lobby_id+"/TilesField/"+enemyId+"/"+shootPosition);
                            if(tileStateList.get(shootPosition)==TileState.SHIP)
                            {
                                shot_position = TileState.HIT;
                                seaFightFieldRef.setValue(shot_position);
                                tileStateList.set(shootPosition,TileState.HIT);
                                if(checkGameFinish()){
                                    DatabaseReference gameEndedReference = FirebaseDatabase.getInstance().getReference("lobbys/" + lobby_id + "/winner");
                                    gameEndedReference.setValue(yourId);
                                    shootTurnReference.setValue("none");
                                }
                            }
                            else if (tileStateList.get(shootPosition)==TileState.EMPTY_TILE){
                                shot_position = TileState.MISS;
                                seaFightFieldRef.setValue(shot_position);
                                shootTurnReference.setValue(enemyId);
                            }

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public boolean checkGameFinish(){
        boolean finish = true;
        for(TileState tileState: tileStateList)
        {
            if(tileState == TileState.SHIP)
            {
                finish=false;
                break;
            }
        }
        return  finish;
    }
}
