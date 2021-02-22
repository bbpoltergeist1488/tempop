package com.example.ppolab3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ppolab3.DataViewModel.ViewModelProfile;
import com.example.ppolab3.DataViewModel.ViewModelRegistration;
import com.example.ppolab3.Databases.UserDatabase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    String name;
    Uri urls;
    private StorageReference storage;
    private FirebaseAuth reg_auth;
    private UserDatabase Dbu;
    private FirebaseUser FB_user;
    Button changenBtn, gravatarBtn;
    EditText changenEdit;
    TextView emailView;
    ImageView image;
    ViewModelProfile VMProfile;
    ListView prevGamesLv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        VMProfile = ViewModelProviders.of(this).get(ViewModelProfile.class);
        changenEdit = (EditText) findViewById(R.id.changenEdit);
        changenBtn = (Button) findViewById(R.id.changenBtn);
        gravatarBtn = (Button) findViewById(R.id.gravatarBtn);
        emailView = (TextView) findViewById(R.id.emailView);
        image = (ImageView) findViewById(R.id.image);
        prevGamesLv = (ListView) findViewById(R.id.prevGamesLv);
        reg_auth = FirebaseAuth.getInstance();
        FB_user = reg_auth.getCurrentUser();
        VMProfile.InitProf(FB_user.getUid());
        storage = FirebaseStorage.getInstance().getReference("storage/" + FB_user.getUid());
        changenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (changenEdit.getText().length() > 0) {
                    name = changenEdit.getText().toString();
                    VMProfile.SetName(name);
                }
            }
        });
        SetProfileInfo(FB_user.getUid());
        setPrevGamesRv();

    }

    public void setPrevGamesRv() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<String> prevGames = new ArrayList <>();
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        prevGames.add(dataSnapshot.getValue(String.class));
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter(getApplicationContext(),
                            android.R.layout.simple_list_item_1, prevGames);
                    prevGamesLv.setAdapter( adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        VMProfile.setPreGamesListener(FB_user.getUid(), valueEventListener);
    }


    public void SetProfileInfo(String id) {
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Dbu = snapshot.getValue(UserDatabase.class);
                    changenEdit.setText(Dbu.user_name);
                    emailView.setText(Dbu.user_email);
                    if (Dbu.isGravatar) {
                        gravatarBtn.setText("Gravatar : ON");
                        gravatarBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                VMProfile.SetGravatar(false);
                            }
                        });
                        String hash = GravatarFunction.md5Hex(Dbu.user_email);
                        String url = "https://www.gravatar.com/avatar/" + hash + "?s=204&d=404";
                        if (url.length() > 0)
                            Glide.with(getApplicationContext()).load(url).into(image);
                        image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                    } else {
                        gravatarBtn.setText("Gravatar : OFF");
                        storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(getApplicationContext()).load(uri).into(image);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Glide.with(getApplicationContext()).load("https://cs6.pikabu.ru/post_img/2015/07/04/10/1436029898_1190099444.jpg").into(image);
                            }
                        });
                        gravatarBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                VMProfile.SetGravatar(true);

                            }
                        });
                        image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                changeFireBaseStoreAvatar();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        VMProfile.ListenerSwitch(eventListener);
    }

    public void changeFireBaseStoreAvatar() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null && resultCode == RESULT_OK && data.getData() != null) {
            urls = data.getData();
            image.setImageURI(urls);
            avatar_net();
        }
    }

    public void avatar_net() {
        storage.putFile(urls);
    }

}
