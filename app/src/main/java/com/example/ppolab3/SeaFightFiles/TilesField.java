package com.example.ppolab3.SeaFightFiles;

import com.example.ppolab3.Enums.TileState;

import java.util.ArrayList;
import java.util.List;

public class TilesField {
    private List<TileState> TilesList = new ArrayList<>();

    public TilesField() {
        for (int i = 0; i < 100; i++)
            TilesList.add(TileState.EMPTY_TILE);
    }

    public TileState getTile(int i) {
        return TilesList.get(i);
    }

    public List<TileState> returnTileList() {
        return TilesList;
    }

    public void setTilesList(List<TileState> list) {
        TilesList = list;
    }

}
