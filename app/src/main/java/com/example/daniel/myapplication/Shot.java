package com.example.daniel.myapplication;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by Daniel on 3/19/2015.
 */
public class Shot extends Entity {
    public Shot(Bitmap bitmap, float x, float y, float width, float height, int c){
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
        xv = 20;
        yv = 20;
        spriteWidth = width;
        spriteHeight = height;
        color = c;
        sourceRect = new RectF(x,y,x+spriteWidth, y+spriteHeight);
        p = new Paint(Color.BLUE);

    }
    @Override
    public void update(){
        if(y+spriteHeight>0) {
            y -= yv;
            sourceRect = new RectF(x, y, x + spriteWidth, y + spriteHeight);
        }else{
            alive = false;
        }
    }
}
