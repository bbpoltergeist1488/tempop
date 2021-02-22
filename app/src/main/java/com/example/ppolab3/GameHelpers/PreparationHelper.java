package com.example.ppolab3.GameHelpers;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.ppolab3.Enums.TileState;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PreparationHelper {
    private String ref, lobbyRef;
    private List<TileState> tileStateList;
    private Context context;
    private int shipSize;
    private boolean orientation;
    private int shipsLeft;
    private int pos;

    public PreparationHelper(Context context, List<TileState> tileStateList, String ref,String lobbyRef, int shipSize, boolean orientation) {
        this.ref = ref;
        this.tileStateList = tileStateList;
        this.context = context;
        this.shipSize = shipSize;
        this.orientation = orientation;
        this.lobbyRef = lobbyRef;
    }

    public void setShip(int position) {
        int temp = position;
        pos = position;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(ref);

        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference(lobbyRef+"/"+ (shipSize - 1));
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    shipsLeft =snapshot.getValue(Integer.class);
                    if (shipsLeft>0&&checkPosition(pos)) {
                        for (int i = 0; i < shipSize; i++) {
                            if (orientation)
                                pos = i * 10 + temp;
                            else
                                pos = i + temp;
                            reference.child(Integer.toString(pos)).setValue(TileState.SHIP);
                        }
                        reference2.setValue(shipsLeft-1);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public boolean checkPosition(int position) {
        int temp = position;
        boolean success = true;
        int[] nearPositions = {-11, -10, -9, 1, 11, 10, 9, -1};
        for (int i = 0; i < shipSize; i++) {
            int temp2 = position;
            if (orientation)
                position = temp + i * 10;
            else
                position = temp + i;
            if(!orientation&&Math.abs(temp2%10-position%10)>1)
            {
                success = false;
                break;
            }
            if (position >= 0 && position < 100)
                for (int j = 0; j < 8; j++) {
                    boolean right = true;
                    int nearpos = position + nearPositions[j];
                        if (Math.abs(position % 10 - nearpos %10) > 3)
                            right = false;
                    if (nearpos >= 0 && nearpos < 100 && right) {
                        if (tileStateList.get(nearpos) == TileState.SHIP) {
                            success = false;
                            break;
                        }
                    }

                }
            else {
                success = false;
                break;
            }
        }
        return success;
    }
}
