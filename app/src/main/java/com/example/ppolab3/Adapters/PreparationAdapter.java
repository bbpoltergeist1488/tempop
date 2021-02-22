package com.example.ppolab3.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ppolab3.Enums.TileState;
import com.example.ppolab3.GameHelpers.PreparationHelper;
import com.example.ppolab3.R;

import java.util.List;

public class PreparationAdapter extends RecyclerView.Adapter<PreparationAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private final List<TileState> tileStateList;
    private final Context context;
    private String ref,lobbyRef;
    private int shipSize;
    private boolean orientation;

    public PreparationAdapter(Context context, List<TileState> tileStateList, String ref,String lobbyRef, int shipSize, boolean orientation) {
        this.tileStateList = tileStateList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.ref = ref;
        this.shipSize = shipSize;
        this.orientation = orientation;
        this.lobbyRef = lobbyRef;
    }

    @Override
    public PreparationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.tiles, parent, false);
        return new ViewHolder(view, context,tileStateList,ref,lobbyRef,shipSize,orientation);
    }

    @Override
    public void onBindViewHolder(PreparationAdapter.ViewHolder holder, int position) {
            holder.tileLayout.setBackgroundResource(R.drawable.sea);

         if(tileStateList.get(position)==TileState.SHIP){
             holder.tileLayout.setBackgroundResource(R.color.ship);
        }

    }

    @Override
    public int getItemCount() {
        return 100;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView tileImage;
        final LinearLayout tileLayout;

        ViewHolder(View view,Context context,List<TileState> tileStateList, String ref, String lobbyRef,int shipSize, boolean orientation) {
            super(view);
            tileLayout = (LinearLayout) view.findViewById(R.id.tileLayout);
            tileImage = (ImageView) view.findViewById(R.id.tileImage);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PreparationHelper helper = new PreparationHelper(context,tileStateList,ref,lobbyRef,shipSize,orientation);
                    helper.setShip(getAdapterPosition());
                }
            });

        }
    }
}


