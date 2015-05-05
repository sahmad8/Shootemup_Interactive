package com.example.daniel.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/**
 * Created by Daniel on 4/21/2015.
 */
public class Missile extends Entity {

    private Context context;

    private int ScreenWidth;
    private int ScreenHeight;

    private Droid droid;
    private Bitmap bmp;

    private int x;   // the X coordinate
    private int y;   // the Y coordinate
    private int changeX;
    private int changeY;
    private boolean alive;
    private int damage;
    private int type; // 0:player, 1:enemy

    private int a;
    private int b;

    public Missile(Context context, int x, int y, int Px, int Py) {
        this.context = context;
        this.a = 0;
        this.b = 0;
        this.bmp = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.nuke);
        this.y = y;
        this.x = x;

        this.alive = true;
        damage = 10;
        this.droid = new Droid(this.bmp, this.x, this.y);
    }
    public void checkAlive(int ScreenWidth, int ScreenHeight){
          y -= 5;
          droid.setY(y);
          if(ScreenHeight>droid.getY()) {
              alive = false;
          }

    }
    @Override
    public Bitmap getBmp() {
        return bmp;
    }
    public boolean isAlive() {

        return alive;
    }
    public int getDamage(){
        return damage;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }


    public void update(Canvas canvas){
        droid.draw(canvas);
    }
}
