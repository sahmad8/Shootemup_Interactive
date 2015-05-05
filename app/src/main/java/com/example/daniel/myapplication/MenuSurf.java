package com.example.daniel.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Daniel on 4/18/2015.
 */
public class MenuSurf extends SurfaceView implements SurfaceHolder.Callback {
    private static Context activity;
    private Canvas canvas;
    private MenuThread thread;
    private RectF Button1;
    private RectF Button2;
    private RectF Button3;
    private Bitmap bg1,bg2;
    private Droid droid1,droid2;
    private int height;
    private int width;
    private boolean running = true;

    public MenuSurf(Context context) {
        super(context);
        getHolder().addCallback(this);
        setFocusable(true);
        height = getHeight();
        width = getWidth();
        Button1 = new RectF(getWidth()/5,getHeight()*2/5,getWidth()*4/5,getHeight()*2/5+200);

        //background
        this.bg1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.background1);
        droid1 = new Droid(bg1,0,0);
        this.bg2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.background1);
        droid2 = new Droid(bg2,0,0 - bg2.getHeight());

        activity = context;Log.d("test", "test");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction()  == MotionEvent.ACTION_DOWN) {
            if(new RectF(getWidth()/5,getHeight()*2/5,getWidth()*4/5,getHeight()*2/5+getHeight()/9).contains(event.getX(),event.getY())){
                Intent intent = new Intent(activity, GameTest.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                activity.startActivity(intent);
                ((Activity)activity).finish();
                running = false;
                bg1.recycle();
                bg2.recycle();
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
        thread = new MenuThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    /**
     * Stops thread when destroyed
     * @param holder
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
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

    /**
     * Draws the background, button and text on the screen along with updating the background.
     * @param c
     */
    public void draw(Canvas c){
        if(c!=null&&running) {
            if(droid1.getY()==0){
                droid2.setY(0 - droid2.getBitmap().getHeight());
            }
            if(droid2.getY()==0){
                droid1.setY(0 - droid1.getBitmap().getHeight());
            }

            droid2.setY(droid2.getY()+2);
            droid1.setY(droid1.getY()+2);
            if(!bg1.isRecycled()&&!bg2.isRecycled()) {
                droid1.draw(c);

            }
            if(!bg1.isRecycled()&&!bg2.isRecycled())
                droid2.draw(c);
            Paint p = new Paint();
            p.setTextAlign(Paint.Align.CENTER);
            p.setColor(Color.WHITE);
            p.setTextSize(getWidth() / 7);
            c.drawText("Galactic Clash", getWidth()/2, getHeight()/5, p);
            p.setTextSize(getWidth() / 15);
            p.setColor(Color.DKGRAY);
            c.drawRect(new RectF(getWidth()/5,getHeight()*2/5,getWidth()*4/5,getHeight()*2/5+getHeight()/9),p);
            p.setColor(Color.WHITE);
            c.drawText("New Game",getWidth()/2, getHeight()*2/5+getHeight()/14, p);
        }
    }
}
