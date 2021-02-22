package com.example.ppolab3.DataViewModel;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ppolab3.Databases.LobbyDatabase;
import com.example.ppolab3.MenuActivity;
import com.example.ppolab3.PreparationActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class ViewModelLobby extends ViewModel {
    private DatabaseReference lobbys;

    public void initLobby() {
        lobbys = FirebaseDatabase.getInstance().getReference("lobbys");
    }

    public void create_lobby(LobbyDatabase lobby) {
        lobbys.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                boolean flag = true;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    LobbyDatabase lobbyDatabase = dataSnapshot.getValue(LobbyDatabase.class);
                    if (lobbyDatabase.lobby_id.equals(lobby.lobby_id)) {
                        flag = false;
                        break;
                    }
                }
                if (flag)
                    lobbys.child(lobby.lobby_id).setValue(lobby);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void setListenerOnLobbyExists(ValueEventListener valueEventListener, String lobby_id){
        DatabaseReference lobbyExistsRef = FirebaseDatabase.getInstance().getReference("lobbys/"+lobby_id);
        lobbyExistsRef.addListenerForSingleValueEvent(valueEventListener);

    }


}
