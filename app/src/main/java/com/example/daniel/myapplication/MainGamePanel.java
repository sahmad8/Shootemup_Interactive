package com.example.daniel.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.TextView;





import java.util.ArrayList;
import java.util.LinkedList;

public class MainGamePanel extends SurfaceView implements SurfaceHolder.Callback, SensorEventListener {

    private static final String TAG = MainThread.class.getSimpleName();
    private float lastPosX = -1;
    private float lastPosY = -1;
    private ArrayList<Entity> entities;
    private LinkedList<Entity> shots;
    private Entity e;
    private boolean running = true;
    private boolean over = false;

    private SensorManager sManager;
    float axisY;
    float axisX;
    private TextView tv;
    private ImageView iv;
    private TextView tv2;

    private MainThread thread;

    public MainGamePanel(Context context){
        super(context);

        sManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_GAME);
        getHolder().addCallback(this);
        setFocusable(true);

        entities = new ArrayList<Entity>();
        shots = new LinkedList<Entity>();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
        thread = new MainThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        while(retry){
            try{
                thread.setRunning(false);
                thread.join();
                retry = false;
            }catch(InterruptedException  e){

            }
        }
    }



    @Override
    public boolean onTouchEvent(MotionEvent event){
        Log.d(TAG, event.getAction() + "");
        if(event.getAction()  == MotionEvent.ACTION_DOWN) {
            /*Log.d(TAG, "Coords: x=" + event.getX() + ",y=" + event.getY());
            lastPosX = event.getX();
            lastPosY = event.getY();
            return true;*/
            running = !running;
        }
        /*if(event.getAction() == MotionEvent.ACTION_MOVE){
            if (lastPosX != -1 && lastPosY != -1){
                if(lastPosX-5 > event.getX()&&e.getLeft()>0){
                    e.setX(-1);
                }else if(lastPosX+5 < event.getX()&&e.getRight()<getWidth()){
                    e.setX(1);
                }
                if(lastPosY-5 > event.getY()&&e.getTop()>0){
                    e.setY(-1);
                }else if(lastPosY+5 < event.getY()&&e.getBottom()<getHeight()){
                    e.setY(1);
                }
            }
            lastPosX = event.getX();
            lastPosY = event.getY();
            return true;
        }*/
        if(event.getAction() == MotionEvent.ACTION_UP){
            /*lastPosX = -1;
            lastPosY = -1;*/
        }
        return super.onTouchEvent(event);
    }

    public void addShot(Entity e){
        shots.add(e);
    }

    @Override
    public void onDraw(Canvas canvas){

    }

    protected void onResume2(){

        //super.
        sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_GAME);

    }


    protected void onStop2()
    {
        //unregister the sensor listener
        sManager.unregisterListener(this);
        //super.onStop();
    }


    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1)
    {

    }


    public void onSensorChanged(SensorEvent event) {
        //axisX = event.values[0];
        //tv.setText(Float.toString(axisX));
        axisX = event.values[0];
        if ((axisX > 2)||(axisX < -2))
        {
            // axisX=0;

            /*axisY=axisX;
            tv.setText(Float.toString(axisY));
            float x=iv.getX();
            tv2.setText(Float.toString(x));*/
            if (axisX > 2) {
                if (e.x==0)
                {
                    return;
                }
                e.setX(-4);
                return;
            }
            if (e.x==getWidth()-e.spriteWidth)
            {
                return;
            }
            e.setX(4);
        }
        //tv.setText(Float.toString(axisY));
    }

    public void update(){
        if(e==null){
            e = new Player(null, getWidth()/2-50f,getHeight()-120f,100f,100f, Color.BLUE, this);
        }
        if(running) {
            ArrayList<Entity> remove = new ArrayList<Entity>();
            e.update();
            for (Entity f : entities) {
                f.update();
            }
            for (Entity f : shots) {
                f.update();
                if (!f.getAlive()) {
                    remove.add(f);
                }
            }
            for (Entity f : remove) {
                shots.remove(f);
            }
            this.collision();
            over = !e.getAlive();
        }
    }

    @Override
    public void draw(Canvas canvas){
        if(canvas != null) {
            canvas.drawColor(Color.GREEN);

            for( Entity f: entities){
                f.draw(canvas);
            }
            for( Entity f: shots){
                f.draw(canvas);
            }
            e.draw(canvas);
        }
    }

    public void collision(){
        e.collision(entities);
    }

}
