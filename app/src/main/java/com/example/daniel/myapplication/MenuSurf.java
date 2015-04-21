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
    private RectF Button1;
    private RectF Button2;
    private RectF Button3;

    public MenuSurf(Context context) {
        super(context);
        getHolder().addCallback(this);
        setFocusable(true);
        Button1 = new RectF(200, 300, 870, 500);
        Button2 = new RectF(100, 400, 500, 200);
        Button3 = new RectF(100, 700, 500, 200);

        activity = context;Log.d("test", "test");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction()  == MotionEvent.ACTION_DOWN) {
            RectF temp = new RectF(event.getX(), event.getY(),1,1);
            if(Button1.contains(event.getX(),event.getY())){
                Intent intent = new Intent(activity, GameTest.class);
                activity.startActivity(intent);
            }else if(Button2.contains(temp)){

            }else if(Button3.contains(temp)){

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
            c.drawRect(Button1,new Paint());
            //c.drawRect(Button2,new Paint());
            //c.drawRect(Button3,new Paint());
        }
    }
}
