package com.anotherworld.model.movable;

import com.anotherworld.tools.datapool.BallData;

public class Ball extends AbstractMovable {

    private BallData ballData;

    public Ball(BallData ballData){
        super(ballData);
        this.ballData = ballData;
//        ballData.setDangerous(false);
    }

    public boolean isDangerous() { return ballData.isDangerous(); }
    public void setDangerous(boolean dangerous) { ballData.setDangerous(dangerous); }

    public String toString() {
        return "Location: x "+ getXCoordinate() + " y " + getYCoordinate();
    }
}
