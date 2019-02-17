package com.anotherworld.view.data;

import static org.lwjgl.opengl.GL11.GL_TRIANGLE_FAN;

import com.anotherworld.tools.datapool.WallData;

public class WallDisplayObject extends DisplayObject {

    private final RectangleDisplayData displayData;

    /**
     * Creates a display object to display the wall.
     * @param displayData The wall to display
     */
    public WallDisplayObject(WallData displayData) {
        super(DisplayObject.genWall(displayData.getWidth(), displayData.getHeight(), 1), GL_TRIANGLE_FAN);
        this.displayData = displayData;
    }

    @Override
    public float getTheta() {
        return displayData.getAngle();
    }

    @Override
    public float getX() {
        return displayData.getXCoordinate();
    }

    @Override
    public float getY() {
        return displayData.getYCoordinate();
    }
    
    @Override
    public boolean shouldCameraFollow() {
        return false;
    }
    
}
