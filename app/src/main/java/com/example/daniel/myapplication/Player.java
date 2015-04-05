package com.example.daniel.myapplication;

import android.graphics.Bitmap;
import android.graphics.RectF;

/**
 * Created by Daniel on 3/19/2015.
 */
public class Player extends Entity {
    private int score;
    private ShootBehavior sb;
    public Player(Bitmap bitmap, float x, float y, float width, float height, int c, MainGamePanel m){
        super(bitmap,x,y,width,height,c,m);
        sb = new BulletBehavior(m);
    }

    @Override
    public void update(){
        if((x != sourceRect.left||y != sourceRect.top)&&alive){
            sourceRect = new RectF(x,y,x+spriteWidth, y+spriteHeight);
        }
        if(cooldown <=0&&alive) {
            sb.shoot(x+spriteWidth/2,y);
        }else{
            cooldown--;
        }
    }
}
