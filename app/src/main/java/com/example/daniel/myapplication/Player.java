package com.example.daniel.myapplication;

import android.graphics.Color;
import android.graphics.RectF;

/**
 * Created by Daniel on 3/19/2015.
 */
public class Player extends Entity {
    private int score;

    @Override
    public void update(){
        if(x != sourceRect.left||y != sourceRect.top){
            sourceRect = new RectF(x,y,x+spriteWidth, y+spriteHeight);
        }
        if(cooldown <=0) {
            game.addShot(new Shot(null, x + spriteWidth / 2 - 5, y - 20f, 10f, 10f, Color.BLACK));
            cooldown = 10;
        }else{
            cooldown--;
        }
    }
}
