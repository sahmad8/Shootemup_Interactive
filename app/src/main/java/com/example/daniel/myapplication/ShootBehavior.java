package com.example.daniel.myapplication;

/**
 * Created by Daniel on 4/5/2015.
 */
public class ShootBehavior {
    protected MainGamePanel m;
    protected int cooldown;
    public ShootBehavior(){}
    public ShootBehavior(MainGamePanel m){
        this.m = m;
    }
    public void shoot(float x, float y){}
}
