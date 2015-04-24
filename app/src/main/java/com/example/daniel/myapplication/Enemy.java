package com.example.daniel.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
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

    private Context context;
    private int ScreenWidth;
    private int ScreenHeight;

    private Droid droid;
    private Bitmap bmp;

    private int x;   // the X coordinate
    private int y;   // the Y coordinate
    private int changeX;
    private int changeY;

    private int type;   //type of enemy


    private int count;
    private int life;   //player life

    //Init Player
    public Enemy(Context context, int ScreenWidth, int ScreenHeight,int x, int y, int type) {
        this.context = context;
        this.Shots = new ArrayList<Shot>();
        this.ShotCreateCountDown = 120;
        count = 0;

        this.type = type;
        this.life = 2;

        //this.changeX = 10;
        //this.changeY = 10;


        this.ScreenWidth = ScreenWidth;
        this.ScreenHeight = ScreenHeight;

        //this.bmp = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.ship1);



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
        this.y = y-bmp.getHeight();
        this.droid = new Droid(this.bmp, x, y);
    }

    public void ChangeX(boolean goLeft){
        /*
        if(goLeft){    this.x -= changeX;  }
        else{   this.x += changeX;  }

        if(this.x < 0){    this.x = 0;    }
        else if(this.x > (ScreenWidth - bmp.getWidth())){ this.x = (ScreenWidth - bmp.getWidth());   }
        droid.setX(x);
        */

    }
    @Override
    public Bitmap getBmp(){
        return bmp;
    }
    //Set player x
    public void setX(int x){
        this.x = x;
    }
    @Override
    public int getX(){
        return x;
    }
    //Set player y
    public void setY(int y){
        this.y = y;
    }
    public int getY(){
        return y;
    }

    public void setScreenWidth(int ScreenWidth){
        this.ScreenWidth = ScreenWidth;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public void update(Canvas canvas,int Px, int Py){
        switch(type){
            case 0:
                count++;
                if(count==20){Shots.add(new Shot(this.context,this.x,this.y,1, Px, Py));}
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

                break;
            case 3:

                break;
            case 4:

                break;
            default:

                break;
        }
        checkShots(canvas);

        //canvas.drawText(Integer.toString(Shots.size()),0,Integer.toString(Shots.size()).length(),50,50,new Paint());  // for debug, check shot number
        //canvas.drawText(Integer.toString(ScreenWidth),0,Integer.toString(ScreenWidth).length(),50,100,new Paint());
        //canvas.drawText(Integer.toString(ScreenWidth),0,Integer.toString(ScreenWidth).length(),50,100,new Paint());
        droid.setX(x);
        droid.setY(y);
        if(life>0) {
            droid.draw(canvas);
        }
    }

    private void checkShots(Canvas canvas){
        ArrayList<Shot> removeList = new ArrayList<Shot>();
        /*
        if(ShotCreateCountDown<=0){
            Shots.add(new Shot(context,x+bmp.getWidth()/2,y));
            ShotCreateCountDown =20;
        }
        else{
            ShotCreateCountDown--;
        }*/

        for(Shot s: Shots){
            s.checkAlive(ScreenWidth,ScreenHeight);
            if(s.isAlive()==false){
                removeList.add(s);
                canvas.drawText("BAD",0,3,50,100,new Paint());
            }
        }

        for(Shot r:removeList){
            Shots.remove(r);
        }


        for(Shot s: Shots){
            s.update(canvas);
        }


    }

    public boolean ShotsAlive(){
        for(Shot s: Shots){
            if(s.isAlive()){
                return true;    // at least one shot still alive
            }
        }
        return false;
    }

    public ArrayList<Shot> getShots(){
        return Shots;
    }
}