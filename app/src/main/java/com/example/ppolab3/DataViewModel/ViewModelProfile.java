package com.example.ppolab3.DataViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewModelProfile extends ViewModel {
    private MutableLiveData<Boolean> isGravatar = new MutableLiveData<>();
    DatabaseReference DBProfile;
    public void InitProf(String id){

       DBProfile =FirebaseDatabase.getInstance().getReference("users_db/"+id);

    }
    public MutableLiveData<Boolean> GetGravatar(){
return isGravatar;
    }
    public void SetGravatar(Boolean item){
        isGravatar.setValue(item);
        DBProfile.child("isGravatar").setValue(item);
    }
    public void SetName(String item){

        DBProfile.child("user_name").setValue(item);
    }
    public void ListenerSwitch(ValueEventListener listen){
        DBProfile.addValueEventListener(listen);

    }

    public void setPreGamesListener(String user_id,ValueEventListener valueEventListener){
        DatabaseReference prevGamesPref = FirebaseDatabase.getInstance().getReference("users_db/"+user_id+"/prev_games");
        prevGamesPref.addValueEventListener(valueEventListener);
    }
}
