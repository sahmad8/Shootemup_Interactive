package com.example.daniel.myapplication;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.daniel.myapplication.test.SendScore;

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
    private Enemies enemies;
    private Context context;

    private Bitmap bg1,bg2;
    private Droid droid1,droid2;

    public static int score;

    private int MissileCountDown;
    private Paint p = new Paint();
    private boolean red = false;
    private int cooldown = 0;


    /**
     * Creates a new MainGamePanel, initializes sensors to the game and initial pictures and values.
     * @param context
     */
    public MainGamePanel(Context context) {
        super(context);
        this.context=context;
        theContext=context;
        //set up all the sensors
        sManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sManager2 = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_GAME);
        sManager2.registerListener(this, sManager2.getDefaultSensor(Sensor.TYPE_GYROSCOPE),SensorManager.SENSOR_DELAY_GAME);
        //menu music, need to move this for the menu activity
        mp3menu= MediaPlayer.create(theContext, R.raw.menumusic);
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


        MissileCountDown = 2500;

        // adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);
        // make the GamePanel focusable so it can handle events
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    /**
     * Starts thread when created
     * @param holder
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // at this point the surface is created and
        // we can safely start the game loop
        thread = new MainThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    /**
     * Stops thread when destroyed
     * @param holder
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // tell the thread to shut down and wait for it to finish
        // this is a clean shutdown
        running = false;
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

    /**
     * Detects touch screen interactions to pause the game and interact with buttons
     * @param event
     * @return boolean based on super class implementation
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if(running) {
                Log.d("Stuff", "stuff");
                running = false;
            }else{
                if(new RectF(getWidth() / 5, getHeight() * 3 / 5, getWidth() * 4 / 5, getHeight() * 3 / 5 + 200).contains(event.getX(), event.getY())){
                    Intent intent = new Intent(context, Menu.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                    bg1.recycle();
                    bg2.recycle();
                    mp3menu.stop();
                }else if(over && new RectF(getWidth() / 5, getHeight() * 4 / 5, getWidth() * 4 / 5, getHeight() * 4 / 5 + getHeight()/9).contains(event.getX(), event.getY())) {
                    Intent intent = new Intent(context, SendScore.class);
                    intent.putExtra("thescore",score );
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    mp3menu.stop();
                     context.startActivity(intent);
                    ((Activity)context).finish();
                    bg1.recycle();
                    bg2.recycle();
                }else{
                    running = !running;

                }
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * Redraws entities on the canvas to update their position, upadate their states, and remove dead entities
     * @param canvas
     */
    @Override
    public void draw(Canvas canvas) {
        if(running&&!over) {//only updates when the game is running and not over
            if(droid1.getY()==0){
                droid2.setY(0 - droid2.getBitmap().getHeight());
            }
            if(droid2.getY()==0){
                droid1.setY(0 - droid1.getBitmap().getHeight());
            }

            droid2.setY(droid2.getY()+2);
            droid1.setY(droid1.getY()+2);
            if(!bg1.isRecycled()&&!bg2.isRecycled()) {
                droid1.draw(canvas);
                droid2.draw(canvas);
            }

            ArrayList<Enemy> tempE = new ArrayList<>();
            ArrayList<Shot> tempS = new ArrayList<>();
            //checks if entities are dead
            if(player1!=null && enemies!=null){
                for(Enemy e : enemies.getEArray()){
                    //checks the player's shots have hit
                    for(Shot s : player1.getShots()){
                            if(collision(s,e)){
                            e.setLife(e.getLife()-s.getDamage());
                            tempS.add(s);
                            if(e.getLife()<=0){
                                score += 100;
                            }
                        }
                    }
                    //checks if player has hit an enemy
                    if(collision(e,player1)&&!player1.hit){
                        player1.hit=true;
                        player1.setLife(player1.getLife()-5);
                        e.setLife(0);
                        red = true;

                    }
                    //checks if player is hit by an enemy shot
                    for(Shot es : e.getShots()){
                        if(collision(es,player1)&&!player1.hit){
                            player1.hit=true;
                            es.setAlive(false);
                            player1.setLife(player1.getLife()-3);
                            red = true;

                        }
                    }
                }
                enemies.removeEArray(tempE);
                player1.removeSArray(tempS);
            }

            //first time set up for enemies and player and updates
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
            //handles if the missile has been triggered
            if(player1.boom){
                score += 100*enemies.clear();
                player1.boom = false;
            }
            //handles if the player died
            if(player1.getLife()<=0){
                over = true;
            }
            //damage indication
            if(!over&&red&&cooldown<5){
                Paint g = new Paint();
                g.setStyle(Style.FILL);
                g.setARGB(100,255,0,0);
                canvas.drawRect(new RectF(0,0,getWidth(),getHeight()),g);
                cooldown++;
            }else{
                cooldown = 0;
                red = false;
            }
        }else{
            //menu when the game is paused
            Paint t = new Paint();
            t.setColor(Color.WHITE);
            t.setTextAlign(Paint.Align.CENTER);
            //extra button when the game is over to submit the score
            if(over){
                t.setTextSize(getWidth()/6);
                canvas.drawText("Game Over", getWidth()/2,getHeight()/5,t);
                t.setColor(Color.DKGRAY);
                canvas.drawRect(new RectF(getWidth() / 5, getHeight() * 4 / 5, getWidth() * 4 / 5, getHeight() * 4 / 5 + getHeight()/9), t);
                t.setColor(Color.WHITE);
                t.setTextSize(getWidth()/15);
                canvas.drawText("Submit Score", getWidth()/2, getHeight()*4/5+getHeight()/14, t);
            }
            t.setTextSize(getWidth()/15);
            canvas.drawText("Score:" + score, getWidth()/2, getHeight()/3,t);
            t.setColor(Color.DKGRAY);
            canvas.drawRect(new RectF(getWidth() / 5, getHeight() * 3 / 5, getWidth() * 4 / 5, getHeight() * 3 / 5 + getHeight()/9), t);
            t.setColor(Color.WHITE);
            canvas.drawText("Return to Menu", getWidth()/2, getHeight()*3/5+getHeight()/14, t);

        }
        //GUI at the bottom of the screen
        p.setColor(Color.DKGRAY);
        canvas.drawRect(new RectF(0, getHeight() - 90, getWidth(), getHeight()), p);
        p.setColor(Color.WHITE);
        p.setTextSize(getWidth() / 20);
        canvas.drawText("Score:" + score, getWidth() * 2 / 3, getHeight() - 10, p);
        canvas.drawText("Health: " + player1.getLife(), getWidth() / 15, getHeight() - 10, p);

        //draw the missile cool down bar
        p.setColor(Color.YELLOW);
        if(MissileCountDown == 2500){    p.setColor(Color.RED);  }
        canvas.drawRect(new RectF(getWidth() / 15, getHeight() - 70, (getWidth() / 15) + (MissileCountDown / 25), getHeight() - 60), p);


    }

    /**
     * Checks if two entities have collided based on their bitmaps
     * @param E1
     * @param E2
     * @return true if the entities bitmaps overlap, false otherwise
     */
    private boolean collision(Entity E1, Entity E2){
        return ((E2.getX()<=E1.getX()+E1.getBmp().getWidth() && E1.getX()<=E2.getX()+E2.getBmp().getWidth()) ||
                (E1.getX()<=E2.getX()+E2.getBmp().getWidth() && E2.getX()<=E1.getX()+E1.getBmp().getWidth())) &&
                ((E2.getY()<=E1.getY()+E1.getBmp().getHeight() && E1.getY()<=E2.getY()+E2.getBmp().getHeight()) ||
                        (E1.getY()<=E2.getY()+E2.getBmp().getHeight() && E2.getY()<=E1.getY()+E1.getBmp().getHeight()));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * Sensor changed event
     * Used for when sensor values changed, called automatically.
     * We implement accelerometer for moving player in a constant y plane.
     * Also implement gyroscope. Feel free to change the threshold values in the if loops
     * Depending on if you want to move the player faster or how fast the user must flick their phone forwards.
     * @param event
     */
    public void onSensorChanged(SensorEvent event) {

        if(player1 != null && running && !over){
            MissileCountDown ++;
            if(MissileCountDown >= 2500){ MissileCountDown = 2500;    }

            player1.setScreenWidth(getWidth());
            player1.setScreenHeight(getHeight());
            int type = event.sensor.getType();                        //integer
            axisX = event.values[0];
            //fires missle when Gyroscope is triggered and cooldown is done.
            if (type == Sensor.TYPE_GYROSCOPE&&running&&!player1.spent&&(MissileCountDown == 2500) ) {
                if ((axisX < -7)) {
                    MissileCountDown = 0;
                    mp3power.start();
                    player1.fireM();
                    return;
                }
            }
            if ((axisX > 1.85) || (axisX < -1.85)) {
                if (axisX > 0) {
                    if (player1.getX() == 0) {
                        return;
                    }
                    player1.ChangeX(axisX);
                    return;
                }
                if (player1.getX() == getWidth() - 100f) {
                    return;
                }
                player1.ChangeX(axisX);
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
