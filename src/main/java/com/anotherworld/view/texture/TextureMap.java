package com.anotherworld.view.texture;

import static org.lwjgl.opengl.GL46.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL46.glBindTexture;
import static org.lwjgl.opengl.GL46.glGetUniformLocation;
import static org.lwjgl.opengl.GL46.glUniform1i;
import static org.lwjgl.opengl.GL46.glUniform2f;
import static org.lwjgl.opengl.GL46.glUniformMatrix4fv;

import com.anotherworld.model.ai.tools.Matrix;
import com.anotherworld.view.graphics.spritesheet.SpriteSheet;

import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

/**
 * Creates and manages a map of different textures.
 * @author Jake Stewart
 *
 */
public class TextureMap {
    
    public static final int PLAYER_TEXTURE_BUFFER = 0;
    public static final int BALL_TEXTURE_BUFFER = 1;
    public static final int TEXT_TEXTURE_BUFFER = 2;
    
    private static TextureBuffer[] textureBuffers;
    
    /**
     * Loads the textures from files the specified folder.
     * @param location the texture folder location
     */
    public TextureMap(String location) throws IOException {
        textureBuffers = new TextureBuffer[3];
        textureBuffers[PLAYER_TEXTURE_BUFFER] = new TextureBuffer(location + "miniDuck.png", 4, 5);
        textureBuffers[BALL_TEXTURE_BUFFER] = new TextureBuffer(location + "NeutralBall/NeutralBall0.png", 1, 1);
        textureBuffers[TEXT_TEXTURE_BUFFER] = new TextureBuffer(location + "tom_font.png", 32, 4);
    }
    
    /**
     * Loads a texture the correct texture for an object to the opengl buffers.
     * @param programmeId The programme id to use
     * @param spriteSheet The sprite sheet to load
     * @param xShear The x shear on the sprite
     * @param yShear The y shear on the sprite
     */
    public void loadTexture(int programmeId, SpriteSheet spriteSheet, float xShear, float yShear) {

        //Change this to select right texture from buffer
        glUniform1i(glGetUniformLocation(programmeId, "hasTex"), spriteSheet.isTextured() ? 1 : 0);
        
        if (spriteSheet.isTextured()) {
            glBindTexture(GL_TEXTURE_2D, textureBuffers[spriteSheet.getTextureBuffer()].getId());
            glUniform1i(glGetUniformLocation(programmeId, "tex"), 0);
            float[] matrix = textureBuffers[spriteSheet.getTextureBuffer()].getTextureTransformation(spriteSheet).getPoints();
            FloatBuffer temp2 = BufferUtils.createFloatBuffer(16);
            temp2.put(matrix);
            temp2.flip();
            glUniformMatrix4fv(glGetUniformLocation(programmeId, "textureTransformation"), false, temp2);
            glUniform2f(glGetUniformLocation(programmeId, "Shear"), xShear, yShear);
        }
        
    }

    /**
     * Destroys the opengl buffers for the textures.
     */
    public void destroy() {
        for (TextureBuffer textureBuffer : textureBuffers) {
            textureBuffer.destroy();
        }
        textureBuffers = new TextureBuffer[0];
    }

    public static Matrix getDimensions(int tb) {
        return new Matrix(textureBuffers[tb].getXNumOfTextures(), textureBuffers[tb].getYNumOfTextures());
    }

    public static Matrix getSpriteDimensions(int tb) {
        return new Matrix(textureBuffers[tb].getWidth() / textureBuffers[tb].getXNumOfTextures(), textureBuffers[tb].getHeight() / textureBuffers[tb].getYNumOfTextures());
    }
    
}