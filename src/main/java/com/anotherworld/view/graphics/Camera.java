package com.anotherworld.view.graphics;

import com.anotherworld.view.data.Points2d;

/**
 * Stores the information that defines where the camera is looking.
 * @author Jake Stewart
 *
 */
public interface Camera {

    /**
     * The Camera's x location in the world coordinate frame.
     * @return the x location
     */
    public float getX();

    /**
     * The Camera's y location in the world coordinate frame.
     * @return the y location
     */
    public float getY();
    
    public float getZ();
    
    public default Points2d getViewDirection() {
        Points2d tempUp = new Points2d(3, 1);
        tempUp.setValue(2, 0, 1f);
        return tempUp;
    }

    /**
     * The Camera's width in the world coordinate frame.
     * @return the width
     */
    public float getWidth();

    /**
     * The Camera's height in the world coordinate frame.
     * @return the height
     */
    public float getHeight();

    /**
     * Returns the camera's depth of field.
     * @return the depth
     */
    public float getDepth();
    
    public default Points2d getHandedness() {
        Points2d tempUp = new Points2d(3, 1);
        tempUp.setValue(1, 0, 1f);
        return tempUp.crossProduct(this.getViewDirection());
    }
    
    public default Points2d getUpDirection() {
        return this.getViewDirection().crossProduct(this.getHandedness());
    }

}
