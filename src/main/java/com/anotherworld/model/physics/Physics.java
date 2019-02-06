package com.anotherworld.model.physics;

import java.util.ArrayList;
import java.util.List;

import com.anotherworld.model.ai.tools.Matrix;
import com.anotherworld.model.ai.tools.MatrixMath;
import com.anotherworld.model.movable.AbstractMovable;
import com.anotherworld.model.movable.Ball;
import com.anotherworld.model.movable.Player;

public class Physics {

    static float friction;
    static float rate;
    static float minimumSpeed = 0.4f;

    public Physics(float rate, float friction) {
        this.friction = friction;
        this.rate = rate;
    }

    /**
     * To make the object move
     *
     * @param AbstractMovable
     *            object
     */
    public static void move(AbstractMovable object) {
        float newXCoordinate = object.getXCoordinate() + object.getXVelocity();
        float newYCoordinate = object.getYCoordinate() + object.getYVelocity();
        object.setCoordinates(newXCoordinate, newYCoordinate);
    }

    /**
     * To check collision of the objects.
     *
     * @param AbstractMovable
     *            a, b
     */
    public static boolean checkCollision(AbstractMovable a, AbstractMovable b) {
        float xDistance = a.getXCoordinate() - b.getXCoordinate();
        float yDistance = a.getYCoordinate() - b.getYCoordinate();

        float sumOfRadii = a.getRadius() + b.getRadius();
        float distanceSquared = xDistance * xDistance + yDistance * yDistance;

        boolean isOverlapping = distanceSquared < sumOfRadii * sumOfRadii;
        return isOverlapping;
    }

    /**
     * To make the ball bounce within the wall
     *
     * @param Ball
     *            a, float[] wallCoordinate
     */
    public static void bouncedWall(Ball a, float[] wallCoordinate) {
        float circleR = a.getRadius();
        float circleX = a.getXCoordinate();
        float circleY = a.getYCoordinate();
        float[] direction = { circleY - circleR, circleX + circleR,
                circleY + circleR, circleX - circleR };
        if (direction[0] < wallCoordinate[0]) {
            a.setCoordinates(a.getXCoordinate(), wallCoordinate[0] + circleR);
            a.setYVelocity(-a.getYVelocity());
        } else if (direction[2] > wallCoordinate[2]) {
            a.setCoordinates(a.getXCoordinate(), wallCoordinate[2] - circleR);
            a.setYVelocity(-a.getYVelocity());
        }
        if (direction[1] > wallCoordinate[1]) {
            a.setCoordinates(wallCoordinate[1] - circleR, a.getYCoordinate());
            a.setXVelocity(-a.getXVelocity());
        } else if (direction[3] < wallCoordinate[3]) {
            a.setCoordinates(wallCoordinate[3] + circleR, a.getYCoordinate());
            a.setXVelocity(-a.getXVelocity());
        }
    }

    /**
     * To make the object move
     *
     * @param AbstractMovable
     *            object
     */
    public static void applyFriction(AbstractMovable a) {
        float speed = a.getSpeed() * friction;
        if (speed < 0.5f) {
            speed = 0.0f;
        }
        a.setSpeed(speed);
    }

    /**
     * To make the object accelerate
     *
     * @param AbstractMovable
     *            object
     */
    public static void accelerate(AbstractMovable a) {
        float speed = a.getSpeed() + rate;
        if (speed > 3.0f) {
            speed = 3.0f;
        }
        a.setSpeed(speed);
    }

    /**
     * To apply force to the object
     * 
     * @param AbstractMovable
     *            object
     */
    public static void forceApplying(AbstractMovable a, Matrix velocity) {
        float xVelocity = a.getXVelocity() + velocity.getY();
        float yVelocity = a.getYVelocity() + velocity.getX();
        float angle = (float) Math.toDegrees(Math.atan2(xVelocity, yVelocity));
        if (angle < 0) {
            angle += 360;
        }
        float speed = (float) Math.sqrt(xVelocity * xVelocity + yVelocity
                * yVelocity);
        a.setAngle(angle);
        a.setSpeed(speed);
        a.setXVelocity(xVelocity);
        a.setYVelocity(yVelocity);
    }

    /**
     * What happened if the player is colliding with eachother
     * 
     * @param Player
     *            player
     * @param Martix
     *            outsideVelocity
     */
    public static void collided(Player player, Matrix outsideVelocity) {
        player.setXVelocity(outsideVelocity.getX());
        player.setYVelocity(outsideVelocity.getY());
    }

    /**
     * What happened if the player is collided by a ball.
     * 
     * @param player
     * @param ball
     */
    public static void collidedByBall(Player player, Ball ball) {
        player.setXVelocity(ball.getXVelocity());
        player.setYVelocity(ball.getYVelocity());
        if (ball.canDamage()) {
            int health = player.getHealth();
            player.setHealth(health - 30);
        } else {
            ball.setDamage(true);
        }
    }

    /**
     * It is to check collision for all the objects within the game session. It
     * checks if each ball collided on either wall or player Then add the player
     * into collided list And check for the remaining player who is not collided
     * with any other thing
     * 
     * @param listOfBalls
     * @param listOfPlayers
     * @param wallDimensions
     */
    public static void onCollision(List<Ball> listOfBalls,
            List<Player> listOfPlayers, float[] wallDimensions) {
        List<Integer> collided = new ArrayList<Integer>();
        for (int i = 0; i < listOfBalls.size(); i++) {
            Ball ball = listOfBalls.get(i);
            bouncedWall(ball, wallDimensions);
            for (int j = 0; j < listOfPlayers.size(); j++) {
                if (collided.contains(j)) {
                    continue;
                }
                Player victim = listOfPlayers.get(i);
                if (checkCollision(ball, victim)) {
                    collidedByBall(victim, ball);
                    collided.add(j);
                }
            }
        }
        for (int i = 0; i < listOfPlayers.size(); i++) {
            if (collided.contains(i)) {
                continue;
            }
            Player player = listOfPlayers.get(i);
            for (int j = i + 1; i < listOfPlayers.size(); j++) {
                if (collided.contains(j)) {
                    continue;
                }
                Player player2 = listOfPlayers.get(j);
                if (checkCollision(player, player2)) {
                    Matrix veloToPlayer = new Matrix(player2.getXVelocity(),
                            player2.getYVelocity());
                    Matrix veloToPlayer2 = new Matrix(player.getXVelocity(),
                            player.getYVelocity());
                    collided(player, veloToPlayer);
                    collided(player2, veloToPlayer2);
                    collided.add(i);
                    collided.add(j);
                }
            }
        }
    }
}
