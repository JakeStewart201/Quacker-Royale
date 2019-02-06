package com.anotherworld.model.movable;

import com.anotherworld.tools.datapool.PlayerData;

public class Player extends AbstractMovable {
    private boolean aiEnabled;

    PlayerData playerData;

    public Player(PlayerData playerData, boolean aiEnabled) {
        super(playerData);
        this.playerData = playerData;
    }

    public String getCharacterID() { return playerData.getCharacterID(); }

    public void setCharacterID(String characterID) {
        playerData.setCharacterID(characterID);
    }

    public int getHealth() { return playerData.getHealth(); }

    public void setHealth(int health) {
        playerData.setHealth(health);
    }

    public boolean isAIEnabled() { return aiEnabled; }

    public void moveRight() {}

}
