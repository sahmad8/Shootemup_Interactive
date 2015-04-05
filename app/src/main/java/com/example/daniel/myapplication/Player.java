package com.example.daniel.myapplication;

import android.graphics.Bitmap;
import android.graphics.RectF;

/**
 * Created by Daniel on 3/19/2015.
 */
public class Player extends Entity {
    private ShootBehavior sb;

    public Player(Bitmap bitmap, float x, float y, float width, float height, int c, boolean e, MainGamePanel m){
        super(bitmap,x,y,width,height,c,e,m);
        sb = new BulletBehavior(m, 10, false);
    }

    @Override
    public void update(){
        if((x != sourceRect.left||y != sourceRect.top)&&alive){
            sourceRect = new RectF(x,y,x+spriteWidth, y+spriteHeight);
        }
        if(alive) {
            sb.shoot(x+spriteWidth/2,y);
        }
    }
}
