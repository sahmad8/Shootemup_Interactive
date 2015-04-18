package com.example.daniel.myapplication;

import android.graphics.Bitmap;

/**
 * Created by Kevin Tsai on 2015/4/11.
 */
public class Entity {
    private int ScreenWidth;
    private int ScreenHeight;

    private Droid droid;
    private Bitmap bmp;

    private int x;   // the X coordinate
    private int y;   // the Y coordinate
    private int changeX;
    private int changeY;

    protected int life;   //player life

    protected  Entity(){

    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public Bitmap getBmp() {
        return bmp;
    }

    public void setBmp(Bitmap bmp) {
        this.bmp = bmp;
    }



    public int getScreenHeight() {
        return ScreenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        ScreenHeight = screenHeight;
    }

    public int getScreenWidth() {
        return ScreenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        ScreenWidth = screenWidth;
    }




}

