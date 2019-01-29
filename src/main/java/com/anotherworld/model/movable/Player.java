package com.anotherworld.model.movable;

public class Player extends AbstractMovable {
    private String characterID;
    private boolean aiEnabled;
    private int health;

    public Player(String characterID, int health, float xCoordinate,
                  float yCoordinate, ObjectState state, boolean aiEnabled) {
        super(xCoordinate, yCoordinate, state);
        this.characterID = characterID;
        this.health = health;
        this.aiEnabled = aiEnabled;
    }

    public String getCharacterID() { return characterID; }

    public void setCharacterID(String characterID) {
        this.characterID = characterID;
    }

    public int getHealth() { return health; }

    public void setHealth(int health) {
        this.health = health;
    }

    public boolean isAIEnabled() { return aiEnabled; }
}