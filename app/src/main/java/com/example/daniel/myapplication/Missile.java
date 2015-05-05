package com.example.daniel.myapplication;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/**
 * Created by Daniel on 4/21/2015.
 */
public class Missile extends Entity {

    private int damage;

    /**
     * initializes values like the position and bitmap.
     * @param context
     * @param x
     * @param y
     * @param Px
     * @param Py
     */
    public Missile(Context context, int x, int y, int Px, int Py) {
        this.context = context;
        this.bmp = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.nuke);
        this.y = y;
        this.x = x;

        this.alive = true;
        damage = 10;
        this.droid = new Droid(this.bmp, this.x, this.y);
    }

    /**
     * checks if the missile is above the bottom of the screen, if it is it is set to not be alive
     * and will explode
     * @param ScreenWidth
     * @param ScreenHeight
     */
    public void checkAlive(int ScreenWidth, int ScreenHeight){
          y -= 5;
          droid.setY(y);
          if(ScreenHeight>droid.getY()) {
              alive = false;
          }

    }

    /**
     * updated the entity position
     * @param canvas
     */
    public void update(Canvas canvas){
        droid.draw(canvas);
    }
}
