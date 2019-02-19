package com.anotherworld.control;

import com.anotherworld.audio.AudioControl;
import com.anotherworld.audio.BackgroundMusic;
import com.anotherworld.settings.GameSettings;
import com.anotherworld.settings.MenuDemo;
import com.anotherworld.tools.input.KeyListenerNotFoundException;
import com.anotherworld.view.View;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;

import java.awt.*;

public class Main {
    private MenuDemo view;

    private static Logger logger = LogManager.getLogger(Main.class);

    public static void main (String args[]) {
        Main main = new Main();
        main.startTheGame();
        MenuDemo viewMenu = new MenuDemo();

    }

    public void add(MenuDemo view) {
        this.view = view;
    }

    public Main() {
        // need to set default config files?

    }

    
    public void startTheGame() {

        GameSettings settings = new GameSettings(4,3,4, true, true);

        GLFW.glfwInit();
        GLFWVidMode mode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

        try {
            View view = new View((int)(mode.width() * 0.75), (int)(mode.height() * 0.75));

            new GameSessionController(view, settings);

        } catch (KeyListenerNotFoundException ex) {
            logger.fatal(ex);
        } catch (RuntimeException ex) {
            logger.fatal(ex);
            ex.printStackTrace();
        }
    }

    public void startMultiplayer(boolean host) {
        if (host) {
            // establish the connection
            // wait for people to connect
            // tell when to start the game
            // finds how many people connected
            startTheGame();
            // when all players have been created
            // allocate the player id to the ip address so network knows who to send it to
        } else {
            // find the host and connect
        }
    }

    public void startSinglePlayer() {
        startTheGame();
    }

    public static boolean sfxSetting(boolean on) {
//        boolean state = GameSettings.toggleOnOff("soundEffects");
        logger.info("Toggle soundEffect "  + on);
        AudioControl.setEffectsOn(on);
        return on;
    }
    
    public static boolean musicSetting(boolean on) {
        AudioControl.setMusicOn(on);
//        boolean state = GameSettings.toggleOnOff("backgroundMusic");
        logger.info("Toggle backgroundMusic "  + on);
        return on;
    }


}
