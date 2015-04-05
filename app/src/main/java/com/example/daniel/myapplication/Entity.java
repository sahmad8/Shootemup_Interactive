package com.example.daniel.myapplication;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.List;

/**
 * Created by Daniel on 3/18/2015.
 */
public class Entity {
    protected static final String TAG = Entity.class.getSimpleName();

    protected boolean alive = true;

    protected Bitmap bitmap;
    protected RectF sourceRect;

    protected float spriteWidth;
    protected float spriteHeight;
    protected float x;
    protected float y;

    protected double xv, yv;

    protected Paint p;
    protected int color;

    protected MainGamePanel game;
    protected int cooldown = 0;

    protected Entity(){

    }


    public Entity(Bitmap bitmap, float x, float y, float width, float height, int c, MainGamePanel m){
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
        xv = 10;
        yv = 10;
        spriteWidth = width;
        spriteHeight = height;
        color = c;
        game = m;
        sourceRect = new RectF(x,y,x+spriteWidth, y+spriteHeight);
        p = new Paint(Color.BLUE);

    }

    public void setX(float f){
        if(f > 0){
            x += xv;
        }else if(f<0){
            x -= xv;
        }
    }

    public void setY(float f){
        if(f > 0){
            y += yv;
        }else if(f<0){
            y -= yv;
        }
    }
    public float getLeft(){
        return x;
    }
    public float getRight(){
        return x+spriteWidth;
    }
    public float getTop(){
        return y;
    }
    public float getBottom(){
        return y+spriteHeight;
    }
    public RectF getBox(){
        return sourceRect;
    }
    public boolean getAlive(){
        return alive;
    }
    public void update(){}

    public void draw(Canvas canvas){
        if(alive) {
            p.setColor(this.color);
            canvas.drawRect(sourceRect, p);
        }
    }

    public boolean collision(List< Entity> entities){
        for(Entity e: entities){
            if(sourceRect.intersect(e.getBox())){
                alive = false;
                return true;
            }
        }
        return false;
    }


}
