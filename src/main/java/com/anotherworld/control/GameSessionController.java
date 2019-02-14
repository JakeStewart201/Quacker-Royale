package com.anotherworld.control;

import com.anotherworld.model.logic.GameSession;
import com.anotherworld.model.movable.ObjectState;
import com.anotherworld.model.movable.Player;
import com.anotherworld.tools.datapool.BallData;
import com.anotherworld.tools.datapool.PlatformData;
import com.anotherworld.tools.datapool.PlayerData;
import com.anotherworld.tools.datapool.WallData;
import com.anotherworld.view.View;

import com.anotherworld.tools.input.KeyListener;
import com.anotherworld.tools.input.KeyListenerNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.*;


public class GameSessionController {
    
    private static Logger logger = LogManager.getLogger(GameSessionController.class);

    private GameSession session;
    private View view;
    private Thread viewThread;
    private KeyListener keyListener;
    
    private PlayerData currentPlayer;
    private ArrayList<PlayerData> networkPlayers;
    private ArrayList<PlayerData> ais;
    private ArrayList<BallData> balls;
    private ArrayList<PlatformData> platforms;
    private ArrayList<WallData> walls;

    //TODO make a constructor for the real main (Main.java)
    public GameSessionController(View view, PlayerData currentPlayer, ArrayList<PlayerData> networkPlayers, ArrayList<PlayerData> ais,ArrayList<BallData> balls, PlatformData platform, WallData wall) throws KeyListenerNotFoundException {
        this.currentPlayer = currentPlayer;
        this.networkPlayers = networkPlayers;
        this.ais = ais;
        this.balls = balls;
        this.platforms = new ArrayList<PlatformData>(Arrays.asList(platform));
        this.walls = new ArrayList<WallData>(Arrays.asList(wall));
//        initDataPool();
        
        this.session = new GameSession(currentPlayer,networkPlayers,ais, balls, platform, wall);
        this.view = view;

        // Starting the View thread
        this.viewThread = new Thread(view);
        viewThread.start();

        // Sleeping the main thread for 1 second to register the key inputs.
        try { Thread.sleep(1000); }
        catch (Exception e){ e.printStackTrace(); }

        // Obtain the key listener.
        this.keyListener = view.getKeyListener();
        mainLoop();

        //Clean up ie close connection if there are any and close the graphics window

    }

    private void mainLoop() {
        render();
        while(viewThread.isAlive()) {
            //update();

            try{
                Thread.sleep(0);
            }catch (InterruptedException e){
                e.printStackTrace();
            }

        }
    }

    private void update() {

        // Send the input key presses to the model.
        session.updatePlayer(keyListener.getKeyPresses());
        session.update();
    }

    private void render() {
        ArrayList<PlayerData> players = new ArrayList<>();
        players.addAll(ais);
        players.addAll(networkPlayers);
        players.add(currentPlayer);
        view.updateGameObjects(players, balls, platforms, walls);
    }
}
