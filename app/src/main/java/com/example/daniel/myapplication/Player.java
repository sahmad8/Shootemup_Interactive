package com.example.daniel.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;


/**
 * Created by Kevin Tsai on 2015/4/7.
 */
public class Player extends Entity{
    private int ShotCreateCountDown;
    private Missile missile;
    public boolean boom = false;
    private ArrayList<Shot> Shots;
    private ArrayList<Shot> removeList;

    private Context context;
    //private int ScreenWidth;
    //private int ScreenHeight;

    private Droid droid;
    private Bitmap bmp;

    private int x;   // the X coordinate
    private int y;   // the Y coordinate
    private int changeX;
    private int changeY;




    private int life;   //player life

    //Init Player
    public Player(Context context, int ScreenWidth, int ScreenHeight) {
        this.context = context;
        this.Shots = new ArrayList<Shot>();
        this.removeList = new ArrayList<Shot>();
        this.ShotCreateCountDown = 0;
        this.life = 1;

        this.changeX = 10;
        this.changeY = 10;


        this.ScreenWidth = ScreenWidth;
        this.ScreenHeight = ScreenHeight;

        this.bmp = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.ship1);
        this.x = ScreenWidth/2 -bmp.getWidth()/2;
        this.y = ScreenHeight - bmp.getHeight()-100;
        this.droid = new Droid(this.bmp, x, y);

    }

    public ArrayList<Shot> getShots(){
        return Shots;
    }
    @Override
    public Bitmap getBmp(){
        return bmp;
    }
    public void ChangeX(boolean goLeft){
        if(goLeft){    this.x -= changeX;  }
        else{   this.x += changeX;  }

        if(this.x < 0){    this.x = 0;    }
        else if(this.x > (ScreenWidth - bmp.getWidth())){ this.x = (ScreenWidth - bmp.getWidth());   }
        droid.setX(x);
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
    @Override
    public int getY(){
        return y;
    }
    /*
    public void setScreenWidth(int ScreenWidth){
        this.ScreenWidth = ScreenWidth;
    }
    */

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public void update(Canvas canvas){

        checkShots(canvas);

        //canvas.drawText(Integer.toString(Shots.size()),0,Integer.toString(Shots.size()).length(),50,50,new Paint());  // for debug, check shot number
        //canvas.drawText(Integer.toString(ScreenWidth),0,Integer.toString(ScreenWidth).length(),50,100,new Paint());
        //canvas.drawText(Integer.toString(ScreenWidth),0,Integer.toString(ScreenWidth).length(),50,100,new Paint());
        droid.draw(canvas);
    }

    public void removeSArray(ArrayList<Shot> s){
        removeList.clear();
        removeList.addAll(s);
    }

    private void checkShots(Canvas canvas){
        if(ShotCreateCountDown<=0){
            Shots.add(new Shot(context,x+bmp.getWidth()/2,y,0,0,0));
            ShotCreateCountDown =20;
        }
        else{
            ShotCreateCountDown--;
        }

        for(Shot s: Shots){
            s.checkAlive(ScreenWidth,ScreenHeight);
            if(s.isAlive()==false){
                removeList.add(s);
            }
        }

        for(Shot r:removeList){
            Shots.remove(r);
        }


        for(Shot s: Shots){
            s.update(canvas);
        }
        if(missile !=  null) {
            if (missile.isAlive()) {
                Log.d("position: ", "" + missile.getY());
                missile.checkAlive(ScreenWidth, ScreenHeight);
                missile.update(canvas);
            } else {
                boom = true;
                missile = null;
            }
        }


    }
    public void fireM(){
        Log.d("stuff", "stuff");
        missile = new Missile(context,x+bmp.getWidth()/2,y,0,0);
    }

}