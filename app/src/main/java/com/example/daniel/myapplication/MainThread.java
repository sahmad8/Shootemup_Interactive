package com.example.daniel.myapplication;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class MainThread extends Thread {

    private static final String TAG = MainThread.class.getSimpleName();

    private SurfaceHolder surfaceHolder;
    private MainGamePanel gamePanel;
    private boolean running;

    /**
     * Sets the running variable to the given value.
     * @param running
     */
    public void setRunning(boolean running) {
        this.running = running;
    }

    /**
     * @return the current value of running
     */
    public boolean getRunning(){
        return running;
    }

    /**
     * Initializes the global variables for the thread.
     * @param surfaceHolder
     * @param gamePanel
     */
    public MainThread(SurfaceHolder surfaceHolder, MainGamePanel gamePanel) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }

    /**
     * Initializes the values for a thread based on another thread.
     * @param thread
     */
    public MainThread(MainThread thread){
        super();
        this.surfaceHolder = thread.surfaceHolder;
        this.gamePanel = thread.gamePanel;
    }

    /**
     * Runs the game loop while limiting the FPS of the game. this calls the update method for the
     * MainGamePanel and provides the canvas.
     */
    @Override
    public void run() {
        long tickCount = 0L;
        Canvas canvas;
        Log.d(TAG, "Starting game loop");
        while (running) {
            tickCount++;
            // update game state
            // render state to the screen
            canvas = null;
            // try locking the canvas for exclusive pixel editing on the surface
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    if(canvas == null){break;}
                    // update game state
                    // draws the canvas on the panel
                    this.gamePanel.draw(canvas);
                }
            } finally {
                // in case of an exception the surface is not left in
                // an inconsistent state
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            } // end finally
        }
        Log.d(TAG, "Game loop executed " + tickCount + " times");
    }
}
