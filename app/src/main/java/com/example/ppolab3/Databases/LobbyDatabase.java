package com.example.ppolab3.Databases;

import com.example.ppolab3.Enums.Game;

public class LobbyDatabase {
    public LobbyDatabase(){}
    public LobbyDatabase(String lobby_id, String player1_id, String player2_id, Game state, boolean player1SetField, boolean player2SetField,String winner){
        this.lobby_id=lobby_id;
        this.player1_id=player1_id;
        this.player2_id=player2_id;
        this.player1SetField = player1SetField;
        this.player2SetField = player2SetField;
        this.state=state;
        this.winner = winner;
    }
    public String lobby_id,player1_id,player2_id,winner;
    public Game state;
    public boolean player1SetField, player2SetField;
}
