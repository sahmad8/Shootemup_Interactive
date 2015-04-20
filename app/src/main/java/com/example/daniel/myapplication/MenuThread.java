package com.example.daniel.myapplication;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by Daniel on 4/18/2015.
 */
public class MenuThread extends Thread {

    private final static int 	MAX_FPS = 60;
    private final static int	MAX_FRAME_SKIPS = 15;
    private final static int	FRAME_PERIOD = 1000 / MAX_FPS;

    private SurfaceHolder surfaceHolder;
    private MenuSurf menu;
    private boolean running;
    public void setRunning(boolean running){
        this.running = running;
    }

    public MenuThread(SurfaceHolder surfaceHolder, MenuSurf menu){
        super();
        this.surfaceHolder = surfaceHolder;
        this.menu = menu;
    }
    @Override
    public void run(){
        long beginTime;		// the time when the cycle begun
        long timeDiff;		// the time it took for the cycle to execute
        int sleepTime;		// ms to sleep (<0 if we're behind)
        int framesSkipped;	// number of frames being skipped

        Canvas canvas;
        long tickCount = 0L;
        while(running){
            canvas = null;
            tickCount++;
            //update other stuff
            try{
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    beginTime = System.currentTimeMillis();
                    framesSkipped = 0;	// resetting the frames skipped

                    this.menu.draw(canvas);

                    timeDiff = System.currentTimeMillis() - beginTime;
                    // calculate sleep time
                    sleepTime = (int)(FRAME_PERIOD - timeDiff);
                    if (sleepTime > 0) {
                        // if sleepTime > 0 we're OK
                        try {
                            // send the thread to sleep for a short period
                            // very useful for battery saving
                            Thread.sleep(sleepTime);
                        } catch (InterruptedException e) {}
                    }

                    while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS) {
                        // we need to catch up
                        sleepTime += FRAME_PERIOD;	// add frame period to check if in next frame
                        framesSkipped++;
                    }
                }
            }finally {
                // in case of an exception the surface is not left in
                // an inconsistent state
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}
