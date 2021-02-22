package com.example.ppolab3.DataViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;

import com.example.ppolab3.Databases.UserDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ViewModelRegistration extends ViewModel {
    DatabaseReference DBRegist = FirebaseDatabase.getInstance().getReference("users_db");
    public void UserCreation(UserDatabase users_db,String id){
        DBRegist.child(id).setValue(users_db);


    }

}
