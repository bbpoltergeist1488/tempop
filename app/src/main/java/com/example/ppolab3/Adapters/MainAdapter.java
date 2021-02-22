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
import com.example.ppolab3.GameHelpers.SeaFightGameHelper;
import com.example.ppolab3.R;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private final List<TileState> tileStateList;
    private final Context context;
    private boolean yourField;
    private String hostPlayerId,enemyId,lobby_id, yourId;

    public MainAdapter(Context context, String lobby_id,List<TileState> tileStateList, boolean yourField, String hostPlayerId,String enemyId,String yourId) {
        this.tileStateList = tileStateList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.yourField = yourField;
        this.enemyId = enemyId;
        this.hostPlayerId = hostPlayerId;
        this.lobby_id = lobby_id;
        this.yourId = yourId;
    }

    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.smaller_tiles, parent, false);
        return new MainAdapter.ViewHolder(view, context, lobby_id,tileStateList, yourField,hostPlayerId,enemyId, yourId);
    }

    @Override
    public void onBindViewHolder(MainAdapter.ViewHolder holder, int position) {
        holder.smallerTileLayout.setBackgroundResource(R.drawable.sea);

        if (yourField) {
            if (tileStateList.get(position) == TileState.HIT)
                holder.smallerTileImage.setImageResource(R.drawable.cross);
            else if (tileStateList.get(position) == TileState.SHIP) {
                holder.smallerTileLayout.setBackgroundResource(R.color.ship);
            } else if (tileStateList.get(position) == TileState.MISS) {
                holder.smallerTileLayout.setBackgroundResource(R.color.miss);
            }
        }
        else{
            if (tileStateList.get(position) == TileState.HIT)
                holder.smallerTileImage.setImageResource(R.drawable.cross);
            else if (tileStateList.get(position) == TileState.MISS) {
                holder.smallerTileLayout.setBackgroundResource(R.color.miss);
            }
        }

    }

    @Override
    public int getItemCount() {
        return 100;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView smallerTileImage;
        final LinearLayout smallerTileLayout;

        ViewHolder(View view, Context context, String lobby_id,List<TileState> tileStateList, boolean yourField,String hostId, String enemyId, String yourId) {
            super(view);
            smallerTileLayout = (LinearLayout) view.findViewById(R.id.smallerTileLayout);
            smallerTileImage = (ImageView) view.findViewById(R.id.smallerTileImage);
            if (!yourField) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SeaFightGameHelper seaFightGameHelper = new SeaFightGameHelper(lobby_id,hostId, yourId,enemyId,getAdapterPosition(),tileStateList);
                        seaFightGameHelper.tryToShoot();
                    }
                });
            }

        }
    }
}
