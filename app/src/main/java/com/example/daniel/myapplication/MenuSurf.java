package com.example.daniel.myapplication;

import android.content.Context;
import android.content.Intent;
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

    public MenuSurf(Context context) {
        super(context);
        getHolder().addCallback(this);
        setFocusable(true);


        activity = context;Log.d("test", "test");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction()  == MotionEvent.ACTION_DOWN) {
            Intent intent = new Intent(activity, GameTest.class);
            activity.startActivity(intent);
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
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

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

    public void draw(Canvas c){
        if(c!=null) {
            c.drawColor(Color.YELLOW);
            c.drawRect(new RectF(0, 0, 100, 100), new Paint(Color.BLUE));
        }
    }
}
