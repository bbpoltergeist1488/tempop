package com.example.ppolab3.DataViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewModelWaiting extends ViewModel {

    MutableLiveData<Boolean> player1SetField = new MutableLiveData<>(false);

    MutableLiveData<Boolean> player2SetField = new MutableLiveData<>(false);

    public void setPlayer1SetField(boolean b){
        player1SetField.setValue(b);
    }

    public void setPlayer2SetField(boolean b){
        player2SetField.setValue(b);
    }

    public MutableLiveData<Boolean> getPlayer1SetField(){
        return  player1SetField;
    }
    public MutableLiveData<Boolean> getPlayer2SetField(){
        return  player2SetField;
    }

    public void player1SetFieldListener(String ref){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(ref);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                    setPlayer1SetField(snapshot.getValue(Boolean.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void setTurnToShoot(String user_id, String lobby_id){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("lobbys/"+lobby_id+"/turn_to_shoot");
        reference.setValue(user_id);
    }
    public void player2SetFieldListener(String ref){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(ref);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                    setPlayer2SetField(snapshot.getValue(Boolean.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void setReady(Boolean player1, String lobby_id){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("lobbys/"+lobby_id);
        if(player1){
            reference.child("player1SetField").setValue(false);
        }
        else{
            reference.child("player2SetField").setValue(false);
            reference.child("player2_id").setValue("");
        }
    }


}
