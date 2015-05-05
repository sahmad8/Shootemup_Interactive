package com.example.daniel.myapplication;

import android.content.Context;
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

    public boolean spent = false;

    public boolean hit = false;
    public int cooldown = 0;

    /**
     * Initializes the values for the player such as image bitmap, health and shot storage.
     * @param context
     * @param ScreenWidth
     * @param ScreenHeight
     */
    public Player(Context context, int ScreenWidth, int ScreenHeight) {
        this.context = context;
        this.Shots = new ArrayList<Shot>();
        this.removeList = new ArrayList<Shot>();
        this.ShotCreateCountDown = 0;
        this.life = 20;

        this.changeX = 10;
        this.changeY = 10;


        this.ScreenWidth = ScreenWidth;
        this.ScreenHeight = ScreenHeight;

        this.bmp = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.ship1);
        this.x = ScreenWidth/2 -bmp.getWidth()/2;
        this.y = ScreenHeight - bmp.getHeight()-100;
        this.droid = new Droid(this.bmp, x, y);

    }

    /**
     * @return the list of shots fired by the player.
     */
    public ArrayList<Shot> getShots(){
        return Shots;
    }

    /**
     * Changes the players position based on the magnitude of the provided value.
     * @param value
     */
    public void ChangeX(float value){
        if(value > 0){    this.x -= (int)(1.95* value)  ; }
        else{   this.x += (int)(2.10)*value*-1;  }

        if(this.x < 0){    this.x = 0;    }
        else if(this.x > (ScreenWidth - bmp.getWidth())){ this.x = (ScreenWidth - bmp.getWidth());   }
        droid.setX(x);
    }

    /**
     * Calls the updates for the shots and updates condition variables for collision and
     * invulnerability.
     * @param canvas
     */
    public void update(Canvas canvas){

        checkShots(canvas);
        if(cooldown == 100){
            cooldown = 0;
            hit = false;
        }
        if(!hit ||  cooldown%20 <10) {
            droid.draw(canvas);
            if(hit) {
                cooldown++;
            }
        }else{
            cooldown++;
        }
    }

    /**
     * Stores the provided shots to be removed later.
     * @param s
     */
    public void removeSArray(ArrayList<Shot> s){
        removeList.clear();
        removeList.addAll(s);
    }

    /**
     * Checks to make sure shot are still within the bounds of the screen. Updates the shots and
     * missile. Detonates the missile after it has been created.
     * @param canvas
     */
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

    /**
     * Creates a new Missile and adds it to the game world right in front of the player
     */
    public void fireM(){
        missile = new Missile(context,x-10,y+160,0,0);
    }

}