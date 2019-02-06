package com.anotherworld.model.ai.behaviour;

import com.anotherworld.model.logic.Platform;
import com.anotherworld.model.movable.Ball;
import com.anotherworld.model.movable.Player;

/**
 * Represents the job which has 3 states file success or running.
 * @author Roman
 */
public abstract class Job {

    public enum JobState {
        SUCCESS,FAILURE, RUNNING
    }

    protected JobState state;

    protected Player ai;
    protected Player[] players;
    protected Ball[] balls;
    protected Platform platform;

    /**
     * Call when need to initialise a new Job.
     */
    public Job() {

    }

    /**
     * Starts the Job and sets the Job state to RUNNING.
     */
    public void start() {
        this.state = JobState.RUNNING;
    }

    /**
     * Resets the job.
     */
    public abstract void reset();

    /**
     * Tells the job to act based on the current situation.
     *
     * @param ai The AI player who is doing the job
     * @param players The other players on the board
     * @param balls All the balls on the boards
     * @param platform The platform
     */
    public abstract void act(Player ai, Player[] players, Ball[] balls, Platform platform);

    /**
     * Sets the Job state to SUCCESS.
     */
    protected void succeed() {
        this.state = JobState.SUCCESS;
    }

    /**
     * Sets the Job state to FAILURE.
     */
    protected void fail() {
        this.state = JobState.FAILURE;
    }

    public boolean isSuccess() {
        return state.equals(JobState.SUCCESS);
    }

    public boolean isFailure() {
        return state.equals(JobState.FAILURE);
    }

    public boolean isRunning() {
        return state.equals(JobState.RUNNING);
    }

    public JobState getState() {
        return state;
    }
}
