package com.example.daniel.myapplication;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by Daniel on 3/19/2015.
 */
public class Shot extends Entity {
    private int dir;
    public Shot(Bitmap bitmap, float x, float y,int u){
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
        xv = 20;
        yv = 20;
        spriteWidth = 20;
        spriteHeight = 20;
        color = Color.WHITE;
        sourceRect = new RectF(x,y,x+spriteWidth, y+spriteHeight);
        p = new Paint(Color.BLUE);
        dir = u;

    }
    @Override
    public void update(){
        if(y+spriteHeight>0) {
            y += yv*dir;
            sourceRect = new RectF(x, y, x + spriteWidth, y + spriteHeight);
        }else{
            alive = false;
        }
    }
}
