package com.example.daniel.myapplication;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by Kevin Tsai on 2015/4/11.
 */
public class Entity {
    protected Context context;

    protected int ScreenWidth;
    protected int ScreenHeight;

    protected Droid droid;
    protected Bitmap bmp;

    protected int x;   // the X coordinate
    protected int y;   // the Y coordinate
    protected int changeX;
    protected int changeY;
    protected int type;

    protected int life;   //player life

    protected boolean alive;

    protected  Entity(){}

    /**
     * Gets the y position of the entity
     * @return the current y position of the entity
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the y position to the provided value
     * @param y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Gets the x position of the entity
     * @return the current x position of the entity
     */
    public int getX() {
        return x;
    }
    /**
     * Sets the x position to the provided value
     * @param x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return the bitmap of the entity
     */
    public Bitmap getBmp() {
        return bmp;
    }

    /**
     * sets the bitmap of the entity to the provided bitmap
     * @param bmp
     */
    public void setBmp(Bitmap bmp) {
        this.bmp = bmp;
    }

    /**
     * @return the current recorded screen height
     */
    public int getScreenHeight() {
        return ScreenHeight;
    }

    /**
     * Sets the screen height to the provided value
     * @param screenHeight
     */
    public void setScreenHeight(int screenHeight) {
        ScreenHeight = screenHeight;
    }

    /**
     * @return the current recorded screen width
     */
    public int getScreenWidth() {
        return ScreenWidth;
    }

    /**
     * Sets the screen width to the provided value
     * @param screenWidth
     */
    public void setScreenWidth(int screenWidth) {
        ScreenWidth = screenWidth;
    }

    /**
     *
     * @return
     */
    public boolean isAlive() {

        return alive;
    }
    /**
     * Sets the entities alive variable to be the provided value
     * @param alive
     */
    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    /**
     * @return the current value of life
     */
    public int getLife() {
        return life;
    }

    /**
     * Sets life to the provided value as long as it is greater then 0 otherwise set it to 0
     * @param life
     */
    public void setLife(int life) {
        if(life<0){ life = 0;  }
        this.life = life;
    }

    /**
     * frees up the memory allocated to the bitmap
     */
    public void recycle(){
        bmp.recycle();
    }
}

