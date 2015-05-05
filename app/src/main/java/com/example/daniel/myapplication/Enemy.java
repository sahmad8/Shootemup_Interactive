package com.example.daniel.myapplication;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;


/**
 * Created by Kevin Tsai on 2015/4/7.
 */
public class Enemy extends Entity{
    private int ShotCreateCountDown;
    private ArrayList<Shot> Shots;
    private int count;

    //for enemy 2
    private boolean GoRight;

    /**
     * Initializes the values for the enemy based on what type it is by assigning different health,
     * start positions, and bitmaps
     * @param context
     * @param ScreenWidth
     * @param ScreenHeight
     * @param x
     * @param y
     * @param type
     */
    public Enemy(Context context, int ScreenWidth, int ScreenHeight,int x, int y, int type) {
        this.context = context;
        this.Shots = new ArrayList<Shot>();
        this.ShotCreateCountDown = 120;
        count = 0;

        this.type = type;
        this.life = 2;

        this.GoRight = true;
        this.changeX = 10;
        this.ScreenWidth = ScreenWidth;
        this.ScreenHeight = ScreenHeight;

        switch(type){
            case 0:
                this.bmp = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.enemy2);
                //Shots.add(new Shot(this.context,this.x,this.y,0, Px, Py));
                this.changeX = 5;
                this.life = 1;
                break;
            case 1:
                this.bmp = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.enemy2);
                this.changeY = 5;

                break;
            case 2:
                this.bmp = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.enemy3);
                this.life = 100;
                break;
            case 3:

                break;
            case 4:

                break;
            default:
                this.bmp = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.enemy2);
                break;

        }
        this.x = x-bmp.getWidth()/2;
        this.y = y;
        this.droid = new Droid(this.bmp, x, y);
    }

    /**
     * @return what type from 0 to 3 the enemy is
     */
    public int getType(){
        return type;
    }

    /**
     * Updates the enemy's position based on provided inputs and their specified behavior based on
     * their type
     * @param canvas
     * @param Px
     * @param Py
     */
    public void update(Canvas canvas,int Px, int Py){
        switch(type){
            case 0:
                count++;
                if(count==20){Shots.add(new Shot(this.context,this.x,this.y+bmp.getHeight(),1, Px, Py));}
                x+=5;
                if(x>ScreenWidth+bmp.getWidth()){
                    for(Shot s : Shots){
                        if(!s.isAlive()){
                            life = 0;
                        }
                    }
                }
                break;
            case 1:
                y += 5;
                if(y>ScreenHeight){ life = 0;   }
                break;
            case 2:
                if(life>0 && count%5==0 && 0<=count%100 && count%100<=25){ Shots.add(new Shot(this.context,this.x,this.y+bmp.getHeight(),1, Px, Py)); }
                count = (count+1)%100;

                if(x>=ScreenWidth){ changeX = -5;   }
                if(x<=0){   changeX = 5;    }
                x += changeX;

                break;
            case 3:

                break;
            case 4:

                break;
            default:

                break;
        }
        checkShots(canvas);

        droid.setX(x);
        droid.setY(y);
        if(life>0&&!bmp.isRecycled()) {
            droid.draw(canvas);
        }
        else{
            bmp.recycle();
        }
    }

    /**
     * Updates the enemy's shots, changes the state for out of bound shots and removes dead shots.
     * @param canvas
     */
    private void checkShots(Canvas canvas){
        ArrayList<Shot> removeList = new ArrayList<Shot>();
        for(Shot s: Shots){
            s.checkAlive(ScreenWidth,ScreenHeight);
            if(s.isAlive()==false){
                removeList.add(s);
                canvas.drawText("BAD",0,3,50,100,new Paint());
            }
        }

        for(Shot r:removeList){
            r.recycle();
            Shots.remove(r);
        }


        for(Shot s: Shots){
            s.update(canvas);
        }
    }

    /**
     * @return true if at least one of the enemy's shots is still on screen and alive
     */
    public boolean ShotsAlive(){
        for(Shot s: Shots){
            if(s.isAlive()){
                return true;    // at least one shot still alive
            }
        }
        return false;
    }

    /**
     * @return returns the array of shots associated with the enemy
     */
    public ArrayList<Shot> getShots(){
        return Shots;
    }
}