package com.anotherworld.model.ai.behaviour.player;

import com.anotherworld.model.ai.behaviour.Job;
import com.anotherworld.model.ai.tools.Line;
import com.anotherworld.model.ai.tools.Matrix;
import com.anotherworld.model.ai.tools.MatrixMath;
import com.anotherworld.model.logic.Platform;
import com.anotherworld.model.movable.Ball;
import com.anotherworld.model.movable.Player;

import java.util.ArrayList;

/**
 * Job class which makes the player chase the neutral ball.
 * @author roman
 */
public class ChaseBall extends Job {

    @Override
    public void reset() {
        start();
    }

    @Override
    public void act(Player ai, ArrayList<Player> players, ArrayList<Ball> balls, Platform platform) {
        // Theoretically if this job comes after avoiding balls
        // the balls array should already be sorted by the distance from the player

        for (Ball ball: balls){
            if (!ball.isDangerous()) {
                Matrix neighbour = MatrixMath.nearestNeighbour(new Line(ball.getCoordinates(),ball.getVelocity()),ai.getCoordinates());
                Matrix vector = MatrixMath.pointsVector(ai.getCoordinates(), neighbour);
                ai.setXVelocity(vector.getX());
                ai.setYVelocity(vector.getY());

            }
        }

    }
}