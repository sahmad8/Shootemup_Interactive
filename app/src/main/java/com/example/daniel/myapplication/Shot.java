package com.example.daniel.myapplication;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/**
 * Created by Kevin Tsai on 2015/4/11.
 */
public class Shot extends Entity{
    private int damage;
    private float a;
    private float b;

    /**
     * initializes the values such as the image bitmap damage and position.
     * @param context
     * @param x
     * @param y
     * @param type
     * @param Px
     * @param Py
     */
    public Shot(Context context, int x, int y, int type, int Px, int Py){
        this.context = context;
        this.a = 0;
        this.b = 0;
        //determines which bitmap to use
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

    /**
     * Checks if the shot is within the bounds of the screen, if it is not it is set to dead to be
     * removed
     * @param ScreenWidth
     * @param ScreenHeight
     */
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

    /**
     * @return the preset damage assigned to the shot
     */
    public int getDamage(){
        return damage;
    }

    /**
     * updates the position of the shot and its bitmap if the bitmap hasn't been recycled
     * @param canvas
     */
    public void update(Canvas canvas){
        if(!bmp.isRecycled()) {
            droid.draw(canvas);
        }
    }


}
