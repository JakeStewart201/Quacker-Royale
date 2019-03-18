package com.anotherworld.view.graphics;

import com.anotherworld.view.data.Matrix2d;
import com.anotherworld.view.data.Points2d;

/**
 * Stores the information that defines where the camera is looking.
 * @author Jake Stewart
 *
 */
public abstract class Camera {

    /**
     * The Camera's x location in the world coordinate frame.
     * @return the x location
     */
    public abstract float getX();

    /**
     * The Camera's y location in the world coordinate frame.
     * @return the y location
     */
    public abstract float getY();
    
    /**
     * The Camera's z location in the world coordinate frame.
     * @return the z location
     */
    public abstract float getZ();
    
    /**
     * The vector direction the camera is looking at.
     * @return the vector direction
     */
    public abstract Points2d getViewDirection();

    /**
     * The Camera's width in the world coordinate frame.
     * @return the width
     */
    public abstract float getWidth();

    /**
     * The Camera's height in the world coordinate frame.
     * @return the height
     */
    public abstract float getHeight();

    /**
     * Returns the camera's depth of field.
     * @return the depth
     */
    public abstract float getDepth();


    /**
     * Applies matrix transformations using the camera's position so it is centred on the screen.
     * @param camera The camera to use for display
     */
    public Matrix2d transform() {
        Matrix2d transformation = (Matrix2d.homScale3d(1 / this.getWidth(), -1 / this.getHeight(), 0));
        transformation = transformation.mult(Matrix2d.homTranslate3d(-this.getX(), -this.getY(), -this.getZ()));
        return transformation;
    }
    
    /**
     * The handedness direction of the camera.
     * @return the handedness vector
     */
    public Points2d getHandedness() {
        Points2d tempUp = new Points2d(3, 1);
        tempUp.setValue(1, 0, 1f);
        return tempUp.crossProduct(this.getViewDirection());
    }
    
    /**
     * The up direction of the camera.
     * @return the up vector
     */
    public Points2d getUpDirection() {
        return this.getViewDirection().crossProduct(this.getHandedness());
    }

}
