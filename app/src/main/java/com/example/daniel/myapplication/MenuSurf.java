package com.example.daniel.myapplication;

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

    public MenuSurf(Context context) {
        super(context);
        getHolder().addCallback(this);
        setFocusable(true);
        height = getHeight();
        width = getWidth();
        Button1 = new RectF(getWidth()/5,getHeight()*2/5,getWidth()*4/5,getHeight()*2/5+200);
        Button2 = new RectF(100, 400, 500, 200);
        Button3 = new RectF(100, 700, 500, 200);

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
                activity.startActivity(intent);
            }else if(Button2.contains(event.getX(),event.getY())){

            }else if(Button3.contains(event.getX(),event.getY())){

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



            // fills the canvas with cyan
            if(droid1.getY()==0){
                droid2.setY(0 - droid2.getBitmap().getHeight());
            }
            if(droid2.getY()==0){
                droid1.setY(0 - droid1.getBitmap().getHeight());
            }

            droid2.setY(droid2.getY()+2);
            droid1.setY(droid1.getY()+2);
            droid1.draw(c);
            droid2.draw(c);
            Paint p = new Paint();
            p.setTextAlign(Paint.Align.CENTER);
            p.setColor(Color.WHITE);
            p.setTextSize(getWidth() / 7);
            c.drawText("Galactic Clash", getWidth()/2, getHeight()/5, p);
            //canvas.drawText("Galatctic Clash",width/2,height/5,new Paint());
            p.setTextSize(getWidth() / 15);
            p.setColor(Color.DKGRAY);
            c.drawRect(new RectF(getWidth()/5,getHeight()*2/5,getWidth()*4/5,getHeight()*2/5+getHeight()/9),p);
            p.setColor(Color.WHITE);
            c.drawText("New Game",getWidth()/2, getHeight()*2/5+getHeight()/14, p);

            //c.drawRect(Button2,new Paint());
            //c.drawRect(Button3,new Paint());
        }
    }
}
