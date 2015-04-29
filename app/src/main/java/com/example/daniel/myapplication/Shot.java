package com.example.daniel.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/**
 * Created by Kevin Tsai on 2015/4/11.
 */
public class Shot extends Entity{
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

    // y = ax + b direction of the shot
    // x = (y-b)/a
    private float a;
    private float b;
    public Shot(Context context, int x, int y, int type, int Px, int Py){
        this.context = context;
        this.a = 0;
        this.b = 0;
        switch(type) {
            case(0):
                this.bmp = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.shot_1);
                break;
            case(1):
                this.bmp = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.shot_2);
                if(x-Px !=0) {
                    a = ((y - Py) / (float)(x - Px));
                }else{
                    a = (float) ((y - Py));
                }
                b = (y-a*(float)x);
                break;
            default:
                break;
        }
        this.x = x - this.bmp.getWidth()/2;
        this.y = y;
        this.type = type;
        this.alive = true;
        damage = 1;
        this.droid = new Droid(this.bmp, this.x, this.y);
    }


    public void checkAlive(int ScreenWidth, int ScreenHeight){
        switch(type){
            case(0):
                y -= 5;
                droid.setY(y);
                if(!(0<=x && 0<=y)) {
                    alive = false;
                }
                break;
            case(1):
                y += 4;
                x = (int)((y-b)/a);
                droid.setX(x);
                droid.setY(y);

                if(ScreenHeight<=y) {
                    alive = false;
                }
                break;
            default:
                break;
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
        if(!bmp.isRecycled()) {
            droid.draw(canvas);
        }
    }

    public void recycle(){
        bmp.recycle();
    }
}
