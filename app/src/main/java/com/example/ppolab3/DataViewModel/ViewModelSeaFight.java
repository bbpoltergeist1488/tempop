package com.example.ppolab3.DataViewModel;

import android.annotation.SuppressLint;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ppolab3.Databases.UserDatabase;
import com.example.ppolab3.Enums.Game;
import com.example.ppolab3.Enums.TileState;
import com.example.ppolab3.SeaFightFiles.TilesField;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewModelSeaFight extends ViewModel {

    private MutableLiveData<TilesField> player1TileField = new MutableLiveData<>();
    private MutableLiveData<TilesField> player2TileField = new MutableLiveData<>();
    private MutableLiveData<Game> game = new MutableLiveData<>();
    private MutableLiveData<UserDatabase> userData1 = new MutableLiveData<>();
    private MutableLiveData<UserDatabase> userData2 = new MutableLiveData<>();
    private MutableLiveData<String> winner = new MutableLiveData<>();

    private MutableLiveData<String> player2Id = new MutableLiveData<>("");

    DatabaseReference referenceLobby;


    public void setWinner(String id) {
        winner.setValue(id);
    }

    public MutableLiveData<String> getWinner() {
        return winner;
    }

    public void setWinnerListener(String lobby_id) {
        DatabaseReference winnerRef = FirebaseDatabase.getInstance().getReference("lobbys/" + lobby_id+"/winner");
        winnerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    {
                        setWinner(snapshot.getValue(String.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setGame(Game new_game) {
        game.setValue(new_game);
    }

    public MutableLiveData<Game> getGame() {
        return game;
    }

    public void deleteLobby(String lobby_id) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("lobbys/" + lobby_id);
        reference.removeValue();
    }

    public void setGameListener(String lobby_id) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("lobbys/" + lobby_id + "/state");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    setGame(snapshot.getValue(Game.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public MutableLiveData<String> getPlayer2Id() {
        return player2Id;
    }

    public void setUserData1(UserDatabase userData) {
        userData1.setValue(userData);
    }

    public void setUserData2(UserDatabase userData) {
        userData2.setValue(userData);
    }

    public MutableLiveData<UserDatabase> getUserData1() {
        return userData1;
    }

    public MutableLiveData<UserDatabase> getUserData2() {
        return userData2;
    }

    public void userData1Listener(String id) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users_db/" + id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    setUserData1(snapshot.getValue(UserDatabase.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void userData2Listener(String id) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users_db/" + id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    setUserData2(snapshot.getValue(UserDatabase.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setPlayer2Id(String s) {
        player2Id.setValue(s);
        referenceLobby.child("TilesField").child(player2Id.getValue()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<TileState> tileStateList = new ArrayList<>();
                    for (DataSnapshot tileState : snapshot.getChildren()) {
                        tileStateList.add(tileState.getValue(TileState.class));
                    }
                    setPlayer2TileField(tileStateList);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void player2IdListener() {
        referenceLobby.child("player2_id").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    setPlayer2Id(snapshot.getValue(String.class));
                    userData2Listener(snapshot.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setPlayer1TileField(List<TileState> list) {
        TilesField tilesField = new TilesField();
        tilesField.setTilesList(list);
        player1TileField.setValue(tilesField);
    }

    public void setPlayer2TileField(List<TileState> list) {
        TilesField tilesField = new TilesField();
        tilesField.setTilesList(list);
        player2TileField.setValue(tilesField);
    }

    public MutableLiveData<TilesField> getPlayer1TileField() {
        return player1TileField;
    }

    public MutableLiveData<TilesField> getPlayer2TileField() {
        return player2TileField;
    }

    public void setPlayer1TileFieldListener(String user_id) {
        referenceLobby.child("TilesField").child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<TileState> tileStateList = new ArrayList<>();
                    for (DataSnapshot tileState : snapshot.getChildren()) {
                        tileStateList.add(tileState.getValue(TileState.class));
                    }
                    setPlayer1TileField(tileStateList);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setReferenceLobby(String ref) {
        referenceLobby = FirebaseDatabase.getInstance().getReference(ref);
    }

    public void setReady(Boolean player1, String lobby_id, String user_id) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("lobbys/" + lobby_id);
        if (player1) {
            reference.child("player1SetField").setValue(false);
        } else {
            reference.child("player2SetField").setValue(false);
            reference.child("player2_id").setValue("");
        }
        reference.child("TilesField").child(user_id).setValue(null);
        reference.child("amountOfShips").child(user_id).setValue(null);
    }
    String name1 = "none", name2 = "none";
    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users_db");

    public void setUserStatistic(String user_id, String player1, String player2, String winner) {

        userRef.child(player1).child("user_name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    name1 = snapshot.getValue(String.class);
                    userRef.child(player2).child("user_name").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                name2 = snapshot.getValue(String.class);
                                String res="";
                                if(user_id.equals(winner))
                                    res = " Победа";
                                else res = " Поражение";
                                userRef.child(user_id).child("prev_games").push().setValue(name1+" vs "+name2+""+res);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
