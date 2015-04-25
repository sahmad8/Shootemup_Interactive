package com.example.daniel.myapplication;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class MainGamePanel extends SurfaceView implements SurfaceHolder.Callback,SensorEventListener {
    //for debug
    private static final String TAG = MainGamePanel.class.getSimpleName();

    private Context theContext;
    private SensorManager sManager2;
    private SensorManager sManager;
    private float axisX;
    private boolean running = false;
    private boolean over = false;

    private MainThread thread;
    public MediaPlayer mp3power;
    public MediaPlayer mp3menu;

    private Player player1;
    //private Entity player1;
    private Enemies enemies;
    private Context context;

    private Bitmap bg1,bg2;
    private Droid droid1,droid2;

    private int score;



    public MainGamePanel(Context context) {
        super(context);
        this.context=context;
        theContext=context;
        sManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sManager2 = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_GAME);
        sManager2.registerListener(this, sManager2.getDefaultSensor(Sensor.TYPE_GYROSCOPE),SensorManager.SENSOR_DELAY_GAME);
        mp3menu= MediaPlayer.create(theContext, R.raw.menumusic);       //menu music, need to move this for the menu activity.
        mp3power = MediaPlayer.create(theContext, R.raw.powernuke);
        //background
        this.bg1 = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.background1);
        droid1 = new Droid(bg1,0,0);
        this.bg2 = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.background1);
        droid2 = new Droid(bg2,0,0 - bg2.getHeight());

        running = true;
        sManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_GAME);
        axisX = 0;
        score = 0;

        // adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);
        // make the GamePanel focusable so it can handle events
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        // at this point the surface is created and
        // we can safely start the game loop
        thread = new MainThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // tell the thread to shut down and wait for it to finish
        // this is a clean shutdown
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                // try again shutting down the thread
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //thread.setRunning(!thread.getRunning());
            if(running) {
                running = !running;
            }else {
                RectF menuB = new RectF(getWidth()/5,getHeight()*3/5,getWidth()*4/5,getHeight()*3/5+200);
                if (menuB.contains(event.getX(), event.getY())) {
                    Intent intent = new Intent(context, Menu.class);
                    context.startActivity(intent);
                }else{
                    running = !running;
                }
            }
        }
        return super.onTouchEvent(event);
        //return true;
    }
    /*
    @Override
    protected void onDraw(Canvas canvas) {
        // fills the canvas with cyan
        canvas.drawColor(Color.CYAN);
        player1.setScreenWidth(getWidth());
        player1.setScreenHeight(getHeight());
        //update player1 status
        player1.update(canvas);
    }
    */
    @Override
    public void draw(Canvas canvas) {
        if(running&&!over) {
            // fills the canvas with cyan
            canvas.drawColor(Color.CYAN);
            if(droid1.getY()==0){
                droid2.setY(0 - droid2.getBitmap().getHeight());
            }
            if(droid2.getY()==0){
                droid1.setY(0 - droid1.getBitmap().getHeight());
            }

            droid2.setY(droid2.getY()+2);
            droid1.setY(droid1.getY()+2);
            droid1.draw(canvas);
            droid2.draw(canvas);


            ArrayList<Enemy> tempE = new ArrayList<>();
            ArrayList<Shot> tempS = new ArrayList<>();
            if(player1!=null && enemies!=null){
                for(Enemy e : enemies.getEArray()){
                    for(Shot s : player1.getShots()){
                            if(collision(s,e)){
                            e.setLife(e.getLife()-s.getDamage());
                            //tempE.add(e);
                            tempS.add(s);
                            if(e.getLife()<=0){
                                score += 100;
                            }
                        }
                    }
                    if(collision(e,player1)){
                        player1.setLife(player1.getLife()-5);
                        e.setLife(0);
                    }
                    for(Shot es : e.getShots()){
                        if(collision(es,player1)){
                            es.setAlive(false);
                            player1.setLife(player1.getLife()-3);
                        }
                    }
                }
                enemies.removeEArray(tempE);
                player1.removeSArray(tempS);
            }







            if (enemies == null) {
                enemies = new Enemies(context, getWidth(), getHeight());
            } else {
                enemies.update(canvas,player1.getX(),player1.getY());
            }

            if (player1 == null) {
                player1 = new Player(context, getWidth(), getHeight());
                mp3menu.start();
            } else {
                //update player1 status
                player1.update(canvas);
            }
            if(player1.boom){
                score += 100*enemies.clear();
                player1.boom = false;
            }
            if(player1.getLife()<=0){
                over = true;
            }
        }else{
            Paint t = new Paint();
            t.setColor(Color.WHITE);
            t.setTextAlign(Paint.Align.CENTER);
            if(over){
                t.setTextSize(150);
                canvas.drawText("Game Over", getWidth()/2,getHeight()/5,t);
            }
            t.setTextSize(75);
            canvas.drawText("Score:" + score, getWidth()/2, getHeight()/3,t);
            t.setColor(Color.DKGRAY);
            canvas.drawRect(new RectF(getWidth()/5,getHeight()*3/5,getWidth()*4/5,getHeight()*3/5+200),t);
            t.setColor(Color.WHITE);
            canvas.drawText("Return to Menu", getWidth()/2, getHeight()*3/5+125, t);
        }
        Paint p = new Paint();
        p.setColor(Color.DKGRAY);
        canvas.drawRect(new RectF(0,getHeight()-90,getWidth(),getHeight()),p);
        p.setColor(Color.WHITE);
        p.setTextSize(50);
        canvas.drawText("Score:" + score,getWidth()-400,getHeight()-10,p );
        canvas.drawText("Health: " + player1.getLife(),50, getHeight()-10,p);


    }
    private boolean collision(Entity E1, Entity E2){
        return ((E2.getX()<=E1.getX()+E1.getBmp().getWidth() && E1.getX()<=E2.getX()+E2.getBmp().getWidth()) ||
                (E1.getX()<=E2.getX()+E2.getBmp().getWidth() && E2.getX()<=E1.getX()+E1.getBmp().getWidth())) &&
                ((E2.getY()<=E1.getY()+E1.getBmp().getHeight() && E1.getY()<=E2.getY()+E2.getBmp().getHeight()) ||
                        (E1.getY()<=E2.getY()+E2.getBmp().getHeight() && E2.getY()<=E1.getY()+E1.getBmp().getHeight()));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /*
 Sensor changed event
 Used for when sensor values changed, called automatically.
 We implement accelerometer for moving player in a constant y plane.
 Also implement gyroscope. Feel free to change the threshold values in the if loops
 Depending on if you want to move the player faster or how fast the user must flick their phone forwards.
  */
    public void onSensorChanged(SensorEvent event) {

        if(player1 != null){
            player1.setScreenWidth(getWidth());
            player1.setScreenHeight(getHeight());
            int type = event.sensor.getType();                        //integer
            axisX = event.values[0];
            if (type == Sensor.TYPE_GYROSCOPE) {
                if ((axisX < -7)) {
                    // axisX=0;
                    if (!(mp3power.isPlaying())) {                  //sound effect. Already set when main game panel was created (-- look at the constructor).
                        // We just start it here.
                        mp3power.start();
                        player1.fireM();

                    }
                }
                /*
                Add code for creating nuke (type of bullet, but special). Have to make it travel upwards.
                Start at same x position of player. Above player's y position.
                 */
            }

            if (running&&((axisX > 1.5) || (axisX < -1.5))) {
                if (axisX > 0) {
                    if (player1.getX() == 0) {
                        return;
                    }
                    player1.ChangeX(true);
                    return;
                }
                if (player1.getX() == getWidth() - 100f) {
                    return;
                }
                player1.ChangeX(false);
            }
        }
    }

    /**
    Sensor changed method test.
    Dependent on hardware (motion). The sensor changed event is called automatically.
    We only test for a change in player positions (since this has to change because motion was detected).
    Precondition: Current player position.
    Postcondition: Change in player position.
     */
     public void testsensor () {
         //runs with sensor changed event
         //@Before (before sensor changed event starts running), save player position, sensor values
         //@After(after sensorchanged event completed), save players position again
         //Something like--> assertEquals (beforepostiion, afterpostion), if sensor values > abs|2.5|, player position should have changed. Then-->
         //If they were equal, throw an error exception. Something is wrong in the implementation of changing player position inside sensor changed event.
         //Else if sensor values< abs|2.5| , positions should be equal.
     }

}
