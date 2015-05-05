package com.example.daniel.myapplication;

/**
 * Created by Saad on 4/18/2015.
 */

import android.content.Context;
import android.graphics.Canvas;

import java.util.ArrayList;

public class Enemies {
    private ArrayList<Enemy> eArray;
    private ArrayList<Shot> esArray;
    private ArrayList<Enemy> removeE;
    private boolean BossCreated;
    private int ScreenWidth;
    private int ScreenHeight;
    private Context context;
    private double count1,count2,count3;

    /**
     * Initializes the values for screen height/width and the initial value for count
     * @param context
     * @param ScreenWidth
     * @param ScreenHeight
     */
    public Enemies(Context context, int ScreenWidth, int ScreenHeight){
        this.context = context;
        this.ScreenWidth = ScreenWidth;
        this.ScreenHeight = ScreenHeight;
        esArray = new ArrayList<Shot>();
        eArray = new  ArrayList<Enemy>();
        removeE = new  ArrayList<Enemy>();
        count1 = 0;
        BossCreated = false;
    }

    /**
     * @return returns the list of enemies in the game.
     */
    public ArrayList<Enemy> getEArray(){
        return eArray;
    }

    /**
     * Records the provided enemies to be removed later.
     * @param e
     */
    public void removeEArray(ArrayList<Enemy> e){
        removeE.clear();
        removeE.addAll(e);
    }

    /**
     * Spawns new enemies based on set patterns for each enemy type.
     */
    private void CreateEnemy(){
        if(count1 == 0 && 0<=count2 && count2<=4) {
            eArray.add(new Enemy(context, ScreenWidth, ScreenHeight, ScreenWidth / 4, 0, 1));
        }
        if(count1 == 0 && 10<=count2 && count2<=14) {
            eArray.add(new Enemy(context, ScreenWidth, ScreenHeight, ScreenWidth *3/ 4, 0, 1));
        }
        if(count2>=20 && count1 == 0 && 0<=count2%10 && count2%10<=4){// &&20<=count2 && count2<=24) {
            eArray.add(new Enemy(context, ScreenWidth, ScreenHeight, 0, 70, 0));
        }
        if(count3 == 50){
            if(!BossCreated){
                eArray.add(new Enemy(context, ScreenWidth, ScreenHeight, 0, 70, 2));
            }

        }
        count1 = (count1 + 1)%40;
        if(count1==0){  count2 = (count2+1)%40;  }
        if(count2==0){  count3 = (count3 + 1)%51;  }
    }

    /**
     * Updates all enemies and remove the dead enemies.
     * @param canvas
     * @param Px
     * @param Py
     */
    public void update(Canvas canvas, int Px, int Py){
        CreateEnemy();
        // get the enemy that are dead
        for(Enemy e : eArray){
            if(e.getLife()<=0 && !e.ShotsAlive()){
                if(e.getType()==2){ BossCreated = false;    }
                removeE.add(e);
            }
        }
        // remove from enemy array
        for(Enemy r : removeE){
            r.recycle();
            eArray.remove(r);
        }
        // display the rest enemy
        for(Enemy e : eArray){
            e.update(canvas,Px,Py);
        }
    }

    /**
     * Removes all enemies asside from the boss and clears the shots from the screen.
     * @return the number of enemies it removed
     */
    public int clear(){
        int temp = eArray.size();
        for(Enemy e: eArray){
            if(e.getType()!=3){
                e.setLife(e.getLife()-3);
            }
            e.getShots().clear();
        }
        for(Shot s: esArray){
            s.setAlive(false);
        }
        return temp;
    }
}