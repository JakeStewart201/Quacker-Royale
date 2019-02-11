package com.anotherworld.view;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import com.anotherworld.tools.datapool.WallData;
import com.anotherworld.tools.input.KeyListener;
import com.anotherworld.tools.input.KeyListenerNotFoundException;
import com.anotherworld.view.data.BallDisplayData;
import com.anotherworld.view.data.DisplayObject;
import com.anotherworld.view.data.PlayerDisplayData;
import com.anotherworld.view.data.RectangleDisplayData;
import com.anotherworld.view.graphics.GameScene;
import com.anotherworld.view.graphics.Scene;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL;


/**
 * Creates a window and manages the game's display.
 * @author Jake Stewart
 *
 */
public class View implements Runnable {

    private static Logger logger = LogManager.getLogger(View.class);

    private Long window;

    private Scene currentScene;

    private CountDownLatch keyListenerLatch;
    
    private volatile KeyListener keyListener;

    private int height;

    private int width;
    
    private Queue<ViewEvent> eventQueue;

    /**
     * Creates the View object initialising it's values.
     * @param width The screen width
     * @param height The screen height
     */
    public View(int width, int height) {
        logger.info("Creating view");
        this.height = height;
        this.width = width;
        eventQueue = new LinkedList<>();
        keyListenerLatch = new CountDownLatch(1);
    }

    /**
     * Returns the key listener for the view's window.
     * @return The keyListener
     * @throws KeyListenerNotFoundException If the keyListener couldn't be retrieved in 10 seconds
     */
    public KeyListener getKeyListener() throws KeyListenerNotFoundException {
        logger.info("Request for key listener objected");
        try {
            if (keyListenerLatch.await(10, TimeUnit.SECONDS)) {
                return keyListener;
            }
        } catch (InterruptedException e) {
            logger.catching(e);
        }
        throw new KeyListenerNotFoundException("Timeout of 10 seconds, was window initialized");
    }
    
    /**
     * Stages new objects to be used for the game display.
     * @param playerObjects The new player objects.
     * @param ballObjects The new ball objects.
     * @param rectangleObjects The new platform objects.
     * @param wallObjects The new wall objects.
     */
    public void updateGameObjects(ArrayList<? extends PlayerDisplayData> playerObjects, ArrayList<? extends BallDisplayData> ballObjects,
            ArrayList<? extends RectangleDisplayData> rectangleObjects, ArrayList<? extends WallData> wallObjects) {
        ArrayList<DisplayObject> disObj = new ArrayList<>();
        for (int i = 0; i < rectangleObjects.size(); i++) {
            disObj.add(new DisplayObject(rectangleObjects.get(i)));
        }
        for (int i = 0; i < wallObjects.size(); i++) {
            disObj.add(new DisplayObject(wallObjects.get(i)));
        }
        for (int i = 0; i < playerObjects.size(); i++) {
            disObj.add(new DisplayObject(playerObjects.get(i)));
        }
        for (int i = 0; i < ballObjects.size(); i++) {
            disObj.add(new DisplayObject(ballObjects.get(i)));
        }
        synchronized (eventQueue) {
            eventQueue.add(new UpdateDisplayObjects(disObj));
        }
    }

    @Override
    public void run() {
        logger.info("Running view");
        if (!glfwInit()) {
            logger.fatal("Unable to initialise glfw");
            throw new IllegalStateException("Couldn't initialise glfw");
        }

        logger.debug("Creating window");
        window = glfwCreateWindow(width, height, "Bullet Hell", NULL, NULL);

        if (window == null) {
            logger.fatal("Unable to create game window");
            glfwTerminate();
            throw new RuntimeException("Couldn't create glfw window");
        }

        glfwMakeContextCurrent(window);

        GL.createCapabilities();

        keyListener = new KeyListener(window);
        
        keyListenerLatch.countDown();

        currentScene = new GameScene();

        while (!glfwWindowShouldClose(window)) {

            glClear(GL_COLOR_BUFFER_BIT);
            
            synchronized (eventQueue) {
                while (!eventQueue.isEmpty()) {
                    completeEvent(eventQueue.poll());
                }
            }

            currentScene.draw(width, height);

            glFlush();

            glfwSwapBuffers(window);

            logger.debug("Polling for glfw events");

            glfwPollEvents();

        }
        logger.info("Closing window");
        glfwTerminate();
    }
    
    private void completeEvent(ViewEvent event) {
        if (event.getClass().equals(UpdateDisplayObjects.class) && currentScene.getClass().equals(GameScene.class)) {
            ((GameScene)currentScene).updateGameObjects(((UpdateDisplayObjects)event).getObjects());
        }
    }

}
