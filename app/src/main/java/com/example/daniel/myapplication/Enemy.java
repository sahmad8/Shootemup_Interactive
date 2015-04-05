package com.example.daniel.myapplication;

import android.graphics.Bitmap;
import android.graphics.RectF;

/**
 * Created by Daniel on 4/5/2015.
 */
public class Enemy extends Entity {
    private ShootBehavior sb;
    private int point = 0;
    public Enemy(Bitmap bitmap, float x, float y, float width, float height, int c, boolean e, MainGamePanel m){
        super(bitmap,x,y,width,height,c,e,m);
        sb = new BulletBehavior(m,30,true);
        sb.setDir(1);
    }
    @Override
    public void update(){
        y += yv;
        sourceRect = new RectF(x,y,x+spriteWidth, y+spriteHeight);
        sb.shoot(x+spriteWidth/2,y+spriteHeight);
    }
    public int getPoints(){
        return point;
    }
}
