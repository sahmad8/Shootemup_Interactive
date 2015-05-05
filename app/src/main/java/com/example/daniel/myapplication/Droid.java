package com.example.daniel.myapplication;

/**
 * Created by Saad on 4/18/2015.
 */
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Droid {

    private Bitmap bitmap; // the actual bitmap
    private int x;   // the X coordinate
    private int y;   // the Y coordinate
    private boolean touched; // if droid is touched/picked up

    /**
     * Initialize the values with the provided values.
     * @param bitmap
     * @param x
     * @param y
     */
    public Droid(Bitmap bitmap, int x, int y) {
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
    }

    /**
     * @return the bitmap that is drawn here
     */
    public Bitmap getBitmap() {
        return bitmap;
    }

    /**
     * Sets x to be the provided value
     * @param x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return the current value of y
     */
    public int getY() {
        return y;
    }

    /**
     * Sets y to be the provided value
     * @param y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * draws the bitmap to the canvas so it will be seen.
     * @param canvas
     */
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap,x, y, null);
    }


}
