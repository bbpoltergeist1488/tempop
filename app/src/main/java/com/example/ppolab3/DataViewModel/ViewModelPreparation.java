package com.example.ppolab3.DataViewModel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ppolab3.Enums.TileState;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewModelPreparation extends ViewModel {

  DatabaseReference RefField;
  DatabaseReference RefLobby;
    DatabaseReference RefPlayer1;
  MutableLiveData<String> player1_id = new MutableLiveData<>("");

  public MutableLiveData<String> getPlayer1_id(){
      return player1_id;
  }

  public void setRefField(String reference){
      RefField = FirebaseDatabase.getInstance().getReference(reference);
  }

  public void setRefLobby(String reference){
      RefLobby = FirebaseDatabase.getInstance().getReference(reference);
  }
  public void updateField(ValueEventListener valueEventListener){
      RefField.addValueEventListener(valueEventListener);
  }
  public void setField(List<TileState> tileStateList){
      RefField.setValue(tileStateList);
  }
  public void setAmountOfShips(){
      List<Integer> list = new ArrayList<>();
      list.add(4);
      list.add(3);
      list.add(2);
      RefLobby.setValue(list);
  }
    public void setRefLobbyListener(ValueEventListener valueEventListener){
        RefLobby.addValueEventListener(valueEventListener);
    }
  public void getPlayer1IdFromDb(String ref){
      RefPlayer1 = FirebaseDatabase.getInstance().getReference(ref);
      RefPlayer1.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
              if(snapshot.exists()){
                  player1_id.setValue(snapshot.getValue(String.class));
              }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {

          }
      });
  }
  public void setReady(Boolean player1, String user_id, String lobby_id){
      DatabaseReference reference = FirebaseDatabase.getInstance().getReference("lobbys/"+lobby_id);
      if(player1){
          reference.child("player1SetField").setValue(true);
      }
      else{
          reference.child("player2SetField").setValue(true);
          reference.child("player2_id").setValue(user_id);
      }
  }

}
